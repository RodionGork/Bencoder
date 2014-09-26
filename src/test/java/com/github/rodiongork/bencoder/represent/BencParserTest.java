package com.github.rodiongork.bencoder.represent;

import com.github.rodiongork.bencoder.represent.BencParser;
import com.github.rodiongork.bencoder.represent.BytesNode;
import com.github.rodiongork.bencoder.represent.DictNode;
import com.github.rodiongork.bencoder.represent.IntNode;
import com.github.rodiongork.bencoder.represent.ListNode;
import org.junit.Test;
import org.junit.Assert;

public class BencParserTest {

    BencParser parser;
    
    @Test
    public void testReadIntNode() {
        initWith("i15e");
        IntNode node = parser.readIntNode();
        Assert.assertEquals(15, node.getValue());
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
    
    @Test
    public void testListNode() {
        initWith("li-2e2: .e");
        ListNode list = parser.readListNode();
        Assert.assertEquals(2, list.size());
        IntNode node0 = (IntNode) list.get(0);
        BytesNode node1 = (BytesNode) list.get(1);
        Assert.assertEquals(-2, node0.getValue());
        Assert.assertEquals(" .", node1.getValueAsString());
    }
    
    @Test
    public void testDictNode() {
        initWith("d3:zloi0e4:mlin2:yoe");
        DictNode dict = parser.readDictNode();
        Assert.assertEquals(2, dict.size());
        IntNode node0 = (IntNode) dict.get("zlo");
        BytesNode node1 = (BytesNode) dict.get("mlin");
        Assert.assertEquals(0, node0.getValue());
        Assert.assertEquals("yo", node1.getValueAsString());
    }
    private void initWith(String input) {
        parser = new BencParser(input.getBytes());
    }

}

