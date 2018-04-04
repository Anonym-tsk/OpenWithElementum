package su.css3.openwithelementum.bencode;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

public class BencodeList extends Vector<BencodeObject> implements BencodeObject {

    public BencodeList() {
        super();
    }

    public void write(OutputStream out) throws IOException {
        out.write('l');
        for (BencodeObject o : this) {
            o.write(out);
        }
        out.write('e');
    }
}