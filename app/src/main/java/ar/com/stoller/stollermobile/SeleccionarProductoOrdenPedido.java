package ar.com.stoller.stollermobile;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;

import java.sql.Date;
import java.util.ArrayList;

import ar.com.stoller.stollermobile.app.SeleccionarProducto;
import ar.com.stoller.stollermobile.app.SeleccionarProductoManager;
import ar.com.stoller.stollermobile.objects.OrdenPedido;

/**
 * Created by gallardp on 2/10/14.
 */
public class SeleccionarProductoOrdenPedido extends SeleccionarProducto{

    private OrdenPedido ordenPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        ordenPedido = (OrdenPedido)b.getSerializable("OrdenPedido");
        manager = new SeleccionarProductoManager(ordenPedido, b.getString("cliente"));
        populateProductos();
        new PopulateDireccion().execute();
    }

    @Override
    protected void okPress() {
        ordenPedido.agregarDetalle(producto.getText().toString(), Float.parseFloat(precio.getText().toString()),
                Integer.parseInt(cantidad.getText().toString()), new Date(calendarEnvio.getDate()), 1,
                address.getSelectedItem().toString());
        Intent data = new Intent();
        data.putExtra("data", ordenPedido);
        setResult(Activity.RESULT_OK, data);
        super.onBackPressed();
    }

    private class PopulateDireccion extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            return manager.getDirecciones();
        }

        @Override
        protected void onPostExecute(ArrayList<String> list) {
            ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.pedidos_view, list);
            address.setAdapter(aa);
        }
    }


}
