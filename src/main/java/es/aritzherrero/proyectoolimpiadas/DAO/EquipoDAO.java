package es.aritzherrero.proyectoolimpiadas.DAO;

import es.aritzherrero.proyectoolimpiadas.Conexion.ConexionBD;
import es.aritzherrero.proyectoolimpiadas.Modelo.Equipo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EquipoDAO {

    private ConexionBD conexion;
    private PrincipalDAO principal;

    public EquipoDAO() throws SQLException {
        conexion= new ConexionBD();
        principal= new PrincipalDAO();
    }
    /**

    /**
     * Añade a la BBDD la olimpiada del parámetro.
     * @param eq
     * @return true(añadido con éxito) / false(error al añadir).
     */
    public boolean aniadirEquipo(Equipo eq) {
        String consulta = "INSERT INTO Equipo VALUES ("+eq.getIdEquipo()+",'"+eq.getNombre()+"','"+eq.getIniciales()+"');";
        return principal.ejecutarConsulta(consulta);
    }

    /**
     * Carga todos los registros de la tabla equipo.
     * @return lista de equipos.
     */
    public ObservableList<Equipo> cargarEquipo() {
        ObservableList<Equipo> listaEquipo= FXCollections.observableArrayList();
        String consultaModificada = principal.consultaEquipo+ ";";
        listaEquipo = crearListaEquipo(consultaModificada);
        return listaEquipo;
    }

    /**
     * Filtra los datos de equipo dependiendo del campo seleccionado en el ChoiceBox y del valor del TextField.
     * @param campoSeleccionado
     * @param txFiltro
     * @return
     */
    public ObservableList<Equipo> filtrarEquipo(String campoSeleccionado, String txFiltro) {
        ObservableList<Equipo> listaEquipo = FXCollections.observableArrayList();
        String consultaModificada = principal.consultaEquipo + " WHERE "+campoSeleccionado+" LIKE '%"+txFiltro+"%';";
        listaEquipo = crearListaEquipo(consultaModificada);
        return listaEquipo;
    }

    /**
     * Cambia el equipo de la BBDD por el pasado por parámetro.
     * @param e
     * @return
     */
    public boolean modificarEquipo(Equipo e) {
        String consulta = "UPDATE Equipo SET"+
                " nombre='"+e.getNombre()+
                "',iniciales='"+e.getIniciales()+
                "' WHERE id_equipo="+e.getIdEquipo()+";";
        return principal.ejecutarConsulta(consulta);
    }

    /**
     * Crea una lista de equipos con la consulta pasada como parametro.
     * @param consulta
     * @return lista de equipos.
     */
    private ObservableList<Equipo> crearListaEquipo(String consulta) {
        ObservableList<Equipo> listaDeporte= FXCollections.observableArrayList();
        try {
            PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {

                int nIdDeporte = rs.getInt("id_equipo");
                String sNombre = rs.getString("nombre");
                String sIniciales = rs.getString("iniciales");
                Equipo e = new Equipo(nIdDeporte, sNombre, sIniciales);
                listaDeporte.add(e);
            }
            conexion.CloseConexion();
        }catch(SQLException e) {}
        return listaDeporte;
    }


}
