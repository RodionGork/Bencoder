package com.github.rodiongork.bencoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class BencGenerator {
    
    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    
    public void writeTree(BencNode root) {
        if (out == null) {
            throw new WriteException("Generator could not be reused");
        }
        writeUnknownTypeNode(root);
    }
    
    private void writeUnknownTypeNode(BencNode node) {
        if (node instanceof IntNode) {
            writeIntNode((IntNode) node);
        } else if (node instanceof BytesNode) {
            writeBytesNode((BytesNode) node);
        } else if (node instanceof ListNode) {
            writeListNode((ListNode) node);
        } else if (node instanceof DictNode) {
            writeDictNode((DictNode) node);
        } else {
            throw new WriteException("Unsupported type of node? this should never happen!");
        }
    }

    private void writeIntNode(IntNode node) {
        out.write('i');
        writeNumber(node.getValue());
        out.write('e');
    }

    private void writeBytesNode(BytesNode node) {
        writeNumber(node.length());
        out.write(':');
        writeBytes(node.getValue());
    }

    private void writeListNode(ListNode list) {
        out.write('l');
        for (BencNode child : list) {
            writeUnknownTypeNode(child);
        }
        out.write('e');
    }

    private void writeDictNode(DictNode dict) {
        out.write('d');
        BytesNode key = new BytesNode();
        for (Map.Entry<String, BencNode> entry : dict.entrySet()) {
            key.setValue(entry.getKey());
            writeBytesNode(key);
            writeUnknownTypeNode(entry.getValue());
        }
        out.write('e');
    }

    private void writeNumber(Number value) {
        writeString(value.toString());
    }

    private void writeString(String s) {
        writeBytes(s.getBytes());
    }

    private void writeBytes(byte[] bytes) {
        try {
            out.write(bytes);
        } catch (IOException e) {
            throw new WriteException("IOException during writing to ByteArray? Funny!");
        }
    }

    public byte[] output() {
        byte[] res =  out.toByteArray();
        out = null;
        return res;
    }

    public static byte[] treeToBytes(BencNode tree) {
        BencGenerator gen = new BencGenerator();
        gen.writeTree(tree);
        return gen.output();
    }

    public static class WriteException extends RuntimeException {
        private WriteException(String s) {
            super(s);
        }
    }
}
