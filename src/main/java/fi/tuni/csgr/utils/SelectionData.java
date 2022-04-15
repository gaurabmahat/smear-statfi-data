package fi.tuni.csgr.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class SelectionData {
    String fromDate;
    String toDate;
    Map<String, ArrayList<String>> searchData;
    Type type;

    public enum Type {SMEAR, Statfi};

    public SelectionData(Type type, LocalDate fromDate, LocalDate toDate, Map<String, ArrayList<String>> searchData) {
        this.type = type;
        this.fromDate = String.valueOf(fromDate);
        this.toDate = String.valueOf(toDate);
        this.searchData = searchData;
    }

    public Type getType() {
        return type;
    }

    public LocalDate getFromDate() {
        return LocalDate.parse(fromDate);
    }

    public LocalDate getToDate() {
        return LocalDate.parse(toDate);
    }

    public Map<String, ArrayList<String>> getSearchData() {
        return searchData;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public void setSearchData(Map<String, ArrayList<String>> searchData) {
        this.searchData = searchData;
    }
}
