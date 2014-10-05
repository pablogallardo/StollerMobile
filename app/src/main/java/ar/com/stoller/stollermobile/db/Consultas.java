package ar.com.stoller.stollermobile.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Iterator;

import ar.com.stoller.stollermobile.objects.DetalleOrdenPedido;
import ar.com.stoller.stollermobile.objects.OrdenPedido;

/**
 * Created by gallardp on 7/09/14.
 */
public class Consultas {
    /**
     * Conexión con la base de datos.
     */
    private Connection connection;

    /**
     * Toma la instancia de conexión a la BD.
     */
	public Consultas(){
        connection = DBConnection.getInstance().getConnection();
	}

    /**
     * Chequea los datos del loggeo con la BD
     * @param user
     * @param pass
     * @return
     */
	public String login(String user, String pass){
		Statement stmt;
		ResultSet reset;
		try {

			stmt = connection.createStatement();
			reset = stmt.executeQuery("select * from usuario u join recurso r on u.orareferencia" +
                    " = r.recurso where " + "u.usuario = '" + user + "' AND u.clave = '" +
                    pass + "' and u.idrol = 4");
			String razonsocial;
			if(reset.next()){
				razonsocial = reset.getString("apellidonombre");
				stmt.close();
				return razonsocial;
			} else {
				stmt.close();
				return "FAILED";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (NullPointerException n){
            return null;
        }

	}

    /**
     * Devuelve un ResultSet con los datos de todos los clientes del vendedor.
     * @param vendedor
     * @return
     */
	public ResultSet getClientes(String vendedor) {
        Statement stmt;
        ResultSet reset;
        try {
            stmt = connection.createStatement();
            reset = stmt.executeQuery("select * from cliente c join usuario u"
                    + " on c.vendedor = u.orareferencia where u.usuario = '" + vendedor + "'");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return reset;
    }

    /**
     * Devuelve un ResultSet con todos los datos del Stock del cliente en un determinado mes y año.
     * @param cliente
     * @param mes
     * @param año
     * @return
     */
    public ResultSet getStock(String cliente, int mes, int año){
        Statement stmt;
        ResultSet reset;
        try {
            stmt = connection.createStatement();
            String query = "select * from DetalleStock ds join Item i" +
                    " on ds.iditem = i.iditem join Cliente c on c.cliente=ds.cliente" +
                    " where c.razonsocial = '" + cliente + "' AND " +
                    "ds.año = '" + año + "' AND ds.mes = '" + mes + "'";
            reset = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return reset;
    }

    /**
     * Devuelve un ResultSet con todos los datos de los productos existentes de la BD.
     * @return
     */
    private ResultSet getTabla(String tabla){
        Statement stmt;
        ResultSet reset;
        try {
            stmt = connection.createStatement();
            String query = "Select * from " + tabla;
            reset = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return reset;
    }

    public ResultSet getListaPrecios(){
        return getTabla("ListaPrecios");
    }

    public ResultSet getProductos(){
        return getTabla("Item");
    }


    /**
     * Elimina de la BD el Stock de un cliente de un determinado mes y año.
     * @param cliente
     * @param producto
     * @param mes
     * @param año
     * @return
     */
    public boolean eliminarStockProducto(String cliente, String producto, int mes, int año){
        try{
            Statement stmt = connection.createStatement();
            String sql = "delete from detallestock where cliente = " + getIdCliente(cliente) +
                    " and iditem = " + getIdProducto(producto) + " and mes = " + mes +
                    " and año = " + año;
            int n = stmt.executeUpdate(sql);
            return (n == 1);
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet existeProducto(String producto){
        Statement stmt;
        ResultSet reset;
        try {
            stmt = connection.createStatement();
            String query = "Select * from item where nombre = '" + producto + "'";
            reset = stmt.executeQuery(query);
        } catch (SQLException e) {

            e.printStackTrace();
            return null;
        }
        return reset;
    }



    public boolean insertarStock(String producto, String mes, String año,
                              String cantidad, String cliente){
        try {
            Statement stmt = connection.createStatement();
            String sql = "insert into detallestock (iditem, cantidad, fechaalta, cliente" +
                    ", fechamodificacion, mes, año) values ("+ getIdProducto(producto) + ", " +
                    cantidad + ", getdate(), " + getIdCliente(cliente) + ", getdate(), " + mes +
                    ", " + año + ")";
            int n = stmt.executeUpdate(sql);
            return (n == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet getDirecciones(String cliente){
        Statement stmt;
        ResultSet reset;
        try {
            stmt = connection.createStatement();
            String query = "Select * from Direccion where cliente = '" + getIdCliente(cliente) +
                    "' AND idtipodireccion = 2";
            reset = stmt.executeQuery(query);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return reset;
    }



    private long getIdCliente(String cliente){
        Statement stmt;
        ResultSet reset;
        long id;
        try {
            stmt = connection.createStatement();
            String query = "Select * from cliente where razonsocial = '" + cliente + "'";
            reset = stmt.executeQuery(query);
            reset.next();
            id = reset.getLong("cliente");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return id;
    }

    private int getIdATC(String ATC){
        Statement stmt;
        ResultSet reset;
        int id;
        try {
            stmt = connection.createStatement();
            String query = "Select * from recurso r join usuario u on r.recurso = u.orareferencia " +
                    "where u.usuario = '" + ATC + "'";
            reset = stmt.executeQuery(query);
            reset.next();
            id = reset.getInt("recurso");
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
        return id;
    }

    private int getIdProducto(String producto){
        Statement stmt;
        ResultSet reset;
        int id;
        try {
            stmt = connection.createStatement();
            String query = "Select * from item where nombre = '" + producto + "'";
            reset = stmt.executeQuery(query);
            reset.next();
            id = reset.getInt("iditem");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return id;
    }

    private void insertarOrdenPedido(String cliente, OrdenPedido orden, String vendedor)
            throws SQLException {
        String sql = "Insert into OrdenPedido (cliente, idtermino, ordencompra, fechaordenpedido," +
                "idlistaprecios, iddivisa, idestado, creadopor, fechacreacion, " +
                "iddireccionfacturacion, vendedor) values (?, 1, ?, ?, ?, 1, 1, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setLong(1, getIdCliente(cliente));
        stmt.setString(2, orden.getOrdenCompra());
        stmt.setDate(3, orden.getFecha());
        stmt.setInt(4, getIdListaPrecios(orden.getListaPrecios()));
        stmt.setString(5, orden.getCreadoPor());
        stmt.setDate(6, getActualDate());
        stmt.setInt(7, getIdDireccion(getIdCliente(cliente), orden.getDireccionFacturacion()));
        stmt.setInt(8, getIdATC(vendedor));
        stmt.executeUpdate();
    }

    private Date getActualDate(){
        Calendar cal = Calendar.getInstance();
        java.util.Date dateUtil = cal.getTime();
        return new Date(dateUtil.getTime());
    }


    private int getIdDireccion(long cliente, String direccion){
        Statement stmt;
        ResultSet reset;
        int id;
        try {
            stmt = connection.createStatement();
            String query = "select * from Direccion where cliente = '" + cliente +
                    "' AND domicilio = '" + direccion + "'";
            reset = stmt.executeQuery(query);
            reset.next();
            id = reset.getInt("iddireccion");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return id;
    }

    private int getIdListaPrecios(String lista){
        Statement stmt;
        ResultSet reset;
        int id;
        try {
            stmt = connection.createStatement();
            String query = "Select * from ListaPrecios where nombre = '" + lista + "'";
            reset = stmt.executeQuery(query);
            reset.next();
            id = reset.getInt("idlistaprecios");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return id;
    }

    private int getLastID() throws SQLException {
        String sql = "Select @@Identity as 'ID'";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet reset = stmt.executeQuery();
        reset.next();
        return reset.getInt("ID");
    }

    private void insertarDetallePedido(String cliente,DetalleOrdenPedido detalle, String creadoPort,
                                       int idOP)
            throws SQLException {
        String sql = "INSERT INTO DetalleOrdenPedido (iditem, preciou, cantidad, nrolinea, " +
                "iddireccionenvio, fechaenvio, idestado, creadopor, fechacreacion, idordenpedido) " +
                "values (?, ?, ?, ?, ?, ?, 1, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, getIdProducto(detalle.getProducto()));
        stmt.setFloat(2, detalle.getPrecioUnitario());
        stmt.setInt(3, detalle.getCantidad());
        stmt.setInt(4, detalle.getNroLinea());
        stmt.setInt(5, getIdDireccion(getIdCliente(cliente), detalle.getDireccionEnvio()));
        stmt.setDate(6, detalle.getFechaEnvio());
        stmt.setString(7, creadoPort);
        stmt.setDate(8, getActualDate());
        stmt.setInt(9, idOP);
    }

    public boolean coordinatedInsertOP(String cliente, OrdenPedido orden, String vendedor) {
        try {
            connection.setAutoCommit(false);
            insertarOrdenPedido(cliente, orden, vendedor);
            Iterator<DetalleOrdenPedido> iterador = orden.getDetalle().iterator();
            int idOP = getLastID();
            while (iterador.hasNext()) {
                insertarDetallePedido(cliente, iterador.next(), orden.getCreadoPor(), idOP);
            }
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }



}
