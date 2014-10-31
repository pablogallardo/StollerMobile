package ar.com.stoller.stollermobile.app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ar.com.stoller.stollermobile.db.Consultas;

/**
 * Created by pablo on 10/10/14.
 */
public class VerPedidosManager {

    private String cliente;
    private ArrayList<String> pedidos;
    private Consultas consulta;

    public VerPedidosManager(String cliente){
        this.cliente = cliente;
        consulta = new Consultas();
        pedidos = new ArrayList<String>();
    }

    public ArrayList<String> getPedidos(){
        ResultSet reset = consulta.getOrdenes(cliente);
        pedidos.clear();
        try {
            while (reset.next()) {
                pedidos.add(reset.getString("idordenpedido"));
            }
            return pedidos;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }


}
