package ar.com.stoller.stollermobile;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

import ar.com.stoller.stollermobile.app.LobbyManager;


public class Lobby extends Activity {
    private ListView clienteslv;
	private String usuario;
	private String razonSocial;
    ArrayAdapter<String> aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        Bundle bundle = getIntent().getExtras();
		clienteslv = (ListView)findViewById(R.id.clienteslv);
		if(bundle.getString("Usuario")!= null)
        {

			usuario = bundle.getString("Usuario");
        }
		if(bundle.getString("razonsocial")!= null)
        {

			razonSocial = bundle.getString("razonsocial");
			setTitle(razonSocial);
        }

		new Clientes().execute(usuario);
        registerForContextMenu(clienteslv);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lobby, menu);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.clienteslv) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(aa.getItem(info.position));
            String[] menuItems = getResources().getStringArray(R.array.clientes_context_menu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.clientes_context_menu);
        String menuItemName = menuItems[menuItemIndex];
        String listItemName = aa.getItem(info.position);
        Intent intent;
        switch (menuItemIndex){
            case 0:{
                intent = new Intent(this,IngresarPedido.class);
                intent.putExtra("clienteseleccionado", listItemName);
                intent.putExtra("usuariovendedor", usuario);
                startActivity(intent);
                break;
            }
            case 1:{
                intent = new Intent(this,VerPedidos.class);
                intent.putExtra("clienteseleccionado", listItemName);
                startActivity(intent);
                break;
            }
            case 2:{
                intent = new Intent(this,RegistrarStock.class);
                intent.putExtra("clienteseleccionado", listItemName);
                startActivity(intent);
                break;
            }
            case 3:{
                intent = new Intent(this,InfoCliente.class);
                intent.putExtra("clienteseleccionado", listItemName);
                startActivity(intent);
                break;
            }
        }
        return true;
    }


    private class Clientes extends AsyncTask<String, Void, ArrayList<String>> {

    	@Override
    	public ArrayList<String> doInBackground(String... params) {
    		LobbyManager lm = new LobbyManager(params[0]);
    		try {
				return lm.getClientes();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

    	}



    	protected void onPostExecute(ArrayList<String> result) {
    		findViewById(R.id.loadingPanel).setVisibility(View.GONE);

    			aa = new ArrayAdapter<String>(getApplicationContext(),
    					R.layout.clientesview, result);
    			clienteslv.setAdapter(aa);

        }


    }

	protected void onDestroy(){
    	super.onDestroy();
    }
}
