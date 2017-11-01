package core.io;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * @author 16307110325
 * Created on 2017/10/25.
 */
public class MyScanner {
    private StringBuilder buf;
    private BufferedInputStream fileInputStream;

    public MyScanner(BufferedInputStream fileInputStream) {
        buf = new StringBuilder();
        this.fileInputStream = fileInputStream;
    }

    public String nextLine() throws IOException {
        for (int nextByte = fileInputStream.read(); nextByte != -1; nextByte = fileInputStream.read()) {
            if (nextByte != 0x0A) {
                buf.append((char) nextByte);
            } else {
                // 把游标放到换行符后面位置
                String st = buf.toString();
                buf = new StringBuilder();
                return st;
            }
        }
        throw new IOException();
    }
}
