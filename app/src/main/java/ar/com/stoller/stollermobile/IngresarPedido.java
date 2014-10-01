package ar.com.stoller.stollermobile;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import ar.com.stoller.stollermobile.app.IngresarPedidoManager;


public class IngresarPedido extends Activity{

    private TextView fecha;
    private Spinner direccion;
    private Spinner divisa;
    private Spinner listaPrecios;
    private EditText ordenCompra;
    private Button buttonAdd;
    private ListView productos;
    private IngresarPedidoManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_pedido);
        instantiateObjectView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ingresar_pedido, menu);
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

    private void instantiateObjectView(){
        fecha = (TextView)findViewById(R.id.date);
        direccion = (Spinner)findViewById(R.id.sp_address);
        divisa = (Spinner)findViewById(R.id.sp_currency);
        listaPrecios = (Spinner)findViewById(R.id.sp_priceList);
        ordenCompra = (EditText)findViewById(R.id.buyOrder);
        buttonAdd = (Button)findViewById(R.id.buttonAdd);
        productos = (ListView)findViewById(R.id.productos);
    }


}
