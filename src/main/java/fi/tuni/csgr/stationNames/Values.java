package fi.tuni.csgr.stationNames;

import java.time.LocalDate;

public class Values {
    private final String variableName;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Values(String variableName_, LocalDate periodStart_, LocalDate periodEnd_) {
        this.variableName = variableName_;
        this.startDate = periodStart_;
        this.endDate = periodEnd_;
    }

    public String getVariableName() {
        return this.variableName;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }
}
