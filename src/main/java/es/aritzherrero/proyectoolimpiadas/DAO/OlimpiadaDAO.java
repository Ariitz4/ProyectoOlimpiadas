package es.aritzherrero.proyectoolimpiadas.DAO;

import es.aritzherrero.proyectoolimpiadas.Conexion.ConexionBD;
import es.aritzherrero.proyectoolimpiadas.Modelo.Olimpiada;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OlimpiadaDAO {
    private ConexionBD conexion;
    private PrincipalDAO principal;


    public OlimpiadaDAO() throws SQLException {
        conexion= new ConexionBD();
        principal= new PrincipalDAO();
    }
    /**
     * Carga todos los registros de la tabla Olimpiada.
     * @return lista de olimpiadas.
     */
    public ObservableList<Olimpiada> cargarOlimpiada(){
        ObservableList<Olimpiada> listaOlimpiada= FXCollections.observableArrayList();
        String consultaModificada = principal.consultaOlimpiada + ";";
        listaOlimpiada= crearListaOlimpiada(consultaModificada);
        return listaOlimpiada;
    }

    /**
     * Filtra los datos de olimpiada dependiendo del campo seleccionado en el ChoiceBox y del valor del TextField.
     * @param campoSeleccionado
     * @param txFiltro valor del campo.
     * @return lista de olimpiadas.
     */
    public ObservableList<Olimpiada> filtrarOlimpiada(String campoSeleccionado, String txFiltro) {
        ObservableList<Olimpiada> listaOlimpiada = FXCollections.observableArrayList();
        if (campoSeleccionado.equals("Año")) {campoSeleccionado = "anio";}
        String consultaModificada = principal.consultaOlimpiada + " WHERE "+campoSeleccionado+" LIKE '%"+txFiltro+"%';";
        listaOlimpiada = crearListaOlimpiada(consultaModificada);
        return listaOlimpiada;
    }

    /**
     * Añade a la BBDD la olimpiada del parámetro.
     * @param o
     * @return true(añadido con éxito) / false(error al añadir).
     */
    public boolean aniadirOlimpiada(Olimpiada o) {
        String consulta = "INSERT INTO Olimpiada VALUES ("+o.getIdOlimpiada()+",'"+o.getNombre()+"',"+o.getAnio()+",'"+o.getTemporada()+"','"+o.getCiudad()+"');";
        return principal.ejecutarConsulta(consulta);
    }

    /**
     * Cambia la Olimpiada de la BBDD por el pasado por parámetro.
     * @param o
     * @return true(modificado con éxito) / false(error al modificar).
     */
    public boolean modificarOlimpiada(Olimpiada o) {
        String consulta = "UPDATE Olimpiada SET"+
                " nombre='"+o.getNombre()+
                "',anio="+o.getAnio()+
                ",temporada='"+o.getTemporada()+
                "',ciudad='"+o.getCiudad()+
                "' WHERE id_olimpiada="+o.getIdOlimpiada()+";";
        return principal.ejecutarConsulta(consulta);
    }

    /**
     * Crea una lista de Olimpiadas con la consulta pasada como parametro.
     * @param consulta
     * @return lista de olimpiadas.
     */
    private ObservableList<Olimpiada> crearListaOlimpiada(String consulta) {
        ObservableList<Olimpiada> listaOlimpiada= FXCollections.observableArrayList();
        try {
            PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {

                int nIdOlimpiada = rs.getInt("id_olimpiada");
                String sNombre = rs.getString("nombre");
                Integer nAnio = rs.getInt("anio");
                String sTemporada = rs.getString("temporada");
                String sCiudad = rs.getString("ciudad");
                Olimpiada o = new Olimpiada(nIdOlimpiada, sNombre, nAnio, sTemporada, sCiudad);
                listaOlimpiada.add(o);
            }
            conexion.CloseConexion();
        }catch(SQLException e) {e.printStackTrace();}
        return listaOlimpiada;
    }

}
