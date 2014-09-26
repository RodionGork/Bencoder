package com.github.rodiongork.bencoder;

import org.junit.Assert;
import org.junit.Test;

public class BencGeneratorTest {

    @Test
    public void testSmoke() {
        ListNode root = new ListNode();
        root.add(new IntNode(7));
        DictNode dict = new DictNode();
        dict.put("first", new BytesNode("xyzzy"));
        dict.put("second", new IntNode(-314159));
        ListNode subList = new ListNode();
        subList.add(new BytesNode(":)"));
        dict.put("last", subList);
        root.add(dict);

        byte[] res = BencGenerator.treeToBytes(root);
        Assert.assertEquals("li7ed5:first5:xyzzy4:lastl2::)e6:secondi-314159eee", new String(res));
    }

    @Test
    public void testBijection(){
        String testString = "d4:listl5:black4:ruste1:xi15e1:yi41ee";
        BencNode tree = BencParser.bytesToTree(testString.getBytes());
        byte[] res = BencGenerator.treeToBytes(tree);
        Assert.assertEquals(testString, new String(res));
    }

}
