package dsc.auxid.packingjafra.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<ProductPhoto> photos;
    private ImageView imageView;

    public ImageAdapter(Context _context, List<ProductPhoto> _photos) {
        this.mContext = _context;
        this.photos = _photos;

    }

    public int getCount(){
        return photos.size();
    }

    public Object getItem(int position) {
        return photos.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    //Membuat ImageView baru untuk setiap item yang direferensikan oleh Adaptor
    public View getView(int position, View convertView, ViewGroup parent) {
        //Jika tidak di daur ulang
        if (convertView == null) {
            //Menginisialisasi beberapa atribut
            imageView = new ImageView(mContext);
            //Mengatur Panjang dan Lebar pada ImageView
            imageView.setLayoutParams(new GridView.LayoutParams(600, 600));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //Mengatur Padding
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        //Mengset Image dari Resource/Sumber berdasarkan posisinya
        imageView.setImageBitmap(photos.get(position).getPhoto());
        return imageView;
    }

}
