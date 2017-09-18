package ru.ex4.apibt.bd;



import org.junit.Assert;
import java.util.ArrayList;
import java.util.List;

public class PreparedParamsSetter {

    List<PreparedParams> preparedParams = new ArrayList<>();

    public void setValues(String name, Object value) {
        Assert.assertNotNull(name);
        Assert.assertNotNull(value);

        preparedParams.add(new PreparedParams(name, value));
    }

    List<PreparedParams> getPreparedParams() {
        return preparedParams;
    }
}
