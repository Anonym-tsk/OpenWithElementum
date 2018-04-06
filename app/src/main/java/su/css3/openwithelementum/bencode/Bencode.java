package su.css3.openwithelementum.bencode;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class Bencode {

    public static BencodeObject parseBencode(InputStream in) throws IOException {
        return parseBencode(new PushbackInputStream(in));
    }

    public static BencodeObject parseBencode(PushbackInputStream in) throws IOException {
        int first = in.read();
        in.unread(first);

        switch (first) {
            case 'i':
                return parseBencodeInt(in);

            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
                return parseBencodeString(in);

            case 'l':
                return parseBencodeList(in);

            case 'd':
                return parseBencodeMap(in);

            default:
                throw new IOException("no bencode: illecal char: "+ ((char)first));
        }
    }

    private static BencodeObject parseBencodeInt(PushbackInputStream in) throws IOException {
        parseBencodeExpect(in, 'i');
        return new BencodeInt(parseBencodeReadLong(in));
    }

    private static long parseBencodeReadLong(PushbackInputStream in) throws IOException {
        long result=0;

        while (true) {
            int b=in.read();

            if (b<'0' || b>'9') break;

            result = result * 10 + (b-'0');
        }
        return result;
    }

    private static BencodeObject parseBencodeString(PushbackInputStream in) throws IOException {
        int len = (int) parseBencodeReadLong(in);

        byte[] bs = new byte[len];
        int pos = 0;

        while(pos<len) {
            pos += in.read(bs, pos, bs.length-pos);
        }

        return new BencodeString(bs);
    }

    private static BencodeObject parseBencodeList(PushbackInputStream in) throws IOException {
        parseBencodeExpect(in, 'l');

        int first;
        BencodeList result = new BencodeList();

        while ('e'!= (first = in.read())) {
            in.unread(first);
            result.add(parseBencode(in));
        }
        return result;
    }

    private static BencodeObject parseBencodeMap(PushbackInputStream in) throws IOException {
        parseBencodeExpect(in, 'd');

        int first;
        BencodeMap result = new BencodeMap();

        while ('e'!= (first = in.read())) {
            in.unread(first);
            BencodeString key = (BencodeString)parseBencode(in);
            BencodeObject val = parseBencode(in);

            result.put(key, val);
        }
        return result;
    }

    private static void parseBencodeExpect(PushbackInputStream in, int b) throws IOException {
        int bIn = in.read();
        if (b != bIn) {
            throw new IOException("no bencode: illecal char: "+ ((char)bIn)+" ("+bIn+") expected: "+((char)b));
        }
    }
}