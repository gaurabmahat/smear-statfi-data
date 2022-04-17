package fi.tuni.csgr.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * List of possible queries to populate dropdown for selecting queries.
 */
public class QueriesInfo {
    public static final ArrayList<String> queryList = new ArrayList<>(Arrays.asList("Smear", "Statfi", "Smear and Statfi"));

    public static final Map<String, ArrayList<String>> queryMap = Map.of(
            "Smear", new ArrayList(Arrays.asList("Smear")),
            "Statfi",new ArrayList(Arrays.asList("Statfi")),
            "Smear and Statfi", new ArrayList(Arrays.asList("Smear", "Statfi"))
    );
}


