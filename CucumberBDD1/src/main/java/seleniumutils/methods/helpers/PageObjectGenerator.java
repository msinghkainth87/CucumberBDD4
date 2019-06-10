package seleniumutils.methods.helpers;

import seleniumutils.methods.PageObject;

import java.util.HashMap;

public class PageObjectGenerator {
    public static HashMap<String,PageObject> pageObjects;
    static String elementName;
    static String access_name;
    static String type;
    //TODO Add pageobject specific methods here

   /* public static HashMap<String, By> generatePageObjects(){
        for(String k: flatData.keySet()){
            int period = k.lastIndexOf("\\.");
            elementName = k.substring(0,period);
            access_name=flatData.get(k);
            type= k.replaceAll(".*\\.","");
        }
    }
   /* public static HashMap<String, HashMap<String, HashMap<String, String>>> getPageObjects() {
        pageObjects = ApachePOIExcel.getXlData();
        return pageObjects;
    }

    public static String[] getPageObject(String sheetName,String field) {
        pageObjects= ApachePOIExcel.getXlData();
        HashMap<String,String> username = pageObjects.get(sheetName.toLowerCase()).get(field.toLowerCase());
        pageObject[0]=username.keySet().toArray()[0].toString();
        pageObject[1]=username.get(pageObject[0]);
        return pageObject;
    }*/
}
