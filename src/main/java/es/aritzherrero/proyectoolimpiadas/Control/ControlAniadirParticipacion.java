package es.aritzherrero.proyectoolimpiadas.Control;

import java.net.URL;
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

public class ControlAniadirParticipacion implements Initializable{

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

    private PrincipalDAO pDao = new PrincipalDAO();
    private ParticipacionDAO partiDao = new ParticipacionDAO();
    private DeportistaDAO distaDao = new DeportistaDAO();
    private EventoDAO evenDao = new EventoDAO();
    private EquipoDAO equiDao= new EquipoDAO();
    private String[] medallas = {"Gold","Silver","Bronze","N/A"};
    private boolean modificar=false;

    /**
     * Inserta los datos de la ventana en una participación y añadirá/modificará dependiendo desde donde se haya abierto la ventana.
     * @param event
     */
    @FXML
    void Aceptar(ActionEvent event) {
        try {
            Deportista d = cbDeportista.getSelectionModel().getSelectedItem();
            Equipo eq = cbEquipo.getSelectionModel().getSelectedItem();
            Evento ev = cbEvento.getSelectionModel().getSelectedItem();
            Integer nEdad = Integer.parseInt(tfEdad.getText());
            String sMedalla = cbMedalla.getSelectionModel().getSelectedItem();

            Participacion p = new Participacion(d, ev, eq, nEdad, sMedalla);
            boolean resultado;
            if (modificar) {
                resultado = partiDao.modificarParticipacion(p);
                if (resultado) {
                    ControlPrincipal.ventanaAlerta("I", "Participación modificada con éxito");
                    Cancelar(event);
                }else {
                    ControlPrincipal.ventanaAlerta("E", "Error al modificar participación");
                }
            }else {
                resultado = partiDao.aniadirParticipacion(p);
                if (resultado) {
                    ControlPrincipal.ventanaAlerta("I", "Participación añadida con éxito");
                    Cancelar(event);
                }else {
                    ControlPrincipal.ventanaAlerta("E", "Error al añadir participación");
                }
            }
        }catch(NumberFormatException e) {
            ControlPrincipal.ventanaAlerta("E", "Inserte un valor entero positivo en edad");
        }
    }

    /**
     * Cierra la ventana actual.
     * @param event
     */
    @FXML
    void Cancelar(ActionEvent event) {
        Node node = (Node)event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    /**
     * Al iniciar, carga los deportistas, eventos, equipos y medallas de la BBDD y los añade a sus respectivos ChoiceBox.
     * También controla si se va a añadir o modificar en la BBDD.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ObservableList<Deportista> listaDeportistas = distaDao.cargarDeportista();
        ObservableList<Equipo> listaEquipos = equiDao.cargarEquipo();
        ObservableList<Evento> listaEventos = evenDao.cargarEvento();

        cbDeportista.getItems().addAll(listaDeportistas);
        cbEquipo.getItems().addAll(listaEquipos);
        cbEvento.getItems().addAll(listaEventos);
        cbMedalla.getItems().addAll(medallas);

        try {
            Participacion p = ControlPrincipal.pModificar;
            mostrarDatosModificar(p);
            modificar=true;
        }catch(Exception e) {}
    }

    /**
     * Carga los datos de la participacion pasada como parámetro en la ventana. Desactiva los ChoiceBox de deportista y evento (Clave primaria compuesta).
     * @param p
     */
    private void mostrarDatosModificar(Participacion p) {
        ObservableList<Deportista>listaDeportista = distaDao.filtrarDeportista("id_deportista", p.getIdDeportista()+"");
        ObservableList<Evento>listaEvento = evenDao.filtrarEvento("id_evento", p.getIdEvento()+"");
        ObservableList<Equipo>listaEquipo = equiDao.filtrarEquipo("id_equipo", p.getIdEquipo()+"");

        Deportista d = listaDeportista.get(0);
        Evento ev = listaEvento.get(0);
        Equipo eq= listaEquipo.get(0);

        cbDeportista.getSelectionModel().select(d);
        cbEvento.getSelectionModel().select(ev);
        cbEquipo.getSelectionModel().select(eq);
        tfEdad.setText(p.getEdad().toString());
        cbMedalla.getSelectionModel().select(p.getMedalla());

        cbDeportista.setDisable(true);
        cbEvento.setDisable(true);
    }
}