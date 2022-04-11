package fi.tuni.csgr.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.control.CheckBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuButton;

/**
 *
 * @author Roger Wanamo
 */
public class CheckBoxMenu extends MenuButton {


    /**
     * Creates CustomMenuItems containing a checkbox for each String in itemList
     * and adds them to sourceMenu.
     *
     * @param itemList List of items to be added as menu items.
     * @param sourceMenu MenuButton to add menu items to.
     * @param defaultText Text that shows on MenuButton when nothing selected.
     * @return ArrayList containing all selected MenuItems.
     */

    private ArrayList<String> selectedItems;
    private HashMap<String, CheckBox> checkBoxes;
    private String defaultText;

    public CheckBoxMenu(List<String> itemList, String defaultText) {

        selectedItems = new ArrayList<>();
        checkBoxes = new HashMap<String, CheckBox>();
        this.defaultText = defaultText;
        this.setText(defaultText);
        this.setPrefWidth(100);

        for (String item : itemList) {
            CheckBox cb = new CheckBox(item);
            cb.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue) {
                    selectedItems.add(item);
                } else {
                    selectedItems.remove(item);
                }
                if (selectedItems.isEmpty()) {
                    this.setText(defaultText);
                }
                else {
                    this.setText(String.join(" ", selectedItems));
                }
            });

            checkBoxes.put(item, cb);
            CustomMenuItem cbItem = new CustomMenuItem(cb);
            cbItem.setHideOnClick(false);
            this.getItems().add(cbItem);
        }
    }

    public void clearSelections() {
        checkBoxes.values().forEach(cb -> cb.setSelected(false));
    }

    public boolean setSelected(String item) {
        CheckBox cb = checkBoxes.get(item);
        if (cb == null) {
            return false;
        }
        cb.setSelected(true);
        return true;
    }

    public ArrayList<String> getSelectedItems() {
        return selectedItems;
    }
}
