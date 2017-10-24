package test;

import core.dataStructure.CodeHuffTree;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * @author 16307110325
 * Created on 2017/10/23.
 */
public class CodeHuffTreeTest {
    int[] ft = {1,2,3,4,5};
    CodeHuffTree ct = new CodeHuffTree(ft, 5);

    @Test
    public void baseTest() {
        String[] tb = ct.getCodeTable();
        for(int i = 0; i < tb.length; i++)
            System.out.println(tb[i]);
    }

}
