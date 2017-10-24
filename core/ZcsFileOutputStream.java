package core;

import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * @author 16307110325
 * Created on 2017/10/19.
 */
public class ZcsFileOutputStream {
    private BufferedOutputStream outputStream;
    private static int buffer = 0;
    private static int count = 0;

    public ZcsFileOutputStream(BufferedOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(String bitStr) throws IOException {
        int bit;
        for (int i = 0; i < bitStr.length(); i++) {
            bit = bitStr.charAt(i) - 48;
            if (bit == 1) {
                buffer = buffer | (1 << (7-count));
            } else if (bit == 0) {
                //do nothing
            } else {
                throw new IllegalArgumentException("bitStr should consist of only '0' and '1'");
            }
            count ++;

            //一个byte填满的时候，write这个byte
            if (count == 8) {
                count = 0;
                outputStream.write(buffer);
                buffer = 0;
            }
        }
    }

    public void flush() throws IOException {
        if (buffer != 0) {
            outputStream.write((byte)buffer);
        }
        outputStream.flush();
    }

    public void close() throws IOException {
        // 最后一码不足8个倍数时，后面补0
        // TODO 补0可能造成歧义，为此需要在解压缩信息中标清结束位置
        flush();
        outputStream.close();
    }
}
