package fi.tuni.csgr.converters.json;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class StatfiJsonToResultConverterTest {

    ResultList resultList;

    @BeforeEach
    void convert() {
        try {
            String json = Files.readString(Path.of("src/test/java/fi/tuni/csgr/converters/json/statfi_2.json"));
            StatfiJsonToResultConverter converter = new StatfiJsonToResultConverter();
            resultList = converter.convert(json);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testResultListLen() {
        assertEquals(2, resultList.size());
    }

    @Test
    void testContainsCorrectGases() {
        assertTrue(resultList.getGases().contains("Khk_yht_index"));
        assertTrue(resultList.getGases().contains("Khk_yht_las_index"));
    }

    @Test
    void testContainsCorrectYears() {
        assertTrue(resultList.getSGResult("statfi", "Khk_yht_index").getData()
                .containsKey(Long.valueOf(2010)));
        assertTrue(resultList.getSGResult("statfi", "Khk_yht_index").getData()
                .containsKey(Long.valueOf(2011)));
    }

    @Test
    void testContainsCorrectValuesForYears() {
        assertEquals(107.4,
                resultList.getSGResult("statfi", "Khk_yht_index").getData()
                .get(Long.valueOf(2010)));
        assertEquals(96.4,
                resultList.getSGResult("statfi", "Khk_yht_index").getData()
                .get(Long.valueOf(2011)));
    }
}