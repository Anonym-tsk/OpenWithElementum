package su.css3.openwithelementum.bencode;

import java.io.IOException;
import java.io.OutputStream;

public class BencodeInt implements BencodeObject {
    private long value; // Yes, BencodeInt is long

    public BencodeInt(long value) {
        this.value = value;
    }

    public long getLong() {
        return this.value;
    }

    public void write(OutputStream out) throws IOException {
        out.write('i');
        out.write(Long.toString(this.value).getBytes());
        out.write('e');
    }

    @Override
    public String toString() {
        return Long.toString(this.value);
    }
}