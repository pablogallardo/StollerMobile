package ar.com.stoller.stollermobile.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Iterator;

/**
 * Created by gallardp on 2/10/14.
 */
public class OrdenPedido implements Serializable{

    private String ordenCompra;
    private Date fecha;
    private String listaPrecios;
    private String divisa;
    private String estado;
    private String direccionFacturacion;
    private ArrayList<DetalleOrdenPedido> detalle;

    public OrdenPedido(String ordenCompra, String listaPrecios, String divisa,
                       String estado, String direccionFacturacion) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        java.util.Date utilDate = cal.getTime();
        this.ordenCompra = ordenCompra;
        this.fecha = new Date(utilDate.getTime());
        this.listaPrecios = listaPrecios;
        this.divisa = divisa;
        this.estado = estado;
        this.direccionFacturacion = direccionFacturacion;
        detalle = new ArrayList<DetalleOrdenPedido>();
    }

    public OrdenPedido(Date fecha){
        this.fecha = fecha;
        detalle = new ArrayList<DetalleOrdenPedido>();
        //detalle.add(new DetalleOrdenPedido("producto","test","5000","test","test","test"));
    }

    public String getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(String ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getListaPrecios() {
        return listaPrecios;
    }

    public void setListaPrecios(String listaPrecios) {
        this.listaPrecios = listaPrecios;
    }

    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDireccionFacturacion() {
        return direccionFacturacion;
    }

    public void setDireccionFacturacion(String direccionFacturacion) {
        this.direccionFacturacion = direccionFacturacion;
    }

    public void agregarDetalle(String producto, String precioUnitario, String cantidad,
                               String fechaEnvio, String nroLinea, String direccionEnvio){
        detalle.add(new DetalleOrdenPedido(producto, precioUnitario, cantidad, fechaEnvio,
                nroLinea, direccionEnvio));
    }

    public ArrayList<String[]> getArrayDetalles(){
        Iterator<DetalleOrdenPedido> i = detalle.iterator();
        ArrayList<String[]> s = new ArrayList<String[]>();
        while(i.hasNext()){
            s.add(i.next().toArray());
        }
        return s;
    }


}
