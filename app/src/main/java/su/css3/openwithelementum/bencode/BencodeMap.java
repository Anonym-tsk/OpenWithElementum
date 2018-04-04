package su.css3.openwithelementum.bencode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class BencodeMap extends LinkedHashMap<BencodeString, BencodeObject> implements BencodeObject {

    public BencodeMap() {
        super();
    }

    public void write(OutputStream out) throws IOException {
        out.write('d');
        for (Map.Entry<BencodeString, BencodeObject> e : this.entrySet()) {
            e.getKey().write(out);
            e.getValue().write(out);
        }
        out.write('e');
    }

    public byte[] getByteArray() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        write(out);
        out.close();
        return out.toByteArray();
    }

    public BencodeObject get(String key) {
        return get(new BencodeString(key));
    }
}