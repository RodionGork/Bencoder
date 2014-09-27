package com.github.rodiongork.bencoder.deserialize;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Map;

class TypeRef {
    
    private Class container;
    private TypeRef items;
    
    static TypeRef fromField(Field f) {
        ParameterizedType ptype;
        try {
            ptype = (ParameterizedType) f.getGenericType();
        } catch (Exception e) {
            return fromRaw(f.getType());
        }
        return fromParameterized(ptype);
    }
    
    private static TypeRef fromRaw(Class cls) {
        if (List.class.isAssignableFrom(cls) || Map.class.isAssignableFrom(cls)) {
            return new TypeRef(cls, new TypeRef(Object.class, null));
        } else {
            throw new RuntimeException("ListRef should be created for List or Map");
        }
    }
    
    private static TypeRef fromAny(Type ata) {
        if (ata instanceof ParameterizedType) {
            return fromParameterized((ParameterizedType) ata);
        } else if (ata instanceof WildcardType) {
            return fromWildcard((WildcardType) ata);
        } else {
            return new TypeRef((Class) ata, null);
        }
    }
    
    private static TypeRef fromWildcard(WildcardType wtype) {
        if (wtype.getUpperBounds().length == 0) {
            return new TypeRef(Object.class, null);
        } else {
            return fromAny(wtype.getUpperBounds()[0]);
        }
    }
    
    private static TypeRef fromParameterized(ParameterizedType ptype) {
        Type ata;
        Class owner = (Class) ptype.getRawType();
        if (List.class.isAssignableFrom(owner)) {
            ata = ptype.getActualTypeArguments()[0];
        } else if (Map.class.isAssignableFrom(owner)) {
            ata = ptype.getActualTypeArguments()[1];
        } else {
            throw new RuntimeException("Only Maps and Lists are supported");
        }
        return new TypeRef(owner, fromAny(ata));
    }
    
    TypeRef(Class container, TypeRef items) {
        this.container = container;
        this.items = items;
    }
    
    @Override
    public String toString() {
        if (items != null) {
            return String.format("(%s of %s)", container.getSimpleName(), items.toString());
        } else {
            return container.getSimpleName();
        }
    }
}
