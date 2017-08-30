package ru.ex4.apibt;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MainProperties {

    public static Properties getProperties() {
        final String path = "main.properties";

        try {
            Properties mainProperties = new Properties();

            FileInputStream file = new FileInputStream(path);
            mainProperties.load(file);
            file.close();

            return mainProperties;

        } catch (IOException e) {
            throw new RuntimeException("ОШИБКА: Файл свойств отсуствует!");
        }
    }

}
