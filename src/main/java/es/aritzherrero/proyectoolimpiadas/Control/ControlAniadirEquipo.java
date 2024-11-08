package es.aritzherrero.proyectoolimpiadas.Control;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import es.aritzherrero.proyectoolimpiadas.DAO.DeportistaDAO;
import es.aritzherrero.proyectoolimpiadas.DAO.EquipoDAO;
import es.aritzherrero.proyectoolimpiadas.DAO.PrincipalDAO;
import es.aritzherrero.proyectoolimpiadas.Modelo.Equipo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador para gestionar la ventana de añadir y modificar equipos.
 * Esta clase permite añadir un nuevo equipo a la base de datos o modificar uno existente.
 * Además, valida los datos ingresados y muestra mensajes de éxito o error
 * según el resultado de la operación.
 */
public class ControlAniadirEquipo implements Initializable {

    @FXML
    private TextField tfIniciales;

    @FXML
    private TextField tfNombre;

    private PrincipalDAO pDao;
    private EquipoDAO eDao;
    private Equipo eq;
    private boolean modificar;

    public ControlAniadirEquipo() throws SQLException {
        eDao = new EquipoDAO();
        pDao = new PrincipalDAO();
    }
    /**
     * Procedimiento que procesa los datos ingresados para añadir o modificar un equipo en la base de datos.
     * Si el modo de modificación está activado, actualiza un equipo existente; en caso contrario,
     * añade uno nuevo a la base de datos. Muestra mensajes de éxito o error
     * en función del resultado de la operación.
     *
     * @param event evento de acción generado al pulsar el botón de aceptar.
     */
    @FXML
    void Aceptar(ActionEvent event) {
        // Genera un nuevo ID para el equipo si se trata de una inserción
        int nId = pDao.generarId("Equipo");

        // Obtiene los valores del nombre y las iniciales desde los campos de texto
        String sNombre = tfNombre.getText();
        String sIniciales = tfIniciales.getText();

        if (modificar) {
            // Modifica el equipo existente con los datos nuevos
            Equipo e = new Equipo(eq.getIdEquipo(), sNombre, sIniciales);
            boolean resultado = eDao.modificarEquipo(e);

            // Muestra el mensaje correspondiente según el resultado de la operación
            if (resultado) {
                ControlPrincipal.ventanaAlerta("I", "Equipo modificado con éxito");
                Cancelar(event);  // Cierra la ventana al finalizar
            } else {
                ControlPrincipal.ventanaAlerta("E", "Error al modificar Equipo");
            }
        } else {
            // Añade un nuevo equipo con los datos ingresados
            Equipo e = new Equipo(nId, sNombre, sIniciales);
            boolean resultado = eDao.aniadirEquipo(e);

            // Muestra el mensaje correspondiente según el resultado de la operación
            if (resultado) {
                ControlPrincipal.ventanaAlerta("I", "Equipo añadido con éxito");
                Cancelar(event);  // Cierra la ventana al finalizar
            } else {
                ControlPrincipal.ventanaAlerta("E", "Error al añadir Equipo");
            }
        }
    }

    /**
     * Procedimiento que cierra la ventana actual.
     *
     * @param event evento de acción generado al pulsar el botón de cancelar.
     */
    @FXML
    void Cancelar(ActionEvent event) {
        // Obtiene el nodo de origen del evento y cierra la ventana asociada
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    /**
     * Procedimiento de inicialización del controlador.
     * Al iniciar, verifica si el controlador ha sido invocado para modificar un equipo,
     * cargando los datos correspondientes en la ventana.
     *
     * @param arg0 URL para la localización de recursos.
     * @param arg1 Bundle de recursos para la inicialización.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        modificar = false;

        try {
            // Intenta cargar el equipo a modificar, si está disponible
            eq = ControlPrincipal.gEquipoModificar;
            mostrarDatosModificar();
            modificar = true;  // Activa el modo de modificación
        } catch (Exception e) {
            // No realiza ninguna acción si no hay equipo a modificar
        }
    }

    /**
     * Procedimiento que muestra los datos del equipo a modificar en la ventana.
     * Asigna el nombre y las iniciales del equipo a los campos correspondientes
     * para permitir la edición.
     */
    private void mostrarDatosModificar() {
        // Establece los valores del equipo en los campos de la interfaz
        tfNombre.setText(eq.getNombre());
        tfIniciales.setText(eq.getIniciales());
    }

}
