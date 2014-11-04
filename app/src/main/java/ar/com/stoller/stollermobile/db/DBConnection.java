package ar.com.stoller.stollermobile.db;

import net.sourceforge.jtds.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by gallardp on 7/09/14.
 */
public class DBConnection {

    private static DBConnection instance = null;
    public static String HOST;
    public static String USER;
    public static String PASS;
    //private static final String URL="jdbc:jtds:sqlserver://172.16.185.128:1433/SOBDD;";
    //private static final String URL="jdbc:jtds:sqlserver://192.168.0.103:1433/SOBDD;";

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
        String URL="jdbc:jtds:sqlserver://" + HOST + ";instance=SQLEXPRESS;" +
                "databaseName=SOBDD;integratedSecurity=true;";
		Connection conn = null;
		try {
			new Driver();
			conn = DriverManager.getConnection(URL, USER, PASS);
			System.out.println("Connection successfull");
		} catch (Exception e){
			System.out.println("Connection unsuccessfull");
			e.printStackTrace();
            return null;
		}
		return conn;

	}

}
