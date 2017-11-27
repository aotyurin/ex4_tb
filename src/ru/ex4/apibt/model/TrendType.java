package ru.ex4.apibt.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TrendType {
    upward,     //восходящий, больше чем
    downward,   //нисходящий, меньше чем
    flat        //боковой   , равно или примерно равно
    ;


    public static List<TrendType> getList() {
        return new ArrayList<>(Arrays.asList(TrendType.values()));
    }
}
