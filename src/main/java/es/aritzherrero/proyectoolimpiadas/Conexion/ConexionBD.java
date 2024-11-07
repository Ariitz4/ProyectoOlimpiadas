package es.aritzherrero.proyectoolimpiadas.Conexion;

import es.aritzherrero.proyectoolimpiadas.Modelo.Propiedades;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimeZone;

/**
 * Clase encargada de gestionar la conexión a la base de datos.
 * Esta clase utiliza parámetros de configuración para establecer una conexión con la base de datos
 * y proporciona procedimientos para obtener y cerrar dicha conexión.
 */
public class ConexionBD {

    private Connection conn;

    /**
     * Procedimiento que establece una conexión con la base de datos utilizando los valores
     * de configuración obtenidos de la clase de propiedades.
     *
     * @throws SQLException si ocurre un error al intentar conectar con la base de datos.
     */
    public ConexionBD() throws SQLException {
        // Obtiene los parámetros de conexión de las propiedades de configuración
        String host = Propiedades.getValor("host");
        String baseDatos = Propiedades.getValor("bbdd");
        String usuario = Propiedades.getValor("usuario");
        String password = Propiedades.getValor("contrasena");

        // Construye la cadena de conexión para el acceso a la base de datos
        String cadenaConexion = "jdbc:mysql://" + host + "/" + baseDatos + "?serverTimezone=" + TimeZone.getDefault().getID();

        // Establece la conexión con la base de datos
        conn = DriverManager.getConnection(cadenaConexion, usuario, password);

        // Configura la conexión para que el modo de auto-commit esté activo
        conn.setAutoCommit(true);
    }

    /**
     * Procedimiento que devuelve la conexión actual a la base de datos.
     *
     * @return la conexión a la base de datos.
     */
    public Connection getConexion() {
        return conn;
    }

    /**
     * Procedimiento que cierra la conexión actual a la base de datos.
     *
     * @throws SQLException si ocurre un error al intentar cerrar la conexión.
     */
    public void CloseConexion() throws SQLException {
        this.conn.close();
    }
}
