package seleniumutils.methods;

import env.DriverUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import env.Log4j;
public class GetElementUsingBy implements Log4j
{
	protected WebDriver driver;
	protected WebDriverWait wait;
	public GetElementUsingBy(){
		try {
			driver = DriverUtil.getDefaultDriver();
			wait = new WebDriverWait(driver, 30);
		}catch (Exception e){log.error(e.getMessage());}
	}
	/**Method to select element 'by' type
	 * @param attr : String : 'By' type
	 * @param access_name : String : Locator value
	 * @return By
	 */
	public By getElementByAttributes(String attr, String access_name)
	{
		try {
			switch (attr.toLowerCase()) {
				case "id":
					return By.id(access_name);
				case "name":
					return By.name(access_name);
				case "class":
					return By.className(access_name);
				case "xpath":
					return By.xpath(access_name);
				case "css":
					return By.cssSelector(access_name);
				case "linkText":
					return By.linkText(access_name);
				case "partialLinkText":
					return By.partialLinkText(access_name);
				case "tagName":
					return By.tagName(access_name);
				default:
					return null;
			}
		}catch (Exception e){log.error(e.getMessage());
		log.error("Locator not found");
		return null;}
	}

	/**
	 * Identifies the type of element passed.
	 *
	 * @param attr        the attr
	 * @param access_name the access name
	 * @return the element tag/type
	 */
	public String getElementType(String attr, String access_name)
	{
		try {
			String tagName = driver.findElement(getElementByAttributes(attr, access_name)).getTagName();
			switch (tagName.toLowerCase()) {
				case "input":
					String elementType = driver.findElement(getElementByAttributes(attr, access_name)).getAttribute("type");
					switch (elementType.toLowerCase()) {
						case "tel":
							return "textbox";
						case "time":
							return "textbox";
						case "url":
							return "textbox";
						case "search":
							return "textbox";
						case "number":
							return "textbox";
						case "email":
							return "textbox";
						case "datetime-local":
							return "textbox";
						case "date":
							return "textbox";
						case "week":
							return "textbox";
						case "month":
							return "textbox";
						case "password":
							return "textbox";
						case "text":
							return "textbox";

						case "color":
							return "button";
						case "button":
							return "button";
						case "checkbox":
							return "button";
						case "radio":
							return "button";
						case "reset":
							return "button";
						case "submit":
							return "button";
						default:
							return "button";
					}
				default:
					return tagName;
			}
		}catch (Exception e){log.error(e.getMessage());
		return  driver.findElement(getElementByAttributes(attr, access_name)).getTagName();}
 	}
}
