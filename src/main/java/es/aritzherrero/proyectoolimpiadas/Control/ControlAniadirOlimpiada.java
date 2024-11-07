package es.aritzherrero.proyectoolimpiadas.Control;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import es.aritzherrero.proyectoolimpiadas.DAO.OlimpiadaDAO;
import es.aritzherrero.proyectoolimpiadas.DAO.PrincipalDAO;
import es.aritzherrero.proyectoolimpiadas.Modelo.Olimpiada;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador para la ventana de añadir o modificar una Olimpiada.
 * Este controlador permite gestionar las Olimpiadas, ya sea añadiendo nuevas Olimpiadas
 * o modificando las existentes en la base de datos.
 */
public class ControlAniadirOlimpiada implements Initializable {

    @FXML
    private TextField tfAnio;

    @FXML
    private TextField tfCiudad;

    @FXML
    private ChoiceBox<String> cbTemporada;

    private PrincipalDAO pDao;
    private OlimpiadaDAO oDao;
    private String[] campos = {"Summer", "Winter"};
    private Olimpiada o;
    private boolean modificar;

    /**
     * Constructor de la clase ControlAniadirOlimpiada.
     * Este constructor inicializa las instancias de los objetos necesarios
     * para interactuar con la base de datos.
     *
     * @throws SQLException si ocurre un error en la conexión a la base de datos.
     */
    public ControlAniadirOlimpiada() throws SQLException {
        oDao = new OlimpiadaDAO();
        pDao = new PrincipalDAO();
    }

    /**
     * Procedimiento que se ejecuta al pulsar el botón "Aceptar".
     * Este procedimiento genera una nueva Olimpiada o modifica una existente,
     * según el valor de la variable modificar. Si la operación es exitosa,
     * se muestra un mensaje de éxito; de lo contrario, se muestra un mensaje de error.
     *
     * @param event Evento de acción generado al pulsar el botón "Aceptar".
     */
    @FXML
    void Aceptar(ActionEvent event) {
        // Obtiene los datos introducidos por el usuario
        int nId = pDao.generarId("Olimpiada");
        Integer nAnio = Integer.parseInt(tfAnio.getText());
        String sTemporada = cbTemporada.getSelectionModel().getSelectedItem();
        String sCiudad = tfCiudad.getText();
        String sNombre = nAnio + " " + sTemporada;

        if (modificar) {
            // Si se está modificando una Olimpiada existente
            Olimpiada oMod = new Olimpiada(o.getIdOlimpiada(), sNombre, nAnio, sTemporada, sCiudad);
            boolean resultado = oDao.modificarOlimpiada(oMod);
            if (resultado) {
                ControlPrincipal.ventanaAlerta("I", "Olimpiada modificada con éxito");
                Cancelar(event);
            } else {
                ControlPrincipal.ventanaAlerta("E", "Error al modificar Olimpiada");
            }
        } else {
            // Si se está añadiendo una nueva Olimpiada
            Olimpiada oMod = new Olimpiada(nId, sNombre, nAnio, sTemporada, sCiudad);
            boolean resultado = oDao.aniadirOlimpiada(oMod);
            if (resultado) {
                ControlPrincipal.ventanaAlerta("I", "Olimpiada añadida con éxito");
                Cancelar(event);
            } else {
                ControlPrincipal.ventanaAlerta("E", "Error al añadir Olimpiada");
            }
        }
    }

    /**
     * Procedimiento que se ejecuta al pulsar el botón "Cancelar".
     * Este procedimiento cierra la ventana actual sin realizar ninguna acción.
     *
     * @param event Evento de acción generado al pulsar el botón "Cancelar".
     */
    @FXML
    void Cancelar(ActionEvent event) {
        // Cierra la ventana actual
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    /**
     * Procedimiento de inicialización de la ventana.
     * Este procedimiento carga las opciones de temporada ("Summer" y "Winter")
     * en el ChoiceBox correspondiente y verifica si se está añadiendo o modificando
     * una Olimpiada. En el caso de modificación, carga los datos de la Olimpiada
     * en los campos correspondientes.
     *
     * @param arg0 URL para la localización de recursos.
     * @param arg1 Bundle de recursos para la inicialización.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Añade las opciones de temporada al ChoiceBox
        cbTemporada.getItems().addAll(campos);

        // Verifica si se está modificando una Olimpiada existente
        try {
            o = ControlOlimpiada.gOliModificar;
            mostrarDatosModificar();
            modificar = true;
        } catch (Exception e) {
            // Si no se está modificando, no realiza ninguna acción
        }
    }

    /**
     * Procedimiento que muestra los datos de la Olimpiada a modificar en los campos correspondientes.
     * Este procedimiento establece los valores del año, ciudad y temporada de la
     * Olimpiada seleccionada para permitir su modificación.
     */
    private void mostrarDatosModificar() {
        // Rellena los campos con los datos de la Olimpiada seleccionada
        tfAnio.setText(o.getAnio().toString());
        tfCiudad.setText(o.getCiudad());
        if (o.getTemporada().equals("Summer")) {
            cbTemporada.getSelectionModel().select(campos[0]);
        } else {
            cbTemporada.getSelectionModel().select(campos[1]);
        }
    }
}
