package ar.com.stoller.stollermobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_stock);
        year = (Spinner)findViewById(R.id.yearspin);
        month = (Spinner)findViewById(R.id.monthspin);
        add = (Button)findViewById(R.id.addbtn);
        stocklv = (ListView)findViewById(R.id.stocklv);

        fillMonths();
        fillYears();
        new StockCliente().execute("hola");
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



    private class StockCliente extends AsyncTask<String, Void, ArrayList<String[]>> {
        @Override
        protected ArrayList<String[]> doInBackground(String... params) {
            sm = new StockManager("ROJO ALDO HUGO", "8","2014");
            try {
                return sm.getStock();
            } catch (SQLException e) {
                e.printStackTrace();
                return new ArrayList<String[]>();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String[]> list) {
            StockAdapter sa = new StockAdapter(getApplicationContext(), list);
            stocklv.setAdapter(sa);
        }
    }




}
