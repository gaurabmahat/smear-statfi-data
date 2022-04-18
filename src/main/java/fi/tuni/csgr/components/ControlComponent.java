package fi.tuni.csgr.components;

import javafx.scene.control.Control;

import java.util.ArrayList;

/**
 * An interface for control components. Components must include a Control object, a label, a method to validate
 * the control input and methods to set and get selection data as a list of strings for saving selection to disk.
 */

public interface ControlComponent {

    // Return true if current selection is valid
    boolean selectionValid();

    // Return true if component input is required
    boolean isMandatory();

    // Return label text to be displayed with component
    String getLabel();

    // Return control component
    Control getControl();

    // Return current selection as list of String
    ArrayList<String> getSelectionData();

    // Set selection in control component
    void setSelectionData(ArrayList<String> data);
}
