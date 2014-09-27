package com.github.rodiongork.bencoder;

import java.util.Map;
import java.util.TreeMap;
import org.junit.Assert;
import org.junit.Test;

public class BencodeMapperTest {
    
    private BencodeMapper mapper = new BencodeMapper();
    
    public static class Person {
        private String name;
        private Long money;
        private byte[] signature;
        private Map<String, Short> children;
    }
    
    @Test
    public void testTypeless() {
        String bencode = "d3:agei25e8:childrenl3:Doe5:Franke4:name4:Jesse";
        Object obj = mapper.readValue(bencode.getBytes(), Object.class);
        //System.out.println(obj); // prints: {children=[Doe, Frank], name=Jess, age=25}
        String res = new String(mapper.writeValue(obj));
        Assert.assertEquals(bencode, res);
    }
    
    @Test
    public void testPojo() {
        Person person = new Person();
        person.name = "Rich Perkins";
        person.money = 987654321L;
        person.signature = "e8.z|1x65%41?'2b".getBytes();
        person.children = new TreeMap();
        person.children.put("Ian", (short) 1959);
        person.children.put("Franchesca", (short) 1973);
        byte[] bencode = mapper.writeValue(person);
        System.err.println(new String(bencode));
        Person person2 = mapper.readValue(bencode, Person.class);
        Assert.assertEquals(person.name, person2.name);
        Assert.assertEquals(person.money, person2.money);
        Assert.assertArrayEquals(person.signature, person2.signature);
        Assert.assertEquals(person.children, person2.children);
    }

}

