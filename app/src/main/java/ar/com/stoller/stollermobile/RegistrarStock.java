package ar.com.stoller.stollermobile;

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
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import ar.com.stoller.stollermobile.app.StockAdapter;
import ar.com.stoller.stollermobile.app.StockManager;


public class RegistrarStock extends ActionBarActivity {

    private Spinner year;
    private Spinner month;
    private Button add;
    private ListView stocklv;
    private StockManager sm;
    private String cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_stock);
        year = (Spinner)findViewById(R.id.yearspin);
        month = (Spinner)findViewById(R.id.monthspin);
        add = (Button)findViewById(R.id.addbtn);
        stocklv = (ListView)findViewById(R.id.stocklv);
        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("clienteseleccionado")!= null)
        {

            cliente = bundle.getString("clienteseleccionado");
        }
        setTitle(cliente);
        dateItemChanged();
        fillMonths();
        fillYears();
        new StockCliente().execute(cliente);
        stockLongClick();
        addClick();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registrar_stock, menu);
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
                Intent i = new Intent(getApplicationContext(),SeleccionarProducto.class);
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

    private void stockLongClick(){
        stocklv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialogRemove();
                return true;
            }
        });
    }

    private void dialogRemove(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("¿Realmente desea remover este item?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
            StockAdapter sa = new StockAdapter(getApplicationContext(), list);
            stocklv.setAdapter(sa);
        }
    }




}
