package es.aritzherrero.proyectoolimpiadas.Control;

import java.net.URL;
import java.util.ResourceBundle;

import es.aritzherrero.proyectoolimpiadas.DAO.DeporteDAO;
import es.aritzherrero.proyectoolimpiadas.DAO.PrincipalDAO;
import es.aritzherrero.proyectoolimpiadas.Modelo.Deporte;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador encargado de gestionar la ventana de añadir y modificar deportes.
 * Este controlador permite crear nuevos deportes o modificar los existentes, mostrando
 * mensajes de éxito o error según el resultado de la operación.
 */
public class ControlAniadirDeporte implements Initializable {

    @FXML
    private TextField tfNombre;

    private DeporteDAO dDao;
    private PrincipalDAO pDao;
    private Deporte d;
    private boolean modificar;

    /**
     * Procedimiento que acepta y procesa los datos ingresados para añadir o modificar
     * un deporte en la base de datos.
     * Si el modo modificar está activo, actualiza un deporte existente; de lo contrario,
     * añade uno nuevo. Al finalizar, muestra un mensaje de éxito o error.
     *
     * @param event evento de acción generado al pulsar el botón de aceptar.
     */
    @FXML
    void Aceptar(ActionEvent event) {
        // Genera un nuevo ID para el deporte en caso de añadir uno nuevo
        int nId = pDao.generarId("Deportista");

        // Obtiene el nombre del deporte ingresado por el usuario
        String sNombre = tfNombre.getText();

        if (modificar) {
            // Procedimiento de modificación de un deporte existente
            Deporte dMod = new Deporte(d.getIdDeporte(), sNombre);
            boolean resultado = dDao.modificarDeporte(dMod);

            // Muestra el mensaje correspondiente según el resultado de la operación
            if (resultado) {
                ControlPrincipal.ventanaAlerta("I", "Deporte modificado con éxito");
                Cancelar(event); // Cierra la ventana después de la modificación exitosa
            } else {
                ControlPrincipal.ventanaAlerta("E", "Error al modificar Deporte");
            }
        } else {
            // Procedimiento para añadir un nuevo deporte
            Deporte d = new Deporte(nId, sNombre);
            boolean resultado = dDao.aniadirDeporte(d);

            // Muestra el mensaje correspondiente según el resultado de la operación
            if (resultado) {
                ControlPrincipal.ventanaAlerta("I", "Deporte añadido con éxito");
                Cancelar(event); // Cierra la ventana después de añadir el deporte exitosamente
            } else {
                ControlPrincipal.ventanaAlerta("E", "Error al añadir Deporte");
            }
        }
    }

    /**
     * Procedimiento que cancela la operación actual y cierra la ventana.
     *
     * @param event evento de acción generado al pulsar el botón de cancelar.
     */
    @FXML
    void Cancelar(ActionEvent event) {
        // Obtiene la ventana actual y la cierra
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    /**
     * Procedimiento de inicialización del controlador.
     * Si hay un deporte seleccionado para modificar, carga los datos de dicho deporte
     * y establece el modo de modificación.
     * @param arg0 URL para la localización de recursos.
     * @param arg1 Bundle de recursos para la inicialización.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            // Intenta cargar el deporte que se desea modificar
            d = ControlEvento.gDepModificar;
            mostrarDatosModificar();
            modificar = true; // Activa el modo de modificación
        } catch (Exception e) {
            // Manejo silencioso de excepciones en caso de que no haya un deporte a modificar
        }
    }

    /**
     * Procedimiento que muestra los datos del deporte que se va a modificar en los campos de entrada.
     */
    private void mostrarDatosModificar() {
        // Establece el nombre del deporte en el campo de texto
        tfNombre.setText(d.getNombre());
    }
}
