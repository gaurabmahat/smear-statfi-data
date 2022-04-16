package fi.tuni.csgr.components;

import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class YearPicker extends MenuButton {
    private VBox yearGrid;
    private static final int COLUMNS = 4;
    private int selectionStart;
    private int selectionEnd;
    private int startYear;
    private int endYear;
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
        setRange(from, to);
        CustomMenuItem menuItem = new CustomMenuItem(yearGrid);
        menuItem.setHideOnClick(false);
        menuItem.getStyleClass().add("year-picker-menu-item");

        this.getItems().add(menuItem);
        addGridMouseListeners();
    }

    public void setRange(int from, int to) {
        yearGrid.getChildren().clear();
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
            addCellMouseListeners(newCell);
        }
        if (colIterator > 0) {
            yearGrid.getChildren().add(row);
        }
        selectionStart = Integer.MIN_VALUE;
        selectionEnd = Integer.MIN_VALUE;
        updateCells();
    }

    /**
     * Add listeners to cell to select cell on click and handle drag.
     * @param cell
     */
    private void addCellMouseListeners(SelectableCell cell) {
        cell.setOnMousePressed((MouseEvent me) -> {
            selectionStart = cell.getValue();
            selectionEnd = cell.getValue();
            updateCells();
        });

        cell.setOnMouseDragEntered((MouseEvent me) -> {
            selectionEnd = cell.getValue();
            updateCells();
        });
    }

    /**
     * Add listener to activate dragging.
     */
    private void addGridMouseListeners() {
        yearGrid.setOnDragDetected((MouseEvent e) -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                e.consume();
                yearGrid.startFullDrag();
            }
        });
    }

    /**
     * Updates all cells in the grid to reflect current selection.
     */
    private void updateCells() {
        startYear = Math.min(selectionStart, selectionEnd);
        endYear = Math.max(selectionStart, selectionEnd);
        cells.forEach((cell) -> {
            int value = cell.getValue();
            if  (value >= startYear && value <= endYear) {
                cell.selectCell();
            }
            else
                cell.deselectCell();
        });
        if (startYear > Integer.MIN_VALUE) {
            if (startYear == endYear) {
                this.setText(String.valueOf(startYear));
            } else {
                this.setText(startYear + "-" + endYear);
            }
        }
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setSelection(int start, int end) {
        selectionStart = start;
        selectionEnd = end;
        updateCells();
    }
}
