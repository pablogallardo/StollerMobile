package ar.com.stoller.stollermobile.app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ar.com.stoller.stollermobile.db.Consultas;
import ar.com.stoller.stollermobile.objects.OrdenPedido;

/**
 * Created by gallardp on 21/09/14.
 */
public class SeleccionarProductoManager {

    private ArrayList<String> productos;
    private String usuario;
    private Consultas consulta;
    private ResultSet reset;
    private OrdenPedido ordenPedido;
    private String cliente;

    public SeleccionarProductoManager(){
        productos = new ArrayList<String>();
        consulta = new Consultas();

    }

    public SeleccionarProductoManager(OrdenPedido ordenPedido, String cliente){
        productos = new ArrayList<String>();
        consulta = new Consultas();
        this.ordenPedido = ordenPedido;
        this.cliente = cliente;
    }

    public ArrayList<String> getProductos()  {
        reset = consulta.getProductos();
        try {
            while (reset.next()) {
                productos.add(reset.getString("nombre"));
            }
        } catch (SQLException e){
            e.printStackTrace();
            productos = null;
        }
        return productos;
    }

    public boolean existeProducto(String producto){
        reset = consulta.existeProducto(producto);
        try{
            if(reset.next()){
                return true;
            } else {
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public String getUM(String producto){
        reset = consulta.existeProducto(producto);
        try{
            reset.next();
            return reset.getString("um");
        } catch (SQLException e){
            e.printStackTrace();
            return "lalala";
        }
    }


    public boolean RegistrarStock(String producto, String mes, String año, String cantidad,
                                  String cliente){
        return consulta.insertarStock(producto,mes, año, cantidad,cliente);
    }

    public ArrayList<String> getDirecciones(){
        return getColumnaEnTabla(consulta.getDirecciones(cliente), "domicilio");
    }

    private ArrayList<String> getColumnaEnTabla(ResultSet reset, String columna){

        ArrayList<String> al = new ArrayList<String>();
        try{
            while(reset.next()){
                al.add(reset.getString(columna));
            }
            return al;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
