package test;

import core.HuffmanCompress;
import core.HuffmanDecompress;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.junit.Assert.*;

/**
 * @author 16307110325
 * Created on 2017/10/20.
 */
public class HuffmanCompressTest {
    @Test
    public void compress() throws Exception {
        File inputFile = new File("C:/Users/19098/Desktop/0.txt");
        File outputFile = new File("C:/Users/19098/Desktop/okTestCompress.zcs");
        HuffmanCompress.compress(inputFile, outputFile);
    }

    @Test
    public void compressAndDec() throws Exception {
        File zcsFile = new File("C:/Users/19098/Desktop/okTestCompress.zcs");
        File outputFile = new File("C:/Users/19098/Desktop/Test decompress");
        HuffmanDecompress.decompressFile(zcsFile, outputFile);
    }
}
