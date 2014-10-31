package ar.com.stoller.stollermobile;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import ar.com.stoller.stollermobile.app.DireccionAdapter;
import ar.com.stoller.stollermobile.app.InfoClienteManager;


public class InfoCliente extends ActionBarActivity {

    private String cliente;
    private TextView CUIT;
    private TextView usuario;
    private TextView email;
    private TextView telefono;
    private TextView celular;
    private ListView direcciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_cliente);
        instantiateEverything();
        Bundle b = getIntent().getExtras();
        cliente = b.getString("clienteseleccionado");
        setTitle(cliente);
        new GetInfoCliente().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info_cliente, menu);
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

    private void instantiateEverything(){
        CUIT = (TextView)findViewById(R.id.cuit_tv);
        usuario = (TextView)findViewById(R.id.usuario_tv);
        email = (TextView)findViewById(R.id.email_tv);
        telefono = (TextView)findViewById(R.id.telefono_tv);
        celular = (TextView)findViewById(R.id.celular_tv);
        direcciones = (ListView)findViewById(R.id.direcciones_lv);
    }

    private class GetInfoCliente extends AsyncTask<Void, Void, InfoClienteManager>{
        @Override
        protected InfoClienteManager doInBackground(Void... voids) {
            return new InfoClienteManager(cliente);
        }

        @Override
        protected void onPostExecute(InfoClienteManager result) {
            CUIT.setText(result.getCUIT());
            usuario.setText(result.getUsuario());
            email.setText(result.getEmail());
            telefono.setText(result.getTelefono());
            celular.setText(result.getCelular());
            DireccionAdapter da = new DireccionAdapter(getApplicationContext(), result.getDireccion());
            direcciones.setAdapter(da);
        }
    }
}
