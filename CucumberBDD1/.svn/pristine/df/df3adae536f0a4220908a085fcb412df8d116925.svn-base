package seleniumutils.applicationlayer.applicationstepdefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import env.DriverUtil;
import org.openqa.selenium.WebDriver;
import seleniumutils.reusablestepdefinitions.ReusableStepDefinitions;

import java.time.Instant;
import java.util.HashMap;

//import info.seleniumcucumber.applicationstepdefinitions.PredefinedStepDefinitions;

public class ActivitiesPageStepDefinitions {
	protected WebDriver driver;
	protected HashMap<String,HashMap<String,HashMap<String, String>>> pageObjects;
	static ReusableStepDefinitions predef= new ReusableStepDefinitions();

	public String getXLSheetNameFromClass(){
		return this.getClass().getSimpleName().replaceAll("StepDefinitions","").toLowerCase();
	}
	public ActivitiesPageStepDefinitions() {
		//this.driver = DriverUtil.getDefaultDriver();
		//this.pageObjects= ApachePOIExcel.readXL2Hash("PageObjects.xlsx");
	}

	//Navigation Steps

	@Given("^I search for \"([^\"]*)\" in the information bar$")
	public void iSearchForInTheInformationBar(String searchString) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		predef.enter_text(searchString,"general.goto_page");
		predef.enter_key("TAB", "general.goto_page");
		predef.enter_key("ENTER", "general.goto_page");
		System.out.println("Pressed Enter  at "+ Instant.now());
		DriverUtil.waitForJSandJQueryToLoad();
	}
	@Then("^I fill the activities page")
	public static void fillActivitiesPage(){}
}