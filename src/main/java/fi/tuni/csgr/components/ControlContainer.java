package fi.tuni.csgr.components;

import javafx.scene.control.Control;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ControlContainer extends VBox {
    public ControlContainer(ControlComponent component) {
        this.setSpacing(3);
        this.getStyleClass().add("control-component");

        Control control = component.getControl();
        control.setPrefWidth(200);
        this.getChildren().add(new Text(component.getLabel()+":"));
        this.getChildren().add(control);
    }
}
