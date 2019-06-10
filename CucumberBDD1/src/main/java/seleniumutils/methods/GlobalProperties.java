package seleniumutils.methods;

import java.util.Map;
import env.Log4j;
public class GlobalProperties implements Log4j{

    public static Map<String,String> pageIdentifier;

    private static Map<String,String> configProperties;
    private static final String CONFIGPATH= "src/main/resources/cucumber.properties";
    private static final String PAGEIDPATH= "src/main/resources/pageidentifiers.properties";
    private static final String SOURCEPATH=System.getProperty("user.dir")+"\\src\\";
    private static final String TARGETPATH=System.getProperty("user.dir")+"\\target\\";

        private static String currentpage;

    //Auto-instantiation of global properties
    public GlobalProperties() {
        try {
            getConfigProperties();
            getPageIdentifier();
        }catch (Exception e){log.error(e.getMessage());}
    }


    public static String getSOURCEPATH() {
        return SOURCEPATH;
    }
    public static String getTARGETPATH() {
        return TARGETPATH;
    }
    //Alternate method to get config properties
    public static Map<String, String> getConfigProperties() {
       try {
           if (configProperties == null || configProperties.size() == 0) {
               PropertyReader propertyReader = new PropertyReader(CONFIGPATH);
               configProperties = (Map) propertyReader.getProperties();
           }
           return configProperties;
       }catch (Exception e){log.error(e.getMessage());
           return configProperties;}
    }

    public static void setConfigProperties(Map<String, String> configProperties) {
        GlobalProperties.configProperties = configProperties;
    }

    //Alternate method to get pageidentifiers properties
    public static Map<String, String> getPageIdentifier() {
        if (pageIdentifier==null||pageIdentifier.size()==0) {
            PropertyReader propertyReader = new PropertyReader(PAGEIDPATH);
            pageIdentifier = (Map) propertyReader.getProperties();
        }
        return pageIdentifier;
    }

    public static void setPageIdentifier(Map<String, String> pageIdentifier) {
        GlobalProperties.pageIdentifier = pageIdentifier;
    }
}
