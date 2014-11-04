package ar.com.stoller.stollermobile;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;

import ar.com.stoller.stollermobile.app.SeleccionarProducto;
import ar.com.stoller.stollermobile.app.SeleccionarProductoManager;
import ar.com.stoller.stollermobile.objects.DetalleOrdenPedido;
import ar.com.stoller.stollermobile.objects.OrdenPedido;

/**
 * Created by gallardp on 2/10/14.
 */
public class SeleccionarProductoOrdenPedido extends SeleccionarProducto{

    private OrdenPedido ordenPedido;
    private int seleccionado;
    ArrayAdapter<String> direccionaa;
    boolean estaConfirmada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        ordenPedido = (OrdenPedido)b.getSerializable("OrdenPedido");
        manager = new SeleccionarProductoManager(ordenPedido, b.getString("cliente"));
        lista = b.getString("lista");
        populateProductos();
        new PopulateDireccion().execute();
        seleccionado = b.getInt("seleccionado");
        if(seleccionado != 0){
            fillSelectedProduct();
        }
        estaConfirmada = b.getBoolean("confirmada");
        if(estaConfirmada){
            disableEverything();
        }
    }

    private void fillSelectedProduct(){
        DetalleOrdenPedido d = ordenPedido.getDetalle(seleccionado);
        producto.setText(d.getProducto());
        cantidad.setText(d.getCantidad() + "");
        DecimalFormat df = new DecimalFormat("#,###,##0.00");
        precio.setText(df.format(d.getPrecioUnitario()));
        calendarEnvio.setDate(d.getFechaEnvio().getTime());
    }

    @Override
    protected void okPress() {
        if(seleccionado == 0){
            ordenPedido.agregarDetalle(producto.getText().toString(), Float.parseFloat(precio.getText().toString().replace(',','.')),
                    Integer.parseInt(cantidad.getText().toString()), new Date(calendarEnvio.getDate()), ordenPedido.getArrayDetalles().size() + 1,
                    address.getSelectedItem().toString());
            } else {
            ordenPedido.modificarDetalle(producto.getText().toString(), Float.parseFloat(precio.getText().toString().replace(',','.')),
                    Integer.parseInt(cantidad.getText().toString()), new Date(calendarEnvio.getDate()), seleccionado,
                    address.getSelectedItem().toString());
        }
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
            direccionaa = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.pedidos_view, list);
            address.setAdapter(direccionaa);
            if(seleccionado != 0) {
                DetalleOrdenPedido d = ordenPedido.getDetalle(seleccionado);
                address.setSelection(direccionaa.getPosition(d.getDireccionEnvio()));
            }

        }
    }

    private void disableEverything(){
        producto.setEnabled(false);
        cantidad.setEnabled(false);
        um.setEnabled(false);
        calendarEnvio.setEnabled(false);
        address.setEnabled(false);
        precio.setEnabled(false);
    }


}
