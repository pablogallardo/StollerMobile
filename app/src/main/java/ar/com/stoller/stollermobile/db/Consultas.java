package ar.com.stoller.stollermobile.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by gallardp on 7/09/14.
 */
public class Consultas {

    Connection connection = DBConnection.getInstance().getConnection();

	public Consultas(){

	}

	public String login(String user, String pass){
		Statement stmt;
		ResultSet reset;
		try {

			stmt = connection.createStatement();
			reset = stmt.executeQuery("select * from usuario u join recurso r on u.orareferencia = r.recurso where "
					+ "u.usuario = '" + user + "' AND u.clave = '" + pass + "' and u.idrol = 4");
			String razonsocial;
			if(reset.next()){
				razonsocial = reset.getString("apellidonombre");
				stmt.close();
				return razonsocial;
			} else {
				stmt.close();
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public ResultSet getClientes(String vendedor) {
        Statement stmt;
        ResultSet reset;
        try {
            stmt = connection.createStatement();
            reset = stmt.executeQuery("select * from cliente c join usuario u"
                    + " on c.vendedor = u.orareferencia where u.usuario = '" + vendedor + "'");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return reset;
    }
}
