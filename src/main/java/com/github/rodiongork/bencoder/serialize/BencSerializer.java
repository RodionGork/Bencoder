package com.github.rodiongork.bencoder.serialize;

import java.util.List;
import java.util.Map;

import com.github.rodiongork.bencoder.represent.BencNode;
import com.sun.xml.internal.ws.encoding.soap.SerializationException;

public class BencSerializer {

    private NumberConverter numberConverter = new NumberConverter();

    private StringConverter stringConverter = new StringConverter();

    private ListConverter listConverter = new ListConverter(this);

    private ArrayConverter arrayConverter = new ArrayConverter(this);

    private MapConverter mapConverter = new MapConverter(this);

    private PojoConverter pojoConverter = new PojoConverter(this);

    public BencNode serialize(Object value) {
        return chooseConverter(value.getClass()).convert(value);
    }

    private ObjectConverter chooseConverter(Class cls) {
        if (cls.equals(Float.class) || cls.equals(Double.class)) {
            throw new SerializationException("Float values should not be serialized!");
        } else if (Number.class.isAssignableFrom(cls)) {
            return numberConverter;
        } else if (CharSequence.class.isAssignableFrom(cls)) {
            return stringConverter;
        } else if (List.class.isAssignableFrom(cls)) {
            return listConverter;
        } else if (cls.isArray()) {
            return arrayConverter;
        } else if (Map.class.isAssignableFrom(cls)) {
            return mapConverter;
        }
        return pojoConverter;
    }

    public static class SerializationException extends RuntimeException {
        public SerializationException(String s) {
            super(s);
        }
    }

}
