package ar.com.stoller.stollermobile.app;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ar.com.stoller.stollermobile.db.Consultas;
import ar.com.stoller.stollermobile.objects.OrdenPedido;

/**
 * Created by pablo on 10/13/14.
 */
public class ShowPedidoManager {

    private Consultas consulta;
    private int idOP;
    private OrdenPedido orden;

    public ShowPedidoManager(int idOP){
        this.idOP = idOP;
        consulta = new Consultas();
        orden = consulta.getOrdenPedido(idOP);
    }

    public OrdenPedido getOrdenPedido(){
        return orden;
    }

    public String getDireccion(){
        return orden.getDireccionFacturacion();
    }

    public String getFecha(){
        return new SimpleDateFormat("dd/MM/yyyy").format(orden.getFecha());
    }

    public String getListaPrecio(){
        return orden.getListaPrecios();
    }

    public String getOrdenCompra(){
        return orden.getOrdenCompra();
    }

    public float getSubtotal(){
        return orden.getSubTotal();
    }

    public ArrayList<String[]> getDetalles(){
        return orden.getArrayDetalles();
    }

}
