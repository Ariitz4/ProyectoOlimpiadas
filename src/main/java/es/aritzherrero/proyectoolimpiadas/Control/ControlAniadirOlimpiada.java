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

public class ControlAniadirOlimpiada implements Initializable{

    @FXML
    private TextField tfAnio;

    @FXML
    private TextField tfCiudad;

    @FXML
    private ChoiceBox<String> cbTemporada;

    private PrincipalDAO pDao;
    private OlimpiadaDAO oDao;
    private String[] campos = {"Summer","Winter"};
    private Olimpiada o;
    private boolean modificar;

    public ControlAniadirOlimpiada() throws SQLException {
        oDao = new OlimpiadaDAO();
        pDao = new PrincipalDAO();
    }


    /**
     * Genera una olimpiada y la añade/modifica a la BBDD.
     * @param event
     */
    @FXML
    void Aceptar(ActionEvent event) {


        int nId = pDao.generarId("Olimpiada");
        Integer nAnio = Integer.parseInt(tfAnio.getText());
        String sTemporada = cbTemporada.getSelectionModel().getSelectedItem();
        String sCiudad = tfCiudad.getText();
        String sNombre = nAnio + " " + sTemporada;


        if (modificar) {
            Olimpiada oMod = new Olimpiada(o.getIdOlimpiada(), sNombre, nAnio, sTemporada, sCiudad);
            boolean resultado = oDao.modificarOlimpiada(oMod);
            if (resultado) {
                ControlPrincipal.ventanaAlerta("I", "Olimpiada modificada con éxito");
                Cancelar(event);
            }else {
                ControlPrincipal.ventanaAlerta("E", "Error al modificar Olimpiada");
            }
        }else {
            Olimpiada oMod = new Olimpiada(nId, sNombre, nAnio, sTemporada, sCiudad);
            boolean resultado = oDao.aniadirOlimpiada(oMod);
            if (resultado) {
                ControlPrincipal.ventanaAlerta("I", "Olimpiada añadida con éxito");
                Cancelar(event);
            }else {
                ControlPrincipal.ventanaAlerta("E", "Error al añadir Olimpiada");
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

    /**
     * Añade los campos 'Summer' y 'Winter' en el ChoiceBox
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cbTemporada.getItems().addAll(campos);

        try {
            o = ControlOlimpiada.gOliModificar;
            mostrarDatosModificar();
            modificar = true;
        }catch(Exception e) {}

    }

    private void mostrarDatosModificar() {
        tfAnio.setText(o.getAnio().toString());
        tfCiudad.setText(o.getCiudad());
        if (o.getTemporada().equals("Summer")) {
            cbTemporada.getSelectionModel().select(campos[0]);
        }else {
            cbTemporada.getSelectionModel().select(campos[1]);
        }
    }
}
