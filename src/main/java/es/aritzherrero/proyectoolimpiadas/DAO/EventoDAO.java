package es.aritzherrero.proyectoolimpiadas.DAO;

import es.aritzherrero.proyectoolimpiadas.Conexion.ConexionBD;
import es.aritzherrero.proyectoolimpiadas.Modelo.Evento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventoDAO {
    private ConexionBD conexion;
    private PrincipalDAO principal;

    public EventoDAO() throws SQLException {
        conexion= new ConexionBD();
        principal= new PrincipalDAO();
    }
    /**
     * Carga todos los registros de la tabla Evento.
     * @return lista de eventos.
     */
    public ObservableList<Evento> cargarEvento() {
        ObservableList<Evento> listaEvento= FXCollections.observableArrayList();
        String consultaModificada =principal.consultaEvento + ";";
        listaEvento = crearListaEvento(consultaModificada);
        return listaEvento;

    }
    /**
     * Filtra los datos de evento dependiendo del campo seleccionado en el ChoiceBox y del valor del TextField.
     * @param campoSeleccionado
     * @param txFiltro
     * @return
     */
    public ObservableList<Evento> filtrarEvento(String campoSeleccionado, String txFiltro) {
        ObservableList<Evento> listaEvento = FXCollections.observableArrayList();
        String consultaModificada = principal.consultaDeporte + " WHERE "+campoSeleccionado+" LIKE '%"+txFiltro+"%';";
        listaEvento = crearListaEvento(consultaModificada);
        return listaEvento;
    }

    /**
     * Añade a la BBDD el evento del parámetro.
     * @param ev Evento.
     * @return true(añadido con éxito) / false(error al añadir).
     */
    public boolean aniadirEvento(Evento ev) {
        String consulta = "INSERT INTO Evento VALUES ("+ev.getIdEvento()+",'"+ev.getNomEvento()+"','"+ev.getIdOlimpiada()+"',"+ev.getIdDeporte()+");";
        return principal.ejecutarConsulta(consulta);
    }

    /**
     * Cambia el Evento de la BBDD por el pasado por parámetro.
     * @param e
     * @return true(modificado con éxito) / false(error al modificar).
     */
    public boolean modificarEvento(Evento e) {
        String consulta = "UPDATE Evento SET"+
                " nombre='"+e.getNomEvento()+
                "',id_olimpiada="+e.getIdOlimpiada()+
                ",id_deporte="+e.getIdDeporte()+
                " WHERE id_evento="+e.getIdEvento()+";";
        return principal.ejecutarConsulta(consulta);
    }

    /**
     *Crea una lista de eventos con la consulta pasada como parametro.
     * @param consulta
     * @return lista de eventos.
     */
    private ObservableList<Evento> crearListaEvento(String consulta) {
        ObservableList<Evento> listaEvento= FXCollections.observableArrayList();
        try {
            conexion = new ConexionBD();
            PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {

                int nIdEvento = rs.getInt("id_evento");
                Integer nIdOlimpiada = rs.getInt("id_olimpiada");
                Integer nIdDeporte= rs.getInt("id_deporte");
                String sNomEvento = rs.getString("nomevento");
                String sNomOlimpiada = rs.getString("nomolimpiada");
                String sNomDeporte = rs.getString("nomdeporte");
                Evento e = new Evento(nIdEvento, nIdOlimpiada, nIdDeporte, sNomEvento, sNomOlimpiada, sNomDeporte);
                listaEvento.add(e);
            }
            conexion.CloseConexion();
        }catch(SQLException e) {e.printStackTrace();}
        return listaEvento;
    }

}
