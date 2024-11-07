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

public class ControlAniadirDeportista implements Initializable{

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
     * Al pulsar el botón genera un deportista y lo añade en la base de datos.
     * @param event
     * @throws Throwable
     */
    @FXML
    void Aceptar(ActionEvent event) throws Throwable {

        String errores = "";
        try{
            int nId = pDao.generarId("Deportista");
            String sNombre = tfNombre.getText();
            Character cSexo;
            if (rbM.isSelected()) {
                cSexo = 'M';
            }else {
                cSexo = 'F';
            }
            Integer nPeso = 0,nAltura = 0;
            try {nPeso = Integer.parseInt(tfPeso.getText());}catch(Exception e) {errores+="El campo Peso debe tener un número entero positivo.\n";}
            try{nAltura = Integer.parseInt(tfAltura.getText());}catch(Exception e) {errores+="El campo Altura debe tener un número entero positivo.\n";}
            if (!errores.equals("")) {
                ControlPrincipal.ventanaAlerta("E", errores);
            }else {
                boolean resultado = false;
                if(!modificar) {
                    Deportista subd = new Deportista(nId, sNombre, cSexo, nPeso, nAltura);
                    resultado = distaDao.aniadirDeportista(subd);
                }else {
                    Deportista subd = new Deportista(d.getIdDeportista(), sNombre, cSexo, nPeso, nAltura);
                    resultado = distaDao.modificarDeportista(subd);
                }
                if (resultado) {
                    ControlPrincipal.ventanaAlerta("I", "Deportista añadido con éxito");
                    Cancelar(event);
                }else {
                    ControlPrincipal.ventanaAlerta("E", "Error al añadir Deportista");
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
            ControlPrincipal.ventanaAlerta("E", "Los campos nombre y sexo son obligatorios");
        }
    }

    /**
     * Cierra la ventana
     * @param event
     * @throws Throwable
     */
    @FXML
    void Cancelar(ActionEvent event) throws Throwable {
        Node node = (Node)event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        d=null;
        stage.close();
    }

    /**
     * Al iniciar, comprueba si se le llamó para modificar o añadir, y guarda el deportista.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        modificar = false;

        try {
            d = ControlDeportista.gDepModificar;
            mostrarDatosModificar(d);
            modificar = true;
        }catch(Exception e) {}
    }

    /**
     * Muestra los datos del deportista en la ventana.
     * @param d Deportista
     */
    private void mostrarDatosModificar(Deportista d) {
        tfNombre.setText(d.getNombre());
        if (d.getSexo().equals('F')) {
            rdF.setSelected(true);
        }else {
            rbM.setSelected(true);
        }
        tfPeso.setText(d.getPeso().toString());
        tfAltura.setText(d.getAltura().toString());
    }

}
