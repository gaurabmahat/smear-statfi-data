package fi.tuni.csgr.components;

import fi.tuni.csgr.components.ControlComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class to hold all ControlComponents related to a specific search, with functionality
 * to get and set selection data for all components.
 */
public class ControlPanel {
    private LinkedHashMap<String, ControlComponent> controls;

    public ControlPanel() {
        controls = new LinkedHashMap<>();
    }

    /**
     * Add control to map
     * @param name key
     * @param control control to add
     */
    public void addControl(String name, ControlComponent control) {
        controls.put(name, control);
    }

    /**
     * Returns a list of all components for drawing on the screen.
     * @return
     */
    public ArrayList<ControlComponent> getControlComponents() {
        return new ArrayList<>(controls.values());
    }

    /**
     * Returns a map, mapping name of component to a list of all selections in component. Used for saving query.
     *
     * @return
     */
    public HashMap<String, ArrayList<String>> getSelectionData() {
        HashMap<String, ArrayList<String>> data = new HashMap<>();
        controls.keySet().forEach(key -> {
            data.put(key, controls.get(key).getSelectionData());
        });
        return data;
    }

    /**
     * Set selections in all components. Used when loading a saved query.
     *
     * @param data maps name of component to list of all selections in component
     */
    public void setSelectionData(Map<String, ArrayList<String>> data) {
        for (Map.Entry<String, ControlComponent> controlEntry: controls.entrySet()){
            String controlId = controlEntry.getKey();
            ControlComponent component = controlEntry.getValue();
            component.setSelectionData(data.get(controlId));
        }
    }
}
