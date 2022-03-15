package fi.tuni.csgr;

import fi.tuni.csgr.converters.helpers.StationGas;
import fi.tuni.csgr.converters.slug.StationGasSlugConverter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStationGasSlugConverter {

    @Test
    public void testStationGasToSlugMapping1(){
        assertEquals(
                "KUM_EDDY.av_c_ep",
                StationGasSlugConverter.getSlug("Kumpula", "CO2")
        );
    }

    @Test
    public void testStationGasToSlugMapping2(){
        assertEquals(
                "VAR_EDDY.av_c",
                StationGasSlugConverter.getSlug("Värriö", "CO2")
        );
    }

    @Test
    public void testSlugToStationGasMapping(){
        assertEquals(
                new StationGas("Kumpula", "CO2"),
                StationGasSlugConverter.getStationGas("KUM_EDDY.av_c_ep")
        );
    }
}
