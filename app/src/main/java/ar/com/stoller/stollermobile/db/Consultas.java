package ar.com.stoller.stollermobile.db;

import android.content.Context;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
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

    public Consultas(Context context){
        DBConfiguracion conf = new DBConfiguracion(context);
        String [] s = conf.getConfiguracion();
        DBConnection.HOST = s[0];
        DBConnection.USER = s[1];
        DBConnection.PASS = s[2];
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
        Statement stmt;
        ResultSet reset;
        try {
            stmt = connection.createStatement();
            String query = "Select * from item where segmento NOT LIKE '1%000000' and idestado = 1 " +
                    "order by nombre";
            reset = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return reset;
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
            String query = "Select * from item where nombre = '" + producto.replace("'","''") + "'";
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

    public ResultSet getDireccionesFacturacion(String cliente){
        Statement stmt;
        ResultSet reset;
        try {
            stmt = connection.createStatement();
            String query = "Select * from Direccion where cliente = '" + getIdCliente(cliente) +
                    "' AND idtipodireccion = 1";
            reset = stmt.executeQuery(query);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return reset;
    }

    public ResultSet getDireccionesEnvio(String cliente){
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
            String query = "Select * from item where nombre = '" + producto.replace("'","''") + "'";
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
                "iddireccionfacturacion, vendedor) values (?, 1, ?, ?, ?, 1, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setLong(1, getIdCliente(cliente));
        stmt.setString(2, orden.getOrdenCompra());
        stmt.setTimestamp(3, orden.getFecha());
        stmt.setInt(4, getIdListaPrecios(orden.getListaPrecios()));
        stmt.setInt(5, getIdEstadoOrdenPedido(orden.getEstado()));
        stmt.setString(6, orden.getCreadoPor());
        stmt.setTimestamp(7, getActualDate());
        stmt.setInt(8, getIdDireccion(getIdCliente(cliente), orden.getDireccionFacturacion(), 1));
        stmt.setInt(9, getIdATC(vendedor));
        stmt.executeUpdate();
    }

    private Timestamp getActualDate(){
        Calendar cal = Calendar.getInstance();
        java.util.Date dateUtil = cal.getTime();
        return new Timestamp(dateUtil.getTime());
    }


    private int getIdDireccion(long cliente, String direccion, int tipo){
        Statement stmt;
        ResultSet reset;
        int id;
        try {
            stmt = connection.createStatement();
            String query = "select * from Direccion where cliente = '" + cliente +
                    "' AND domicilio = '" + direccion + "' AND idtipodireccion = " + tipo;
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

    private void insertarDetallePedido(String cliente,DetalleOrdenPedido detalle, String creadoPor,
                                       String modificadoPor, int idOP)
            throws SQLException {
        String sql = "INSERT INTO DetalleOrdenPedido (iditem, preciou, cantidad, nrolinea, " +
                "iddireccionenvio, fechaenvio, idestado, creadopor, fechacreacion, idordenpedido, " +
                "modificadopor, fechamodificacion) " +
                "values (?, ?, ?, ?, ?, ?, 1, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, getIdProducto(detalle.getProducto()));
        stmt.setFloat(2, detalle.getPrecioUnitario());
        stmt.setInt(3, detalle.getCantidad());
        stmt.setInt(4, detalle.getNroLinea());
        stmt.setInt(5, getIdDireccion(getIdCliente(cliente), detalle.getDireccionEnvio(), 2));
        stmt.setDate(6, detalle.getFechaEnvio());
        stmt.setString(7, creadoPor);
        stmt.setTimestamp(8, getActualDate());
        stmt.setInt(9, idOP);
        if(modificadoPor == null){
            stmt.setString(10, null);
            stmt.setDate(11, null);
        } else {
            stmt.setString(10, modificadoPor);
            stmt.setTimestamp(11, getActualDate());
        }
        stmt.executeUpdate();
    }

    public int coordinatedInsertOP(String cliente, OrdenPedido orden, String vendedor) {
        try {
            connection.setAutoCommit(false);
            insertarOrdenPedido(cliente, orden, vendedor);
            Iterator<DetalleOrdenPedido> iterador = orden.getDetalle().iterator();
            int idOP = getLastID();
            while (iterador.hasNext()) {
                insertarDetallePedido(cliente, iterador.next(), orden.getCreadoPor(),null, idOP);
            }
            connection.commit();
            connection.setAutoCommit(true);
            return idOP;
        } catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    public ResultSet getOrdenes(String cliente){
        Statement stmt;
        ResultSet reset;
        try {
            stmt = connection.createStatement();
            String query = "Select * from OrdenPedido where cliente = '" + getIdCliente(cliente)+
                    "' AND idestado IN (1, 2, 3, 7)";
            reset = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return reset;
    }

    public ResultSet getPrecio(String producto, String lista){
        String query = "SELECT DetalleListaPrecios.precio FROM ListaPrecios INNER JOIN  " +
                "DetalleListaPrecios ON ListaPrecios.idlistaprecios = " +
                "DetalleListaPrecios.idlistaprecios WHERE (DetalleListaPrecios.iditem = "+ getIdProducto(producto) +
                ") and (DetalleListaPrecios.idlistaprecios = " + getIdListaPrecios(lista) + ")";
        Statement stmt;
        ResultSet reset;
        try {
            stmt = connection.createStatement();
            reset = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return reset;
    }


    public OrdenPedido getOrdenPedido(int id){
        String query = "Select * from ordenpedido where idordenpedido = ?";
        OrdenPedido ordenPedido = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet reset = stmt.executeQuery();
            reset.next();
            String ordenCompra = reset.getString("ordencompra");
            Timestamp fecha = reset.getTimestamp("fechaordenpedido");
            String listaPrecios = getListaPrecios(reset.getInt("idlistaprecios"));
            String divisa = reset.getString("iddivisa");
            String estado = getEstadoOrdenPedido(reset.getInt("idestado"));
            String direccionFacturacion = getDireccion(reset.getInt("iddireccionfacturacion"), 1);
            ArrayList<DetalleOrdenPedido> detalle = getDetalles(id);
            String creadoPor = reset.getString("creadopor");
            int idOP = id;
            ordenPedido = new OrdenPedido(ordenCompra,listaPrecios,divisa,estado,
                    direccionFacturacion,fecha, idOP, detalle);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordenPedido;
    }

    public ResultSet getEstadosOrdenPedido(){
        String query = "Select * from estadoordenpedido where idestadoordenpedido IN (1, 2, 3, 5)";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<DetalleOrdenPedido> getDetalles(int id){
        String query = "Select * from detalleordenpedido where idordenpedido = ?";
        ArrayList<DetalleOrdenPedido> dop = new ArrayList<DetalleOrdenPedido>();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet reset = stmt.executeQuery();
            while(reset.next()){
                String producto = getProducto(reset.getInt("iditem"));
                float precioUnitario = reset.getFloat("preciou");
                int cantidad = reset.getInt("cantidad");
                Date fechaEnvio = reset.getDate("fechaenvio");
                int nroLinea = reset.getInt("nrolinea");
                String direccionEnvio = getDireccion(reset.getInt("iddireccionenvio"), 2);
                dop.add(new DetalleOrdenPedido(producto,precioUnitario,cantidad, fechaEnvio,
                        nroLinea, direccionEnvio));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dop;
    }

    private String getProducto(int id){
        String query = "Select * from item where iditem = ?";
        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet reset = stmt.executeQuery();
            reset.next();
            return reset.getString("nombre");
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    private String getDireccion(int id, int tipo){
        String query = "Select * from direccion where iddireccion = ? and idtipodireccion = ?";
        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.setInt(2, tipo);
            ResultSet reset = stmt.executeQuery();
            reset.next();
            return reset.getString("domicilio");
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    private String getListaPrecios(int id){
        String query = "Select * from listaprecios where idlistaprecios = ?";
        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet reset = stmt.executeQuery();
            reset.next();
            return reset.getString("nombre");
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    private int getIdEstadoOrdenPedido(String estadoNombre) throws SQLException {
        String query = "Select * from estadoordenpedido where nombre = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, estadoNombre);
        ResultSet reset = stmt.executeQuery();
        reset.next();
        return reset.getInt("idestadoordenpedido");
    }
    private String getEstadoOrdenPedido(int id) throws SQLException{
        String query = "Select * from estadoordenpedido where idestadoordenpedido = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet reset = stmt.executeQuery();
        reset.next();
        return reset.getString("nombre");
    }

    private void actualizarOrdenPedido(String cliente, OrdenPedido orden, String vendedor)
            throws SQLException{
        String sql = "update ordenpedido set cliente = ?, idtermino = ?, ordencompra = ?, " +
                "fechaordenpedido = ?, idlistaprecios = ?, iddivisa = ?, idestado = ?, " +
                "modificadopor = ?, fechamodificacion = ?, iddireccionfacturacion = ?, " +
                "vendedor = ? where idordenpedido = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, getIdCliente(cliente));
            stmt.setInt(2, 1);
            stmt.setString(3, orden.getOrdenCompra());
            stmt.setTimestamp(4, orden.getFecha());
            stmt.setInt(5, getIdListaPrecios(orden.getListaPrecios()));
            stmt.setInt(6, 1);
            stmt.setInt(7, getIdEstadoOrdenPedido(orden.getEstado()));
            stmt.setString(8, orden.getModificadoPor());
            stmt.setTimestamp(9, getActualDate());
            stmt.setInt(10, getIdDireccion(getIdCliente(cliente),
                    orden.getDireccionFacturacion(), 1));
            stmt.setInt(11, getIdATC(vendedor));
            stmt.setInt(12, orden.getId());
            stmt.executeUpdate();
    }

    private void eliminarDetalleOrdenPedido(OrdenPedido orden) throws SQLException{
        String delete = "delete from detalleordenpedido where idordenpedido = ?";
            PreparedStatement stmt = connection.prepareStatement(delete);
            stmt.setInt(1, orden.getId());
            stmt.executeUpdate();
    }

    public boolean coordinatedUpdateOP(String cliente, OrdenPedido orden, String vendedor){
        try {
            connection.setAutoCommit(false);
            actualizarOrdenPedido(cliente, orden, vendedor);
            eliminarDetalleOrdenPedido(orden);
            Iterator<DetalleOrdenPedido> i = orden.getDetalle().iterator();
            while(i.hasNext()){
                insertarDetallePedido(cliente, i.next(), orden.getCreadoPor(),
                        orden.getModificadoPor(), orden.getId());
            }
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet getClienteInfo(String cliente){
        String query = "select * from cliente c join usuario u on c.cliente = u.usuario " +
                "where c.razonsocial = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, cliente);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
