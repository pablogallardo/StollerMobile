package ar.com.stoller.stollermobile.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import ar.com.stoller.stollermobile.R;
import ar.com.stoller.stollermobile.app.SeleccionarProductoManager;


public abstract class SeleccionarProducto extends Activity {

    protected AutoCompleteTextView producto;

    protected TextView um;
    protected EditText cantidad;
    protected Button okbtn;
    protected boolean sentinela;
    protected SeleccionarProductoManager manager;
    protected String cliente;
    protected CalendarView calendarEnvio;
    protected Spinner address;
    protected EditText precio;
    protected String lista;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_producto);
        instantiateObjects();
        sentinela = false;

        focusChange();
        okBtnListener();
    }


    protected void populateProductos(){
        new PopulateProductos().execute();
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
                    sentinela = false;
                } else {
                    sentinela = true;
                }
            }
        });
    }

    protected void okBtnListener(){
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sentinela){
                    try{
                      okPress();
                    } catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(),
                            "Por favor, ingrese una cantidad válida.",
                            Toast.LENGTH_LONG).show();
                }
                } else{
                    showToastProductoInvalido();
                }
            }
        });
    }

    protected abstract void okPress();



    protected class PopulateProductos extends AsyncTask<Void, Void, ArrayList<String>> {
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

    protected class CheckProducto extends AsyncTask<Void, Void, String> {
        float precioLista = 0;
        @Override
        protected String doInBackground(Void... voids) {
            if(manager.existeProducto(producto.getText().toString())){
                precioLista = manager.getPrecio(producto.getText().toString(), lista);
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
                showToastProductoInvalido();
            } else {
                um.setText(result);
                precio.setText(""+precioLista);
                sentinela = true;
            }
        }


    }

    private void instantiateObjects(){
        producto = (AutoCompleteTextView)findViewById(R.id.ac_producto);
        okbtn = (Button)findViewById(R.id.okbtn);
        cantidad = (EditText)findViewById(R.id.cantidadet);
        um = (TextView)findViewById(R.id.umtv);
        calendarEnvio = (CalendarView)findViewById(R.id.calendarEnvio);
        address = (Spinner)findViewById(R.id.sp_address);
        precio = (EditText)findViewById(R.id.precioET);
    }


    protected void showToastProductoInvalido(){
        Toast.makeText(this, "Por favor, Ingrese un producto válido.",
                Toast.LENGTH_LONG).show();
    }
}
