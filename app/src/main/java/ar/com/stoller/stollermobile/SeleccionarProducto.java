package ar.com.stoller.stollermobile;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

import ar.com.stoller.stollermobile.app.SeleccionarProductoManager;


public class SeleccionarProducto extends Activity {

    private AutoCompleteTextView producto;
    private SeleccionarProductoManager manager;
    private TextView um;
    private Button okbtn;
    private boolean sentinela;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_producto);
        producto = (AutoCompleteTextView)findViewById(R.id.ac_producto);
        okbtn = (Button)findViewById(R.id.okbtn);
        manager = new SeleccionarProductoManager();
        sentinela = false;
        um = (TextView)findViewById(R.id.umtv);
        (new PopulateProductos()).execute();
        focusChange();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.seleccionar_producto, menu);
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

    private void focusChange(){
        producto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    (new CheckProducto()).execute();
                    //TODO toast
                } else {
                    sentinela = true;
                }
            }
        });
    }

    private void okBtnListener(){
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sentinela){
                    //TODO Registrar stock
                } else{
                    //TODO Toast corregir producto
                }
            }
        });
    }


    private class PopulateProductos extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... voids) {


                ArrayList<String> al = manager.getProductos();
                return al;


        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.clientesview, result);

            producto.setAdapter(aa);
        }
    }

    private class CheckProducto extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            if(manager.existeProducto(producto.getText().toString())){
                return manager.getUM(producto.getText().toString());

            } else {

                return null;
            }
        }

        @Override
        protected void onPostExecute(String result){
            if(result == null){
                producto.requestFocus();
                sentinela = false;
            } else {
                um.setText(result);
                sentinela = true;
            }
        }


    }
}
