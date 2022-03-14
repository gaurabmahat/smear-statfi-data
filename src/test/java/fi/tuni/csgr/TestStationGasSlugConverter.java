package fi.tuni.csgr;

import fi.tuni.csgr.converters.slug.StationGasSlugConverter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStationGasSlugConverter {

    @Test
    public void testStationGasToSlugMapping(){
        assertEquals(
                "KUM_EDDY.av_c_ep",
                StationGasSlugConverter.getSlug("Kumpula", "CO2")
        );
    }
}
