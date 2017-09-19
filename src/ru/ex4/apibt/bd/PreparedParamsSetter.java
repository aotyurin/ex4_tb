package ru.ex4.apibt.bd;



import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class PreparedParamsSetter {

    List<PreparedParams> preparedParamses = new ArrayList<>();

    public void setValues(String name, Object value) {
        Assert.assertNotNull(name);
        Assert.assertNotNull(value);

        preparedParamses.add(new PreparedParams(name, value));
    }

    public List<PreparedParams> getPreparedParamses() {
        return preparedParamses;
    }
}
