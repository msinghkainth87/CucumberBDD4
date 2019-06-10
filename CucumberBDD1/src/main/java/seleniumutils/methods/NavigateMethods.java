package seleniumutils.methods;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class NavigateMethods extends GetElementUsingBy implements BaseTest
{
	//GetElementUsingBy eleType= new GetElementUsingBy();
	private WebElement element=null;
	private String old_win = null;
	private String lastWinHandle;

	/** Method to open link
	 * @param url : String : URL for navigation
	 */
	public void navigateTo(String url) 
	{  try {
		driver.get(url);
		log.info("Navigated to url "+ url);
	}catch (Exception e){log.error(e.getMessage());}
	}
	
	/** Method to navigate back & forward
	 * @param direction : String : Navigate to forward or backward
	 */
	public void navigate(String direction)
	{
		try {
			if (direction.equals("back")) {
				driver.navigate().back();
				log.info("Navigate Back");
			}
			else{
				driver.navigate().forward();
				log.info("Navigate forward");
			}
		}catch (Exception e){log.error(e.getMessage());}
	}
	
	/** Method to quite webdriver instance */
	public void closeDriver()
	{
		try {
			log.info("Driver closed");
			driver.close();
		}catch (Exception e){log.error(e.getMessage());}
	}
	
	/** Method to return key by OS wise
	 * @return Keys : Return control or command key as per OS
	 */
	public Keys getKey()
	{
		String os = System.getProperty("os.name").toLowerCase();
		if(os.contains("win"))
			return Keys.CONTROL;
		else if (os.contains("nux") || os.contains("nix"))
			return Keys.CONTROL;
		else if (os.contains("mac"))
			return Keys.COMMAND;
		else
			return null;
	}
	
	/** Method to zoom in/out page
	 * @param inOut : String : Zoom in or out
	 */
	public void zoomInOut(String inOut)
	{
		WebElement Sel= driver.findElement(getElementByAttributes("tagName","html"));
		if(inOut.equals("ADD"))
			Sel.sendKeys(Keys.chord(getKey(), Keys.ADD));
		else if(inOut.equals("SUBTRACT"))
			Sel.sendKeys(Keys.chord(getKey(), Keys.SUBTRACT));
		else if(inOut.equals("reset"))
			Sel.sendKeys(Keys.chord(getKey(), Keys.NUMPAD0));
	}
	
	/** Method to zoom in/out web page until web element displays
	 * @param accessType : String : Locator type (id, name, class, xpath, css)
	 * @param inOut : String : Zoom in or out
	 * @param accessName : String : Locator value
	 */
	public void zoomInOutTillElementDisplay(String accessType,String inOut,String accessName)
	{
		Actions action = new Actions(driver);
		element = wait.until(ExpectedConditions.presenceOfElementLocated(getElementByAttributes(accessType, accessName)));
		while(true)
		{
			if (element.isDisplayed())
				break;
			else
				action.keyDown(getKey()).sendKeys(inOut).keyUp(getKey()).perform();
		}
	}
	
	/** Method to resize browser
	 * @param width : int : Width for browser resize
	 * @param height : int : Height for browser resize
	 */
	public void resizeBrowser(int width, int height)
	{
		driver.manage().window().setSize(new Dimension(width,height));
	}
	
	/** Method to maximize browser	 */
	public void maximizeBrowser()
	{
		try {
			driver.manage().window().maximize();
			log.info("Browser maximized");
		}catch (Exception e){log.error(e.getMessage());}
	}
	
	/** Method to hover on element
	 * @param accessType : String : Locator type (id, name, class, xpath, css)
	 * @param accessName : String : Locator value
	 */
	public void hoverOverElement(String accessType, String accessName)
	{
		try {
			Actions action = new Actions(driver);
			element = wait.until(ExpectedConditions.presenceOfElementLocated(getElementByAttributes(accessType, accessName)));
			action.moveToElement(element).perform();
		}catch (Exception e){log.error(e.getMessage());}
	}
	
	/** Method to scroll page to particular element
	 * @param accessType : String : Locator type (id, name, class, xpath, css)
	 * @param accessName : String : Locator value
	 */
	public void scrollToElement(String accessType, String accessName)
	{
		element = wait.until(ExpectedConditions.presenceOfElementLocated(getElementByAttributes(accessType, accessName)));
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].scrollIntoView();", element);
	}
	
	/** Method to scroll page to top or end
	 * @param to : String : Scroll page to Top or End
	 * @throws Exception
	 */
	public void scrollPage(String to) throws Exception
	{
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		if (to.equals("end"))
			executor.executeScript("window.scrollTo(0,Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight));");
		else if (to.equals("top"))
            executor.executeScript("window.scrollTo(Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight),0);");
		else
			throw new Exception("Exception : Invalid Direction (only scroll \"top\" or \"end\")");
	}
	
	/**Method to switch to new window */
    public void switchToNewWindow()
    {
    	old_win = driver.getWindowHandle();
    	for(String winHandle : driver.getWindowHandles())
    		lastWinHandle = winHandle;
    	driver.switchTo().window(lastWinHandle);
    }
    
    /** Method to switch to old window */
    public void switchToOldWindow()
    {
    	driver.switchTo().window(old_win);
    }
    
    /** Method to switch to window by title
     * @param windowTitle : String : Name of window title to switch
     * @throws Exception */
    public void switchToWindowByTitle(String windowTitle) throws Exception
    {
    	//System.out.println("++"+windowTitle+"++");
    	old_win = driver.getWindowHandle();
    	boolean winFound = false;
    	for(String winHandle : driver.getWindowHandles())
    	{
    		String str = driver.switchTo().window(winHandle).getTitle();
    		//System.out.println("**"+str+"**");
    		if (str.equals(windowTitle))
    		{
    			winFound = true;
    			break;
    		}
    	}
    	if (!winFound)
    		throw new Exception("Window having title "+windowTitle+" not found");
    }

    /**Method to close new window*/
    public void closeNewWindow()
    {
    	try {
    		log.info("New Window closed");
			driver.close();
		}catch (Exception e){log.error(e.getMessage());}
    }
    
    /** Method to switch frame using web element frame
     * @param accessType : String : Locator type (index, id, name, class, xpath, css)
	 * @param accessName : String : Locator value
     * */
    public void switchFrame(String accessType, String accessName)
    {
    	try {
			if (accessType.equalsIgnoreCase("index"))
				driver.switchTo().frame(accessName);
			else {
				element = wait.until(ExpectedConditions.presenceOfElementLocated(getElementByAttributes(accessType, accessName)));
				driver.switchTo().frame(element);
			}
		}catch (Exception e){log.error(e.getMessage());}
    }
    
    /** method to switch to default content*/
    public void switchToDefaultContent()
    {
    	try {
			driver.switchTo().defaultContent();
		}catch (Exception e){log.error(e.getMessage());}
    }
}
