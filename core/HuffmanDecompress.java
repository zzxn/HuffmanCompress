package core;

import java.io.*;
import java.util.TreeMap;

/**
 * @author 16307110325
 * Created on 2017/10/19.
 */
public class HuffmanDecompress {

    public static void decompressFile(File zcsFile, File outputFile) throws IOException {
        if (zcsFile.isDirectory())
            throw new IllegalArgumentException("Only to decompress single .zcs file!");
        if (outputFile.isFile())
            throw new IllegalArgumentException("Only to decompress to a directory!");
        if (!zcsFile.getName().matches("(.zcs)$"))
            System.out.println("It should have postfix .zcs.");

        FileInputStream inputStream = new FileInputStream(zcsFile);
        ZcsFileInputStream zcsFileInputStream = new ZcsFileInputStream(inputStream);

        // 检查文件格式信息
        String format = zcsFileInputStream.read();
        if (!format.equals("$ZhuxiaoningCompressedFile"))
            throw new IllegalArgumentException("Invalid .zcs file");

        // 获取解压后总大小
        String totalSizeString = zcsFileInputStream.read().substring(11);
        long totalSize = Long.parseLong(totalSizeString);
        if (totalSize > outputFile.getFreeSpace())
            throw new IOException("可用空间不足，无法执行解压，请至少腾出 " + totalSize + " 字节大小的空间");

        //  进行解压
        while (inputStream.available() != 0) {

            // 获取基本信息
            String filePath = zcsFileInputStream.read().substring(10);
            File singleOutputFile = new File(outputFile.getPath()+"/"+filePath);
            if (!singleOutputFile.exists()) {
                File fileParent = singleOutputFile.getParentFile();
                if(!fileParent.exists()){
                    fileParent.mkdirs();
                }
                singleOutputFile.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(singleOutputFile);
            long fileSize = Long.parseLong(zcsFileInputStream.read().substring(10));
            String codeTableFlag = zcsFileInputStream.read();
            if (!codeTableFlag.equals("$CodeTable:"))
                throw new IllegalArgumentException("Invalid .zcs file");

            // 获取编码表
            System.out.println("获取文件编码表：" + singleOutputFile.getPath());
            TreeMap<String, Integer> decodeTable = new TreeMap<>();
            for (int i = 0; i < 256; i++) {
                String code = zcsFileInputStream.read();
                if (code.length() != 0)
                    decodeTable.put(code, i);
            }

            // 进入编码区，zcsFileInputStream行为改变，现在返回0或1单字字符串
            System.out.println("解码中：" + singleOutputFile.getPath());
            String codeHead = zcsFileInputStream.read();
            if (!codeHead.equals("$CodeHead"))
                throw new IllegalArgumentException("Invalid .zcs file");
            long sizeCount = 0L; // 记录已经解压出的数据量
            StringBuilder codeBuilder = new StringBuilder();
            while (sizeCount < fileSize)  {
                codeBuilder.append(zcsFileInputStream.read());
                if (decodeTable.containsKey(codeBuilder.toString())) {
                    fileOutputStream.write(decodeTable.get(codeBuilder.toString()));
                    fileOutputStream.flush();
                    sizeCount++;
                    codeBuilder = new StringBuilder();
                }
            }
            fileOutputStream.close();

            // 跳出编码区，进行下一轮循环
            zcsFileInputStream.skipToNocode();
            String codeTail = zcsFileInputStream.read();
            while (!codeTail.equals("$CodeTail")) {
                codeTail = zcsFileInputStream.read();
            }
            if (!codeTail.equals("$CodeTail") && inputStream.available() == 0)
                throw new IllegalArgumentException("Invalid .zcs File");
            System.out.println("文件 " + singleOutputFile.getPath()+ " 解压完成");
        }

    }
}
