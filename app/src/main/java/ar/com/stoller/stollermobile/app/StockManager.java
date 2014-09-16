package ar.com.stoller.stollermobile.app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ar.com.stoller.stollermobile.db.Consultas;

/**
 * Created by gallardp on 12/09/14.
 */
public class StockManager {

    private ArrayList<String[]> stock;
    private String cliente;
    private int mes;
    private int año;
    private Consultas consulta;
    private ResultSet reset;

    public StockManager(String cliente, int mes, int año){
        this.cliente = cliente;
        this.mes = mes;
        this.año = año;
        stock = new ArrayList<String[]>();
        consulta = new Consultas();
    }

    public ArrayList<String[]> getStock() throws SQLException {
        reset = consulta.getStock(cliente, mes, año);
        while(reset.next()){
            String[] stockItem = new String[3];
            stockItem[0] = reset.getString("nombre");
            stockItem[1] = reset.getString("cantidad");
            stockItem[2] = reset.getString("um");
            stock.add(stockItem);
        }
        return stock;
    }
}
