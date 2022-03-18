package fi.tuni.csgr.network;

import fi.tuni.csgr.managers.graphs.GraphDataManager;

import java.time.LocalDateTime;
import java.util.List;

public abstract class Network {
    protected GraphDataManager graphDataManager;

    public Network(GraphDataManager graphDataManager){
        this.graphDataManager = graphDataManager;
    }

    public void getData(
            LocalDateTime from,
            LocalDateTime to,
            String aggregation,
            List<String> gases,
            List<String> stations
    ){}
}
