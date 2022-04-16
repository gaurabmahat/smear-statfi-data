package fi.tuni.csgr.components;

import javafx.collections.ObservableList;
import javafx.scene.control.Control;

import java.util.ArrayList;

public class MultipleChoiceDropDown implements ControlComponent {
    private CheckBoxMenu control;
    private String label;
    private boolean isMandatory;

    public MultipleChoiceDropDown(ObservableList<String> choices, String label, boolean isMandatory) {
        this.label = label;
        this.isMandatory = isMandatory;
        this.control = new CheckBoxMenu(choices, label);
    }

    @Override
    public boolean selectionValid() {
        if (isMandatory() && control.getSelectedItems().isEmpty()) {
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
        return control.getSelectedItems();
    }

    @Override
    public void setSelectionData(ArrayList<String> data) {
        control.clearSelections();
        data.forEach(selection -> control.setSelected(selection));
    }

    public ArrayList<String> getSelectedItems() {
        return control.getSelectedItems();
    }
}
