package es.aritzherrero.proyectoolimpiadas.Control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import es.aritzherrero.proyectoolimpiadas.DAO.DeportistaDAO;
import es.aritzherrero.proyectoolimpiadas.DAO.PrincipalDAO;
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

public class ControlDeportista implements Initializable{

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

    // VARIABLES DE CLASE INSERTADAS MANUALMENTE \\
    private PrincipalDAO pDao = new PrincipalDAO();
    private DeportistaDAO distaDao= new DeportistaDAO();
    private String[]campos = {"Nombre","Sexo","Peso","Altura"};
    static Deportista gDepModificar;

    /**
     * Abre la ventana para añadir Deportistas
     * @param event
     */
    @FXML
    void aniadirDeportista(ActionEvent event) {
        ventanaSecundaria("VentanaAñadirDeportista", "AÑADIR DEPORTISTA", 450, 500);
    }

    /**
     * Método que filtra los registros de la tabla Deportista dependiendo del campo seleccionado
     * en el ChoiceBox y el valor insertado en el TextField.
     * @param event
     */
    @FXML
    void filtrar(KeyEvent event) {
        String campoSeleccionado = cbBusqueda.getSelectionModel().getSelectedItem();
        String txFiltro = tfBusqueda.getText().toString();
        ObservableList<Deportista>listaFiltrada = distaDao.filtrarDeportista(campoSeleccionado, txFiltro);
        cargarTabla(listaFiltrada);
    }

    /**
     * Modifica el registro seleccionado en la tabla.
     * @param event
     */
    @FXML
    void modificar(ActionEvent event) {
        gDepModificar = tvTabla.getSelectionModel().getSelectedItem();
        ventanaSecundaria("aniadirDeportista", "MODIFICAR DEPORTISTA", 450, 500);
        ObservableList<Deportista>listaDeportistas= distaDao.cargarDeportista();
        tvTabla.setItems(listaDeportistas);
        gDepModificar=null;
    }

    @FXML
    void eliminar(ActionEvent event) {
        Deportista d = tvTabla.getSelectionModel().getSelectedItem();
        boolean resp = pDao.eliminar("Deportista","id_deportista",d.getIdDeportista());
        if(resp) {
            ControlPrincipal.ventanaAlerta("I", "Deportista eliminado con éxito.");
        }else {
            ControlPrincipal.ventanaAlerta("E", "No se pudo eliminar el deportista.");
        }

    }

    /**
     * Al iniciarse la ventana, se sincroniza con la base de datos para mostrar todos los registros de la tabla Deportista.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        cbBusqueda.getItems().addAll(campos);
        ObservableList<Deportista> listaDeportista = distaDao.cargarDeportista();
        cargarTabla(listaDeportista);
    }

    /**
     * Enlaza las columnas con los valores de la BBDD que va a mostrar y cambia los registros de la tabla para sincronizar.
     * @param listaDeportista
     */
    private void cargarTabla(ObservableList<Deportista> listaDeportista) {
        tcAltura.setCellValueFactory(new PropertyValueFactory<Deportista, Integer>("altura"));
        tcNombre.setCellValueFactory(new PropertyValueFactory<Deportista, String>("nombre"));
        tcPeso.setCellValueFactory(new PropertyValueFactory<Deportista, Integer>("peso"));
        tcSexo.setCellValueFactory(new PropertyValueFactory<Deportista, Character>("sexo"));

        tvTabla.setItems(listaDeportista);
    }

    /**
     * Abrir ventana auxiliar.
     * @param f fxml
     * @param t titulo
     * @param altura
     * @param anchura
     */
    private void ventanaSecundaria(String f, String t,Integer altura,Integer anchura) {
        Stage stage = new Stage();
        try {
            FlowPane root = (FlowPane)FXMLLoader.load(getClass().getResource("/fxml/"+f+".fxml"));
            stage.setTitle(t);
            Scene scene = new Scene(root,altura,anchura);
            stage.setScene(scene);
            stage.setMinWidth(altura);
            stage.setMinHeight(anchura);
            stage.setMaxWidth(altura);
            stage.setMaxHeight(anchura);
            stage.getIcons().add(new Image(getClass().getResource("/img/imgOlimpiadas.jpg").toString()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}