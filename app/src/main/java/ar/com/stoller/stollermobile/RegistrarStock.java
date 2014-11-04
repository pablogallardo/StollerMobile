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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import ar.com.stoller.stollermobile.app.StockAdapter;
import ar.com.stoller.stollermobile.app.StockManager;


public class RegistrarStock extends Activity {

    private Spinner year;
    private Spinner month;
    private Button add;
    private ListView stocklv;
    private StockManager sm;
    private String cliente;
    private Boolean resumed;
    private StockAdapter stockAdapter;
    private Button okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_stock);
        year = (Spinner)findViewById(R.id.yearspin);
        month = (Spinner)findViewById(R.id.monthspin);
        add = (Button)findViewById(R.id.addbtn);
        stocklv = (ListView)findViewById(R.id.stocklv);
        okBtn = (Button)findViewById(R.id.okbtn);
        Bundle bundle = getIntent().getExtras();

        if(bundle.getString("clienteseleccionado")!= null)
        {

            cliente = bundle.getString("clienteseleccionado");
        }
        setTitle(cliente);
        dateItemChanged();
        fillMonths();
        fillYears();
        (new StockCliente()).execute(cliente);
        stockLongClick();
        addClick();
        resumed = false;
        okClickListener();
    }




    private void fillMonths(){
        String[] months = getResources().getStringArray(R.array.months);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                months);
        month.setAdapter(aa);
        month.setSelection(Calendar.getInstance().get(Calendar.MONTH));
    }

    private void fillYears(){
        ArrayList<Integer> al = new ArrayList<Integer>();
        int actualyear = Calendar.getInstance().get(Calendar.YEAR);
        for(int i = actualyear ;i>=2011; i--){
            al.add(i);
        }
        ArrayAdapter<Integer> aa = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_list_item_1, al);
        year.setAdapter(aa);
    }

    private void addClick(){
        add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SeleccionarProductoStock.class);
                i.putExtra("cliente", cliente);
                i.putExtra("month", String.valueOf(month.getSelectedItemPosition() + 1));
                i.putExtra("year", year.getSelectedItem().toString());
                resumed = true;
                startActivity(i);
            }
        });
    }

    private void dateItemChanged(){
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new StockCliente().execute(cliente);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                new StockCliente().execute(cliente);
            }
        });

        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new StockCliente().execute(cliente);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                new StockCliente().execute(cliente);
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
                        new EliminarStock().execute(((String[])stockAdapter.getItem(position))[0]);
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

    private void stockLongClick(){
        stocklv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialogRemove(position);

                return true;
            }
        });
    }

    private void okClickListener(){
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    private class StockCliente extends AsyncTask<String, Void, ArrayList<String[]>> {
        @Override
        protected ArrayList<String[]> doInBackground(String... params) {
            sm = new StockManager(params[0], month.getSelectedItemPosition() +1,
                    Integer.parseInt(year.getSelectedItem().toString()));
            try {
                return sm.getStock();
            } catch (SQLException e) {
                e.printStackTrace();
                return new ArrayList<String[]>();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String[]> list) {
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            stockAdapter = new StockAdapter(getApplicationContext(), list);
            stocklv.setAdapter(stockAdapter);
        }
    }

    private class EliminarStock extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            return sm.eliminarStock(strings[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                (new StockCliente()).execute(cliente);
            } else {
                Toast.makeText(getApplicationContext(),"No se pudo eliminar el stock seleccionado",
                        Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(resumed){
            (new StockCliente()).execute(cliente);
            resumed = false;
        }
    }


}
