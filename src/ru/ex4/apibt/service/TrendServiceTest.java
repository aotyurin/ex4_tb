package ru.ex4.apibt.service;

import org.junit.Test;
import ru.ex4.apibt.dto.TrendType;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TrendServiceTest {

    @Test
    public void testExtractType() throws Exception {
        testEmpty();
        testOnUpward();
        testDownward();
        testFlat();
        testFlat2();
    }

    private void testEmpty() throws IOException {
        ArrayList<Float> lastTradeList = new ArrayList<>();
        TrendType trendType = TrendService.extractType(lastTradeList);
        assertNull(trendType);
    }

    private void testOnUpward() throws IOException {
        ArrayList<Float> lastTradeList1 = new ArrayList<>();
        lastTradeList1.add(10F);
        lastTradeList1.add(12F);
        lastTradeList1.add(18F);
        TrendType trendType1 = TrendService.extractType(lastTradeList1);
        assertTrue(trendType1 == TrendType.upward);
    }

    private void testDownward() throws IOException {
        ArrayList<Float> lastTradeList2 = new ArrayList<>();
        lastTradeList2.add(19F);
        lastTradeList2.add(12F);
        lastTradeList2.add(8F);
        TrendType trendType2 = TrendService.extractType(lastTradeList2);
        assertTrue(trendType2 == TrendType.downward);
    }

    private void testFlat() throws IOException {
        ArrayList<Float> lastTradeList3 = new ArrayList<>();
        lastTradeList3.add(25F);
        lastTradeList3.add(18F);
        lastTradeList3.add(20F);

        TrendType trendType3 = TrendService.extractType(lastTradeList3);
        assertTrue(trendType3 == TrendType.flat);
    }

    private void testFlat2() throws IOException {
        ArrayList<Float> lastTradeList3 = new ArrayList<>();
        lastTradeList3.add(20F);
        lastTradeList3.add(20F);
        lastTradeList3.add(20F);

        TrendType trendType3 = TrendService.extractType(lastTradeList3);
        assertTrue(trendType3 == TrendType.flat);
    }
}