package ar.com.stoller.stollermobile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import ar.com.stoller.stollermobile.app.SeleccionarProducto;
import ar.com.stoller.stollermobile.app.SeleccionarProductoManager;
import ar.com.stoller.stollermobile.*;

/**
 * Created by gallardp on 30/09/14.
 */
public class SeleccionarProductoStock extends SeleccionarProducto {

    private String month;
    private String year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ocultarViews();
        Bundle bundle = getIntent().getExtras();
        cliente = bundle.getString("cliente");
        month = bundle.getString("month");
        year = bundle.getString("year");
        manager = new SeleccionarProductoManager();
        populateProductos();
    }

    @Override
    protected void okPress() {
        Integer.parseInt(cantidad.getText().toString());
        (new RegistrarStock()).execute();
        sentinela = false;
    }

    private class RegistrarStock extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            return manager.RegistrarStock(producto.getText().toString(), month, year,
                    cantidad.getText().toString(), cliente);
        }

        @Override
        protected void onPostExecute(Boolean result){
            if (result){
                finish();
            } else {
                showToastProductoInvalido();
            }
        }
    }

    private void ocultarViews(){
        findViewById(R.id.precioLayout).setVisibility(View.GONE);
        findViewById(R.id.calendarEnvio).setVisibility(View.GONE);
        findViewById(R.id.sp_address).setVisibility(View.GONE);
    }
}
