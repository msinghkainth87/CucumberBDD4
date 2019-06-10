package seleniumutils.methods;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;


public class InputMethods extends GetElementUsingBy implements BaseTest
{
	//GetElementUsingBy eleType= new GetElementUsingBy();
	private WebElement dropdown =null;
	private Select selectList=null;
	
	/** Method to enter text into text field
	 * @param accessType : String : Locator type (id, name, class, xpath, css)
	 * @param text : String : Text value to enter in field
	   @param accessName : String : Locator value
	 */
	public void enterText(String accessType,String text,String accessName)
	{wait.until(ExpectedConditions.presenceOfElementLocated(getElementByAttributes(accessType, accessName)));
		WebElement element = driver.findElement(getElementByAttributes(accessType, accessName));
		if(GlobalProperties.getConfigProperties().get("flashfill").equalsIgnoreCase("false"))
			element.sendKeys(text);
		if(GlobalProperties.getConfigProperties().get("flashfill").equalsIgnoreCase("true"))
			JavascriptHandlingMethods.flashfill(element,text);
		//TODO:Handle else case

	}
	/** Method to enter Keys into text field
	 * @param accessType : String : Locator type (id, name, class, xpath, css)
	 * @param keys : String : Key value to press
	 @param accessName : String : Locator value
	 */
	public void enterKeys(String accessType, Keys keys, String accessName)
	{
		wait.until(ExpectedConditions.presenceOfElementLocated(getElementByAttributes(accessType, accessName)));
		driver.findElement(getElementByAttributes(accessType, accessName)).sendKeys(keys);
	}
	/** Method to clear text of text field
	@param accessType : String : Locator type (id, name, class, xpath, css)
	@param accessName : String : Locator value
	*/
	public void clearText(String accessType, String accessName)
	{
		wait.until(ExpectedConditions.presenceOfElementLocated(getElementByAttributes(accessType, accessName)));
		driver.findElement(getElementByAttributes(accessType, accessName)).clear();
	}
	
	/** Method to select element from Dropdown by type
	 * @param select_list : Select : Select variable
	 * @param bytype : String : Name of by type
	 * @param option : String : Option to select
	 */
	public void selectElementFromDropdownByType(Select select_list, String bytype, String option)
	{
		if(bytype.equals("selectByIndex"))
		{
			int index = Integer.parseInt(option);
			select_list.selectByIndex(index-1);
		}
		else if (bytype.equals("value"))
			select_list.selectByValue(option);
		else if (bytype.equals("text"))
			select_list.selectByVisibleText(option);
	}
	
	/** Method to select option from dropdown list
	@param accessType : String : Locator type (id, name, class, xpath, css)
	@param optionBy : String : Criteria for options selection (Whether by index, by value or by inner text)
	@param option : String : Option to select (the option index, the option value attribute or inner text)
	@param accessName : String : Locator value
	*/
	public void selectOptionFromDropdown(String accessType, String optionBy, String option, String accessName)
	{
		dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(getElementByAttributes(accessType, accessName)));
		selectList = new Select(dropdown);
		
		if(optionBy.equals("selectByIndex"))
			selectList.selectByIndex(Integer.parseInt(option)-1);
		else if (optionBy.equals("value"))
			selectList.selectByValue(option);
		else if (optionBy.equals("text"))
			selectList.selectByVisibleText(option);
	}
	
	//method to select all option from dropdwon list
