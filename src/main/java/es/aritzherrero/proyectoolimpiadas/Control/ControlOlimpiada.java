package es.aritzherrero.proyectoolimpiadas.Control;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import es.aritzherrero.proyectoolimpiadas.DAO.OlimpiadaDAO;
import es.aritzherrero.proyectoolimpiadas.DAO.PrincipalDAO;
import es.aritzherrero.proyectoolimpiadas.HelloApplication;
import es.aritzherrero.proyectoolimpiadas.Modelo.Olimpiada;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controlador para gestionar las operaciones relacionadas con las olimpiadas.
 * Este procedimiento permite añadir, modificar, eliminar y filtrar las olimpiadas en la interfaz de usuario.
 * Además, se encarga de la interacción con la base de datos a través del objeto OlimpiadaDAO.
 * La clase maneja las acciones del usuario en la tabla de olimpiadas y en los formularios de la aplicación.
 *

 */
public class ControlOlimpiada implements Initializable {

    @FXML
    private ChoiceBox<String> cbBusqueda;

    @FXML
    private ContextMenu cmTabla;

    @FXML
    private Menu mAyuda;

    @FXML
    private Menu mAñadir;

    @FXML
    private MenuItem miEliminar;

    @FXML
    private MenuItem miModificar;

    @FXML
    private TableColumn<Olimpiada, Integer> tcAnio;

    @FXML
    private TableColumn<Olimpiada, String> tcCiudad;

    @FXML
    private TableColumn<Olimpiada, String> tcNombre;

    @FXML
    private TableColumn<Olimpiada, String> tcTemporada;

    @FXML
    private TextField tfBusqueda;

    @FXML
    private TableView<Olimpiada> tvTabla;

    private PrincipalDAO pDao;
    private OlimpiadaDAO oDao;
    private String[] campos = {"Nombre", "Año", "Temporada", "Ciudad"};
    static Olimpiada gOliModificar;

    public ControlOlimpiada() throws SQLException {
        oDao = new OlimpiadaDAO();
        pDao = new PrincipalDAO();
    }

    /**
     * Abre la ventana para añadir una olimpiada.
     * @param event El evento que activa la acción de añadir una olimpiada.
     */
    @FXML
    void aniadirOlimpiada(ActionEvent event) {
        ventanaSecundaria("VentanaAñadirOlimpiada", "AÑADIR OLIMPIADA", 380, 460);
    }

    /**
     * Filtra los registros dependiendo del campo del ChoiceBox y el texto del TextField.
     * @param event El evento de tecleo que activa la acción de filtrar la tabla.
     */
    @FXML
    void filtrar(KeyEvent event) {
        String campoSeleccionado = cbBusqueda.getSelectionModel().getSelectedItem();
        String txFiltro = tfBusqueda.getText().toString();
        ObservableList<Olimpiada> listaFiltrada = oDao.filtrarOlimpiada(campoSeleccionado, txFiltro);
        cargarTabla(listaFiltrada);
    }

    /**
     * Abre la ventana para modificar una olimpiada seleccionada en la tabla.
     * @param event El evento que activa la acción de modificar una olimpiada.
     */
    @FXML
    void modificar(ActionEvent event) {
        gOliModificar = tvTabla.getSelectionModel().getSelectedItem();
        ventanaSecundaria("aniadirOlimpiada", "MODIFICAR OLIMPIADA", 380, 460);
        ObservableList<Olimpiada> listaOlimpiadas = oDao.cargarOlimpiada();
        tvTabla.setItems(listaOlimpiadas);
        gOliModificar = null;
    }

    /**
     * Elimina la olimpiada seleccionada de la base de datos.
     * @param event El evento que activa la acción de eliminar una olimpiada.
     */
    @FXML
    void eliminar(ActionEvent event) {
        Olimpiada o = tvTabla.getSelectionModel().getSelectedItem();

        boolean resultado = pDao.eliminar("Olimpiada", "id_olimpiada", o.getIdOlimpiada());
        if (resultado) {
            ControlPrincipal.ventanaAlerta("I", "Olimpiada eliminada con éxito");
        } else {
            ControlPrincipal.ventanaAlerta("E", "No se pudo eliminar la olimpiada");
        }
    }

    /**
     * Inicializa la vista, carga los campos del ChoiceBox y sincroniza la tabla con la base de datos.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cbBusqueda.getItems().addAll(campos);
        ObservableList<Olimpiada> olimpiadas = oDao.cargarOlimpiada();
        cargarTabla(olimpiadas);
    }

    /**
     * Sincroniza los campos de la tabla con los de la base de datos y añade olimpiadas a la tabla.
     * @param olimpiadas Lista de olimpiadas a mostrar en la tabla.
     */
    private void cargarTabla(ObservableList<Olimpiada> olimpiadas) {
        tcNombre.setCellValueFactory(new PropertyValueFactory<Olimpiada, String>("nombre"));
        tcAnio.setCellValueFactory(new PropertyValueFactory<Olimpiada, Integer>("anio"));
        tcTemporada.setCellValueFactory(new PropertyValueFactory<Olimpiada, String>("temporada"));
        tcCiudad.setCellValueFactory(new PropertyValueFactory<Olimpiada, String>("ciudad"));

        tvTabla.setItems(olimpiadas);
    }

    /**
     * Abre una ventana secundaria (como un formulario o una ventana de operación adicional).
     * @param f Nombre del archivo FXML de la ventana.
     * @param t Título de la ventana.
     * @param altura Altura de la ventana en píxeles.
     * @param anchura Anchura de la ventana en píxeles.
     */
    private void ventanaSecundaria(String f, String t, Integer altura, Integer anchura) {
        Stage stage = new Stage();
        try {
            FlowPane flwPanel = FXMLLoader.load(HelloApplication.class.getResource("fxml/" + f + ".fxml"));
            stage.setTitle(t);
            Scene scene = new Scene(flwPanel, altura, anchura);
            stage.setScene(scene);
            stage.setMinWidth(altura);
            stage.setMinHeight(anchura);
            stage.setMaxWidth(altura);
            stage.setMaxHeight(anchura);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
