package ar.com.stoller.stollermobile.db;

import net.sourceforge.jtds.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by gallardp on 7/09/14.
 */
public class DBConnection {

    private static DBConnection instance = null;
	private static final String URL="jdbc:jtds:sqlserver://192.168.171.128:1433/SOBDD;";
    //private static final String URL="jdbc:jtds:sqlserver://10.0.0.113:1433/SOBDD;";
	private static final String USER="sa";
	private static final String PASS="asdf1234";
	private static Connection connection = null;

	private DBConnection(){}

	public static DBConnection getInstance(){
		if(instance == null)
			instance = new DBConnection();
		return instance;
	}

	public Connection getConnection(){
		if(connection == null)
			connection = conectar();
		return connection;
	}

	private Connection conectar(){
		Connection conn = null;
		try {
			new Driver();
			conn = DriverManager.getConnection(URL, USER, PASS);
			System.out.println("Connection successfull");
		} catch (Exception e){
			System.out.println("Connection unsuccessfull");
			e.printStackTrace();
		}
		return conn;

	}

}
