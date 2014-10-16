package ar.com.stoller.stollermobile.app;

import java.sql.Timestamp;

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

    public Timestamp getFecha(){
        return orden.getFecha();
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

}
