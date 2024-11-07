module es.aritzherrero.proyectoolimpiadas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens es.aritzherrero.proyectoolimpiadas to javafx.fxml;
    exports es.aritzherrero.proyectoolimpiadas;
    opens es.aritzherrero.proyectoolimpiadas.Control to javafx.fxml, javafx.base;
    opens es.aritzherrero.proyectoolimpiadas.Modelo to javafx.fxml, javafx.base;
    opens es.aritzherrero.proyectoolimpiadas.DAO to javafx.fxml, javafx.base;
    opens es.aritzherrero.proyectoolimpiadas.Conexion to javafx.fxml, javafx.base;
}