package ar.com.stoller.stollermobile;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import ar.com.stoller.stollermobile.R;
import ar.com.stoller.stollermobile.app.ShowPedidoManager;

public class ShowPedido extends ActionBarActivity {
    private int idOP;
    private ShowPedidoManager manager;
    private Spinner direccion;
    private TextView fecha;
    private Spinner divisa;
    private Spinner listaPrecios;
    private EditText ordenCompra;
    private TextView subtotal;
    private LinearLayout buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_pedido);
        instantiateAll();
        deactivateAll();
        Bundle b = getIntent().getExtras();
        idOP = b.getInt("idOP");

        new SetAll().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_pedido, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void instantiateAll(){
        direccion = (Spinner)findViewById(R.id.sp_address);
        fecha = (TextView)findViewById(R.id.date);
        divisa = (Spinner)findViewById(R.id.sp_currency);
        listaPrecios = (Spinner)findViewById(R.id.sp_priceList);
        ordenCompra = (EditText)findViewById(R.id.buyOrder);
        subtotal = (TextView)findViewById(R.id.subtotal);
        buttons = (LinearLayout)findViewById(R.id.buttons_layout);
    }

    private void deactivateAll(){
        direccion.setActivated(false);
        fecha.setActivated(false);
        divisa.setActivated(false);
        listaPrecios.setActivated(false);
        ordenCompra.setActivated(false);
        subtotal.setActivated(false);
        buttons.setVisibility(View.GONE);
    }

    private class SetAll extends AsyncTask<Void, Void, Void>{
        private String[] s = new String[1];
        private String[] s1 = new String[1];
        private String fechadb;
        private String ordenCompradb;
        private String subtotaldb;

        @Override
        protected Void doInBackground(Void... voids) {
            manager = new ShowPedidoManager(idOP);
            s[0] = manager.getListaPrecio();
            s1[0] = manager.getDireccion();
            fechadb = " " + manager.getFecha().toString();
            ordenCompradb = manager.getOrdenCompra();
            subtotaldb = " " + manager.getSubtotal();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), R.layout.pedidos_view, s);
            listaPrecios.setAdapter(aa);
            ArrayAdapter<String> aa1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.pedidos_view, s1);
            direccion.setAdapter(aa1);
            fecha.setText(fechadb);
            ordenCompra.setText(ordenCompradb);
            subtotal.setText(subtotaldb);
        }
    }
}
