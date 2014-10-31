package ar.com.stoller.stollermobile;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import ar.com.stoller.stollermobile.db.Consultas;
import ar.com.stoller.stollermobile.db.DBConfiguracion;
import ar.com.stoller.stollermobile.db.DBConnection;


public class Login extends Activity {
    private Button ingresar;
    private EditText user;
    private EditText pass;
    private Consultas c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        c = new Consultas();
        ingresar = (Button) findViewById(R.id.sendbtn);
        user = (EditText) findViewById(R.id.useret);
        pass = (EditText) findViewById(R.id.passwdet);
        listenerBtn();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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

    public void listenerBtn() {
        ingresar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new LoginThread().execute(user.getText().toString(), pass.getText().toString());
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            }
        });
    }

    private class LoginThread extends AsyncTask<String, Void, String> {

        @Override
        public String doInBackground(String... params) {
            DBConfiguracion dbconfig = new DBConfiguracion(getApplicationContext());
            String [] s = dbconfig.getConfiguracion();
            DBConnection.HOST = s[0];
            DBConnection.USER = s[1];
            DBConnection.PASS = s[2];
            return c.login(params[0], params[1]);
        }

        protected void onProgressUpdate(Void... params) {

        }

        protected void onPostExecute(String result) {
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            if (result != null) {
                if(result == "FAILED"){
                    Toast.makeText(getApplicationContext(), "Usuario o contraseña inválidos",
                            Toast.LENGTH_LONG).show();
                }else {

                    Intent intent = new Intent(getApplicationContext(), Lobby.class);
                    intent.putExtra("Usuario", user.getText().toString());
                    intent.putExtra("razonsocial", result);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Servidor no disponible",
                        Toast.LENGTH_LONG).show();
            }
        }


    }
}
