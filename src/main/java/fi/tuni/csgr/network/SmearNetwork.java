package fi.tuni.csgr.network;

import fi.tuni.csgr.smearAndStatfi.SMEAR.fetchSeriesDataFromSmear.dataFromSmear;
import fi.tuni.csgr.converters.json.JsonToResultConverter;
import fi.tuni.csgr.converters.json.ResultList;
import fi.tuni.csgr.converters.json.SmearJsonToResultConverter;
import fi.tuni.csgr.managers.graphs.GraphDataManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SmearNetwork extends Network {

    public SmearNetwork(GraphDataManager graphDataManager){
        super(graphDataManager);
    }

    @Override
    public void getData(
            LocalDateTime from,
            LocalDateTime to,
            String aggregation,
            List<String> gases,
            List<String> stations
    ){
        dataFromSmear smearDataFetcher = new dataFromSmear(
                from.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                to.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                aggregation,
                gases,
                stations
        );
        JsonToResultConverter resultConverter = new SmearJsonToResultConverter();
        String jsonResult = smearDataFetcher.getDataInStringJson();
        ResultList resultList = resultConverter.convert(jsonResult);

        graphDataManager.update(resultList);
    }
}
