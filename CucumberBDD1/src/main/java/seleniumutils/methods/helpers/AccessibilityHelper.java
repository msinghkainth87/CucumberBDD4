package seleniumutils.methods.helpers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import seleniumutils.methods.GlobalProperties;
import seleniumutils.methods.JavascriptHandlingMethods;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertTrue;

public class AccessibilityHelper {

    private WebDriver driver;
    private JavascriptExecutor js;
    private static final URL scriptUrl = AccessibilityHelper.class.getResource("/axe.min.js");
    private ArrayList<LinkedHashMap<String,String>> messageList;
    private HashMap<String,ArrayList<LinkedHashMap<String,String>>> htmlcsResult;
    public AccessibilityHelper() throws IOException {
        this.driver = JavascriptHandlingMethods.driver;
        js = JavascriptHandlingMethods.js;
        htmlcsResult= new HashMap<>();
    }

    public void runCodeSniffer() throws IOException {
        messageList=new ArrayList<>();
        LinkedHashMap<String,String> message;
        String jquery_content = Jsoup.connect("http://squizlabs.github.io/HTML_CodeSniffer/build/HTMLCS.js").ignoreContentType(true).execute().body();
        js.executeScript(jquery_content);
        js.executeScript("window.HTMLCS_RUNNER.run('WCAG2AA');");
        LogEntries logs = driver.manage().logs().get("browser");
        String[] msg;

        for (LogEntry entry : logs) {
            if(!entry.getMessage().contains("console-api "))
                continue;
            String sentinel=entry.getMessage().replaceFirst("console-api ([\\d:])+ ","");
             if(sentinel.equalsIgnoreCase("\"done\"")){
                 break;
             }
             message = new LinkedHashMap<>();
             String text=entry.getMessage().replaceFirst("^\"(.*)\"$","$1");
             msg=text.split("\\|");
             message.put("Type",msg[0].replaceFirst("^(.*)\\[HTMLCS\\] ",""));
             message.put("Code",msg[1]);
             message.put("Node",msg[2]);
             message.put("elementID",msg[3]);
             message.put("message",msg[4]);
             message.put("HTML",HelperUtils.removeUTFCharacters(msg[5]).toString().replace("\\", ""));
             messageList.add(message);
        }

        //convert to json
        ObjectMapper objectMapper = new ObjectMapper();
        DateTimeHelper dateTimeHelper = new DateTimeHelper();
        String timeStamp=dateTimeHelper.getTimeStamp();
        htmlcsResult=objectMapper.readValue(JSONFileWriter.JSONRead(GlobalProperties.getTARGETPATH()+"cucumber-html-reports\\","HTMLCS.json").toString(),new TypeReference<HashMap<String,ArrayList<LinkedHashMap<String,String>>>>(){});
        htmlcsResult.put(timeStamp,messageList);

        String jsonObject = objectMapper.writeValueAsString(htmlcsResult);
        JSONFileWriter.JSONWrite(jsonObject,GlobalProperties.getTARGETPATH()+"cucumber-html-reports\\","HTMLCS.json");
    }

    public String testAccessibility() {
        String result="";
        JSONObject responseJSON = new AXE.Builder(driver, scriptUrl).analyze();
        JSONArray violations = responseJSON.getJSONArray("violations");

        if (violations.length() == 0) {
            result="No violations found";
        } else {
            AXE.writeResults(GlobalProperties.getTARGETPATH()+"cucumber-html-reports\\testAccessibility", responseJSON);
            result=AXE.report(violations);
            JSONFileWriter.JSONWrite(result, GlobalProperties.getTARGETPATH()+"cucumber-html-reports\\","aXe.json");
        }
        return result;
    }

    public void testAccessibilityWithSkipFrames() {
        driver.get("http://google.com");
        JSONObject responseJSON = new AXE.Builder(driver, scriptUrl)
                .skipFrames()
                .analyze();

        JSONArray violations = responseJSON.getJSONArray("violations");

        if (violations.length() == 0) {
            assertTrue("No violations found", true);
        } else {
            AXE.writeResults("testAccessibilityWithSkipFrames", responseJSON);
            assertTrue(AXE.report(violations), false);
        }
    }

    /**
     * Test with options
     */

    public void testAccessibilityWithOptions() {
        driver.get("http://google.com");
        JSONObject responseJSON = new AXE.Builder(driver, scriptUrl)
                .options("{ rules: { 'accesskeys': { enabled: false } } }")
                .analyze();

        JSONArray violations = responseJSON.getJSONArray("violations");

        if (violations.length() == 0) {
            assertTrue("No violations found", true);
        } else {
            AXE.writeResults("testAccessibilityWithOptions", responseJSON);

            assertTrue(AXE.report(violations), false);
        }
    }

    /**
     * Test a specific selector or selectors
     */

    public void testAccessibilityWithSelector() {
        driver.get("http://google.com");
        JSONObject responseJSON = new AXE.Builder(driver, scriptUrl)
                .include("title")
                .include("p")
                .analyze();

        JSONArray violations = responseJSON.getJSONArray("violations");

        if (violations.length() == 0) {
            assertTrue("No violations found", true);
        } else {
            AXE.writeResults("testAccessibilityWithSelector", responseJSON);

            assertTrue(AXE.report(violations), false);
        }
    }

    /**
     * Test includes and excludes
     */

    public void testAccessibilityWithIncludesAndExcludes() {
        driver.get("http://google.com");
        JSONObject responseJSON = new AXE.Builder(driver, scriptUrl)
                .include("div")
                .exclude("h1")
                .analyze();

        JSONArray violations = responseJSON.getJSONArray("violations");

        if (violations.length() == 0) {
            assertTrue("No violations found", true);
        } else {
            AXE.writeResults("testAccessibilityWithIncludesAndExcludes", responseJSON);
            assertTrue(AXE.report(violations), false);
        }
    }

    /**
     * Test a WebElement
     */

    public void testAccessibilityWithWebElement() {
        driver.get("http://google.com");

        JSONObject responseJSON = new AXE.Builder(driver, scriptUrl)
                .analyze(driver.findElement(By.tagName("p")));

        JSONArray violations = responseJSON.getJSONArray("violations");

        if (violations.length() == 0) {
            assertTrue("No violations found", true);
        } else {
            AXE.writeResults("testAccessibilityWithWebElement", responseJSON);
            assertTrue(AXE.report(violations), false);
        }
    }
}
