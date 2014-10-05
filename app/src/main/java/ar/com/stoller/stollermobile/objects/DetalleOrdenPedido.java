package ar.com.stoller.stollermobile.objects;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by gallardp on 2/10/14.
 */
public class DetalleOrdenPedido implements Serializable{

    private String producto;
    private float precioUnitario;
    private int cantidad;
    private Date fechaEnvio;
    private int nroLinea;
    private String direccionEnvio;

    public DetalleOrdenPedido(String producto, float precioUnitario, int cantidad,
                              Date fechaEnvio, int nroLinea, String direccionEnvio) {
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

    public float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public int getNroLinea() {
        return nroLinea;
    }

    public void setNroLinea(int nroLinea) {
        this.nroLinea = nroLinea;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public String[] toArray(){
        String[] s = new String[2];
        s[0] = producto;
        s[1] = ""+cantidad;
        return s;
    }

}
