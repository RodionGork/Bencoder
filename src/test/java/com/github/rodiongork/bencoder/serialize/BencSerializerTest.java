package com.github.rodiongork.bencoder.serialize;

import java.util.Arrays;
import java.util.List;

import com.github.rodiongork.bencoder.represent.BencGenerator;
import org.junit.Assert;
import org.junit.Test;

public class BencSerializerTest {

    @Test
    public void testArray() {
        Object[] array = {Integer.valueOf(17), "Funny"};
        byte[] res = BencGenerator.treeToBytes(new BencSerializer().serialize(array));
        Assert.assertEquals("li17e5:Funnye", new String(res));
    }

    @Test
    public void testPrimitiveArray() {
        int[] array = {48, 50};
        byte[] res = BencGenerator.treeToBytes(new BencSerializer().serialize(array));
        Assert.assertEquals("li48ei50ee", new String(res));
    }

    @Test
    public void testList() {
        List<Object> array = Arrays.<Object>asList(-8, "How");
        byte[] res = BencGenerator.treeToBytes(new BencSerializer().serialize(array));
        Assert.assertEquals("li-8e3:Howe", new String(res));
    }

    @Test
    public void testCustomPojo() {
        class Pojo {
            private int x = 5;
            private String msg = "zlo";
        }
        byte[] res = BencGenerator.treeToBytes(new BencSerializer().serialize(new Pojo()));
        Assert.assertEquals("d3:msg3:zlo1:xi5ee", new String(res));
    }

}
