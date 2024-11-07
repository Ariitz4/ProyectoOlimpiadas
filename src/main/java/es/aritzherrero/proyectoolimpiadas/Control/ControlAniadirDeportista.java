package es.aritzherrero.proyectoolimpiadas.Control;

import java.net.URL;
import java.util.ResourceBundle;

import es.aritzherrero.proyectoolimpiadas.DAO.DeportistaDAO;
import es.aritzherrero.proyectoolimpiadas.DAO.PrincipalDAO;
import es.aritzherrero.proyectoolimpiadas.Modelo.Deportista;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Controlador para gestionar la ventana de añadir y modificar deportistas.
 * Esta clase permite añadir un nuevo deportista a la base de datos o modificar uno existente.
 * Además, valida los datos ingresados por el usuario y muestra mensajes de éxito o error
 * según el resultado de la operación.
 */
public class ControlAniadirDeportista implements Initializable {

    @FXML
    private ImageView imgImagen;

    @FXML
    private RadioButton rbM;

    @FXML
    private RadioButton rdF;

    @FXML
    private TextField tfAltura;

    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfPeso;

    @FXML
    private ToggleGroup tgSexo;

    private PrincipalDAO pDao;
    private DeportistaDAO distaDao;
    private boolean modificar;
    private Deportista d;

    /**
     * Procedimiento que procesa los datos ingresados para añadir o modificar un deportista en la base de datos.
     * Si el modo modificar está activo, actualiza un deportista existente; de lo contrario,
     * añade uno nuevo. Muestra mensajes de error en caso de que los datos ingresados sean incorrectos.
     *
     * @param event evento de acción generado al pulsar el botón de aceptar.
     * @throws Throwable si ocurre un error inesperado durante la operación.
     */
    @FXML
    void Aceptar(ActionEvent event) throws Throwable {

        String errores = "";  // Cadena para almacenar mensajes de error
        try {
            // Genera un nuevo ID para el deportista si es una inserción
            int nId = pDao.generarId("Deportista");

            // Obtiene el nombre y el sexo ingresado por el usuario
            String sNombre = tfNombre.getText();
            Character cSexo = rbM.isSelected() ? 'M' : 'F';

            // Variables para almacenar el peso y la altura ingresados
            Integer nPeso = 0, nAltura = 0;

            // Intenta convertir el peso y la altura a enteros; agrega errores en caso de fallos
            try {
                nPeso = Integer.parseInt(tfPeso.getText());
            } catch (Exception e) {
                errores += "El campo Peso debe tener un número entero positivo.\n";
            }
            try {
                nAltura = Integer.parseInt(tfAltura.getText());
            } catch (Exception e) {
                errores += "El campo Altura debe tener un número entero positivo.\n";
            }

            // Muestra los errores de validación si existen
            if (!errores.equals("")) {
                ControlPrincipal.ventanaAlerta("E", errores);
            } else {
                boolean resultado = false;
                if (!modificar) {
                    // Añade un nuevo deportista
                    Deportista subd = new Deportista(nId, sNombre, cSexo, nPeso, nAltura);
                    resultado = distaDao.aniadirDeportista(subd);
                } else {
                    // Modifica el deportista existente
                    Deportista subd = new Deportista(d.getIdDeportista(), sNombre, cSexo, nPeso, nAltura);
                    resultado = distaDao.modificarDeportista(subd);
                }

                // Muestra el mensaje de éxito o error según el resultado de la operación
                if (resultado) {
                    ControlPrincipal.ventanaAlerta("I", "Deportista añadido con éxito");
                    Cancelar(event); // Cierra la ventana tras el éxito
                } else {
                    ControlPrincipal.ventanaAlerta("E", "Error al añadir Deportista");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ControlPrincipal.ventanaAlerta("E", "Los campos nombre y sexo son obligatorios");
        }
    }

    /**
     * Procedimiento que cancela la operación actual y cierra la ventana.
     *
     * @param event evento de acción generado al pulsar el botón de cancelar.
     * @throws Throwable si ocurre un error inesperado durante la operación.
     */
    @FXML
    void Cancelar(ActionEvent event) throws Throwable {
        // Cierra la ventana actual
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        d = null; // Limpia el objeto deportista antes de cerrar
        stage.close();
    }

    /**
     * Procedimiento de inicialización del controlador.
     * Al iniciar, comprueba si el controlador ha sido invocado para modificar un deportista,
     * cargando los datos correspondientes en la ventana.
     *
     * @param arg0 URL para la localización de recursos.
     * @param arg1 Bundle de recursos para la inicialización.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        modificar = false;

        try {
            // Intenta cargar el deportista a modificar
            d = ControlDeportista.gDepModificar;
            mostrarDatosModificar(d);
            modificar = true; // Activa el modo de modificación
        } catch (Exception e) {
            // Manejo silencioso de excepciones en caso de que no haya un deportista a modificar
        }
    }

    /**
     * Procedimiento que muestra los datos del deportista a modificar en la ventana.
     *
     * @param d Deportista cuyos datos serán mostrados.
     */
    private void mostrarDatosModificar(Deportista d) {
        // Establece los valores del deportista en los campos de la interfaz
        tfNombre.setText(d.getNombre());
        if (d.getSexo().equals('F')) {
            rdF.setSelected(true);
        } else {
            rbM.setSelected(true);
        }
        tfPeso.setText(d.getPeso().toString());
        tfAltura.setText(d.getAltura().toString());
    }

}
