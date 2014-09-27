package com.github.rodiongork.bencoder.deserialize;

import com.github.rodiongork.bencoder.represent.BytesNode;
import com.github.rodiongork.bencoder.represent.IntNode;
import org.junit.Assert;
import org.junit.Test;

public class BencDeserializerTest {
    
    private BencDeserializer deserializer = new BencDeserializer();
    
    @Test
    public void testIntDeserialization() {
        Object res = deserializer.deserialize(new IntNode(-5), int.class);
        Assert.assertEquals(Integer.valueOf(-5), res);
        res = deserializer.deserialize(new IntNode(8), Double.class);
        Assert.assertEquals(8.0, ((Double) res).doubleValue(), 1e-10);
    }
    
    @Test
    public void testBytesDeserialization() {
        Object res = deserializer.deserialize(new BytesNode("XY"), byte[].class);
        Assert.assertEquals("XY", new String((byte[]) res));
        res = deserializer.deserialize(new BytesNode("test"), String.class);
        Assert.assertEquals("test", res);
    }
    
}
