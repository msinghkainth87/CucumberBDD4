package seleniumutils.reusablestepdefinitions;

import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.ScenarioImpl;
import env.DriverUtil;
import org.apache.commons.lang.ArrayUtils;
import org.openqa.selenium.*;
import seleniumutils.methods.BaseTest;
import seleniumutils.methods.GlobalProperties;
import seleniumutils.methods.PageObject;
import seleniumutils.methods.TestCaseFailed;
import seleniumutils.methods.helpers.AccessibilityHelper;
import seleniumutils.methods.helpers.HelperUtils;
import seleniumutils.methods.helpers.PageObjectGenerator;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;

public class ReusableStepDefinitions implements BaseTest {
	protected WebDriver driver = DriverUtil.getDefaultDriver();
	HashMap<String,PageObject> pageObjects = PageObjectGenerator.pageObjects;
	//HashMap<String,HashMap<String,HashMap<String, String>>> data= ApachePOIExcel.getXlData();
	//Navigation Steps

	//Step to navigate to specified URL
	@Then("^I navigate to \"([^\"]*)\"$")
	public void navigate_to(String url)
	{
		navigationObj.navigateTo(url);
	}

	//Step to navigate to URL from Test Data sheet
	@Then("^I navigate to ([^\"]*) page$")
	public void navigate_toURL(String link)
	{
		PageObject pObj= pageObjects.get(link);
		String url= pObj.getAccess_name();
		navigationObj.navigateTo(url);
	}

	//Step to navigate forward
	@Then("^I navigate forward")
	public void navigate_forward()
	{
		navigationObj.navigate("forward");
	}

	//Step to navigate backward
	@Then("^I navigate back")
	public void navigate_back()
	{
		navigationObj.navigate("back");
	}


	// steps to refresh page
	@Then("^I refresh page$")
	public void refresh_page()
	{
		driver.navigate().refresh();
	}

	// Switch between windows

	//Switch to new window
	@Then("^I switch to new window$")
	public void switch_to_new_window()
	{
		navigationObj.switchToNewWindow();
	}

	//Switch to old window
	@Then("^I switch to previous window$")
	public void switch_to_old_window()
	{
		navigationObj.switchToOldWindow();
	}

	//Switch to new window by window title
	@Then("^I switch to window with title (.+)$")
	public void switch_to_window_by_title(String title) throws Exception
	{
		PageObject pObj= pageObjects.get(title);
		String windowTitle= pObj.getAccess_name().toLowerCase();
		navigationObj.switchToWindowByTitle(windowTitle);
	}

	//Close new window
	@Then("^I close new window$")
	public void close_new_window()
	{
		navigationObj.closeNewWindow();
	}

	// Switch between frame

	// Step to switch to frame by web element
	@Then("^I switch to frame (.*)$")
	public void switch_frame_by_element(String name)
	{
		PageObject pObj= pageObjects.get(name);
		navigationObj.switchFrame(pObj.getType(),pObj.getAccess_name());
	}

	// step to switch to main content
	@Then("^I switch to main content$")
	public void switch_to_default_content()
	{
		navigationObj.switchToDefaultContent();
	}

	// To interact with browser

	// step to resize browser
	@Then("^I resize browser window size to width (\\d+) and height (\\d+)$")
	public void resize_browser(int width, int heigth)
	{
		navigationObj.resizeBrowser(width, heigth);
	}

	// step to maximize browser
	@Then("^I maximize browser window$")
	public void maximize_browser()
	{
		navigationObj.maximizeBrowser();
	}

	//Step to close the browser
	@Then("^I close browser$")
	public void close_browser()
	{
		navigationObj.closeDriver();
	}

	// zoom in/out page

	// steps to zoom in page
	@Then("^I zoom in page$")
	public void zoom_in()
	{
		navigationObj.zoomInOut("ADD");
	}

	// steps to zoom out page
	@Then("^I zoom out page$")
	public void zoom_out()
	{
		navigationObj.zoomInOut("SUBTRACT");
	}

