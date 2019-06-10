package seleniumutils.applicationlayer.applicationstepdefinitions;

import cucumber.api.java.en.Then;
import org.openqa.selenium.WebDriver;
import seleniumutils.reusablestepdefinitions.ReusableStepDefinitions;

import java.util.HashMap;

//import info.seleniumcucumber.applicationstepdefinitions.PredefinedStepDefinitions;

public class FNOLSearchOrCreatePolicyPageStepDefinitions {
	protected WebDriver driver;
	protected HashMap<String,HashMap<String,HashMap<String, String>>> pageObjects;
	static ReusableStepDefinitions predef= new ReusableStepDefinitions();

	public String getXLSheetNameFromClass(){
		return this.getClass().getSimpleName().replaceAll("StepDefinitions","").toLowerCase();
	}
	public FNOLSearchOrCreatePolicyPageStepDefinitions() {
		//this.driver = DriverUtil.getDefaultDriver();
		//this.pageObjects= ApachePOIExcel.readXL2Hash("PageObjects.xlsx");
	}
	@Then("^I fill the fnol search or create policy page and continue")
	public void fillFNOLSearchOrCreatePolicyPage()throws Throwable{
		predef.enter_text("1000000028", "fnolsearchorcreatepolicy.policy_number");
		predef.enter_text("10/13/2018", "fnolsearchorcreatepolicy.enter_loss_date");
		predef.click("fnolsearchorcreatepolicy.search_policy");
		predef.click("fnolsearchorcreatepolicy.next");
	}
}
