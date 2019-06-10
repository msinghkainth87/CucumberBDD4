package seleniumutils.methods.helpers;

import com.github.javafaker.Faker;
import seleniumutils.methods.ApachePOIExcel;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class DataUtils {
    static String[] pageObject={"",""};

    public static HashMap<String,HashMap<String,HashMap<String, String>>> pageObjects;
    public static HashMap<String,HashMap<String,HashMap<String, String>>> testData;

    public static HashMap<String, HashMap<String, HashMap<String, String>>> getPageObjects() {
        pageObjects = ApachePOIExcel.getXlData();
        return pageObjects;
    }

    public static String[] getPageObject(String sheetName,String field) {
        pageObjects= ApachePOIExcel.getXlData();
        HashMap<String,String> username = pageObjects.get(sheetName.toLowerCase()).get(field.toLowerCase());
        pageObject[0]=username.keySet().toArray()[0].toString();
        pageObject[1]=username.get(pageObject[0]);
        return pageObject;
    }

    public static String generateFakeData(String dataValue){
        if (!(dataValue.startsWith("<%") && dataValue.endsWith("%>"))) {
            return dataValue;
        }
        String fakerType = HelperUtils.stringFetch(dataValue,"<%([^:]*):.*");
        String dataExpression = dataValue.substring(8, dataValue.length() - 2);
        Faker faker=new Faker(Locale.US, new Random());
        String result=null;
        switch(fakerType.toLowerCase()) {
            case "regex":
                result = faker.regexify(dataExpression);
                String x = faker.getClass().getDeclaredMethods().toString();
                break;
            case "numerify":
                result = faker.numerify(dataExpression);
                break;
            case "letterify":
                result = faker.letterify(dataExpression);
                break;
            default:
                if(fakerType.contains("faker.")) {
                    try {
                        String[] classDetails = fakerType.split("\\.");
                        Object prefinal = faker.getClass().getDeclaredMethod(classDetails[1]).invoke(faker);
                        Method m = prefinal.getClass().getDeclaredMethod(classDetails[2]);
                        result=m.invoke(prefinal).toString();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                else
                    result=dataValue;
        }
        return result;
    }
}
