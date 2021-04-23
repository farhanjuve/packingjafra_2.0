package dsc.auxid.packingjafra.util;

public class DataPart {
    private String fileName;
    private byte[] content;
    private String type;

    public DataPart() {
    }

    public DataPart(String name, byte[] data) {
        fileName = name;
        content = data;
    }

    public DataPart(String fileName, byte[] content, String type) {
        this.fileName = fileName;
        this.content = content;
        this.type = type;
    }

    String getFileName() {
        return fileName;
    }

    byte[] getContent() {
        return content;
    }

    String getType() {
        return type;
    }

}