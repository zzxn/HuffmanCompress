package Test;

import core.HuffmanCompress;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 16307110325
 * Created on 2017/10/31.
 */
public class Test1 {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\19098\\Desktop\\term3\\数据结构与算法\\project\\project_1\\Project 1\\Test Cases\\test1 - single file");
        File outFile = new File("C:\\Users\\19098\\Desktop\\term3\\数据结构与算法\\project\\project_1\\Project 1\\Test Cases\\test1 - single file\\test.zcs");
        File data1 = new File("C:\\Users\\19098\\Desktop\\term3\\数据结构与算法\\project\\project_1\\Project 1\\Test Cases\\data1.txt");
        PrintWriter printWriter = new PrintWriter(data1);
        for (File aFile : file.listFiles()) {
            long time = System.currentTimeMillis();
            HuffmanCompress.compress(aFile, outFile);
            time = System.currentTimeMillis() - time;
            double outlen = outFile.length();
            double aFilelen = aFile.length();
            printWriter.printf("%s\t%d\t%.2f%%\r\n", aFile.getName(), time, (1-outlen/aFilelen)*100);
            outFile.delete();
        }
        printWriter.close();
    }
}
