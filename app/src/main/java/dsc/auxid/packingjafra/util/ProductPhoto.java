package dsc.auxid.packingjafra.util;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.net.FileNameMap;

public class ProductPhoto {
    private Bitmap photo;
    private Uri uriFileName;


    public ProductPhoto( Bitmap photo, Uri uriFileName) {
        this.photo = photo;
        this.uriFileName = uriFileName;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public Uri getUriFileName() {
        return uriFileName;
    }

    public String getFileName() {
        return new File(uriFileName.getPath()).getName();
    }
}
