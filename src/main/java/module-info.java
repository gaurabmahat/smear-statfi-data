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
}