package fi.tuni.csgr.query;

import java.util.HashMap;

public class QuerySingletonFactory {
    private static HashMap<String, Query> queries = new HashMap<>();

    public Query getInstance(String queryType) {
        if (!queries.containsKey(queryType)) {
            if (queryType == "Smear") {
                queries.put(queryType, new SmearQuery());
            }
            else if (queryType == "Statfi") {
                queries.put(queryType, new StatfiQuery());
            }
        }
        return queries.get(queryType);
    }
}
