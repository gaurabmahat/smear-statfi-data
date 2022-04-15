package fi.tuni.csgr.components;

import javafx.scene.control.Control;

public interface ControlComponent {
    boolean selectionValid();
    boolean isMandatory();
    String getLabel();
    Control getControl();
}
