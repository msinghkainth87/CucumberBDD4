package seleniumutils.methods;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    private Properties properties = new Properties();
    InputStream inputStream = null;

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public PropertyReader(String prop) {
        loadProperties(prop);
    }

    private void loadProperties(String prop) {
        try {
            inputStream = new FileInputStream(prop);
            //inputStream = new FileInputStream("src/main/java/config.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readProperty(String key) {
        return properties.getProperty(key);
    }
}
