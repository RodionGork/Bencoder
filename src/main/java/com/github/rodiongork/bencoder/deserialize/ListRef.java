/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.rodiongork.bencoder.deserialize;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Map;

class ListRef {
    
    private Class container;
    private ListRef items;
    
    static ListRef fromField(Field f) {
        ParameterizedType ptype;
        try {
            ptype = (ParameterizedType) f.getGenericType();
        } catch (Exception e) {
            return fromRaw(f.getType());
        }
        return fromParameterized(ptype);
    }
    
    private static ListRef fromRaw(Class cls) {
        if (List.class.isAssignableFrom(cls) || Map.class.isAssignableFrom(cls)) {
            return new ListRef(cls, new ListRef(Object.class, null));
        } else {
            throw new RuntimeException("ListRef should be created for List or Map");
        }
    }
    
    private static ListRef fromAny(Type ata) {
        if (ata instanceof ParameterizedType) {
            return fromParameterized((ParameterizedType) ata);
        } else if (ata instanceof WildcardType) {
            return fromWildcard((WildcardType) ata);
        } else {
            return new ListRef((Class) ata, null);
        }
    }
    
    private static ListRef fromWildcard(WildcardType wtype) {
        if (wtype.getUpperBounds().length == 0) {
            return new ListRef(Object.class, null);
        } else {
            return fromAny(wtype.getUpperBounds()[0]);
        }
    }
    
    private static ListRef fromParameterized(ParameterizedType ptype) {
        Type ata;
        Class owner = (Class) ptype.getRawType();
        if (List.class.isAssignableFrom(owner)) {
            ata = ptype.getActualTypeArguments()[0];
        } else if (Map.class.isAssignableFrom(owner)) {
            ata = ptype.getActualTypeArguments()[1];
        } else {
            throw new RuntimeException("Only Maps and Lists are supported");
        }
        return new ListRef(owner, fromAny(ata));
    }
    
    ListRef(Class container, ListRef items) {
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
