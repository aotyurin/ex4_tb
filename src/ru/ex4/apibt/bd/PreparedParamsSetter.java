package ru.ex4.apibt.bd;



import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class PreparedParamsSetter {

    List<PreparedParams> preparedParamsList = new ArrayList<>();

    public void setValues(String name, Object value) {
        Assert.assertNotNull(name);
        Assert.assertNotNull(value);

        preparedParamsList.add(new PreparedParams(name, value));
    }

    public List<PreparedParams> getPreparedParamsList() {
        return preparedParamsList;
    }
}
