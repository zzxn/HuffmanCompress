package core.io;

import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * @author 16307110325
 * Created on 2017/10/19.
 */
public class ZcsFileOutputStream {
    private static int buffer = 0;
    private static int count = 0;
    private BufferedOutputStream outputStream;

    public ZcsFileOutputStream(BufferedOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(String bitStr) throws IOException {
        int bit;
        for (int i = 0; i < bitStr.length(); i++) {
            bit = bitStr.charAt(i) - 48;
            if (bit == 1) {
                buffer = buffer | (1 << (7 - count));
            } else if (bit == 0) {
                //do nothing
            } else {
                throw new IllegalArgumentException("bitStr should consist of only '0' and '1'");
            }
            count++;

            //一个byte填满的时候，write这个byte
            if (count == 8) {
                count = 0;
                outputStream.write(buffer);
                buffer = 0;
            }
        }
    }

    public void flush() throws IOException {
        // 如果一个byte没有写完，那么后面补零
        if (count != 0) {
            outputStream.write((byte) buffer);
            count = 0;
            buffer = 0;
        }
        outputStream.flush();
    }

    public void close() throws IOException {
        // 最后一码不足8个倍数时，后面补0
        flush();
        outputStream.close();
    }
}
