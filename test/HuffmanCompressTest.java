package test;

import core.HuffmanCompress;
import core.HuffmanDecompress;
import org.junit.Test;

import java.io.File;

/**
 * @author 16307110325
 * Created on 2017/10/20.
 */
public class HuffmanCompressTest {
    @Test
    public void compress() throws Exception {
//        File inputFile = new File("C:/Users/19098/Desktop/MytestCase");
//        File outputFile = new File("C:/Users/19098/Desktop/okTestCompress.zcs");
//        File inputFile2 = new File("C:/Users/19098/Desktop/MytestCase/13.jpg");
//        File outputFile2 = new File("C:/Users/19098/Desktop/okTestCompress2.zcs");
//        HuffmanCompress.compress(inputFile, outputFile);
//        HuffmanCompress.compress(inputFile2, outputFile2);
        Long timestart = System.currentTimeMillis();
//        File bigInputFile = new File("E:\\迅雷下载\\QQ8.9.5.exe");
//        File bigOutputFile = new File("E:\\迅雷下载\\qq.zcs");
//        HuffmanCompress.compress(bigInputFile, bigOutputFile);
        File inputFile = new File("C:/Users/19098/Desktop/Test Cases");
        File outputFile = new File("C:/Users/19098/Desktop/okTestCompress.zcs");
        HuffmanCompress.compress(inputFile, outputFile);
        Long timeend = System.currentTimeMillis();
        System.out.println((timeend - timestart) / 1000.0 / 60.0);
    }

    @Test
    public void compressAndDec() throws Exception {
//        File zcsFile = new File("C:/Users/19098/Desktop/okTestCompress.zcs");
//        File outputFile = new File("C:/Users/19098/Desktop/Test decompress");
//        File zcsFile2 = new File("C:/Users/19098/Desktop/okTestCompress2.zcs");
//        File outputFile2 = new File("C:/Users/19098/Desktop/Test decompress");
//        HuffmanDecompress.decompressFile(zcsFile, outputFile);
//        HuffmanDecompress.decompressFile(zcsFile2, outputFile2);
        Long timestart = System.currentTimeMillis();
//        File bigOutputFile = new File("E:\\迅雷下载\\qq.zcs");
//        File outputFile = new File("C:/Users/19098/Desktop/Test decompress");
//        HuffmanDecompress.decompressFile(bigOutputFile, outputFile);
        File zcsFile = new File("C:/Users/19098/Desktop/okTestCompress.zcs");
        File outputFile = new File("C:/Users/19098/Desktop/Test decompress");
        HuffmanDecompress.decompressFile(zcsFile, outputFile);
        Long timeend = System.currentTimeMillis();
        System.out.println((timeend - timestart) / 1000.0 / 60.0);
    }
}
