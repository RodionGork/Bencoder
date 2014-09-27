package com.github.rodiongork.bencoder.deserialize;

import com.github.rodiongork.bencoder.represent.ListNode;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ListNodeConverter implements NodeConverter<ListNode> {
    
    private BencDeserializer deserializer;

    public ListNodeConverter(BencDeserializer deserializer) {
        this.deserializer = deserializer;
    }

    public <Y> Y convert(ListNode node, Class<Y> cls) {
        if (Object.class.equals(cls)) {
            cls = (Class<Y>) List.class;
        }
        if (cls.isArray()) {
            return (Y) convertArray(node, cls.getComponentType());
        } else if (List.class.isAssignableFrom(cls)) {
            //convertList(node, cls.isInterface() ? ArrayList.class : cls);
            throw new BencDeserializer.DeserializationException("List could be only deserialized for a pojo field");
        }
        throw new BencDeserializer.DeserializationException("List should be deserialized to list or array");
    }
    
    private Object convertArray(ListNode node, Class<?> itemClass) {
        int size = node.size();
        Object array = Array.newInstance(itemClass, size);
        for (int i = 0; i < size; i++) {
            Array.set(array, i, deserializer.deserialize(node.get(i), itemClass));
        }
        return array;
    }
}
