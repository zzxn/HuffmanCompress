package core;

import core.dataStructure.CodeHuffTree;

import java.io.*;


/**
 * @author 16307110325
 * Created on 2017/10/19.
 */
public class HuffmanCompress {

    // 在compress中用来判断是否是最外层文件夹
    private static boolean isFirstDir = true;
    private static int dirPathLen = 0;
    public static final int FILE_BUFFER_SIZE = 1 << 26;

    /**
     * @param inputFile  需要压缩的文件
     * @param outputFile 压缩编码输出文件
     * @param codeTable  各个byte的编码对应表
     * @param path       解压缩后文件相对当前文件夹的相对路径
     * @throws IOException IOException
     *                     把单个文件进行压缩，并将压缩后编码与特定格式压缩信息写入输出文件
     */
    private static void compressSingleFile(File inputFile, File outputFile, int[][] codeTable, String path) throws IOException {
        if (inputFile.isDirectory())
            throw new IllegalArgumentException("Only to compressDir single file!");
        if (codeTable.length != 256)
            throw new IllegalArgumentException("Invalid Code Table!");

        System.out.print("compressing file ");
        System.out.print(inputFile.getPath());
        System.out.println("...");

        // 根据文件大小确定缓冲区大小
        long fileLength = inputFile.length();
        int bufferSize = (int)(fileLength < FILE_BUFFER_SIZE ? fileLength : FILE_BUFFER_SIZE);
        if (bufferSize == 0) bufferSize = 128;
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile), bufferSize);
        // 向输出文件中追加内容（append: true），考虑到在压缩多个文件时，不能覆盖之前的内容
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile, true), bufferSize);

        // 写入编码信息
        PrintWriter printWriter = new PrintWriter(bufferedOutputStream);
        printWriter.print("$FilePath:");
        printWriter.print(path);
        printWriter.print("\n");
        // 解压时用，大小到了就不再解码（说明剩下的bit都是不满一个byte的补0）
        printWriter.print("$FileSize:");
        printWriter.print(fileLength);
        printWriter.print("\n");
        printWriter.print("$CodeTable:\n");
        for (int[] aCodeTable : codeTable) {
            for (int i : aCodeTable)
                printWriter.print(i);
            printWriter.print("\n");
        }
        printWriter.print("$CodeHead\n");
        printWriter.flush();
        // 不能close，因为之后还要用这个outputStream

        // 写入编码区
        int buffer = 0;
        int count = 7;
        for (int nextByte = inputStream.read(); nextByte != -1; nextByte = inputStream.read()) {
            for (int bit : codeTable[nextByte]) {
                if (bit == 1) {
                    buffer = buffer | (1 << count);
                }
                //一个byte填满的时候，write这个byte
                if (count-- == -1) {
                    count = 7;
                    bufferedOutputStream.write(buffer);
                    buffer = 0;
                }
            }
        }
        // 必须flush
        // 如果一个byte没有写完，那么后面补零
        if (count != 7)
            bufferedOutputStream.write(buffer);
        bufferedOutputStream.flush();
        inputStream.close();

        //打印编码结束
        printWriter.print("\n");
        printWriter.print("$CodeTail\n");
        printWriter.close();
        System.out.println("compression finished");
    }

    /**
     * @param inputFile  需要压缩的文件或文件夹
     * @param outputFile 输出文件
     * @throws IOException IOException
     *                     压缩函数，可以压缩文件和文件夹
     */
    private static void compressDir(File inputFile, File outputFile) throws IOException {
        // 读取并写入文件信息
        if (isFirstDir) {
            if (outputFile.isDirectory())
                throw new IllegalArgumentException("outputFile should be a single file!");
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile, true), 512000);
            PrintWriter printWriter = new PrintWriter(outputStream);
            long fileSize = getDirSize(inputFile);
            dirPathLen = inputFile.getPath().length() - inputFile.getName().length();

            printWriter.print("$ZhuxiaoningCompressedFile\n");
            printWriter.print("$TotalSize:");
            printWriter.print(fileSize);
            printWriter.print("\n");
            printWriter.close();

            isFirstDir = false;
        }

        if (inputFile.isFile()) {
            String fileNameWithPath = inputFile.getPath().substring(dirPathLen);
            int[][] codeTable = getFileCodeTable(inputFile);
            compressSingleFile(inputFile, outputFile, codeTable, fileNameWithPath);
        } else {
            // 逐个文件写入压缩编码
            for (File file : inputFile.listFiles())
                compressDir(file, outputFile);
        }

    }

    public static void compress(File inputFile, File outputFile) throws IOException {
        compressDir(inputFile, outputFile);
        HuffmanCompress.reset();
    }

    /**
     * @param file 需要统计的文件
     * @return int[256][] codeTable，codeTable[byte]就是相应byte的编码（由0\1组成的int[]）
     * @throws IOException IOException
     *                     对输入的文件进行统计，返回编码表
     */

    private static int[][] getFileCodeTable(File file) throws IOException {
        if (file.isFile()) {
            int[] frequencyList = new int[256];
            long fileLength = file.length();
            int bufferSize = (int)(fileLength < FILE_BUFFER_SIZE ? fileLength : FILE_BUFFER_SIZE);
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file), bufferSize);
            for (int nextByte = inputStream.read(); nextByte != -1; nextByte = inputStream.read())
                frequencyList[inputStream.read()]++;
            inputStream.close();

            CodeHuffTree codeHuffTree = new CodeHuffTree(frequencyList);
            int[][] codeTable = codeHuffTree.getCodeTable();

            // 优化
            int[] voidArr = new int[0];
            for (int i = 0; i < 256; i++) {
                if (frequencyList[i] == 0)
                    codeTable[i] = voidArr;
            }

            return codeTable;
        } else {
            throw new IllegalArgumentException("Only for single file!");
        }
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
            } else {
                return file.length();
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0;
        }
    }

    private static void reset() {
        HuffmanCompress.isFirstDir = true;
        HuffmanCompress.dirPathLen = 0;
    }
}
