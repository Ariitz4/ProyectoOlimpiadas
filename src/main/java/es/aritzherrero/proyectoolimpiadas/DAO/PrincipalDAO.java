package es.aritzherrero.proyectoolimpiadas.DAO;


import es.aritzherrero.proyectoolimpiadas.Conexion.ConexionBD;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class PrincipalDAO {

    private ConexionBD conexion;
    public String consultaPrincipal = "SELECT Deportista.id_deportista,Evento.id_evento,Equipo.id_equipo,Deportista.nombre,Evento.nombre,Olimpiada.nombre,Deporte.nombre,Equipo.nombre,iniciales,edad,medalla "
            + "FROM Participacion,Deportista,Evento,Olimpiada,Deporte,Equipo "
            + "WHERE Deportista.id_deportista = Participacion.id_deportista "
            + "AND Equipo.id_equipo = Participacion.id_equipo "
            + "AND Evento.id_evento = Participacion.id_evento "
            + "AND Olimpiada.id_olimpiada = Evento.id_olimpiada "
            + "AND Deporte.id_deporte = Evento.id_deporte";

    public String consultaDeportista = "SELECT id_deportista,nombre,sexo,peso,altura "
            + "FROM Deportista";

    public String consultaEvento = "SELECT e.id_evento,e.id_olimpiada,e.id_deporte,e.nombre as nomevento,o.nombre as nomolimpiada,d.nombre as nomdeporte "
            + "FROM Evento e "
            + "LEFT JOIN Olimpiada o on o.id_olimpiada = e.id_olimpiada "
            + "LEFT JOIN Deporte d on d.id_deporte = e.id_deporte";

    public String consultaOlimpiada = "SELECT id_olimpiada,nombre,anio,temporada,ciudad "
            + "FROM Olimpiada";

    public String consultaDeporte = "SELECT id_deporte,nombre "
            + "FROM Deporte";

    public String consultaEquipo = "SELECT id_equipo,nombre,iniciales "
            + "FROM Equipo";

    // GENÉRICOS \\

    public Integer generarId(String tabla) {
        Integer nId = 0;
        String consulta = "SELECT COUNT(*) FROM " + tabla + ";";
        try {
            conexion = new ConexionBD();
            PreparedStatement ps = conexion.getConexion().prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                nId = rs.getInt(0);
            }
            conexion.CloseConexion();
        } catch (SQLException e) {}

        return nId;
    }


    public boolean ejecutarConsulta(String consulta) {
        try {
            conexion = new ConexionBD();
            PreparedStatement ps = conexion.getConexion().prepareStatement(consulta);
            int i = ps.executeUpdate(consulta);
            conexion.CloseConexion();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    /**
     * Elimina registro de tabla.
     * @param tabla
     * @param campo
     * @param ID
     * @return
     */
    public boolean eliminar(String tabla,String campo, Integer ID) {
        String consulta = "DELETE FROM "+tabla+
                " WHERE "+campo+"="+ID+";";

        return ejecutarConsulta(consulta);

    }

    public Integer buscarRegistros(String tabla, String campo, int ID) {
        String consulta = "SELECT COUNT(*) FROM "+tabla+" WHERE "+campo+" ='"+ID+"';";
        int resp=0;
        try {
            conexion = new ConexionBD();
            PreparedStatement ps = conexion.getConexion().prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resp = rs.getInt(0);
            }
            conexion.CloseConexion();
        } catch (SQLException e) {}
        return resp;
    }
}