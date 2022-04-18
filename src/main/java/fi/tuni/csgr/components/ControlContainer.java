package fi.tuni.csgr.components;

import javafx.scene.control.Control;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Display format for a ControlComponent.
 */

public class ControlContainer extends VBox {
    /**
     * Creates a VBox with a formatted view of the control component.
     * @param component
     */
    public ControlContainer(ControlComponent component) {
        this.setSpacing(3);
        this.getStyleClass().add("control-component");
        Control control = component.getControl();
        control.setPrefWidth(200);
        this.getChildren().add(new Text(component.getLabel()+":"));
        this.getChildren().add(control);
    }
}
