package fi.tuni.csgr.converters.json;

import java.util.List;

public interface JsonToResultConverter {
    ResultList convert(String json);
}

