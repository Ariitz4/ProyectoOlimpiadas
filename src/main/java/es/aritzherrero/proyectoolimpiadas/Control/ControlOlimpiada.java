package es.aritzherrero.proyectoolimpiadas.Control;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import es.aritzherrero.proyectoolimpiadas.DAO.OlimpiadaDAO;
import es.aritzherrero.proyectoolimpiadas.DAO.PrincipalDAO;
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


public class ControlOlimpiada implements Initializable{

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

    private PrincipalDAO pDao = new PrincipalDAO();
    private OlimpiadaDAO oDao = new OlimpiadaDAO();
    private String[] campos = {"Nombre","Año","Temporada","Ciudad"};
    static Olimpiada gOliModificar;

    /**
     * Abre la ventana para añadir una olimpiada.
     * @param event
     */
    @FXML
    void aniadirOlimpiada(ActionEvent event) {
        ventanaSecundaria("VentanaAñadirOlimpiada", "AÑADIR OLIMPIADA", 380, 460);
    }

    /**
     * Filtra los registros dependiendo del campo del ChoiceBox y el texto del TextField.
     * @param event
     */
    @FXML
    void filtrar(KeyEvent event) {
        String campoSeleccionado = cbBusqueda.getSelectionModel().getSelectedItem();
        String txFiltro = tfBusqueda.getText().toString();
        ObservableList<Olimpiada>listaFiltrada = oDao.filtrarOlimpiada(campoSeleccionado, txFiltro);
        cargarTabla(listaFiltrada);
    }


    @FXML
    void modificar(ActionEvent event) {
        gOliModificar = tvTabla.getSelectionModel().getSelectedItem();
        ventanaSecundaria("aniadirOlimpiada", "MODIFICAR OLIMPIADA", 380, 460);
        ObservableList<Olimpiada>listaOlimpiadas= oDao.cargarOlimpiada();
        tvTabla.setItems(listaOlimpiadas);
        gOliModificar=null;
    }

    @FXML
    void eliminar(ActionEvent event) {
        Olimpiada o = tvTabla.getSelectionModel().getSelectedItem();

        boolean resultado = pDao.eliminar("Olimpiada", "id_olimpiada", o.getIdOlimpiada());
        if (resultado) {
            ControlPrincipal.ventanaAlerta("I", "olimpiada eliminada con éxito");
        }else {
            ControlPrincipal.ventanaAlerta("E", "no se pudo eliminar la olimpiada");
        }

    }

    /**
     * Carga los campos del ChoiceBox y sincroniza la tabla con la BBDD.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cbBusqueda.getItems().addAll(campos);
        ObservableList<Olimpiada>olimpiadas = oDao.cargarOlimpiada();
        cargarTabla(olimpiadas);
    }

    /**
     * Sincroniza los campos de la tabla con los de la BBDD y añade olimpiadas a la tabla.
     * @param olimpiadas
     */
    private void cargarTabla(ObservableList<Olimpiada> olimpiadas) {
        tcNombre.setCellValueFactory(new PropertyValueFactory<Olimpiada, String>("nombre"));
        tcAnio.setCellValueFactory(new PropertyValueFactory<Olimpiada, Integer>("anio"));
        tcTemporada.setCellValueFactory(new PropertyValueFactory<Olimpiada, String>("temporada"));
        tcCiudad.setCellValueFactory(new PropertyValueFactory<Olimpiada, String>("ciudad"));

        tvTabla.setItems(olimpiadas);
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