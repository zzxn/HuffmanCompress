package test;

import java.io.IOException;

/**
 * @author 16307110325
 * Created on 2017/10/19.
 */
public class Pubtest {
    public static void main(String[] args) throws IOException {

        long t1 = System.nanoTime();
        System.out.print("Adsfd" + "bsdfsdf");
        System.out.println("Time " + (System.nanoTime() - t1));

        t1 = System.nanoTime();
        System.out.print("Adsfd");
        System.out.print("bsdfsdf");
        System.out.println("Time " + (System.nanoTime() - t1));
    }
}
