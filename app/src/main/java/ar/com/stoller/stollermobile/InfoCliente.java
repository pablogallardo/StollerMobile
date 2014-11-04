package ar.com.stoller.stollermobile;

import android.app.Activity;
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


public class InfoCliente extends Activity {

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
