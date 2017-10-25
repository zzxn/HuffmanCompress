package core;

import core.dataStructure.CodeHuffTree;

import java.io.*;

/**
 * @author 16307110325
 * Created on 2017/10/19.
 */
public class HuffmanCompress {

    public static void main(String[] args) throws IOException {
//        File inputFile = new File("C:/Users/19098/Desktop/1.txt");
//        File outputFile = new File("C:/Users/19098/Desktop/testcprs.zcs");
//        File afterDecompressFile = new File("C:/Users/19098/Desktop/2.txt");
//
//        String[] codeTable = getFileCodeTable(inputFile);
//
//        // TODO 得到解码表，这部分工作应该放到解压缩时进行
//        TreeMap<String, Byte> decodeTable = new TreeMap<>();
//        for (int i = 0; i < codeTable.length; i++) {
//            decodeTable.put(codeTable[i], (byte)i);
//        }
//
//        compressSingleFile(inputFile, outputFile, codeTable, "./1.txt");

        // 测试解压缩
        //core.HuffmanDecompress.decompressFile(outputFile, afterDecompressFile, decodeTable);
    }


    /**
     * @param inputFile 需要压缩的文件
     * @param outputFile 压缩编码输出文件
     * @param codeTable 各个byte的编码对应表
     * @param path 解压缩后文件相对当前文件夹的相对路径
     * @throws IOException IOException
     * 把单个文件进行压缩，并将压缩后编码与特定格式压缩信息写入输出文件
     */
    private static void compressSingleFile(File inputFile, File outputFile, String[] codeTable, String path) throws IOException {
        if (inputFile.isDirectory())
            throw new IllegalArgumentException("Only to compress single file!");
        if (codeTable.length != 256)
            throw new IllegalArgumentException("Invalid Code Table!");

        System.out.println("compressing file " + inputFile.getPath() + "...");

        InputStream inputStream = new FileInputStream(inputFile);
        // 向输出文件中追加内容（append: true），考虑到在压缩多个文件时，不能覆盖之前的内容
        OutputStream outputStream = new FileOutputStream(outputFile, true);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        ZcsFileOutputStream zcsFileOutputStream = new ZcsFileOutputStream(bufferedOutputStream);

        // 写入编码信息
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.print("$FilePath:" + path + "\n");
        // 解压时用，大小到了就不再解码（说明剩下的bit都是不满一个byte的补0）
        printWriter.print("$FileSize:" + inputFile.length() + "\n");
        printWriter.print("$CodeTable:\n");
        for (String aCodeTable : codeTable) {
            printWriter.print(aCodeTable+"\n");
        }
        printWriter.print("$CodeHead\n");
        printWriter.flush();
        // 不能close，因为之后还要用这个outputStream

        // 写入编码区
        while (inputStream.available() != 0) {
            String code = codeTable[inputStream.read()];
            zcsFileOutputStream.write(code);
            // 这里不要flush，否则产生很可能产生bug
            // 可能产生bug的原因是：flush时，如果发现所写的code不是整数个byte，会在末尾补0
            // 而补0只能发生在编码区的末尾
            // 发生在中间时，显然会错误解码
        }
        // 必须flush
        zcsFileOutputStream.flush();
        inputStream.close();

        //打印编码结束
        printWriter.print("\n");
        printWriter.print("$CodeTail\n");
        printWriter.close();
    }

    // 在compress中用来判断是否是最外层文件夹
    private static boolean isFirstDir = true;
    private static int dirPathLen = 0;

    /**
     * @param inputFile 需要压缩的文件或文件夹
     * @param outputFile 输出文件
     * @throws IOException IOException
     * 压缩函数，可以压缩文件和文件夹
     */
    public static void compress(File inputFile, File outputFile) throws IOException {
        // 读取并写入文件信息
        if (isFirstDir) {
            if (outputFile.isDirectory())
                throw new IllegalArgumentException("outputFile should be a single file!");
            FileOutputStream outputStream = new FileOutputStream(outputFile, true);
            PrintWriter printWriter = new PrintWriter(outputStream);
            long fileSize = getDirSize(inputFile);
            System.out.print("FileSize: " + fileSize + "\n");
            dirPathLen = inputFile.getPath().length() - inputFile.getName().length();

            printWriter.print("$ZhuxiaoningCompressedFile\n");
            printWriter.print("$TotalSize:" + fileSize + "\n");
            printWriter.close();

            isFirstDir = false;
        }

        if (inputFile.isDirectory()) {
            // 逐个文件写入压缩编码
            File[] allFiles = inputFile.listFiles();
            for (File file : allFiles) {
                if (file.isDirectory()) {
                    compress(file, outputFile);
                }else {
                    String fileNameWithPath = file.getPath().substring(dirPathLen);
                    String[] codeTable = getFileCodeTable(file);
                    compressSingleFile(file, outputFile, codeTable, fileNameWithPath);
                }
            }
        }else if (inputFile.isFile()) {
            String fileNameWithPath = inputFile.getPath().substring(dirPathLen);
            String[] codeTable = getFileCodeTable(inputFile);
            compressSingleFile(inputFile, outputFile, codeTable, fileNameWithPath);
        }

    }

    /**
     * @param file 需要统计的文件
     * @return String[256] codeTable，codeTable[byte]就是相应byte的编码（由字符0和1组成的String）
     * @throws IOException IOException
     * 对输入的文件进行统计，返回编码表
     */
    private static String[] getFileCodeTable(File file) throws IOException {
        System.out.print("making codeTable for " + file.getPath() + "...");
        if (file.isDirectory())
            throw new IllegalArgumentException("Only to compress single file!");
        InputStream inputStream = new FileInputStream(file);
        int[] frequencyList = new int[256];
        while (inputStream.available() != 0) {
            frequencyList[inputStream.read()]++;
        }
        inputStream.close();

        CodeHuffTree codeHuffTree = new CodeHuffTree(frequencyList);
        String[] codeTable = codeHuffTree.getCodeTable();

        // 优化codeTable，把没用到的字节哈夫曼编码设置为空
        // TODO 解码时应忽略“”这个编码
        for (int i = 0; i < 256; i++) {
            if (frequencyList[i] == 0)
                codeTable[i] = "";
        }
        return codeTable;
    }

    /**
     * @param file 文件
     * @return 文件的大小或文件夹的总大小，以byte数表示
     */
    private static long getDirSize(File file) {

        if (file.exists()) {
            if (file.isDirectory()) {
                long size = 0;
                for (File childFile : file.listFiles()) {
                    size += getDirSize(childFile);
                }
                return size;
            }else {
                return file.length();
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0;
        }
    }
}
