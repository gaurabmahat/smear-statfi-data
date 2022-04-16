package fi.tuni.csgr.query;

import java.util.HashMap;

/**
 * Factory that creates and provides singleton instances of different types of Query-objects.
 */
public class QuerySingletonFactory {
    private static HashMap<String, Query> queries = new HashMap<>();

    /**
     * Looks for requested query in queries-map and creates a new query if not yet present.
     *
     * @param queryType The type of query to get
     * @return existing query if already instanciated, otherwise a new query. Returns null if queryType not valid.
     */
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
