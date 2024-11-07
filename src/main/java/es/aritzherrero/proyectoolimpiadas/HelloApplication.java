package es.aritzherrero.proyectoolimpiadas;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;



public class HelloApplication extends Application{

    FlowPane root;

    @Override
    public void start(Stage stage) throws Exception {
        try {
            root = (FlowPane)FXMLLoader.load(getClass().getResource("fxml/ventanaPrincipal.fxml"));
            stage.setTitle("OLIMPIADAS");
            Scene scene = new Scene(root,1000,600);
            stage.setScene(scene);
            stage.setMinWidth(1000);
            stage.setMinHeight(700);
            stage.setMaxWidth(1000);
            stage.setMaxHeight(700);
            stage.getIcons().add(new Image(getClass().getResource("/img/imgOlimpiadas.png.jpg").toString()));
            stage.show();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

}