package seleniumutils.frameworklayer.frameworkstepdefinitions;

import cucumber.api.java.en.Then;
import org.openqa.selenium.WebDriver;
import seleniumutils.reusablestepdefinitions.ReusableStepDefinitions;

import java.util.HashMap;

import static java.lang.Integer.parseInt;

//import info.seleniumcucumber.applicationstepdefinitions.PredefinedStepDefinitions;

public class GWOOTBReusableStepDefinitions {
	protected WebDriver driver;
	protected HashMap<String,HashMap<String,HashMap<String, String>>> pageObjects;
	static ReusableStepDefinitions predef= new ReusableStepDefinitions();

	public String getXLSheetNameFromClass(){
		return this.getClass().getSimpleName().replaceAll("StepDefinitions","").toLowerCase();
	}
	public GWOOTBReusableStepDefinitions() {
		//this.driver = DriverUtil.getDefaultDriver();
		//this.pageObjects= ApachePOIExcel.readXL2Hash("PageObjects.xlsx");
	}

	//Navigation Steps


	// select option by text/value from dropdown
	@Then("^I gwselect option \"(.*?)\" by (index) from dropdown (.+)$")
	public static void gw_select_option_from_dropdown(String option,String optionBy,String element) throws Exception
	{
		int optionint;
		if(optionBy.equals("index")) {
			optionint = Integer.parseInt(option);
			predef.click(element);
			for (int i = 0; i < optionint; i++)
				predef.enter_key("ARROW_DOWN", element);
			predef.enter_key("ENTER", element);
		}
	}
}