package com.github.rodiongork.bencoder;

import java.util.Arrays;

public class BencParser {
    
    private byte[] data;
    private int pos;
    
    public BencParser(byte[] data) {
        this.data = Arrays.copyOf(data, data.length);
        pos = 0;
    }
    
    public BencNode readValueAsTree() {
        if (data[pos] == 'i') {
            return readIntNode();
        } else if (data[pos] == 'l') {
            return readListNode();
        } else if (data[pos] == 'd') {
            return readDictNode();
        } else if (Character.isDigit(data[pos])) {
            return readBytesNode();
        } else {
            throw new ParseException(String.format("Unknown type prefix %02h at pos %d", data[pos], pos));
        }
    }
    
    IntNode readIntNode() {
        int end = seekFor('e', pos + 1);
        long value = parseLong(pos + 1, end - pos - 1);
        pos = end + 1;
        IntNode res = new IntNode();
        res.setValue(value);
        return res;
    }
    
    ListNode readListNode() {
        return null;
    }
    
    DictNode readDictNode() {
        return null;
    }
    
    BytesNode readBytesNode() {
        int mid = seekFor(':', pos + 1);
        long len = parseLong(pos, mid - pos);
        if (len < 0 || len >= Integer.MAX_VALUE) {
            throw new ParseException(String.format("Invalid byte string length %d at pos %d)", len, pos));
        }
        if (mid + len >= data.length) {
            throw new ParseException("Unexpected end for byte string starting from pos " + pos);
        }
        pos = mid + ((int) len) + 1;
        BytesNode res = new BytesNode();
        res.setValue(Arrays.copyOfRange(data, mid + 1, pos));
        return res;
    }
    
    private int seekFor(char c, int from) {
        try {
            while (data[from] != c) {
                from++;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ParseException("Unexpected end of input at pos " + pos);
        }
        return from;
    }
    
    private long parseLong(int offset, int length) {
        try {
            return Long.parseLong(new String(data, offset, length));
        } catch (NumberFormatException e) {
            throw new ParseException("Error while parsing integer from pos " + pos);
        }
    }
    
    public static class ParseException extends RuntimeException {
        private ParseException(String s) {
            super(s);
        }
    }
    
}

