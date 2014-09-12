package ar.com.stoller.stollermobile.app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ar.com.stoller.stollermobile.db.Consultas;

/**
 * Created by gallardp on 7/09/14.
 */
public class LobbyManager {

    private ArrayList<String> clientes;
	private String usuario;
	private Consultas consulta;
	private ResultSet reset;

	public LobbyManager(String usuario){
		this.usuario = usuario;
		clientes = new ArrayList<String>();
		consulta = new Consultas();
	}

	public ArrayList<String> getClientes() throws SQLException {
		reset = consulta.getClientes(usuario);
		while(reset.next()){
			clientes.add(reset.getString("razonsocial"));
		}
		return clientes;
	}
}
