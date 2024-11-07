package es.aritzherrero.proyectoolimpiadas.DAO;

import es.aritzherrero.proyectoolimpiadas.Conexion.ConexionBD;
import es.aritzherrero.proyectoolimpiadas.Modelo.Deportista;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeportistaDAO {
    private ConexionBD conexion;
    private PrincipalDAO principal;
    /**
     * Carga todos los registros de la tabla Deportista.
     * @return {@link ObservableList} Deportista
     */
    public ObservableList<Deportista> cargarDeportista(){
        ObservableList<Deportista> listaDeportista = FXCollections.observableArrayList();
        String consultaModificada = principal.consultaPrincipal+ ";";
        listaDeportista = crearListaDeportista(consultaModificada);
        return listaDeportista;
    }

    /**
     * Método que añade una condición de busqueda SQL(WHERE) a la consulta genérica de Deportista y llama a CrearListaDeportista()
     * para ejecutarla.
     * @param campoSeleccionado columna que se ejecutara el filtro.
     * @param txFiltro el valor que se quiere buscar.
     * @return lista de deportistas.
     */
    public ObservableList<Deportista> filtrarDeportista(String campoSeleccionado, String txFiltro) {
        ObservableList<Deportista> listaDeportista = FXCollections.observableArrayList();
        String consultaModificada = principal.consultaDeportista + " WHERE "+campoSeleccionado+" LIKE '%"+txFiltro+"%';";
        listaDeportista = crearListaDeportista(consultaModificada);
        return listaDeportista;
    }

    /**
     * Añade a la BBDD el deportista del parámetro.
     * @param d deportista
     * @return true(añadido con éxito) / false(error al añadir).
     */
    public boolean aniadirDeportista(Deportista d) {
        String consulta = "INSERT INTO Deportista VALUES ("+d.getIdDeportista()+",'"+d.getNombre()+"','"+d.getSexo()+"',"+d.getPeso()+","+d.getAltura()+");";
        return principal.ejecutarConsulta(consulta);
    }

    /**
     * Cambia el Deportista de la BBDD por el pasado por parámetro.
     * @param d
     * @return true(modificado con éxito) / false(error al modificar).
     */
    public boolean modificarDeportista(Deportista d) {
        String consulta = "UPDATE Deportista SET nombre='"+d.getNombre()+
                "',sexo='"+d.getSexo()+"'"+
                ",peso="+d.getPeso()+
                ",altura="+d.getAltura()+
                " WHERE id_deportista="+d.getIdDeportista()+";";
        return principal.ejecutarConsulta(consulta);
    }

    /**
     * Crea una lista de deportistas con la consulta pasada como parametro.
     * @param consulta
     * @return {@link ObservableList} Deportista
     */
    private ObservableList<Deportista> crearListaDeportista(String consulta) {
        ObservableList<Deportista> listaDeportista = FXCollections.observableArrayList();
        try {
            conexion = new ConexionBD();
            PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {

                int nId = rs.getInt("id_deportista");
                String sNombre = rs.getString("nombre");
                Character cSexo = rs.getString("sexo").toCharArray()[0];
                Integer nPeso = rs.getInt("peso");
                Integer nAltura = rs.getInt("altura");
                Deportista d = new Deportista(nId, sNombre, cSexo, nPeso, nAltura);
                listaDeportista.add(d);
            }
            conexion.CloseConexion();
        }catch(SQLException e) {}
        return listaDeportista;
    }
}
