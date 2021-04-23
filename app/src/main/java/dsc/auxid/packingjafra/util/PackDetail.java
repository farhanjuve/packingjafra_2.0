package dsc.auxid.packingjafra.util;


public class PackDetail {

    private String name;
    String type;
    String version_number;
    String feature;

    private int sequence;
    private int waveNumber;
    private int soNumber;
    private String itemCode;
    private String itemDesc;
    private int itemQuantity;
    private boolean isPack;

    public PackDetail() {
    }

    public PackDetail(int seq, int waveNumber, int soNumber, String itemCode, String itemDesc, int itemQuantity) {
        this.sequence = seq;
        this.setWaveNumber(waveNumber);
        this.setSoNumber(soNumber);
        this.setItemCode(itemCode);
        this.setItemDesc(itemDesc);
        this.setItemQuantity(itemQuantity);
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
    public int getSequence() { return this.sequence; }

    public int getWaveNumber() {
        return waveNumber;
    }

    public void setWaveNumber(int waveNumber) {
        this.waveNumber = waveNumber;
    }

    public int getSoNumber() {
        return soNumber;
    }

    public void setSoNumber(int soNumber) {
        this.soNumber = soNumber;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public boolean isPack() {
        return isPack;
    }

    public void setPack(boolean pack) {
        isPack = pack;
    }
}


