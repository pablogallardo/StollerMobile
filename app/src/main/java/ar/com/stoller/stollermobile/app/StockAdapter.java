package ar.com.stoller.stollermobile.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.ArrayList;

import ar.com.stoller.stollermobile.R;

/**
 * Created by gallardp on 12/09/14.
 */
public class StockAdapter extends BaseAdapter implements OnClickListener{

    private Context context;
    private ArrayList<String[]> listaStock;
    private LayoutInflater inflater;

    public StockAdapter(Context context, ArrayList<String[]> listaStock){
        this.context = context;
        this.listaStock = listaStock;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listaStock.size();
    }

    @Override
    public Object getItem(int position) {
        return listaStock.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String[] entry = listaStock.get(position);
        if (convertView == null) {
            //inflater = (LayoutInflater) context
            //        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.stockview, null);
        }
        TextView producto = (TextView)convertView.findViewById(R.id.productotv);
        TextView cantidad = (TextView)convertView.findViewById(R.id.cantidadstocktv);
        producto.setText(entry[0]);
        cantidad.setText(entry[1]);
        return convertView;
    }

    @Override
    public void onClick(View v) {

    }
}
