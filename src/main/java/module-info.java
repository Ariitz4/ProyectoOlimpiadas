module es.aritzherrero.proyectoolimpiadas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens es.aritzherrero.proyectoolimpiadas to javafx.fxml;
    exports es.aritzherrero.proyectoolimpiadas;
}