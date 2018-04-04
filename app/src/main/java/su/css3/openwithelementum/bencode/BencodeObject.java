package su.css3.openwithelementum.bencode;

import java.io.IOException;
import java.io.OutputStream;

public interface BencodeObject {
    public void write(OutputStream out) throws IOException;
}