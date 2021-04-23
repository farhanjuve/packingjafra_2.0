package dsc.auxid.packingjafra.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import dsc.auxid.packingjafra.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
  private ArrayList<ProductPhoto> galleryList;
  private Context context;

  public MyAdapter(Context context, ArrayList<ProductPhoto> galleryList) {
    this.galleryList = galleryList;
    this.context = context;
  }

  @Override
  public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_cell_layout, viewGroup, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(MyAdapter.ViewHolder viewHolder, int i) {
    viewHolder.title.setText(galleryList.get(i).getFileName());
    viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
    viewHolder.img.setImageBitmap(galleryList.get(i).getPhoto());
  }

  @Override
  public int getItemCount() {
    return galleryList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder{
    private TextView title;
    private ImageView img;
    public ViewHolder(View view) {
      super(view);

      title = (TextView)view.findViewById(R.id.title);
      img = (ImageView) view.findViewById(R.id.img);
    }
  }
}