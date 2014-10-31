package ar.com.stoller.stollermobile.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ar.com.stoller.stollermobile.R;

/**
 * Created by pablo on 10/31/14.
 */
public class DireccionAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private ArrayList<String[]> listaDirecciones;
    private LayoutInflater inflater;

    public DireccionAdapter(Context context, ArrayList<String[]> listaDirecciones){
        this.context = context;
        this.listaDirecciones = listaDirecciones;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return listaDirecciones.size();
    }

    @Override
    public Object getItem(int i) {
        return listaDirecciones.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String[] entry = listaDirecciones.get(i);
        if (view == null) {
            view = inflater.inflate(R.layout.direccionview, viewGroup, false);
        }
        TextView direccion = (TextView)view.findViewById(R.id.address);
        TextView tipoDireccion = (TextView)view.findViewById(R.id.type_address);
        direccion.setText(entry[0]);
        tipoDireccion.setText(entry[1]);
        return view;
    }

    @Override
    public void onClick(View view) {

    }
}
