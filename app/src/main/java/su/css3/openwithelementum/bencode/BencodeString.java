package su.css3.openwithelementum.bencode;

import java.io.IOException;
import java.io.OutputStream;

public class BencodeString implements BencodeObject {
    private byte[] bytes;

    public BencodeString(byte[] bytes) {
        this.bytes = bytes;
    }

    public BencodeString(String s) {
        this.bytes = s.getBytes();
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return new String(this.bytes);
    }

    public void write(OutputStream out) throws IOException {
        out.write(Integer.toString(bytes.length).getBytes());
        out.write(':');
        out.write(this.bytes);
    }
}