	// zoom out webpage till necessary element displays

	// steps to zoom out till element displays
	@Then("^I zoom out page till I see element (.+)$")
	public void zoom_till_element_display(String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		navigationObj.zoomInOutTillElementDisplay(pObj.getType(),"substract", pObj.getAccess_name());
	}

	// reset webpage view use

	@Then("^I reset page view$")
	public void reset_page_zoom()
	{
		navigationObj.zoomInOut("reset");
	}

	// scroll webpage

	@Then("^I scroll to (top|end) of page$")
	public void scroll_page(String to) throws Exception
	{
		navigationObj.scrollPage(to);
	}


	// scroll webpage to specific element

	@Then("^I scroll to element (.+)$")
	public void scroll_to_element(String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		navigationObj.scrollToElement(pObj.getType(), pObj.getAccess_name());
	}

	// hover over element

	// Note: Doesn't work on Windows firefox
	@Then("^I hover over element (.+)$")
	public void hover_over_element(String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		navigationObj.hoverOverElement(pObj.getType(), pObj.getAccess_name());
	}

	//Assertion steps

	/** page title checking
	 * @param present :
	 * @param title :
	 */
	@Then("^I should\\s*((?:not)?)\\s+see page title as \"(.+)\"$")
	public void check_title(String present,String title) throws TestCaseFailed
	{
		//System.out.println("Present :" + present.isEmpty());
		assertionObj.checkTitle(title,present.isEmpty());
	}

	// step to check element partial text
	@Then("^I should\\s*((?:not)?)\\s+see page title having partial text as \"(.*?)\"$")
	public void check_partial_text(String present, String partialTextTitle) throws TestCaseFailed
	{
		//System.out.println("Present :" + present.isEmpty());
		assertionObj.checkPartialTitle(partialTextTitle, present.isEmpty());
	}

