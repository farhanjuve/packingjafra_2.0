package dsc.auxid.packingjafra.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dsc.auxid.packingjafra.R;

public class ShipmentAdapter extends ArrayAdapter<ShipmentInfo> implements Filterable {

    private List<ShipmentInfo> data;
    private List<ShipmentInfo> dataFiltered;
    private Context mContext;

    public ShipmentAdapter(Context context, List<ShipmentInfo> data) {
        super(context, R.layout.wave_order_list, data);
        this.data = data;
        this.mContext=context;
        this.data = data;
        this.dataFiltered = dataFiltered;
    }

    private int lastPosition = -1;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View myView = convertView;

        if(myView == null) {
            myView = LayoutInflater.from(mContext).inflate(R.layout.wave_order_list,parent,false);
        }

        ShipmentInfo shipment = data.get(position);

        TextView txtNo =  myView.findViewById(R.id.txtNoSeq);
        txtNo.setText(Integer.toString(shipment.getNO()));

        TextView txtDO = myView.findViewById(R.id.txtShipmentId);
        txtDO.setText(shipment.getShipmentId());

        TextView txtCity = myView.findViewById(R.id.txtDeliveryCity);
        txtCity.setText(new StringBuilder(shipment.getShipToName()).append(" - ").append(shipment.getCity()));

        TextView txtTotalQty = myView.findViewById(R.id.textTotalQty);
        txtTotalQty.setText(shipment.getQty());

        return myView;
    }

    public void setLastPosition(int lastPosition) {
        this.lastPosition = lastPosition;
    }

    public int getLastPosition() {
        return lastPosition;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataFiltered = data;
                } else {
                    List<ShipmentInfo> filteredList = new ArrayList<>();
                    for (ShipmentInfo row : data) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getShipmentId().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    dataFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataFiltered = (ArrayList<ShipmentInfo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
