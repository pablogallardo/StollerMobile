package ar.com.stoller.stollermobile.objects;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Iterator;

/**
 * Created by gallardp on 2/10/14.
 */
public class OrdenPedido implements Serializable{

    private String ordenCompra;
    private Timestamp fecha;
    private String listaPrecios;
    private String divisa;
    private String estado;
    private String direccionFacturacion;
    private ArrayList<DetalleOrdenPedido> detalle;
    private String creadoPor;
    private int id;

    public OrdenPedido(String ordenCompra, String listaPrecios, String divisa,
                       String estado, String direccionFacturacion) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        java.util.Date utilDate = cal.getTime();
        this.ordenCompra = ordenCompra;
        this.fecha = new Timestamp(utilDate.getTime());
        this.listaPrecios = listaPrecios;
        this.divisa = divisa;
        this.estado = estado;
        this.direccionFacturacion = direccionFacturacion;
        detalle = new ArrayList<DetalleOrdenPedido>();
    }

    public OrdenPedido(Timestamp fecha){
        this.fecha = fecha;
        detalle = new ArrayList<DetalleOrdenPedido>();
    }

    public String getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(String ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
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

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public ArrayList<DetalleOrdenPedido> getDetalle() {
        return detalle;
    }

    public void setDetalle(ArrayList<DetalleOrdenPedido> detalle) {
        this.detalle = detalle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void agregarDetalle(String producto, float precioUnitario, int cantidad,
                               Date fechaEnvio, int nroLinea, String direccionEnvio){
        detalle.add(new DetalleOrdenPedido(producto, precioUnitario, cantidad, fechaEnvio,
                nroLinea, direccionEnvio));
    }

    public void removeDetalle(int position){
        detalle.remove(position);
    }

    public ArrayList<String[]> getArrayDetalles(){
        Iterator<DetalleOrdenPedido> i = detalle.iterator();
        ArrayList<String[]> s = new ArrayList<String[]>();
        while(i.hasNext()){
            s.add(i.next().toArray());
        }
        return s;
    }

    public float getSubTotal(){
        Iterator<DetalleOrdenPedido> i = detalle.iterator();
        float subtotal = 0;
        while(i.hasNext()){
            DetalleOrdenPedido d = i.next();
            subtotal += d.getPrecioUnitario() * d.getCantidad();
        }
        return subtotal;
    }


}
