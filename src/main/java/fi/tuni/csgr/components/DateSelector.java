package fi.tuni.csgr.components;

import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

import static fi.tuni.csgr.utils.DatePickerUtils.restrictDatePicker;

public class DateSelector implements ControlComponent {
    private boolean isMandatory;
    private String label;
    private DatePicker control;

    public DateSelector(LocalDate defaultDate, String label, boolean isMandatory) {
        control = new DatePicker(defaultDate);
        this.label = label;
        this.isMandatory = isMandatory;
    }

    @Override
    public boolean selectionValid() {
        if (isMandatory() && control.getValue() == null) {
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

    public void limitDatePicker(LocalDate minDate, LocalDate maxDate) {
        restrictDatePicker(control, minDate, maxDate);
    }

    public LocalDate getDate() {
        return control.getValue();
    }
}
