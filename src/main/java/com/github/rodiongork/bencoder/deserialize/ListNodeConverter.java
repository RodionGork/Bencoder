package com.github.rodiongork.bencoder.deserialize;

import com.github.rodiongork.bencoder.represent.BencNode;
import com.github.rodiongork.bencoder.represent.ListNode;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            throw new BencDeserializer.DeserializationException(
                    "List sould be only deserialized for a pojo field or via TypeRef");
        }
        throw new BencDeserializer.DeserializationException("List should be deserialized to list or array");
    }
    
    public List convert(ListNode node, TypeRef ref) {
        Class listClass = suitableClass(ref.getSelf());
        List list = instantiateList(listClass);
        fillList(list, node, ref.getItems());
        return list;
    }
    
    private Class suitableClass(Class listClass) {
        if (!List.class.isAssignableFrom(listClass)) {
            if (listClass.isAssignableFrom(ArrayList.class)) {
                return ArrayList.class;
            } else {
                throw new BencDeserializer.DeserializationException(
                        "TypeRef is expected to hold the List type");
            }
        }
        return listClass.isInterface() ? ArrayList.class : listClass;
    }
    
    private List instantiateList(Class<? extends List> cls) {
        try {
            return cls.newInstance();
        } catch (Exception e) {
            throw new BencDeserializer.DeserializationException(
                    "Could not instantiate list class " + cls);
        }
    }
    
    private void fillList(List list, ListNode node, TypeRef itemRef) {
        Class itemClass = itemRef.getSelf();
        if (List.class.isAssignableFrom(itemClass) || Map.class.isAssignableFrom(itemClass)) {
            for (BencNode item : node) {
                list.add(deserializer.deserialize(item, itemRef));
            }
        } else {
            for (BencNode item : node) {
                list.add(deserializer.deserialize(item, itemClass));
            }
        }
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