	// step to check element text
	@Then("^element (.+) should\\s*((?:not)?)\\s+have text as \"(.*?)\"$")
	public void check_element_text(String element,String present,String value) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		assertionObj.checkElementText(pObj.getType(), value, pObj.getAccess_name(),present.isEmpty());
	}

	//step to check element partial text
	@Then("^element (.+) should\\s*((?:not)?)\\s+have partial text as \"(.*?)\"$")
	public void check_element_partial_text(String element,String present,String value) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		assertionObj.checkElementPartialText(pObj.getType(), value, pObj.getAccess_name(), present.isEmpty());
	}

	// step to check attribute value
	@Then("^element (.+) should\\s*((?:not)?)\\s+have attribute \"(.*?)\" with value \"(.*?)\"$")
	public void check_element_attribute(String element,String present,String attrb,String value) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		assertionObj.checkElementAttribute(pObj.getType(), attrb, value, pObj.getAccess_name(), present.isEmpty());
	}

	// step to check element enabled or not
	@Then("^element (.+) should\\s*((?:not)?)\\s+be (enabled|disabled)$")
	public void check_element_enable(String element,String present,String state) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		boolean flag = state.equals("enabled");
		if(!present.isEmpty())
		{
			flag = !flag;
		}
		assertionObj.checkElementEnable(pObj.getType(), pObj.getAccess_name(), flag);
	}

	//step to check element present or not
	@Then("^element (.+) should\\s*((?:not)?)\\s+be present$")
	public void check_element_presence(String element,String present) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		assertionObj.checkElementPresence(pObj.getType(), pObj.getAccess_name(), present.isEmpty());
	}

	//step to assert checkbox is checked or unchecked
	@Then("^checkbox (.+) should be (checked|unchecked)$")
	public void is_checkbox_checked(String element,String state) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		boolean flag = state.equals("checked");
		assertionObj.isCheckboxChecked(pObj.getType(), pObj.getAccess_name(), flag);
	}

	//steps to assert radio button checked or unchecked
	@Then("^radio button (.+) should be (selected|unselected)$")
	public void is_radio_button_selected(String element,String state) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		boolean flag = state.equals("selected");
		assertionObj.isRadioButtonSelected(pObj.getType(), pObj.getAccess_name(), flag);
	}

	//steps to assert option by text from radio button group selected/unselected - Option by Text or Option by Value
	@Then("^option \"(.*?)\" by (.+) from radio button group (.+) should be (selected|unselected)$")
	public void is_option_from_radio_button_group_selected(String option,String attrb,String element,String state) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		boolean flag = state.equals("selected");
		assertionObj.isOptionFromRadioButtonGroupSelected(pObj.getType(),attrb,option,pObj.getAccess_name(),flag);
	}
	
	//step to assert javascript pop-up alert text
	@Then("^I should see alert text as \"(.*?)\"$")
	public void check_alert_text(String actualValue) throws TestCaseFailed
	{
		assertionObj.checkAlertText(actualValue);
	}

	// step to select dropdown list - Option by Text or Option by Value
	@Then("^option \"(.*?)\" by (.+) from dropdown (.+) should be (selected|unselected)$")
	public void is_option_from_dropdown_selected(String option,String by,String element,String state) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		boolean flag = state.equals("selected");
		assertionObj.isOptionFromDropdownSelected(pObj.getType(),by,option,pObj.getAccess_name(),flag);
	}

	//Input steps

	// enter text into input field steps
	@Then("^I enter \"([^\"]*)\" into input field (.+)$")
	public void enter_text(String text, String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());//type means text or select or any html element type
		inputObj.enterText(pObj.getType(), text, pObj.getAccess_name());
	}

	@Then("^I enter ([^\"]*) into input field (.+)$")
	public void enter_text_from_input_data(String lookup, String element) throws Exception
	{
		PageObject dObj=pageObjects.get(lookup);
		String text= dObj.getAccess_name();
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		inputObj.enterText(pObj.getType(), text, pObj.getAccess_name());
	}


	// press keyboard keys into input field steps
	@Then("^I press \"([^\"]*)\" key into input field (.+)$")
	public void enter_key(String key, String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		Keys k = HelperUtils.identifyKey(key);
		miscmethodObj.validateLocator(pObj.getType());
		inputObj.enterKeys(pObj.getType(), k, pObj.getAccess_name());
	}

	// clear input field steps
	@Then("^I clear input field (.+)$")
	public void clear_text(String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		inputObj.clearText(pObj.getType(), pObj.getAccess_name());
	}

	// select option by text/value from dropdown
	@Then("^I select \"(.*?)\" option by (index|value|text) from dropdown (.+)$")
	public void select_option_from_dropdown(String option,String optionBy,String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		optionBy=optionBy.equalsIgnoreCase("index")?"selectByIndex":optionBy;
		miscmethodObj.validateOptionBy(optionBy);
		inputObj.selectOptionFromDropdown(pObj.getType(),optionBy, option, pObj.getAccess_name());
	}

	// deselect option by text/value from multiselect
	@Then("^I deselect \"(.*?)\" option by (index|value|text) from multiselect dropdown (.+)$")
	public void deselect_option_from_multiselect_dropdown(String option,String optionBy, String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		optionBy=optionBy.equalsIgnoreCase("index")?"selectByIndex":optionBy;
		miscmethodObj.validateOptionBy(optionBy);
		inputObj.deselectOptionFromDropdown(pObj.getType(), optionBy, option, pObj.getAccess_name());
	}

	// step to select option from mutliselect dropdown list
	/*@Then("^I select all options from multiselect dropdown (.+)$")
	public void select_all_option_from_multiselect_dropdown(String element) throws Exception
	{
	miscmethod.validateLocator(type);
	//inputObj.
	//select_all_option_from_multiselect_dropdown(pObj.getType(), access_name)
	}*/

	// step to unselect option from mutliselect dropdown list
	@Then("^I deselect all options from multiselect dropdown (.+)$")
	public void unselect_all_option_from_multiselect_dropdown(String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		inputObj.unselectAllOptionFromMultiselectDropdown(pObj.getType(), pObj.getAccess_name());
	}

	//check checkbox steps
	@Then("^I check the checkbox (.+)$")
	public void check_checkbox(String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		inputObj.checkCheckbox(pObj.getType(), pObj.getAccess_name());
	}

	//uncheck checkbox steps
	@Then("^I uncheck the checkbox (.+)$")
	public void uncheck_checkbox(String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		inputObj.uncheckCheckbox(pObj.getType(), pObj.getAccess_name());
	}

	//steps to toggle checkbox
	@Then("^I toggle checkbox (.+)$")
	public void toggle_checkbox(String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		inputObj.toggleCheckbox(pObj.getType(), pObj.getAccess_name());
	}

	// step to select radio button
	@Then("^I select radio button (.+)$")
	public void select_radio_button(String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		inputObj.selectRadioButton(pObj.getType(), pObj.getAccess_name());
	}

	// steps to select option by text from radio button group
	@Then("^I select \"(.*?)\" option by (value|text) from radio button group (.+)$")
	public void select_option_from_radio_btn_group(String option,String by, String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		//miscmethodObj.validateOptionBy(optionBy);
		inputObj.selectOptionFromRadioButtonGroup(pObj.getType(), option, by, pObj.getAccess_name());
	}

	//Click element Steps

	// click on web element
	@Then("^I click on element (.+)$")
	public void click(String element) throws Exception
	{
		DriverUtil.waitForJSandJQueryToLoad();
		DriverUtil.waitForAjaxCall();
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		clickObj.click(pObj.getType(), pObj.getAccess_name());
		System.out.println("Clicked "+element+" at "+ Instant.now());

	}

	//Forcefully click on element
	@Then("^I forcefully click on element (.+)$")
	public void click_forcefully(String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		clickObj.clickForcefully(pObj.getType(),pObj.getAccess_name());
		DriverUtil.waitForAjaxCall();
	}

	// double click on web element
	@Then("^I double click on element (.+)$")
	public void double_click(String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		clickObj.doubleClick(pObj.getType(), pObj.getAccess_name());
		DriverUtil.waitForAjaxCall();
	}

	//Progress methods

	// explicit wait for specific period of time
	@Then("^I wait for (\\d+) sec$")
	public void wait(String time) throws NumberFormatException, InterruptedException
	{
		progressObj.wait(time);
	}

	@Then("^the element (.+) is displayed$")
	public void ele_display(String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		progressObj.waitForElementToDisplay(pObj.getType(), pObj.getAccess_name(), "10");
	}

	@Then("^the element (.+) is enabled$")
	public void ele_enable(String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		progressObj.waitForElementToClick(pObj.getType(), pObj.getAccess_name(), "10");
	}

	//explicit wait for specific element to display for specific period of time
	@Then("^I wait (\\d+) seconds for element (.+) to display$")
	public void wait_for_ele_to_display(String duration, String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		progressObj.waitForElementToDisplay(pObj.getType(), pObj.getAccess_name(), duration);
	}

	// explicit wait for specific element to enable for specific period of time
	@Then("^I wait (\\d+) seconds for element (.+) to be enabled$")
	public void wait_for_ele_to_click(String duration, String element) throws Exception
	{
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());
		progressObj.waitForElementToClick(pObj.getType(), pObj.getAccess_name(), duration);
	}

	//JavaScript handling steps

	//Step to handle java script
	@Then("^I accept alert$")
	public void handle_alert()
	{
		javascriptObj.handleAlert("accept");
	}

	//Steps to dismiss java script
	@Then("^I dismiss alert$")
	public void dismiss_alert()
	{
		javascriptObj.handleAlert("dismiss");
	}

	//Screen shot methods

	@Then("^I take screenshot$")
	public void take_screenshot() throws IOException
	{
		screenshotObj.takeScreenShot();
	}

	//Configuration steps

	// step to print configuration
	@Then("^I print configuration$")
	public void print_config()
	{
		configObj.printDesktopConfiguration();
	}
	@After
	public final void takeScreenShot(Scenario scenario) {
		if (scenario.isFailed()) {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File srcFile = ts.getScreenshotAs(OutputType.FILE);
			try {
				ScenarioImpl impl = (ScenarioImpl) scenario;
				/*Collection<String> tags = impl.getSourceTagNames();
				String name = "Scenario";
				for (String t : tags) {
				name += "_" + t;
				}
				*/
				String name = "Screenshots/" + impl.getId().replaceAll("\\W", "_");
				FileUtils.copyFile(srcFile, new File(name + ".png"));
			} catch (IOException ex) {
				//Logger.getLogger(SmapScenario.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
	@After
	public final void tearDown() {
		DriverUtil.closeDriver();
	}


	@Then("^get me the current page$")
	public void getCurrentPage() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		PageObject pObj = pageObjects.get("general.page_title");
		miscmethodObj.validateLocator(pObj.getType());
		String pagename = assertionObj.getElementText(pObj.getType(), pObj.getAccess_name());
	}

	@Then("^I land on (.+) page$")
	public void iLandOnPage(String pagename) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		DriverUtil.waitForJSandJQueryToLoad();
		DriverUtil.waitForAjaxCall();
		pagename=pagename.toLowerCase();
		System.out.println("before land on "+pagename+ " check time: "+ Instant.now());
		check_element_text("singlefamily.page_title","", GlobalProperties.pageIdentifier.get(pagename));
		System.out.println("after land on "+pagename+ "check time: "+ Instant.now());
		/*switch(pagename){
			case "activities":
				check_element_text("page_title",null, GlobalProperties.getPageIdentifier().get(pagename));
				break;
			case "fnolsearchorcreatepolicy":
				break;
			case "fnolbasicinformation":
				break;
			case "fnollossdetails":
				break;
			case "fnolvehicledetails":
				break;
			case "fnoldriverdetails":
				break;
			case "fnolservices":
				break;
			case "fnolsaveassignclaim":
				break;
			case "fnolclaimsaved":
				break;
		}*/

	}

	@Then("^I wait for ajax call to be completed$")
	public void iWaitForAjaxCallToBeCompleted() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		DriverUtil.waitForAjaxCall();
	}

	@Then("^I run HTMLCS accessibility test on the current page$")
	public void iValidateHTMLCS() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		AccessibilityHelper accessibilityHelper = new AccessibilityHelper();
		accessibilityHelper.runCodeSniffer();
	//	accessibilityHelper.testAccessibility();
	}
	@Then("^I run aXe accessibility test on the current page$")
	public void iValidateAXE() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		AccessibilityHelper accessibilityHelper = new AccessibilityHelper();
		//accessibilityHelper.runCodeSniffer();
		accessibilityHelper.testAccessibility();
	}
	@Then("^I run comparative accessibility tests using ([a-zA-Z0-9]*,[a-zA-Z0-9]*) tools$")
	public void iValidateAccessibility(String tools) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		tools=tools.toLowerCase();
		List<String> toolSet= Arrays.asList(tools.split(","));
		AccessibilityHelper accessibilityHelper = new AccessibilityHelper();
		if(toolSet.contains("htmlcs"))
			accessibilityHelper.runCodeSniffer();
		if(toolSet.contains("axe"))
			accessibilityHelper.testAccessibility();
	}

	@Given("I upload (.+) file to (.+)")
	public void iUploadIntroToTestingJpgFileToCapsFile_upload(String fileName, String element) throws Exception {
		PageObject pObj= pageObjects.get(element);
		miscmethodObj.validateLocator(pObj.getType());//type means text or select or any html element type
		String filePath = System.getProperty("user.dir") + "\\" + fileName;
		inputObj.enterText(pObj.getType(), filePath, pObj.getAccess_name());

	}


	@Then("^I search for \"([^\"]*)\"$")
	public void iSearchFor(String item) throws Throwable {

		driver.findElement(By.id("twotabsearchtextbox")).sendKeys(item);
		driver.findElement(By.xpath("//input[@value='Go']")).click();
	}
}