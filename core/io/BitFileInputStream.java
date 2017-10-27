package core.io;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * @author 16307110325
 * Created on 2017/10/19.
 * 这个类用来在读取zcs文件编码区的时候，逐个bit地读取编码
 * 被zcsFileInputStream调用
 * 经测试这个类没问题
 */
public class BitFileInputStream {
    private BufferedInputStream inputStream;
    private byte thisByte;
    private int bitCount;

    public BitFileInputStream(BufferedInputStream inputStream) {
        this.inputStream = inputStream;
        thisByte = 0;
        bitCount = 0;
    }

    public int read() throws IOException {
        if (bitCount == 0) {
            thisByte = (byte) inputStream.read();
        }

        int thisBit = (thisByte >> (7 - bitCount)) & 1;

        bitCount++;
        if (bitCount == 8)
            bitCount = 0;

        return thisBit;
    }

    public void reset() {
        thisByte = 0;
        bitCount = 0;
    }

    public long available() throws IOException {
        if (inputStream.available() == 0 && bitCount == 0)
            return 0;
        else
            return inputStream.available() * 8 + 8 - bitCount;
    }

    public void close() throws IOException {
        inputStream.close();
    }

}
