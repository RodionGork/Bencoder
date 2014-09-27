package com.github.rodiongork.bencoder.deserialize;

import com.github.rodiongork.bencoder.represent.BencNode;
import com.github.rodiongork.bencoder.represent.DictNode;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class DictNodeConverter implements NodeConverter<DictNode> {
    
    private BencDeserializer deserializer;

    public DictNodeConverter(BencDeserializer deserializer) {
        this.deserializer = deserializer;
    }
    
    public <Y> Y convert(DictNode node, Class<Y> cls) {
        return convertPojo(node, cls);
    }
    
    public Map convert(DictNode node, TypeRef ref) {
        Class mapClass = suitableClass(ref.getSelf());
        Map map = instantiateMap(mapClass);
        fillMap(map, node, ref.getItems());
        return map;
    }
    
    private Class suitableClass(Class mapClass) {
        if (!Map.class.isAssignableFrom(mapClass)) {
            if (mapClass.isAssignableFrom(Map.class)) {
                return HashMap.class;
            } else {
                throw new BencDeserializer.DeserializationException(
                        "TypeRef is expected to hold the Map type");
            }
        }
        if (mapClass.isInterface()) {
            return SortedMap.class.isAssignableFrom(mapClass) ? TreeMap.class : HashMap.class;
        }
        return mapClass;
    }
    
    private Map instantiateMap(Class<? extends Map> cls) {
        try {
            return cls.newInstance();
        } catch (Exception e) {
            throw new BencDeserializer.DeserializationException(
                    "Could not instantiate map class " + cls);
        }
    }
    
    private void fillMap(Map map, DictNode node, TypeRef itemRef) {
        Class itemClass = itemRef.getSelf();
        if (List.class.isAssignableFrom(itemClass) || Map.class.isAssignableFrom(itemClass)) {
            for (Map.Entry<String, BencNode> entry : node.entrySet()) {
                map.put(entry.getKey(), deserializer.deserialize(entry.getValue(), itemRef));
            }
        } else {
            for (Map.Entry<String, BencNode> entry : node.entrySet()) {
                map.put(entry.getKey(), deserializer.deserialize(entry.getValue(), itemClass));
            }
        }
    }
    
    private <Y> Y convertPojo(DictNode node, Class<Y> cls) {
        Y pojo;
        try {
            pojo = cls.newInstance();
        } catch (Exception e) {
            throw new BencDeserializer.DeserializationException(
                    "Could not instantiate " + cls);
        }
        copyFields(node, pojo);
        return pojo;
    }
    
    private void copyFields(DictNode node, Object pojo) {
        for (Class cls = pojo.getClass(); !cls.equals(Object.class); cls = cls.getSuperclass()) {
            for (Field field : cls.getDeclaredFields()) {
                BencNode valueNode = node.get(field.getName());
                if (valueNode != null) {
                    setFieldValue(pojo, field, valueNode);
                }
            }
        }
    }
    
    private void setFieldValue(Object pojo, Field field, BencNode valueNode) {
        Class fieldType = field.getType();
        Object value;
        if (List.class.isAssignableFrom(fieldType) || Map.class.isAssignableFrom(fieldType)) {
            TypeRef ref = TypeRef.fromField(field);
            value = deserializer.deserialize(valueNode, ref);
        } else {
            value = deserializer.deserialize(valueNode, fieldType);
        }
        try {
            field.setAccessible(true);
            field.set(pojo, value);
        } catch (Exception e) {
            throw new BencDeserializer.DeserializationException(
                    String.format("Error assigning value to %s#%s %s",
                    pojo.getClass(), field.getName(), e));
        }
    }
}
