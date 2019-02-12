package su.css3.openwithelementum.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import su.css3.openwithelementum.bencode.Bencode;
import su.css3.openwithelementum.bencode.BencodeInt;
import su.css3.openwithelementum.bencode.BencodeMap;
import su.css3.openwithelementum.bencode.BencodeString;

public class MagnetBuilder {
    private String hash;
    private String fileName;
    private String tracker;
    private Long fileSize;

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setTracker(String tracker) {
        this.tracker = tracker;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getLink() throws UnsupportedEncodingException {
        if (this.hash == null) {
            return null;
        }

        StringBuilder magnet = new StringBuilder("magnet:");
        magnet.append("?xt=urn:btih:").append(this.hash);

        if (this.tracker != null) {
            magnet.append("&tr=").append(URLEncoder.encode(this.tracker, "UTF-8"));
        }

        if (this.fileName != null) {
            magnet.append("&dn=").append(URLEncoder.encode(this.fileName, "UTF-8"));
        }

        if (this.fileSize != null) {
            magnet.append("&xl=").append(this.fileSize);
        }

        return magnet.toString();
    }

    public static String build(InputStream filestream) throws IOException {
        MagnetBuilder magnet = new MagnetBuilder();
        BencodeMap torrentData = (BencodeMap) Bencode.parseBencode(filestream);

        BencodeMap info = (BencodeMap) torrentData.get("info");
        byte[] infoBuffer = DigestUtils.sha1(info.getByteArray());
        String infoHash = new String(Hex.encodeHex(infoBuffer));
        magnet.setHash(infoHash);

        BencodeString infoName = (BencodeString) info.get("name");
        if (infoName != null) {
            magnet.setFileName(infoName.toString());
        }

        BencodeInt infoLength = (BencodeInt) info.get("length");
        if (infoLength != null) {
            magnet.setFileSize(infoLength.getLong());
        }

        BencodeString announce = (BencodeString) torrentData.get("announce");
        if (announce != null) {
            magnet.setTracker(announce.toString());
        }

        return magnet.getLink();
    }
}
