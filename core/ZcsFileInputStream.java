package core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author 16307110325
 * Created on 2017/10/19.
 * 利用BitFileInputStream扫描编码区并解码
 * 利用FileInputStream读取非编码区信息
 * 在编码区和非编码区的行为不同
 */
public class ZcsFileInputStream {
    private FileInputStream fileInputStream;
    private BitFileInputStream bitFileInputStream;
    private MyScanner scanner;
    // 标明是否处于编码区的flag
    // 能自主判断进入编码区，但不能自主判断走出编码区
    // TODO 需要外部利用skipToNoCode（）来走出编码区
    private boolean inCodeArea = false;

    public ZcsFileInputStream(FileInputStream fileInputStream) throws IOException {
        this.fileInputStream = fileInputStream;
        this.bitFileInputStream = new BitFileInputStream(fileInputStream);
        this.scanner = new MyScanner(fileInputStream);
    }

    public String read() throws IOException {
        if (fileInputStream.available() != 0) {
            if (!inCodeArea) {
                String next = scanner.nextLine();
                if (next.matches("^\\$CodeHead$")) {
                    inCodeArea = true;
                    // 不需跳过换行符，scanner已经做到了这一点
                }
                return next;
            }else {
                return (new Integer(bitFileInputStream.read())).toString();
            }
        }
        return null;
    }

    public void skipToNocode() throws IOException {
        if (!inCodeArea)
            throw new IOException("only skip when in code area");
        inCodeArea = false;
    }

    public int available() throws IOException {
        return fileInputStream.available();
    }

}
