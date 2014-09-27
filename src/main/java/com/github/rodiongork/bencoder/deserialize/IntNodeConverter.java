package com.github.rodiongork.bencoder.deserialize;

import com.github.rodiongork.bencoder.represent.IntNode;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class IntNodeConverter implements NodeConverter<IntNode> {
    
    private final static Map<Class<? extends Number>, Class<? extends Number>> WRAPPERS;
    
    static {
        WRAPPERS = new HashMap<>();
        WRAPPERS.put(int.class, Integer.class);
        WRAPPERS.put(long.class, Long.class);
        WRAPPERS.put(short.class, Short.class);
        WRAPPERS.put(byte.class, Byte.class);
        WRAPPERS.put(float.class, Float.class);
        WRAPPERS.put(double.class, Double.class);
    }
    
    IntNodeConverter() {
    }
    
    @Override
    public <Y> Y convert(IntNode node, Class<Y> cls) {
        if (Object.class.equals(cls) || Number.class.equals(cls)) {
            cls = (Class<Y>) Long.class;
        } else if (cls.isPrimitive()) {
            cls = (Class<Y>) WRAPPERS.get((Class<? extends Number>) cls);
            if (cls == null) {
                throw new BencDeserializer.DeserializationException(
                        "Number should be deserialized to numeric primitive field");
            }
        }
        if (!Number.class.isAssignableFrom(cls)) {
            throw new BencDeserializer.DeserializationException(
                    "Number should be deserialized to numeric field");
        }
        String val = Long.toString(node.getValue());
        try {
            Constructor c = cls.getConstructor(String.class);
            return (Y) c.newInstance(val);
        } catch (NoSuchMethodException | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new BencDeserializer.DeserializationException(
                    "Unexpected error: " + e);
        }
    }
    
}
