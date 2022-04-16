package fi.tuni.csgr.query;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * List of possible queries to populate dropdown for selecting queries.
 */
public class QueriesInfo {
    public static final ObservableList<String> queryList = FXCollections.observableArrayList("Smear", "Statfi");
}
