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

public class ControlAniadirDeporte implements Initializable{

    @FXML
    private TextField tfNombre;

    private DeporteDAO dDao;
    private PrincipalDAO pDao;
    private Deporte d;
    private boolean modificar;

    @FXML
    void Aceptar(ActionEvent event) {

        int nId = pDao.generarId("Deportista");
        String sNombre = tfNombre.getText();

        if (modificar) {
            Deporte dMod = new Deporte(d.getIdDeporte(), sNombre);
            boolean resultado = dDao.modificarDeporte(dMod);
            if (resultado) {
                ControlPrincipal.ventanaAlerta("I", "Deporte modificado con éxito");
                Cancelar(event);
            }else {
                ControlPrincipal.ventanaAlerta("E", "Error al modificar Deporte");
            }
        }else {
            Deporte d = new Deporte(nId, sNombre);
            boolean resultado = dDao.aniadirDeporte(d);
            if (resultado) {
                ControlPrincipal.ventanaAlerta("I", "Deporte añadido con éxito");
                Cancelar(event);
            }else {
                ControlPrincipal.ventanaAlerta("E", "Error al añadir Deporte");
            }
        }
    }

    @FXML
    void Cancelar(ActionEvent event) {
        Node node = (Node)event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            d = ControlEvento.gDepModificar;
            mostrarDatosModificar();
            modificar = true;
        }catch(Exception e) {}

    }

    private void mostrarDatosModificar() {
        tfNombre.setText(d.getNombre());
    }

}
