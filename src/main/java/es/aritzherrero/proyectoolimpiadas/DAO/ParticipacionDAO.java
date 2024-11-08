package es.aritzherrero.proyectoolimpiadas.DAO;

import es.aritzherrero.proyectoolimpiadas.Conexion.ConexionBD;
import es.aritzherrero.proyectoolimpiadas.Modelo.Participacion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParticipacionDAO {
    private ConexionBD conexion;
    private PrincipalDAO principal;

    public ParticipacionDAO() throws SQLException {
        conexion= new ConexionBD();
        principal= new PrincipalDAO();

    }
    /**
     * Carga todos los registros de la tabla Participacion.
     * @return ObservableList con Participacion
     */
    public ObservableList<Participacion> cargarParticipacion() {

        ObservableList<Participacion>listaParticipacion = FXCollections.observableArrayList();
        String consultaModificada =principal.consultaPrincipal + ";";
        listaParticipacion=crearListaParticipaciones(consultaModificada);
        return listaParticipacion;
    }

    /**
     * Filtra los datos de participacion dependiendo del campo seleccionado en el ChoiceBox y del valor del TextField.
     * @param campo Columna de la tabla seleccionada.
     * @param valor El valor con el que quieres que coincidan los registros.
     * @return ObservableList con Participacion
     */
    public ObservableList<Participacion> filtrarParticipaciones(String campo,String valor){

        ObservableList<Participacion>listaParticipacion= FXCollections.observableArrayList();
        if (!valor.equals("")) {
            String consultaModificada=principal.consultaPrincipal;

            // DEPENDIENDO DEL CAMPO SELECCIONADO TIENE UNA SINTAXIS
            switch (campo){
                case "Edad","Medalla":
                    consultaModificada += " AND Participacion." + campo + " LIKE '%" + valor + "%';";
                    break;

                case "Abreviatura":
                    consultaModificada += " AND Equipo.iniciales LIKE '%" + valor + "%';";
                    break;

                case "Deportista","Evento","Olimpiada","Deporte","Equipo":
                    consultaModificada += " AND " + campo + ".nombre LIKE '%" + valor + "%';";
                    break;
            }
            listaParticipacion=crearListaParticipaciones(consultaModificada);
        }else {
            listaParticipacion = cargarParticipacion();
        }
        return listaParticipacion;
    }

    /**
     * Añade a la BBDD la participación del parámetro.
     * @param p Participacion
     * @return true(añadido con éxito) / false(error al añadir).
     */
    public boolean aniadirParticipacion(Participacion p) {
        String consulta = "INSERT INTO Participacion VALUES ("+p.getIdDeportista()+",'"+p.getIdEvento()+"','"+p.getIdEquipo()+"',"+p.getEdad()+",'"+p.getMedalla()+"');";
        return principal.ejecutarConsulta(consulta);
    }

    /**
     * Cambia el registro por los datos pasados como parámetro.
     * @param p Participacion
     * @return true(modificado con éxito) / false(error al modificar).
     */
    public boolean modificarParticipacion(Participacion p) {
        String consulta = "UPDATE Participacion SET id_deportista="+p.getIdDeportista()+
                ",id_evento="+p.getIdEvento()+
                ",id_equipo="+p.getIdEquipo()+
                ",edad="+p.getEdad()+
                ",medalla='"+p.getMedalla()+"'"+
                " WHERE id_deportista="+p.getIdDeportista()+
                " AND id_evento="+p.getIdEvento();

        return principal.ejecutarConsulta(consulta);
    }

    /**
     * Elimina la participación.
     * @param p
     * @return
     */
    public boolean eliminarParticipacion(Participacion p) {
        String consulta = "DELETE FROM Participacion" +
                " WHERE id_deportista="+p.getIdDeportista() +
                " AND id_evento="+p.getIdEvento()+";";

        return principal.ejecutarConsulta(consulta);
    }

    /**
     * Crea una lista de participaciones con la consulta pasada como parametro.
     * @param consulta
     * @return {@link ObservableList} Participacion
     */
    private ObservableList<Participacion> crearListaParticipaciones (String consulta) {
        ObservableList<Participacion>listaParticipacion= FXCollections.observableArrayList();
        try {
            PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {

                int nIdDeportista = rs.getInt("id_deportista");
                int nIdEvento = rs.getInt("id_evento");
                int nIdEquipo = rs.getInt("id_equipo");
                String sNomDeportista = rs.getString("Deportista.nombre");
                String sNomEvento = rs.getString("Evento.nombre");
                String sNomOlimpiada = rs.getString("Olimpiada.nombre");
                String sNomDeporte = rs.getString("Deporte.nombre");
                String sNomEquipo = rs.getString("Equipo.nombre");
                String sIniciales = rs.getString("iniciales");
                Integer nEdad = rs.getInt("edad");
                String sMedalla = rs.getString("medalla");
                Participacion p = new Participacion(nIdDeportista, nIdEvento, nIdEquipo, sNomDeportista, sNomEvento, sNomOlimpiada, sNomDeporte, sNomEquipo, sIniciales, nEdad, sMedalla);
                listaParticipacion.add(p);
            }

            conexion.CloseConexion();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return listaParticipacion;
    }
}
