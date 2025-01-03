module org.kosa.bookmanagement {

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires javafx.controls;
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;
    requires com.google.common;
    requires org.checkerframework.checker.qual;

    opens org.kosa.bookmanagement to javafx.fxml;
    opens org.kosa.bookmanagement.controller to javafx.fxml;
    exports org.kosa.bookmanagement;
    opens org.kosa.bookmanagement.model.dto to javafx.base;
    opens org.kosa.bookmanagement.model.service to javafx.fxml;
}