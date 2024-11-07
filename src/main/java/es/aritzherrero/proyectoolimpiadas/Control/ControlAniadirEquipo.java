package es.aritzherrero.proyectoolimpiadas.Control;


import java.net.URL;
import java.util.ResourceBundle;

import es.aritzherrero.proyectoolimpiadas.DAO.EquipoDAO;
import es.aritzherrero.proyectoolimpiadas.DAO.PrincipalDAO;
import es.aritzherrero.proyectoolimpiadas.Modelo.Equipo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControlAniadirEquipo implements Initializable{

    @FXML
    private TextField tfIniciales;

    @FXML
    private TextField tfNombre;

    private PrincipalDAO pDao;
    private EquipoDAO eDao;
    private Equipo eq;
    private boolean modificar;

    /**
     * Al pulsar el botón genera un Equipo y lo añade a la BBDD.
     * @param event
     */
    @FXML
    void Aceptar(ActionEvent event) {
        int nId = pDao.generarId("Equipo");
        String sNombre = tfNombre.getText();
        String sIniciales = tfIniciales.getText();

        if (modificar) {
            Equipo e = new Equipo(eq.getIdEquipo(), sNombre, sIniciales);
            boolean resultado = eDao.modificarEquipo(e);
            if (resultado) {
                ControlPrincipal.ventanaAlerta("I", "Equipo modificado con éxito");
                Cancelar(event);
            }else {
                ControlPrincipal.ventanaAlerta("E", "Error al modificar Equipo");
            }
        }else {
            Equipo e = new Equipo(nId, sNombre, sIniciales);
            boolean resultado = eDao.aniadirEquipo(e);
            if (resultado) {
                ControlPrincipal.ventanaAlerta("I", "Equipo añadido con éxito");
                Cancelar(event);
            }else {
                ControlPrincipal.ventanaAlerta("E", "Error al añadir Equipo");
            }
        }
    }

    /**
     * Cierra la ventana.
     * @param event
     */
    @FXML
    void Cancelar(ActionEvent event) {
        Node node = (Node)event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        modificar = false;

        try {
            eq = ControlPrincipal.gEquipoModificar;
            mostrarDatosModificar();
            modificar = true;
        }catch(Exception e) {}
    }

    private void mostrarDatosModificar() {
        tfNombre.setText(eq.getNombre());
        tfIniciales.setText(eq.getIniciales());
    }

}