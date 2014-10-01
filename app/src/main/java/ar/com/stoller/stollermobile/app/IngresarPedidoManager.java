package ar.com.stoller.stollermobile.app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ar.com.stoller.stollermobile.db.Consultas;

/**
 * Created by gallardp on 30/09/14.
 */
public class IngresarPedidoManager {

    String cliente;
    Consultas consulta;

    public IngresarPedidoManager(String cliente){
        this.cliente = cliente;
        consulta = new Consultas();
    }

    public ArrayList<String> getDirecciones(){
        ResultSet reset = consulta.getTabla("direccion");
        ArrayList<String> al = new ArrayList<String>();
        try{
            while(reset.next()){
                al.add(reset.getString("nombre"));
            }
            return al;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }




}
