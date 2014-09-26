package com.github.rodiongork.bencoder;

import org.junit.Test;
import org.junit.Assert;

public class BencParserTest {

    BencParser parser;
    
    @Test
    public void testReadIntNode() {
        initWith("i15e");
        IntNode node = parser.readIntNode();
        Assert.assertEquals(15L, node.getValue());
    }
    
    @Test(expected = BencParser.ParseException.class)
    public void testReadIntNodeFail() {
        initWith("i15");
        parser.readIntNode();
    }
    
    @Test
    public void testReadBytesNode() {
        initWith("3:Az!");
        BytesNode node = parser.readBytesNode();
        Assert.assertEquals("Az!", node.getValueAsString());
    }
    
    @Test(expected = BencParser.ParseException.class)
    public void testReadBytesNodeFail() {
        initWith("3:Az");
        parser.readBytesNode();
    }
    
    private void initWith(String input) {
        parser = new BencParser(input.getBytes());
    }

}

