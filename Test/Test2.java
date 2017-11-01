package Test;

import core.HuffmanCompress;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 16307110325
 * Created on 2017/10/31.
 */
public class Test2 {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\19098\\Desktop\\term3\\数据结构与算法\\project\\project_1\\Project 1\\Test Cases\\test2 - folder");
        File outFile = new File("C:\\Users\\19098\\Desktop\\term3\\数据结构与算法\\project\\project_1\\Project 1\\Test Cases\\test1 - single file\\test.zcs");
        File data1 = new File("C:\\Users\\19098\\Desktop\\term3\\数据结构与算法\\project\\project_1\\Project 1\\Test Cases\\data2.txt");
        PrintWriter printWriter = new PrintWriter(data1);
        for (File aFile : file.listFiles()) {
            long time = System.currentTimeMillis();
            HuffmanCompress.compress(aFile, outFile);
            time = System.currentTimeMillis() - time;
            double outlen = outFile.length();
            double aFilelen = getDirSize(aFile);
            System.out.println(outlen);
            System.out.println(aFilelen);
            printWriter.printf("%s\t%d\t%.2f%%\r\n", aFile.getName(), time, (1-(outlen/aFilelen))*100);
            outFile.delete();
        }
        printWriter.close();
    }
    public static double getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {
                double size = (double) file.length();
                return size;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }
}

