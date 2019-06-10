package seleniumutils.methods;

import env.DriverUtil;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class JavascriptHandlingMethods implements BaseTest {
	public static WebDriver driver = DriverUtil.getDefaultDriver();
	public static JavascriptExecutor js = (JavascriptExecutor) driver;
	/**Method to handle alert
	 * @param decision : String : Accept or dismiss alert
	 */
	public void handleAlert(String decision)
	{
		if(decision.equals("accept"))
			driver.switchTo().alert().accept();
		else
			driver.switchTo().alert().dismiss();
	}

	public static void flashfill(WebElement element, String text){
		js.executeScript("arguments[0].setAttribute('value', arguments[1])", element,text);
	}

	public static void highlightElementInControl(WebElement element,String color) {
		try{
			js.executeScript("arguments[0].setAttribute('style','background:"+color+"; border: 0px solid blue;');",element);
			Thread.sleep(50);
			js.executeScript("arguments[0].setAttribute('style','background:white; border: 0px solid blue;');",element);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static Object runFromFile(String jsPath,WebDriver driver) {
		ArrayList<HashMap<String,String>> result=null;
		Object resultq=null;
		try {
			String content = readFile(jsPath, Charset.defaultCharset());
			resultq= js.executeScript(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	static String readFile(String path, Charset encoding)
			throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

}
