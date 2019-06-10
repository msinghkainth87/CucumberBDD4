package seleniumutils.methods;

import env.DriverUtil;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class ConfigurationMethods implements BaseTest
{	  
	protected WebDriver driver;
	public static Map<String,String> configProperties;
	private static PropertyReader propertyReader;

	public ConfigurationMethods() {

		driver = DriverUtil.getDefaultDriver();
//		propertyReader=new PropertyReader("config.properties");
	}

/*	public static Map<String, String> getConfigProperties() {
		return (Map)propertyReader.getProperties();
	}*/
	/** Method to print desktop configuration	 */
	public void printDesktopConfiguration()
	{
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		System.out.println("Following are machine configurations : \n");
		System.out.println("Date (MM/DD/YYYY) and Time (HH:MM:SS) : "+dateFormat.format(cal.getTime()));
		
		Capabilities cap = (Capabilities)((RemoteWebDriver) driver).getCapabilities();
		System.out.println("Browser : "+cap.getBrowserName());
		System.out.println("Platform : "+cap.getPlatform());
	}
}
