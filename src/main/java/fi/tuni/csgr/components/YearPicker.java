package fi.tuni.csgr.components;

import javafx.scene.control.CustomMenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class YearPicker extends CustomMenuItem {
    private VBox yearGrid;
    private static final int COLUMNS = 4;
    private int selectionStart;
    private int selectionEnd;
    private ArrayList<SelectableCell> cells;

    /**
     * Creates a VBox populated with a grid of selectable cells, each cell representing a year. Cells can be selected
     * by clicking or dragging with the mouse.
     *
     * @param from from year
     * @param to to year
     */
    public YearPicker(int from, int to) {
        yearGrid = new VBox();
        cells = new ArrayList<>();

        int colIterator = 0;
        HBox row = new HBox();
        for (int i = from ; i <= to ; i++) {
            SelectableCell newCell = new SelectableCell(i);
            cells.add(newCell);
            row.getChildren().add(newCell);
            colIterator++;
            if (colIterator >= COLUMNS) {
                yearGrid.getChildren().add(row);
                row = new HBox();
                colIterator = 0;
            }
        }
        if (colIterator > 0) {
            yearGrid.getChildren().add(row);
        }
        this.setContent(yearGrid);
        addMouseListeners();
    }

    /**
     * Add listeners to select a single cell when clicking a cell and a range of cells when dragging.
     */
    private void addMouseListeners() {
        yearGrid.setOnDragDetected((MouseEvent e) -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                e.consume();
                yearGrid.startFullDrag();
            }
        });

        cells.forEach((cell) -> {
            cell.setOnMousePressed((MouseEvent me) -> {
                selectionStart = cell.getValue();
                selectionEnd = cell.getValue();
                updateCells();
            });

            cell.setOnMouseDragEntered((MouseEvent me) -> {
                selectionEnd = cell.getValue();
                updateCells();
            });
        });
    }

    /**
     * Updates all cells in the grid to reflect current selection.
     */
    private void updateCells() {
        int start = Math.min(selectionStart, selectionEnd);
        int end = Math.max(selectionStart, selectionEnd);
        cells.forEach((cell) -> {
            int value = cell.getValue();
            if  (value >= start && value <= end) {
                cell.selectCell();
            }
            else
                cell.deselectCell();
        });
    }
}
