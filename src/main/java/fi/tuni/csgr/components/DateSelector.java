package fi.tuni.csgr.components;

import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static fi.tuni.csgr.utils.DatePickerUtils.restrictDatePicker;

/**
 * Component with DatePicker control
 */

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

    @Override
    public ArrayList<String> getSelectionData() {
        ArrayList<String> data = new ArrayList<>();
        data.add(control.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
        return data;
    }

    @Override
    public void setSelectionData(ArrayList<String> data) {
        LocalDate date = LocalDate.parse(data.get(0), DateTimeFormatter.ISO_LOCAL_DATE);
        control.setValue(date);
    }

    public void limitDatePicker(LocalDate minDate, LocalDate maxDate) {
        restrictDatePicker(control, minDate, maxDate);
    }

    public LocalDate getDate() {
        return control.getValue();
    }
}
