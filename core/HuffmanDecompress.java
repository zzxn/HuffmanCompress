package core;

import core.io.ZcsFileInputStream;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
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

        // 根据文件大小确定缓冲区大小
        int bufferSize = (int)(zcsFile.length() < 52428800 ? zcsFile.length() : 52428800); // 50MB
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(zcsFile), bufferSize);
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
            File singleOutputFile = new File(outputFile.getPath() + "/" + filePath);
            if (!singleOutputFile.exists()) {
                File fileParent = singleOutputFile.getParentFile();
                if (!fileParent.exists()) {
                    fileParent.mkdirs();
                }
                singleOutputFile.createNewFile();
            }
            BufferedOutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(singleOutputFile), bufferSize);
            long fileSize = Long.parseLong(zcsFileInputStream.read().substring(10));
            String codeTableFlag = zcsFileInputStream.read();
            if (!codeTableFlag.equals("$CodeTable:"))
                throw new IllegalArgumentException("Invalid .zcs file");

            // 获取编码表
            System.out.println("getting code table: " + singleOutputFile.getPath());
            HashMap<String, Integer> decodeTable = new HashMap<>();
            for (int i = 0; i < 256; i++) {
                String code = zcsFileInputStream.read();
                if (code.length() != 0)
                    decodeTable.put(code, i);
            }

            // 进入编码区，zcsFileInputStream行为改变，现在返回0或1
            System.out.println("decompressing: " + singleOutputFile.getPath());
            String codeHead = zcsFileInputStream.read();
            if (!codeHead.equals("$CodeHead"))
                throw new IllegalArgumentException("Invalid .zcs file");
            long sizeCount = 0L; // 记录已经解压出的数据量
            StringBuilder codeBuilder = new StringBuilder(256);
            while (sizeCount < fileSize) {
                codeBuilder.append(zcsFileInputStream.read());
                if (decodeTable.containsKey(codeBuilder.toString())) {
                    fileOutputStream.write(decodeTable.get(codeBuilder.toString()));
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
            System.out.println("file " + singleOutputFile.getPath() + " is finished");
        }

    }
}
