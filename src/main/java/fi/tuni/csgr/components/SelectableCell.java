package fi.tuni.csgr.components;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class SelectableCell extends StackPane{
    private int value;

    public SelectableCell(int val) {
        this.value = val;
        this.getStyleClass().add("year-picker-cell");
        this.getChildren().add(new Text(Integer.toString(val)));
    }

    public int getValue() {
        return value;
    }

    public void selectCell() {
        if (!this.getStyleClass().contains("year-picker-selected-cell")) {
            this.getStyleClass().add("year-picker-selected-cell");
        }
    }

    public void deselectCell() {
        this.getStyleClass().remove("year-picker-selected-cell");
    }
}
