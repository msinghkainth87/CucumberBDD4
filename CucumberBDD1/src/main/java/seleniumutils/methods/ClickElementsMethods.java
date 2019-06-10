package seleniumutils.methods;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class ClickElementsMethods extends GetElementUsingBy implements BaseTest
{
	//GetElementUsingBy eleType= new GetElementUsingBy();
	private WebElement element=null;
	
	/** Method to click on an element
	@param accessType : String : Locator type (id, name, class, xpath, css)
	@param accessName : String : Locator value
	*/
	public void click(String accessType, String accessName)
	{
		try {
			element = wait.until(ExpectedConditions.presenceOfElementLocated(getElementByAttributes(accessType, accessName)));
			element.click();
			log.info("Element is Clicked");
		}catch (Exception e){log.error(e.getMessage());}
	}
	
	/** Method to forcefully click on an element
	@param accessType : String : Locator type (id, name, class, xpath, css)
	@param accessName : String : Locator value
	*/
	public void clickForcefully(String accessType, String accessName)
	{
		try {
			element = wait.until(ExpectedConditions.presenceOfElementLocated(getElementByAttributes(accessType, accessName)));
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", element);
			log.info("Click Forcefully Done");
		}catch (Exception e){log.error(e.getMessage());}
	}
	
	/** Method to Double click on an element
	@param accessType : String : Locator type (id, name, class, xpath, css)
	@param accessValue : String : Locator value
	*/
	public void doubleClick(String accessType, String accessValue)
	{
		try {
			element = wait.until(ExpectedConditions.presenceOfElementLocated(getElementByAttributes(accessType, accessValue)));
			Actions action = new Actions(driver);
			action.moveToElement(element).doubleClick().perform();
			log.info("Double Click Done");
		}catch (Exception e){log.error(e.getMessage());}
	}
}