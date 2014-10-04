package ar.com.stoller.stollermobile.objects;

import java.io.Serializable;

/**
 * Created by gallardp on 2/10/14.
 */
public class DetalleOrdenPedido implements Serializable{

    private String producto;
    private String precioUnitario;
    private String cantidad;
    private String fechaEnvio;
    private String nroLinea;
    private String direccionEnvio;

    public DetalleOrdenPedido(String producto, String precioUnitario, String cantidad,
                              String fechaEnvio, String nroLinea, String direccionEnvio) {
        this.producto = producto;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.fechaEnvio = fechaEnvio;
        this.nroLinea = nroLinea;
        this.direccionEnvio = direccionEnvio;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(String precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getNroLinea() {
        return nroLinea;
    }

    public void setNroLinea(String nroLinea) {
        this.nroLinea = nroLinea;
    }

    public String[] toArray(){
        String[] s = new String[2];
        s[0] = producto;
        s[1] = cantidad;
        return s;
    }

}
