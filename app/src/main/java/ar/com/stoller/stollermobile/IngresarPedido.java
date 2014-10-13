package ar.com.stoller.stollermobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ar.com.stoller.stollermobile.app.IngresarPedidoManager;
import ar.com.stoller.stollermobile.app.StockAdapter;
import ar.com.stoller.stollermobile.objects.OrdenPedido;


public class IngresarPedido extends Activity{

    private TextView fecha;
    private Spinner direccion;
    private Spinner divisa;
    private String usuario;
    private Spinner listaPrecios;
    private EditText ordenCompra;
    private Button buttonAdd;
    private ListView productos;
    private IngresarPedidoManager manager;
    private String cliente;
    private Button buttonSave;
    private TextView subtotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_pedido);
        Bundle b = getIntent().getExtras();
        cliente = b.getString("clienteseleccionado");
        usuario = b.getString("usuariovendedor");
        manager = new IngresarPedidoManager(cliente);
        instantiateObjectView();
        fecha.setText(" " + new SimpleDateFormat("dd/MM/yyyy").format(manager.getOrdenPedido().getFecha()));
        new PopulateDireccion().execute();
        new PopulateListaPrecio().execute();
        populateProductos();
        addButonListener();
        saveButtonListener();
        ordenLongClick();
        setSubtotal();
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
        subtotal = (TextView)findViewById(R.id.subtotal);
        direccion = (Spinner)findViewById(R.id.sp_address);
        divisa = (Spinner)findViewById(R.id.sp_currency);
        listaPrecios = (Spinner)findViewById(R.id.sp_priceList);
        ordenCompra = (EditText)findViewById(R.id.buyOrder);
        buttonAdd = (Button)findViewById(R.id.buttonAdd);
        productos = (ListView)findViewById(R.id.productos);
        buttonSave = (Button)findViewById(R.id.buttonSave);
    }

    private void populateProductos(){
        ArrayList<String[]> list = manager.getArrayDetalles();
        StockAdapter stockAdapter = new StockAdapter(getApplicationContext(), list);
        productos.setAdapter(stockAdapter);
    }
    private void addButonListener(){
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        SeleccionarProductoOrdenPedido.class);
                i.putExtra("OrdenPedido", manager.getOrdenPedido());
                i.putExtra("cliente", cliente);
                i.putExtra("lista", listaPrecios.getSelectedItem().toString());
                startActivityForResult(i, 1);
            }
        });
    }

    private void saveButtonListener(){
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrdenPedido op = manager.getOrdenPedido();
                op.setDireccionFacturacion(direccion.getSelectedItem().toString());
                op.setDivisa("USD");
                op.setEstado("Ingresado");
                op.setListaPrecios(listaPrecios.getSelectedItem().toString());
                op.setOrdenCompra(ordenCompra.getText().toString());
                op.setCreadoPor(usuario);
                new SaveOrdenPedido().execute();
            }
        });
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
            direccion.setAdapter(aa);
        }
    }
    private class PopulateListaPrecio extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            return manager.getListaPrecios();
        }

        @Override
        protected void onPostExecute(ArrayList<String> list) {
            ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.pedidos_view, list);
            listaPrecios.setAdapter(aa);
        }
    }

    private void ordenLongClick(){
        productos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialogRemove(position);

                return true;
            }
        });
    }

    private void dialogRemove(final int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("¿Realmente desea remover este item?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        manager.removeDetalle(position);
                        populateProductos();
                        setSubtotal();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onResume(){
        super.onResume();
        populateProductos();
        setSubtotal();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            manager.setOrdenPedido((OrdenPedido)data.getSerializableExtra("data"));
            //Do whatever you want with yourData
        }
    }

    private class SaveOrdenPedido extends AsyncTask<Void, Void, Integer>{
        @Override
        protected Integer doInBackground(Void... voids) {
            return manager.guardarOrdenPedido(cliente, manager.getOrdenPedido(), usuario);
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result > 0){
                Toast.makeText(getApplicationContext(),"El número de orden de pedido es " + result,
                        Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(),"Hubo un problema al intentar grabar la " +
                        "orden de pedido", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setSubtotal(){
        subtotal.setText(" " + manager.getSubtotal());
    }


}
