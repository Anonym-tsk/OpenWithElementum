package su.css3.openwithelementum.bencode;

import java.io.IOException;
import java.io.OutputStream;

public class BencodeInt implements BencodeObject {
    private int value;

    public BencodeInt(int value) {
        this.value = value;
    }

    public int getInt() {
        return this.value;
    }

    public void write(OutputStream out) throws IOException {
        out.write('i');
        out.write(Integer.toString(this.value).getBytes());
        out.write('e');
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }
}