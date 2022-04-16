package fi.tuni.csgr.components;

import javafx.scene.control.Control;

/**
 * An interface for control components. Components must include a Component object, a label, and a method to
 * validate the control input.
 */

public interface ControlComponent {
    boolean selectionValid();
    boolean isMandatory();
    String getLabel();
    Control getControl();
}
