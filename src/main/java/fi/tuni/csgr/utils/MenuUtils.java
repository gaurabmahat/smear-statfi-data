package fi.tuni.csgr.utils;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.CheckBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuButton;

/**
 *
 * @author Roger Wanamo
 */
public class MenuUtils {

    /**
     * Creates CustomMenuItems containing a checkbox for each String in itemList
     * and adds them to sourceMenu.
     *
     * @param itemList List of items to be added as menu items.
     * @param sourceMenu MenuButton to add menu items to.
     * @param defaultText Text that shows on MenuButton when nothing selected.
     * @return ArrayList containing all selected MenuItems.
     */
    public static ArrayList<String> createCheckboxMenuItems(List<String> itemList, MenuButton sourceMenu, String defaultText) {

        ArrayList<String> selectedItems = new ArrayList<>();

        for (String item : itemList) {
            CheckBox cb = new CheckBox(item);
            cb.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue) {
                    selectedItems.add(item);
                } else {
                    selectedItems.remove(item);
                }
                if (selectedItems.isEmpty()) {
                    sourceMenu.setText(defaultText);
                }
                else {
                    String items = "";
                    for (String s : selectedItems) {
                        items = items + s + " ";
                    }
                    sourceMenu.setText(items);
                }
            });

            CustomMenuItem menuItem = new CustomMenuItem(cb);
            menuItem.setHideOnClick(false);
            sourceMenu.getItems().add(menuItem);
        }

        return selectedItems;
    }
}
