module com.example.defter {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;


    opens com.example.defter to javafx.fxml;
    opens com.example.defter.Admin;
    opens com.example.defter.Model;
    exports com.example.defter;
}