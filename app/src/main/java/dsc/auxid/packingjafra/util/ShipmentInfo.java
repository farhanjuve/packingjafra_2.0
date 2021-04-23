package dsc.auxid.packingjafra.util;

public class ShipmentInfo {

    private int no;
    private String shipmentId;
    private String shipToName;
    private String city;
    private String item;
    private String qty;

    public ShipmentInfo() {
    }

    public ShipmentInfo(int no, String shipmentId, String city, String item, String qty) {
        this.no = no;
        this.shipmentId = shipmentId;
        this.city = city;
        this.item = item;
        this.qty = qty;
    }

    public int getNO() {
        return this.no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipToName(String shipToName) {
        this.shipToName = shipToName;
    }

    public String getShipToName() {
        return shipToName;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }


}
