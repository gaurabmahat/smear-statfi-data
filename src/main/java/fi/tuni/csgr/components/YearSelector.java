package fi.tuni.csgr.components;

import javafx.scene.control.Control;
import javafx.scene.control.MenuButton;

public class YearSelector implements ControlComponent {

    private String label;
    private boolean isMandatory;
    private MenuButton control;
    YearPicker yearPicker;

    public YearSelector(int from, int to, String label, boolean isMandatory) {
        this.label = label;
        this.isMandatory = isMandatory;
        control = new MenuButton();

        yearPicker = new YearPicker(from, to);
        yearPicker.setHideOnClick(false);
        yearPicker.getStyleClass().add("year-picker-menu-item");
        control.getItems().add(yearPicker);
    }

    public int getFromDate() {
        return yearPicker.getStartYear();
    }

    public int getToDate() {
        return yearPicker.getEndYear();
    }

    public void setRange(int from, int to) {
        yearPicker.setRange(from, to);
    }

    @Override
    public boolean selectionValid() {
        if (isMandatory() && (yearPicker.getStartYear() == 0 || yearPicker.getEndYear() == 0)) {
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
