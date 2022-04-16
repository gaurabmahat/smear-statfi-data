module fi.tuni.csgr {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.logging;
    requires com.google.gson;
    requires java.net.http;

    opens fi.tuni.csgr to javafx.fxml;
    exports fi.tuni.csgr;
    exports fi.tuni.csgr.managers.graphs;
    opens fi.tuni.csgr.managers.graphs to javafx.fxml;
    opens fi.tuni.csgr.utils to com.google.gson;
    opens fi.tuni.csgr.managers.userdata to com.google.gson;
    opens fi.tuni.csgr.components to com.google.gson;
    exports fi.tuni.csgr.query.resultviews;
    opens fi.tuni.csgr.query.resultviews to javafx.fxml;
}