package es.aritzherrero.proyectoolimpiadas.DAO;

import es.aritzherrero.proyectoolimpiadas.Conexion.ConexionBD;
import es.aritzherrero.proyectoolimpiadas.Modelo.Deporte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeporteDAO {

    private ConexionBD conexion;
    private PrincipalDAO principal;

    public DeporteDAO() throws SQLException {
        conexion= new ConexionBD();
        principal= new PrincipalDAO();
    }
    /**

    /**
     * Carga todos los registros de la tabla deporte.
     * @return lista de deportes.
     */
    public ObservableList<Deporte> cargarDeporte() {
        ObservableList<Deporte> listaDeporte= FXCollections.observableArrayList();
        String consultaModificada = principal.consultaDeporte + ";";
        listaDeporte= crearListaDeporte(consultaModificada);
        return listaDeporte;

    }

    /**
     * Añade a la BBDD el deporte del parámetro.
     * @param d Deporte
     * @return true(añadido con éxito) / false(error al añadir).
     */
    public boolean aniadirDeporte(Deporte d) {
        String consulta = "INSERT INTO Deporte VALUES ("+d.getIdDeporte()+",'"+d.getNombre()+"');";
        return principal.ejecutarConsulta(consulta);
    }

    /**
     * Filtra los datos de deporte dependiendo del campo seleccionado en el ChoiceBox y del valor del TextField.
     * @param campoSeleccionado
     * @param txFiltro
     * @return
     */
    public ObservableList<Deporte> filtrarDeporte(String campoSeleccionado, String txFiltro) {
        ObservableList<Deporte> listaDeporte = FXCollections.observableArrayList();
        String consultaModificada = principal.consultaDeporte+ " WHERE "+campoSeleccionado+" LIKE '%"+txFiltro+"%';";
        listaDeporte = crearListaDeporte(consultaModificada);
        return listaDeporte;
    }

    /**
     * Cambia el Deporte de la BBDD por el pasado por parámetro.
     * @param d
     * @return
     */
    public boolean modificarDeporte(Deporte d) {
        String consulta = "UPDATE Deporte SET"+
                " nombre='"+d.getNombre()+
                "' WHERE id_deporte="+d.getIdDeporte()+";";
        return principal.ejecutarConsulta(consulta);
    }

    /**
     * Crea una lista de deportes con la consulta pasada como parametro.
     * @param consulta
     * @return lista de deportes.
     */
    private ObservableList<Deporte> crearListaDeporte(String consulta) {
        ObservableList<Deporte> listaDeporte= FXCollections.observableArrayList();
        try {
            PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {

                int nIdDeporte = rs.getInt("id_deporte");
                String sNombre = rs.getString("nombre");
                Deporte d = new Deporte(nIdDeporte, sNombre);
                listaDeporte.add(d);
            }
            conexion.CloseConexion();
        }catch(SQLException e) {}
        return listaDeporte;
    }
}
