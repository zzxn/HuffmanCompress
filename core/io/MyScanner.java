package core.io;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author 16307110325
 * Created on 2017/10/25.
 */
public class MyScanner {
    private StringBuilder buf;
    private FileInputStream fileInputStream;

    public MyScanner(FileInputStream fileInputStream) {
        buf = new StringBuilder();
        this.fileInputStream = fileInputStream;
    }

    public String nextLine() throws IOException {
        while (fileInputStream.available() != 0) {
            int nextByte = fileInputStream.read();
            if (nextByte != 0x0A) {
                buf.append((char) nextByte);
            } else {
                // 把游标放到换行符后面位置
                String st = buf.toString();
                buf = new StringBuilder();
                return st;
            }
        }
        return buf.toString();
    }

    public String next() throws IOException {
        return nextLine();
    }
}