//	public void select_all_option_from_multiselect_dropdown(String access_type, String access_name)
//	{
//		dropdown = driver.findElement(getElementByAttributes(access_type, access_name));
//		selectList = new Select(dropdown);
//		
//		//Select all method not present in JAVA
//	}
	
	/** Method to unselect all option from dropdwon list
	@param accessType : String : Locator type (id, name, class, xpath, css)
	@param accessName : String : Locator value
	*/
	public void unselectAllOptionFromMultiselectDropdown(String accessType, String accessName)
	{
		dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(getElementByAttributes(accessType, accessName)));
		selectList = new Select(dropdown);
		selectList.deselectAll();
	}
	
	/** Method to unselect option from dropdwon list
	@param accessType : String : Locator type (id, name, class, xpath, css)
	@param accessName : String : Locator value
	*/
	public void deselectOptionFromDropdown(String accessType, String optionBy, String option, String accessName) 
	{
		dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(getElementByAttributes(accessType, accessName)));
		selectList = new Select(dropdown);
		
		if(optionBy.equals("selectByIndex"))
			selectList.deselectByIndex(Integer.parseInt(option)-1);
		else if (optionBy.equals("value"))
			selectList.deselectByValue(option);
		else if (optionBy.equals("text"))
			selectList.deselectByVisibleText(option);
	}
	
	/** Method to check check-box
	@param accessType : String : Locator type (id, name, class, xpath, css)
	@param accessName : String : Locator value
	*/
	public void checkCheckbox(String accessType, String accessName)
	{
		WebElement checkbox= wait.until(ExpectedConditions.presenceOfElementLocated(getElementByAttributes(accessType, accessName)));
		if (!checkbox.isSelected())
			checkbox.click();
	}
	
	/** Method to uncheck check-box
	@param accessType : String : Locator type (id, name, class, xpath, css)
	@param accessName : String : Locator value
	*/
	public void uncheckCheckbox(String accessType, String accessName)
	{
		WebElement checkbox= wait.until(ExpectedConditions.presenceOfElementLocated(getElementByAttributes(accessType, accessName)));
		if (checkbox.isSelected())
			checkbox.click();
	}
	
	/** Method to toggle check-box status
	@param accessType : String : Locator type (id, name, class, xpath, css)
	@param accessName : String : Locator value
	*/
	public void toggleCheckbox(String accessType, String accessName)
	{
		wait.until(ExpectedConditions.presenceOfElementLocated(getElementByAttributes(accessType, accessName))).click();
	}
	
	/** Method to select radio button
	@param accessType : String : Locator type (id, name, class, xpath, css)
	@param accessName : String : Locator value
	*/
	public void selectRadioButton(String accessType, String accessName)
	{
		WebElement radioButton = wait.until(ExpectedConditions.presenceOfElementLocated(getElementByAttributes(accessType, accessName)));
		if(!radioButton.isSelected())
			radioButton.click();
	}
	
	/** Method to select option from radio button group
	@param accessType : String : Locator type (id, name, class, xpath, css)
	@param by : String : Name of by type
	@param option : String : Option to select
	@param accessName : String : Locator value
	*/
	public void selectOptionFromRadioButtonGroup(String accessType, String option, String by, String accessName)
	{
		List<WebElement> radioButtonGroup = driver.findElements(getElementByAttributes(accessType, accessName));
		for(WebElement rb : radioButtonGroup)
		{
			if(by.equals("value"))
			{
				if(rb.getAttribute("value").equals(option) && !rb.isSelected())
					rb.click();
			}
			else if(by.equals("text"))
			{
				if(rb.getText().equals(option) && !rb.isSelected())
					rb.click();
			}
		}
	}

	/** Method to fill any element
	 @param accessType : String : Locator type (id, name, class, xpath, css)
	 @param value : String : Variable args for Name of by type and String Option to select in that order
	 @param accessName : String : Locator value
	 */
	public void fillElement(String accessType,String accessName,String value){
		String elementType= getElementType(accessType,accessName);
		switch(elementType.toLowerCase())
		{
			case "textbox":
			case "textarea":
				clearText(accessType, accessName);
				enterText(accessType,value,accessName);
				break;

			case "select":
				selectOptionFromDropdown(accessType, "text", value, accessName);
				break;
			default:
				toggleCheckbox(accessType, accessName);		//nothing more than an element click. Didn't wanna import ClickElementMethods class just for this.
				break;
		}
		/*switch(value.length){
			case 0:
				toggleCheckbox(accessType, accessName);
				break;
			case 1:
				enterText(accessType,value[0],accessName);
				break;
			case 2:
				selectOptionFromDropdown(accessType, value[0], value[1], accessName);
				break;
			default:

		}*/

	}
}
