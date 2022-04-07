package fi.tuni.csgr.components;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class SelectableCell {
    private StackPane cellPane;
    private int value;

    public SelectableCell(int val) {
        cellPane = new StackPane();
        this.value = val;
        cellPane.getStyleClass().add("year-picker-cell");
        cellPane.getChildren().add(new Text(Integer.toString(val)));
    }

    public StackPane getCellPane() {
        return cellPane;
    }

    public int getValue() {
        return value;
    }

    public void selectCell() {
        if (!cellPane.getStyleClass().contains("year-picker-selected-cell")) {
            cellPane.getStyleClass().add("year-picker-selected-cell");
        }
    }

    public void deselectCell() {
        cellPane.getStyleClass().remove("year-picker-selected-cell");
    }
}
