package es.aritzherrero.proyectoolimpiadas.Control;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import es.aritzherrero.proyectoolimpiadas.DAO.DeportistaDAO;
import es.aritzherrero.proyectoolimpiadas.DAO.PrincipalDAO;
import es.aritzherrero.proyectoolimpiadas.HelloApplication;
import es.aritzherrero.proyectoolimpiadas.Modelo.Deportista;
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
 * Controlador para la ventana de gestión de deportistas.
 * Este controlador maneja las acciones relacionadas con la tabla de deportistas, incluyendo la
 * adición, modificación, eliminación, y filtrado de deportistas.
 */
public class ControlDeportista implements Initializable {

    @FXML
    private ChoiceBox<String> cbBusqueda;

    @FXML
    private ContextMenu cmTabla;

    @FXML
    private Menu mAñadir;

    @FXML
    private MenuItem miEliminar;

    @FXML
    private MenuItem miModificar;

    @FXML
    private TableColumn<Deportista, Integer> tcAltura;

    @FXML
    private TableColumn<Deportista, ?> tcFoto;

    @FXML
    private TableColumn<Deportista, String> tcNombre;

    @FXML
    private TableColumn<Deportista, Integer> tcPeso;

    @FXML
    private TableColumn<Deportista, Character> tcSexo;

    @FXML
    private TextField tfBusqueda;

    @FXML
    private TableView<Deportista> tvTabla;

    // Variables de clase
    private PrincipalDAO pDao;
    private DeportistaDAO distaDao;
    private String[] campos = {"Nombre", "Sexo", "Peso", "Altura"};
    static Deportista gDepModificar;

    /**
     * Constructor de la clase ControlDeportista.
     * Inicializa las conexiones con los objetos de acceso a datos para gestionar deportistas.
     *
     * @throws SQLException si ocurre un error de conexión a la base de datos.
     */
    public ControlDeportista() throws SQLException {
        distaDao = new DeportistaDAO();
        pDao = new PrincipalDAO();
    }

    /**
     * Abre la ventana para añadir un deportista nuevo.
     * Este método abre una ventana secundaria que permite al usuario añadir un deportista a la base de datos.
     *
     * @param event Evento que dispara la acción (clic en el botón de añadir deportista).
     */
    @FXML
    void aniadirDeportista(ActionEvent event) {
        ventanaSecundaria("VentanaAñadirDeportista", "AÑADIR DEPORTISTA", 450, 500);
    }

    /**
     * Filtra los registros de la tabla según el campo seleccionado en el ChoiceBox y el valor ingresado en el TextField.
     * Este método realiza un filtrado dinámico de los deportistas en la tabla según el campo seleccionado y
     * el texto ingresado en el campo de búsqueda.
     *
     * @param event Evento que se dispara al escribir en el TextField de búsqueda.
     */
    @FXML
    void filtrar(KeyEvent event) {
        String campoSeleccionado = cbBusqueda.getSelectionModel().getSelectedItem();
        String txFiltro = tfBusqueda.getText().toString();
        ObservableList<Deportista> listaFiltrada = distaDao.filtrarDeportista(campoSeleccionado, txFiltro);
        cargarTabla(listaFiltrada);
    }

    /**
     * Abre la ventana para modificar un deportista seleccionado en la tabla.
     * Este método abre una ventana secundaria para modificar los datos del deportista seleccionado.
     *
     * @param event Evento que dispara la acción (clic en el botón de modificar deportista).
     */
    @FXML
    void modificar(ActionEvent event) {
        gDepModificar = tvTabla.getSelectionModel().getSelectedItem();
        ventanaSecundaria("aniadirDeportista", "MODIFICAR DEPORTISTA", 450, 500);
        ObservableList<Deportista> listaDeportistas = distaDao.cargarDeportista();
        tvTabla.setItems(listaDeportistas);
        gDepModificar = null;
    }

    /**
     * Elimina el deportista seleccionado de la tabla.
     * Este método elimina el deportista seleccionado de la base de datos, utilizando el DAO correspondiente.
     *
     * @param event Evento que dispara la acción (clic en el botón de eliminar deportista).
     */
    @FXML
    void eliminar(ActionEvent event) {
        Deportista d = tvTabla.getSelectionModel().getSelectedItem();
        boolean resp = pDao.eliminar("Deportista", "id_deportista", d.getIdDeportista());
        if (resp) {
            ControlPrincipal.ventanaAlerta("I", "Deportista eliminado con éxito.");
        } else {
            ControlPrincipal.ventanaAlerta("E", "No se pudo eliminar el deportista.");
        }
    }

    /**
     * Inicializa la ventana, cargando los datos de los deportistas desde la base de datos.
     * Este método se ejecuta al iniciar la ventana y se encarga de cargar todos los deportistas
     * en la tabla y de configurar el ChoiceBox para realizar filtros de búsqueda.
     *
     * @param arg0 URL del recurso de la ventana.
     * @param arg1 ResourceBundle con la configuración de los recursos de la ventana.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cbBusqueda.getItems().addAll(campos);
        ObservableList<Deportista> listaDeportista = distaDao.cargarDeportista();
        cargarTabla(listaDeportista);
    }

    /**
     * Carga los datos de los deportistas en la tabla, enlazando las columnas con las propiedades del modelo.
     * Este método se encarga de configurar las columnas de la tabla para mostrar los valores de los deportistas
     * y sincroniza la tabla con los datos obtenidos desde la base de datos.
     *
     * @param listaDeportista Lista de deportistas que se cargará en la tabla.
     */
    private void cargarTabla(ObservableList<Deportista> listaDeportista) {
        tcAltura.setCellValueFactory(new PropertyValueFactory<Deportista, Integer>("altura"));
        tcNombre.setCellValueFactory(new PropertyValueFactory<Deportista, String>("nombre"));
        tcPeso.setCellValueFactory(new PropertyValueFactory<Deportista, Integer>("peso"));
        tcSexo.setCellValueFactory(new PropertyValueFactory<Deportista, Character>("sexo"));

        tvTabla.setItems(listaDeportista);
    }

    /**
     * Abre una ventana secundaria con el FXML y las dimensiones especificadas.
     * Este método se encarga de crear y mostrar una ventana secundaria que puede ser usada para añadir o modificar
     * un deportista.
     *
     * @param f Fichero FXML que define la interfaz de la ventana secundaria.
     * @param t Título de la ventana.
     * @param altura Altura de la ventana.
     * @param anchura Anchura de la ventana.
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
