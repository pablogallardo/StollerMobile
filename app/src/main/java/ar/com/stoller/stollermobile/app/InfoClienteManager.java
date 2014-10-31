package ar.com.stoller.stollermobile.app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ar.com.stoller.stollermobile.db.Consultas;

/**
 * Created by pablo on 10/30/14.
 */
public class InfoClienteManager {

    private String cliente;
    private Consultas consulta;
    private String CUIT;
    private String usuario;
    private String email;
    private String telefono;
    private String celular;
    private ArrayList<String[]> direccion;

    public InfoClienteManager(String cliente){
        this.cliente = cliente;
        consulta = new Consultas();
        direccion = new ArrayList<String[]>();
        try{
            infoCliente();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void infoCliente() throws SQLException{
        ResultSet reset = consulta.getClienteInfo(cliente);
        reset.next();
        CUIT = reset.getString("cliente");
        usuario = reset.getString("usuario");
        email = reset.getString("email");
        telefono = reset.getString("telefono");
        celular = reset.getString("celular");
        infoDirecciones();
    }

    public String getCliente() {
        return cliente;
    }

    public Consultas getConsulta() {
        return consulta;
    }

    public String getCUIT() {
        return CUIT;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCelular() {
        return celular;
    }

    public ArrayList<String[]> getDireccion() {
        return direccion;
    }

    private void infoDirecciones(){
        ResultSet reset1 = consulta.getDireccionesEnvio(cliente);
        try {
            while (reset1.next()) {
                String[] s = new String[2];
                s[0] = reset1.getString("domicilio");
                s[1] = "Envio";
                direccion.add(s);
            }
            ResultSet reset2 = consulta.getDireccionesFacturacion(cliente);
            while (reset2.next()) {
                String[] s = new String[2];
                s[0] = reset2.getString("domicilio");
                s[1] = "Facturacion";
                direccion.add(s);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
