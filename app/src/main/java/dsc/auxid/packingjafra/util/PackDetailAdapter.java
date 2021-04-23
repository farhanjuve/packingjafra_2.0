package dsc.auxid.packingjafra.util;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import dsc.auxid.packingjafra.R;

public class PackDetailAdapter extends ArrayAdapter<PackDetail>{

    private List<PackDetail> packDetails;
    private Context mContext;

    public PackDetailAdapter(Context context, List<PackDetail> data) {
        super(context, R.layout.packing_item, data);
        this.packDetails = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View myView = convertView;

        if(myView == null) {
            myView = LayoutInflater.from(mContext).inflate(R.layout.packing_item,parent,false);
        }

        PackDetail  pack = packDetails.get(position);

        TextView txtNo = (TextView) myView.findViewById(R.id.txtNoSeq);
        txtNo.setText(Integer.toString(pack.getSequence()));

        TextView txtItemCode =  myView.findViewById(R.id.txtItemCode);
        txtItemCode.setText(pack.getItemCode());

        TextView txtItemDesc =   myView.findViewById(R.id.txtItemDesc);
        txtItemDesc.setText(pack.getItemDesc());

        TextView txtQuantity =  myView.findViewById(R.id.txtPackQty);
        txtQuantity.setText(Integer.toString(pack.getItemQuantity()));

        if(pack.isPack()) {
            txtQuantity.setBackgroundColor(ContextCompat.getColor(mContext, R.color.dhlYellow ));
        } else {
            txtQuantity.setBackgroundColor(ContextCompat.getColor(mContext, R.color.dhlRed ));
        }


        return myView;
    }
}
