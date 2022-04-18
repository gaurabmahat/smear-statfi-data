package fi.tuni.csgr.components;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;

import java.util.ArrayList;

/**
 * Component with single choice dropdown control
 */

public class SingleChoiceDropdown implements ControlComponent {
    private ComboBox control;
    private String label;
    private boolean isMandatory;

    public SingleChoiceDropdown(ObservableList<String> choices, String label, boolean isMandatory) {
        control = new ComboBox();
        this.label = label;
        this.isMandatory = isMandatory;
        control.setItems(choices);
    }
    @Override
    public boolean selectionValid() {
        if (isMandatory() && control.getSelectionModel().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isMandatory() {
        return isMandatory;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Control getControl() {
        return control;
    }

    @Override
    public ArrayList<String> getSelectionData() {
        ArrayList<String> data = new ArrayList<>();
        data.add(control.getSelectionModel().toString());
        return data;
    }

    @Override
    public void setSelectionData(ArrayList<String> data) {
        control.getSelectionModel().select(data.get(0));
    }

    public String getSelection() {
        return control.getSelectionModel().getSelectedItem().toString();
    }

    public void setSelection(String value) {
        control.getSelectionModel().select(value);
    }
}
