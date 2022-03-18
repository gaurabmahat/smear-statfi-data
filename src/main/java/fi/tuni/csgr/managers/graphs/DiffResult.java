package fi.tuni.csgr.managers.graphs;

import java.util.List;

public class DiffResult {
    List<String> toAdd;
    List<String> toRemove;

    public List<String> getToAdd() {
        return toAdd;
    }

    public List<String> getToRemove() {
        return toRemove;
    }

    public DiffResult(List<String> addedKeys, List<String> deletedKeys) {
        this.toAdd = addedKeys;
        this.toRemove = deletedKeys;
    }
}
