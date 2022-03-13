package fi.tuni.csgr.converters.json;

import java.util.List;

public interface JsonToResultConverter {
    List<Result> convert(String json);
}

