package ru.ex4.apibt.bd;

class PreparedParams {
    private String name;
    private String value;

    PreparedParams(String name, Object value) {
        this.name = name;
        if (value instanceof String) {
            this.value = "'" + value + "'";
        } else {
            this.value = value.toString();
        }
    }

    String getName() {
        return name;
    }

    String getValue() {
        return value;
    }
}
