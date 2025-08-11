package utilities;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class DataUtility {
    private static final String TEST_DATA_PATH = "src/test/resources/TestData/";

    public static String getJsonData(String fileName, String field) {
        try {
            FileReader reader = new FileReader(TEST_DATA_PATH + fileName + ".json");
            JsonElement jsonElement = JsonParser.parseReader(reader);
            return jsonElement.getAsJsonObject().get(field).getAsString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //T000: reading data from properties file

    public static String getPropertyValue(String fileName, String key) throws IOException {
        Properties properties = new Properties();

        FileInputStream input = new FileInputStream(TEST_DATA_PATH + fileName + ".properties");
        properties.load(input);
        return properties.getProperty(key);

    }

}
