package ru.ex4.apibt.extermod.processing;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.*;


public class JSONProcessor<T> {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public List<T> process(Class<T> clazz, String jsonString) throws IOException {
        List<T> tradeDtos = new ArrayList<>();

        Map tradeMap = objectMapper.readValue(jsonString, Map.class);
        for (Object keySet : tradeMap.keySet()) {
            Object o = tradeMap.get(keySet);

            if (o instanceof LinkedHashMap) {
                LinkedHashMap linkedHashMap = (LinkedHashMap) o;
                linkedHashMap.put("pair", keySet);
                String valueAsString1 = objectMapper.writer().writeValueAsString(linkedHashMap);

                T tradeDto1 = objectMapper.readValue(valueAsString1, clazz);

                tradeDtos.add(tradeDto1);
            }

            if (o instanceof ArrayList) {
                Map<String, Object> listMap = new HashMap<>();
                listMap.put("pair", keySet);
                listMap.put("values", o);

                String valueAsString = objectMapper.writer().writeValueAsString(listMap);
                T tradeDto = objectMapper.readValue(valueAsString, clazz);
                tradeDtos.add(tradeDto);
            }
        }

        return tradeDtos;
    }

    public T processSimple(Class<T> clazz, String jsonString) throws IOException {
        return objectMapper.readValue(jsonString, clazz);
    }

}
