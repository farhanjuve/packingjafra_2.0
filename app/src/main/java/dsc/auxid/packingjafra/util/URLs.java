package dsc.auxid.packingjafra.util;

public class URLs {
    public static String VERSION = "1.0.0";
    public static String BASE_PATH = "https://dots.dhl.com/dsc-aux/";
    public static String IMAGE_UPLOAD_SERVER = "http://192.168.1.16:8000";

    public static String getShipments =  "/api/jafra/get-shipments";
    public static String getShipmentDetail =   "/api/jafra/get-pack";

    public static String confirmPacking =  "/api/jafra/pack-confirm";
    public static String submitPhotos = "/api/jafra/pack-confirm";

    public static String imageUpload() {
        if(IMAGE_UPLOAD_SERVER != "") {
            return IMAGE_UPLOAD_SERVER + "/api/jafra/pack-confirm";
        }
        return confirmPacking;
    }

}