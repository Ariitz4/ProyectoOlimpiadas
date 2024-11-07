package es.aritzherrero.proyectoolimpiadas.Control;

import java.net.URL;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ResourceBundle;

import es.aritzherrero.proyectoolimpiadas.DAO.DeporteDAO;
import es.aritzherrero.proyectoolimpiadas.DAO.EventoDAO;
import es.aritzherrero.proyectoolimpiadas.DAO.OlimpiadaDAO;
import es.aritzherrero.proyectoolimpiadas.DAO.PrincipalDAO;
import es.aritzherrero.proyectoolimpiadas.Modelo.Deporte;
import es.aritzherrero.proyectoolimpiadas.Modelo.Evento;
import es.aritzherrero.proyectoolimpiadas.Modelo.Olimpiada;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador para la ventana de añadir o modificar un evento.
 * Este controlador permite gestionar los eventos dentro de las Olimpiadas,
 * añadiendo nuevos eventos o modificando los existentes en la base de datos.
 */
public class ControlAniadirEvento implements Initializable {

    @FXML
    private ChoiceBox<Deporte> cbDeporte;

    @FXML
    private ChoiceBox<Olimpiada> cbOlimpiada;

    @FXML
    private TextField tfNombre;

    private PrincipalDAO pDao;
    private OlimpiadaDAO oDao;
    private EventoDAO eDao;
    private DeporteDAO dDao;
    private Evento eve;
    private boolean modificar;

    /**
     * Constructor de la clase ControlAniadirEvento.
     * Inicializa las instancias de los objetos necesarios para interactuar
     * con la base de datos.
     *
     * @throws SQLException si ocurre un error en la conexión a la base de datos.
     */
    public ControlAniadirEvento() throws SQLException {
        dDao = new DeporteDAO();
        eDao = new EventoDAO();
        oDao = new OlimpiadaDAO();
        pDao = new PrincipalDAO();
    }

    /**
     * Procedimiento que se ejecuta al pulsar el botón "Aceptar".
     * Este procedimiento genera un nuevo evento o modifica uno existente,
     * según el valor de la variable modificar. Si la operación es exitosa,
     * se muestra un mensaje de éxito; de lo contrario, se muestra un mensaje de error.
     *
     * @param event Evento de acción generado al pulsar el botón "Aceptar".
     */
    @FXML
    void Aceptar(ActionEvent event) {
        try {
            // Obtiene los datos introducidos por el usuario
            String sNombre = tfNombre.getText();
            Olimpiada o = cbOlimpiada.getSelectionModel().getSelectedItem();
            Deporte d = cbDeporte.getSelectionModel().getSelectedItem();

            if (modificar) {
                // Si se está modificando un evento existente
                Evento e = new Evento(eve.getIdEvento(), o.getIdOlimpiada(), d.getIdDeporte(), sNombre, o.getNombre(), d.getNombre());
                boolean resultado = eDao.modificarEvento(e);
                if (resultado) {
                    ControlPrincipal.ventanaAlerta("I", "Evento modificado con éxito");
                    Cancelar(event);
                } else {
                    ControlPrincipal.ventanaAlerta("E", "Error al modificar Evento");
                }
            } else {
                // Si se está añadiendo un nuevo evento
                int nId = pDao.generarId("Evento");
                Evento e = new Evento(nId, o.getIdOlimpiada(), d.getIdDeporte(), sNombre, o.getNombre(), d.getNombre());
                boolean resultado = eDao.aniadirEvento(e);
                if (resultado) {
                    ControlPrincipal.ventanaAlerta("I", "Evento añadido con éxito");
                    Cancelar(event);
                } else {
                    ControlPrincipal.ventanaAlerta("E", "Error al añadir Evento");
                }
            }
        } catch (Exception e) {
            // Manejo de excepciones (se pueden agregar más detalles si es necesario)
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
     * Al iniciar la ventana, este procedimiento carga las Olimpiadas y los Deportes
     * en los ChoiceBoxes correspondientes y verifica si se está añadiendo o modificando un evento.
     * En el caso de modificación, carga los datos del evento en los campos correspondientes.
     *
     * @param arg0 URL para la localización de recursos.
     * @param arg1 Bundle de recursos para la inicialización.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        modificar = false;

        // Carga las olimpiadas en el ChoiceBox
        ObservableList<Olimpiada> listaOlimpiada = oDao.cargarOlimpiada();
        Iterator<Olimpiada> itO = listaOlimpiada.iterator();
        while (itO.hasNext()) {
            Olimpiada o = itO.next();
            cbOlimpiada.getItems().add(o);
        }

        // Carga los deportes en el ChoiceBox
        ObservableList<Deporte> listaDeporte = dDao.cargarDeporte();
        Iterator<Deporte> itD = listaDeporte.iterator();
        while (itD.hasNext()) {
            Deporte d = itD.next();
            cbDeporte.getItems().add(d);
        }

        // Verifica si se está modificando un evento existente
        try {
            eve = ControlEvento.gEveModificar;
            mostrarDatosModificar();
            modificar = true;
        } catch (Exception e) {
            // No realiza ninguna acción si no hay evento a modificar
        }
    }

    /**
     * Procedimiento que muestra los datos del evento a modificar en los campos correspondientes.
     * Este procedimiento establece los valores del nombre, deporte y olimpiada
     * del evento seleccionado para permitir su modificación.
     */
    private void mostrarDatosModificar() {
        // Filtra los deportes y olimpiadas relacionados con el evento para mostrarlos
        ObservableList<Deporte> listaDeporte = dDao.filtrarDeporte("id_deporte", eve.getIdDeporte().toString());
        ObservableList<Olimpiada> listaOlimpiada = oDao.filtrarOlimpiada("id_olimpiada", eve.getIdOlimpiada().toString());

        Deporte d = listaDeporte.get(0);
        Olimpiada o = listaOlimpiada.get(0);

        // Rellena los campos con los datos del evento
        tfNombre.setText(eve.getNomEvento());
        cbDeporte.getSelectionModel().select(d);
        cbOlimpiada.getSelectionModel().select(o);
    }
}
