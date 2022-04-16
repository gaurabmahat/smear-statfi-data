package fi.tuni.csgr.components;

import javafx.scene.control.Control;
import javafx.scene.control.MenuButton;

public class YearSelector implements ControlComponent {

    private String label;
    private boolean isMandatory;
    private YearPicker control;

    public YearSelector(int from, int to, String label, boolean isMandatory) {
        this.label = label;
        this.isMandatory = isMandatory;
        control = new YearPicker(from, to);
    }

    public int getFromDate() {
        return control.getStartYear();
    }

    public int getToDate() {
        return control.getEndYear();
    }

    public void setRange(int from, int to) {
        control.setRange(from, to);
    }

    @Override
    public boolean selectionValid() {
        if (isMandatory() && (control.getStartYear() == 0 || control.getEndYear() == 0)) {
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
}
