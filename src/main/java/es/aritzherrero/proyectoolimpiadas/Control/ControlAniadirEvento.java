package es.aritzherrero.proyectoolimpiadas.Control;

import java.net.URL;
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

public class ControlAniadirEvento implements Initializable{

    @FXML
    private ChoiceBox<Deporte> cbDeporte;

    @FXML
    private ChoiceBox<Olimpiada> cbOlimpiada;

    @FXML
    private TextField tfNombre;

    private PrincipalDAO pDao = new PrincipalDAO();
    private OlimpiadaDAO oDao = new OlimpiadaDAO();
    private EventoDAO eDao = new EventoDAO();
    private DeporteDAO dDao = new DeporteDAO();
    private Evento eve;
    private boolean modificar;


    /**
     * Genera un Evento y lo añade/modifica a la BBDD.
     * @param event
     */
    @FXML
    void Aceptar(ActionEvent event) {
        try {

            String sNombre = tfNombre.getText();
            Olimpiada o = cbOlimpiada.getSelectionModel().getSelectedItem();
            Deporte d = cbDeporte.getSelectionModel().getSelectedItem();

            if (modificar) {

                Evento e = new Evento(eve.getIdEvento(), o.getIdOlimpiada(), d.getIdDeporte(), sNombre, o.getNombre(), d.getNombre());
                boolean resultado = eDao.modificarEvento(e);
                if (resultado) {
                    ControlPrincipal.ventanaAlerta("I", "Deportista modificado con éxito");
                    Cancelar(event);
                }else {
                    ControlPrincipal.ventanaAlerta("E", "Error al modificar Deportista");
                }

            }else{

                int nId = pDao.generarId("Evento");
                Evento e = new Evento(nId, o.getIdOlimpiada(), d.getIdDeporte(), sNombre, o.getNombre(), d.getNombre());
                boolean resultado = eDao.aniadirEvento(e);
                if (resultado) {
                    ControlPrincipal.ventanaAlerta("I", "Deportista añadido con éxito");
                    Cancelar(event);
                }else {
                    ControlPrincipal.ventanaAlerta("E", "Error al añadir Deportista");
                }
            }
        }catch (Exception e) {

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
     * Al iniciar la ventana carga las olimpiadas y deportes para añadir a los ChoiceBox.
     * Comprueba si la orden es añadir o modificar; si es la última, muestra los datos del evento
     * en la ventana.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        modificar = false;
        ObservableList<Olimpiada> listaOlimpiada= oDao.cargarOlimpiada();
        Iterator<Olimpiada> itO = listaOlimpiada.iterator();
        while (itO.hasNext()) {
            Olimpiada o = itO.next();
            cbOlimpiada.getItems().add(o);
        }
        ObservableList<Deporte> listaDeporte = dDao.cargarDeporte();
        Iterator<Deporte> itD = listaDeporte.iterator();
        while (itD.hasNext()) {
            Deporte d = itD.next();
            cbDeporte.getItems().add(d);
        }

        try {
            eve = ControlEvento.gEveModificar;
            mostrarDatosModificar();
            modificar = true;
        }catch(Exception e) {}
    }

    /**
     * Muestra el evento en la ventana.
     */
    private void mostrarDatosModificar() {
        ObservableList<Deporte> listaDeporte = dDao.filtrarDeporte("id_deporte", eve.getIdDeporte().toString());
        ObservableList<Olimpiada> listaOlimpiada = oDao.filtrarOlimpiada("id_olimpiada", eve.getIdOlimpiada().toString());

        Deporte d = listaDeporte.get(0);
        Olimpiada o = listaOlimpiada.get(0);

        tfNombre.setText(eve.getNomEvento());
        cbDeporte.getSelectionModel().select(d);
        cbOlimpiada.getSelectionModel().select(o);
    }

}