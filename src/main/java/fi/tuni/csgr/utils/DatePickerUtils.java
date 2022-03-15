package fi.tuni.csgr.utils;

import java.time.LocalDate;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

/**
 *
 * @author Roger Wanamo
 */
public class DatePickerUtils {
    /**
     * Restricts selectable dates on datePicker to allow only dates from minDate to maxDate.
     * @param datePicker
     * @param minDate
     * @param maxDate
     */
    public static void restrictDatePicker(DatePicker datePicker, LocalDate minDate, LocalDate maxDate) {
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(minDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }else if (item.isAfter(maxDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
    }
}
