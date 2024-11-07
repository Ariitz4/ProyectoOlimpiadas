package es.aritzherrero.proyectoolimpiadas.Control;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import es.aritzherrero.proyectoolimpiadas.DAO.*;
import es.aritzherrero.proyectoolimpiadas.Modelo.Deportista;
import es.aritzherrero.proyectoolimpiadas.Modelo.Equipo;
import es.aritzherrero.proyectoolimpiadas.Modelo.Evento;
import es.aritzherrero.proyectoolimpiadas.Modelo.Participacion;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador para la ventana de añadir o modificar una participación en un evento deportivo.
 * Este controlador gestiona los eventos de la ventana para permitir la adición o modificación
 * de una participación en la base de datos. Incluye la selección de un deportista, un equipo,
 * un evento, y una medalla.
 */
public class ControlAniadirParticipacion implements Initializable {

    @FXML
    private ChoiceBox<Deportista> cbDeportista;

    @FXML
    private ChoiceBox<Equipo> cbEquipo;

    @FXML
    private ChoiceBox<Evento> cbEvento;

    @FXML
    private ChoiceBox<String> cbMedalla;

    @FXML
    private TextField tfEdad;

    private PrincipalDAO pDao;
    private ParticipacionDAO partiDao;
    private DeportistaDAO distaDao;
    private EventoDAO evenDao;
    private EquipoDAO equiDao;
    private String[] medallas = {"Gold", "Silver", "Bronze", "N/A"};
    private boolean modificar = false;

    /**
     * Constructor de la clase ControlAniadirParticipacion.
     * Este constructor inicializa las instancias de los objetos necesarios para interactuar
     * con la base de datos y gestiona las acciones de adición o modificación de participaciones.
     *
     * @throws SQLException si ocurre un error en la conexión a la base de datos.
     */
    public ControlAniadirParticipacion() throws SQLException {
        pDao = new PrincipalDAO();
        partiDao = new ParticipacionDAO();
        distaDao = new DeportistaDAO();
        evenDao = new EventoDAO();
        equiDao = new EquipoDAO();
    }

    /**
     * Procedimiento que se ejecuta al pulsar el botón "Aceptar".
     * Este procedimiento crea o modifica una participación dependiendo de si la variable
     * `modificar` es verdadera o falsa. Se obtienen los datos desde los campos de la interfaz
     * de usuario y se inserta o actualiza la participación en la base de datos.
     *
     * @param event Evento de acción generado al pulsar el botón "Aceptar".
     */
    @FXML
    void Aceptar(ActionEvent event) {
        try {
            // Obtiene los datos de la ventana
            Deportista d = cbDeportista.getSelectionModel().getSelectedItem();
            Equipo eq = cbEquipo.getSelectionModel().getSelectedItem();
            Evento ev = cbEvento.getSelectionModel().getSelectedItem();
            Integer nEdad = Integer.parseInt(tfEdad.getText());
            String sMedalla = cbMedalla.getSelectionModel().getSelectedItem();

            Participacion p = new Participacion(d, ev, eq, nEdad, sMedalla);
            boolean resultado;

            if (modificar) {
                // Si está modificando la participación
                resultado = partiDao.modificarParticipacion(p);
                if (resultado) {
                    ControlPrincipal.ventanaAlerta("I", "Participación modificada con éxito");
                    Cancelar(event);
                } else {
                    ControlPrincipal.ventanaAlerta("E", "Error al modificar participación");
                }
            } else {
                // Si está añadiendo una nueva participación
                resultado = partiDao.aniadirParticipacion(p);
                if (resultado) {
                    ControlPrincipal.ventanaAlerta("I", "Participación añadida con éxito");
                    Cancelar(event);
                } else {
                    ControlPrincipal.ventanaAlerta("E", "Error al añadir participación");
                }
            }
        } catch (NumberFormatException e) {
            // Captura el error si la edad no es un número válido
            ControlPrincipal.ventanaAlerta("E", "Inserte un valor entero positivo en edad");
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
     * Este procedimiento carga los datos de deportistas, equipos, eventos y medallas
     * desde la base de datos y los añade a los correspondientes ChoiceBox. También
     * se verifica si la ventana se ha abierto para añadir o modificar una participación.
     *
     * @param arg0 URL para la localización de recursos.
     * @param arg1 Bundle de recursos para la inicialización.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Carga los datos de la base de datos en los ChoiceBox
        ObservableList<Deportista> listaDeportistas = distaDao.cargarDeportista();
        ObservableList<Equipo> listaEquipos = equiDao.cargarEquipo();
        ObservableList<Evento> listaEventos = evenDao.cargarEvento();

        cbDeportista.getItems().addAll(listaDeportistas);
        cbEquipo.getItems().addAll(listaEquipos);
        cbEvento.getItems().addAll(listaEventos);
        cbMedalla.getItems().addAll(medallas);

        try {
            // Si se está modificando una participación, carga los datos correspondientes
            Participacion p = ControlPrincipal.pModificar;
            mostrarDatosModificar(p);
            modificar = true;
        } catch (Exception e) {
            // Si no se está modificando, no hace nada
        }
    }

    /**
     * Procedimiento que carga los datos de la participación pasada como parámetro
     * en los campos correspondientes de la ventana.
     * Este procedimiento también desactiva los ChoiceBox de deportista y evento
     * para evitar que se modifiquen, ya que estas son las claves primarias compuestas
     * de la participación.
     *
     * @param p Participación que se desea mostrar en la ventana.
     */
    private void mostrarDatosModificar(Participacion p) {
        // Filtra los datos de la base de datos según los identificadores de la participación
        ObservableList<Deportista> listaDeportista = distaDao.filtrarDeportista("id_deportista", p.getIdDeportista() + "");
        ObservableList<Evento> listaEvento = evenDao.filtrarEvento("id_evento", p.getIdEvento() + "");
        ObservableList<Equipo> listaEquipo = equiDao.filtrarEquipo("id_equipo", p.getIdEquipo() + "");

        Deportista d = listaDeportista.get(0);
        Evento ev = listaEvento.get(0);
        Equipo eq = listaEquipo.get(0);

        // Rellena los campos con los datos de la participación
        cbDeportista.getSelectionModel().select(d);
        cbEvento.getSelectionModel().select(ev);
        cbEquipo.getSelectionModel().select(eq);
        tfEdad.setText(p.getEdad().toString());
        cbMedalla.getSelectionModel().select(p.getMedalla());

        // Desactiva los ChoiceBox para evitar modificaciones en las claves primarias compuestas
        cbDeportista.setDisable(true);
        cbEvento.setDisable(true);
    }
}
