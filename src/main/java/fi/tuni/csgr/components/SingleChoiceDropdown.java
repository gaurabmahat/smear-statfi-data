package fi.tuni.csgr.components;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;

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

    public String getSelection() {
        return control.getSelectionModel().getSelectedItem().toString();
    }

    public void setSelection(String value) {
        control.getSelectionModel().select(value);
    }
}
