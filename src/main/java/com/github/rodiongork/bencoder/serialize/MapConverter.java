package com.github.rodiongork.bencoder.serialize;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.github.rodiongork.bencoder.represent.DictNode;

public class MapConverter implements ObjectConverter<Map, DictNode> {

    private BencSerializer serializer;

    MapConverter(BencSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public DictNode convert(Map map) {
        DictNode dict = new DictNode();
        for (Object key : map.keySet()) {
            if (key instanceof CharSequence) {
                dict.put(key.toString(), serializer.serialize(map.get(key)));
            } else {
                throw new BencSerializer.SerializationException("Map with non-string key should not be serialized!");
            }
        }
        return dict;
    }
}
