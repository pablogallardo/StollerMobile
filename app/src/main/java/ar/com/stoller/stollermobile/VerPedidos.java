package ar.com.stoller.stollermobile;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.logging.SocketHandler;

import ar.com.stoller.stollermobile.app.VerPedidosManager;


public class VerPedidos extends Activity {

    private ListView pedidos;
    private VerPedidosManager manager;
    private String cliente;
    private ArrayAdapter<String> aaPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_pedidos);
        pedidos = (ListView)findViewById(R.id.pedidos);

        Bundle b = getIntent().getExtras();
        cliente = b.getString("clienteseleccionado");
        setTitle(cliente);
        manager = new VerPedidosManager(cliente);
        onItemClick();
    }




    private void onItemClick(){
        pedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), IngresarPedido.class);
                intent.putExtra("clienteseleccionado", cliente);
                intent.putExtra("usuariovendedor", "hgomez");
                intent.putExtra("idOP", Integer.parseInt(aaPedidos.getItem(i)));
                startActivity(intent);
            }
        });
    }

    private class PopulatePedidos extends AsyncTask<Void, Void, ArrayList<String>>{
        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            return manager.getPedidos();
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            aaPedidos = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.clientesview, strings);
            pedidos.setAdapter(aaPedidos);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new PopulatePedidos().execute();
    }
}
