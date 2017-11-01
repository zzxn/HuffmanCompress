package Test;

import core.HuffmanDecompress;

import java.io.File;
import java.io.IOException;

/**
 * @author 16307110325
 * Created on 2017/11/1.
 */
public class TestDecompress {
    public static void main(String[] args) throws IOException {
        File inputFile = new File("C:\\Users\\19098\\Desktop\\-1925701395.zcs");
        File outDir = new File("C:\\Users\\19098\\Desktop");
        HuffmanDecompress.decompressFile(inputFile, outDir);
    }
}
