package seleniumutils.applicationlayer.applicationstepdefinitions;

import cucumber.api.java.en.Then;
import org.openqa.selenium.WebDriver;
import seleniumutils.reusablestepdefinitions.ReusableStepDefinitions;

import java.util.HashMap;

//import info.seleniumcucumber.applicationstepdefinitions.PredefinedStepDefinitions;

public class FNOLAssignmentPageStepDefinitions {
	protected WebDriver driver;
	protected HashMap<String,HashMap<String,HashMap<String, String>>> pageObjects;
	static ReusableStepDefinitions predef= new ReusableStepDefinitions();

	public String getXLSheetNameFromClass(){
		return this.getClass().getSimpleName().replaceAll("StepDefinitions","").toLowerCase();
	}
	public FNOLAssignmentPageStepDefinitions() {
		//this.driver = DriverUtil.getDefaultDriver();
		//this.pageObjects= ApachePOIExcel.readXL2Hash("PageObjects.xlsx");
	}
	@Then("^I fill the fnol assignment page and continue")
	public void fillFNOLAssignmentPage()throws Throwable{
		predef.click("fnolassignment.finish");
	}
}
