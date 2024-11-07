package es.aritzherrero.proyectoolimpiadas.Conexion;

import es.aritzherrero.proyectoolimpiadas.Modelo.Propiedades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimeZone;



public class ConexionBD {

    private Connection conn;

    public ConexionBD() throws SQLException {
        String host = Propiedades.getValor("host");
        String baseDatos = Propiedades.getValor("bbdd");
        String usuario = Propiedades.getValor("usuario");
        String password = Propiedades.getValor("contrasena");
        String cadenaConexion = "jdbc:mysql://" + host + "/" + baseDatos+ "?serverTimezone=" + TimeZone.getDefault().getID();
        conn = DriverManager.getConnection(cadenaConexion, usuario, password);
        conn.setAutoCommit(true);
    }
    public Connection getConexion() {
        return conn;
    }

    public void CloseConexion() throws SQLException{
        this.conn.close();
    }
}
