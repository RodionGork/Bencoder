package com.github.rodiongork.bencoder.deserialize;

import com.github.rodiongork.bencoder.represent.BencNode;
import com.github.rodiongork.bencoder.represent.DictNode;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class DictNodeConverter implements NodeConverter<DictNode> {
    
    private BencDeserializer deserializer;

    public DictNodeConverter(BencDeserializer deserializer) {
        this.deserializer = deserializer;
    }
    
    public <Y> Y convert(DictNode node, Class<Y> cls) {
        return convertPojo(node, cls);
    }
    
    public Map convert(DictNode node, TypeRef ref) {
        return null;
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
            value = deserializer.deserializeCollection(valueNode, ref);
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
