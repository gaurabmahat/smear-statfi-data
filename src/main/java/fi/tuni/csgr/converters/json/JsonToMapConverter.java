package fi.tuni.csgr.converters.json;

import com.google.gson.Gson;

import java.util.Map;

public class JsonToMapConverter {

    public static Map convert(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Map.class);
    }
}
