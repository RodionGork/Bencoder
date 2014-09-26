package com.github.rodiongork.bencoder.serialize;

import java.lang.reflect.Field;

import com.github.rodiongork.bencoder.represent.DictNode;

public class PojoConverter implements ObjectConverter<Object, DictNode> {

    private BencSerializer serializer;

    PojoConverter(BencSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public DictNode convert(Object object) {
        DictNode dict = new DictNode();
        for (Class cls = object.getClass(); !cls.equals(Object.class); cls = cls.getSuperclass()) {
            for (Field field : cls.getDeclaredFields()) {
                Object value = getFieldValue(field, object);
                if (value != null && !forbiddenField(field)) {
                    dict.put(field.getName(), serializer.serialize(value));
                }
            }
        }
        return dict;
    }

    private boolean forbiddenField(Field field) {
        return "this$0".equals(field.getName());
    }

    private Object getFieldValue(Field field, Object object) {
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}
