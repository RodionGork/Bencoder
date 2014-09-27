package com.github.rodiongork.bencoder.deserialize;

import com.github.rodiongork.bencoder.represent.BytesNode;
import com.github.rodiongork.bencoder.represent.DictNode;
import com.github.rodiongork.bencoder.represent.IntNode;
import com.github.rodiongork.bencoder.represent.ListNode;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class BencDeserializerTest {
    
    private BencDeserializer deserializer = new BencDeserializer();
    
    public static class Pojo {
        private int intField;
        private String strField;
        private int[] array;
    }
    
    public static class Container {
        private List<Long> longList;
        private List<Integer> intList;
        private LinkedList<? extends Number> numList;
        private List objList;
        private List<ArrayList<Integer>> intListList;
        private Map<String, Short> shortMap;
        private Map<String, List<String>> strListMap;
    }
        
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
    
    @Test
    public void testArrayDeserialization() {
        ListNode node = new ListNode();
        node.add(new IntNode(355));
        node.add(new IntNode(113));
        Object res = deserializer.deserialize(node, Long[].class);
        Assert.assertEquals(Long[].class, res.getClass());
        Assert.assertArrayEquals(new Long[]{355L, 113L}, (Long[]) res);
        res = deserializer.deserialize(node, short[].class);
        Assert.assertEquals(short[].class, res.getClass());
        Assert.assertArrayEquals(new short[]{355, 113}, (short[]) res);
    }
    
    @Test
    public void testPojoDeserialization() {
        DictNode node = new DictNode();
        node.put("intField", new IntNode(18));
        node.put("strField", new BytesNode("well done"));
        node.put("array", makeIntListNode(271828));
        Object res = deserializer.deserialize(node, Pojo.class);
        Assert.assertEquals(Pojo.class, res.getClass());
        Pojo pojo = (Pojo) res;
        Assert.assertEquals(18, pojo.intField);
        Assert.assertEquals("well done", pojo.strField);
        Assert.assertArrayEquals(new int[]{271828}, pojo.array);
    }
    
    @Test
    public void testListDeserialization() {
        DictNode node = new DictNode();
        node.put("intList", makeIntListNode(1, 2, 3));
        node.put("longList", makeIntListNode(2, 3, 5));
        node.put("numList", makeIntListNode(3, 5, 8));
        node.put("objList", makeIntListNode(5, 8, 13));
        ListNode subList = new ListNode();
        subList.add(node.get("intList"));
        subList.add(node.get("objList"));
        node.put("intListList", subList);
        Container cont = deserializer.deserialize(node, Container.class);
        Assert.assertEquals(Arrays.asList(1, 2, 3), cont.intList);
        Assert.assertEquals(Arrays.asList(2L, 3L, 5L), cont.longList);
        Assert.assertEquals(Arrays.asList(3L, 5L, 8L), cont.numList);
        Assert.assertEquals(Arrays.asList(5L, 8L, 13L), cont.objList);
        Assert.assertEquals(Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(5, 8, 13)), cont.intListList);
    }
    
    @Test
    public void testMapDeserialization() {
        DictNode node = new DictNode();
        DictNode map = new DictNode();
        map.put("val", new IntNode(432));
        node.put("shortMap", map);
        map = new DictNode();
        ListNode list = new ListNode();
        list.add(new BytesNode("thing"));
        map.put("some", list);
        node.put("strListMap", map);
        Container cont = deserializer.deserialize(node, Container.class);
        Assert.assertEquals(Collections.singletonMap("val", Short.valueOf((short) 432)), cont.shortMap);
        Assert.assertEquals(Collections.singletonMap("some", Arrays.asList("thing")), cont.strListMap);
    }
    
    private ListNode makeIntListNode(int ... values) {
        ListNode list = new ListNode();
        for (int x : values) {
            list.add(new IntNode(x));
        }
        return list;
    }
    
}
