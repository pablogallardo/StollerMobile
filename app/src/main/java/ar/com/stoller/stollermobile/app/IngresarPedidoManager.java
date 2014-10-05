package ar.com.stoller.stollermobile.app;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ar.com.stoller.stollermobile.db.Consultas;
import ar.com.stoller.stollermobile.objects.OrdenPedido;

/**
 * Created by gallardp on 30/09/14.
 */
public class IngresarPedidoManager {

    String cliente;
    Consultas consulta;
    OrdenPedido ordenPedido;

    public IngresarPedidoManager(String cliente){
        this.cliente = cliente;
        consulta = new Consultas();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        java.util.Date utilDate = cal.getTime();
        ordenPedido = new OrdenPedido(new Date(utilDate.getTime()));
    }



    public ArrayList<String> getDirecciones(){
        return getColumnaEnTabla(consulta.getDirecciones(cliente), "domicilio");
    }

    public ArrayList<String> getListaPrecios(){
        return getColumnaEnTabla(consulta.getListaPrecios(), "nombre");
    }

    public void agregarDetalleOrdenPedido(String producto, float precioUnitario, int cantidad,
                                          Date fechaEnvio, int nroLinea,
                                          String direccionEnvio){
        ordenPedido.agregarDetalle(producto, precioUnitario, cantidad, fechaEnvio, nroLinea,
                direccionEnvio);
    }

    public void setOrdenPedido(OrdenPedido op){
        ordenPedido = op;
    }


    private ArrayList<String> getColumnaEnTabla(ResultSet reset, String columna){

        ArrayList<String> al = new ArrayList<String>();
        try{
            while(reset.next()){
                al.add(""+reset.getString(columna));
            }
            return al;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public OrdenPedido getOrdenPedido(){
        return ordenPedido;
    }

    public ArrayList<String[]> getArrayDetalles(){
        return ordenPedido.getArrayDetalles();
    }

    public boolean guardarOrdenPedido(String cliente, OrdenPedido orden, String vendedor){
        return consulta.coordinatedInsertOP(cliente, orden, vendedor);
    }
}
