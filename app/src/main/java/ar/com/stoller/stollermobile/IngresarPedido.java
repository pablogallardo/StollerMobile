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

    private int idOP;
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
    private Spinner estados;
    private boolean aModificar = false;
    private ArrayAdapter<String> direccionaa;
    private ArrayAdapter<String> listaaa;
    private ArrayAdapter<String> estadoaa;
    private boolean estaConfirmada = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_pedido);
        Bundle b = getIntent().getExtras();
        cliente = b.getString("clienteseleccionado");
        setTitle(cliente);
        usuario = b.getString("usuariovendedor");
        manager = new IngresarPedidoManager(cliente);
        instantiateObjectView();
        fecha.setText(" " + new SimpleDateFormat("dd/MM/yyyy").format(manager.getOrdenPedido().getFecha()));
        new PopulateDireccion().execute();
        new PopulateListaPrecio().execute();
        new PopulateStates().execute();
        populateProductos();
        addButonListener();
        saveButtonListener();
        ordenLongClick();
        setSubtotal();
        idOP = b.getInt("idOP");
        if(idOP != 0){
            aModificar = true;
            new SelectEverything().execute();
        }
        onProductoClickListener();
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
        estados = (Spinner)findViewById(R.id.state);
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
                op.setEstado(estados.getSelectedItem().toString());
                op.setListaPrecios(listaPrecios.getSelectedItem().toString());
                op.setOrdenCompra(ordenCompra.getText().toString());
                if(aModificar){
                    op.setModificadoPor(usuario);
                } else {
                    op.setCreadoPor(usuario);
                }
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
            direccionaa = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.pedidos_view, list);
            direccion.setAdapter(direccionaa);
        }
    }
    private class PopulateListaPrecio extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            return manager.getListaPrecios();
        }

        @Override
        protected void onPostExecute(ArrayList<String> list) {
            listaaa = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.pedidos_view, list);
            listaPrecios.setAdapter(listaaa);
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

    private void onProductoClickListener(){
        productos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),
                        SeleccionarProductoOrdenPedido.class);
                intent.putExtra("OrdenPedido", manager.getOrdenPedido());
                intent.putExtra("cliente", cliente);
                intent.putExtra("lista", listaPrecios.getSelectedItem().toString());
                intent.putExtra("seleccionado", i + 1);
                intent.putExtra("confirmada", estaConfirmada);
                startActivityForResult(intent, 1);
            }
        });
    }

    private class SaveOrdenPedido extends AsyncTask<Void, Void, Integer>{
        @Override
        protected Integer doInBackground(Void... voids) {
            if(aModificar){
                manager.actualizarOrdenPedido(cliente, manager.getOrdenPedido(), usuario);

                return idOP;
            }else{
                return manager.guardarOrdenPedido(cliente, manager.getOrdenPedido(), usuario);
            }

        }

        @Override
        protected void onPostExecute(Integer result) {
            String s;
            if(result > 0){
                if(aModificar){
                    s = "La orden de pedido número " + result + " ha sido modificada correctamente";
                } else {
                    s= "El número de orden de pedido es " + result;
                }
                aModificar = false;
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(),"Hubo un problema al intentar grabar la " +
                        "orden de pedido", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class PopulateStates extends AsyncTask<Void, Void, ArrayList<String>>{
        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            return manager.getEstadosOrdenPedido();
        }

        @Override
        protected void onPostExecute(ArrayList<String> list) {
            estadoaa = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.pedidos_view, list);
            estados.setAdapter(estadoaa);
        }
    }

    private void setSubtotal(){
        subtotal.setText(" " + manager.getSubtotal());
    }

    private class SelectEverything extends AsyncTask<Void, Void, OrdenPedido>{
        @Override
        protected OrdenPedido doInBackground(Void... voids) {
            manager.setOrdenPedido(idOP);
            return manager.getOrdenPedido();
        }

        @Override
        protected void onPostExecute(OrdenPedido orden) {
            direccion.setSelection(direccionaa.getPosition(orden.getDireccionFacturacion()));
            listaPrecios.setSelection(listaaa.getPosition(orden.getListaPrecios()));
            estados.setSelection(estadoaa.getPosition(orden.getEstado()));
            ordenCompra.setText(orden.getOrdenCompra());
            fecha.setText(" " + new SimpleDateFormat("dd/MM/yyyy").format(manager.getOrdenPedido().getFecha()));
            populateProductos();
            setSubtotal();
            if (orden.getEstado().equals("Confirmada")){
                disableEverything();
                estaConfirmada = true;
            }
        }
    }

    private void disableEverything(){
        fecha.setEnabled(false);
        subtotal.setEnabled(false);
        direccion.setEnabled(false);
        divisa.setEnabled(false);
        listaPrecios.setEnabled(false);
        ordenCompra.setEnabled(false);
        buttonAdd.setEnabled(false);
        buttonSave.setEnabled(false);
        estados.setEnabled(false);
    }


}
