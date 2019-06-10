package seleniumutils.applicationlayer.applicationstepdefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import seleniumutils.methods.ApachePOIExcel;
import seleniumutils.methods.YAMLReader;
import seleniumutils.methods.helpers.DataUtils;
import org.openqa.selenium.WebDriver;
import seleniumutils.methods.helpers.TestDataHandler;
import seleniumutils.reusablestepdefinitions.ReusableStepDefinitions;

import java.util.HashMap;
import java.util.Map;

//import info.seleniumcucumber.applicationstepdefinitions.PredefinedStepDefinitions;

public class LoginPageStepDefinitions {
	protected WebDriver driver;
	protected HashMap<String,HashMap<String,HashMap<String, String>>> pageObjects;
	static ReusableStepDefinitions predef= new ReusableStepDefinitions();

	public String getXLSheetNameFromClass(){
		return this.getClass().getSimpleName().replaceAll("StepDefinitions","").toLowerCase();
	}
	public LoginPageStepDefinitions() {
		//this.driver = DriverUtil.getDefaultDriver();
		//this.pageObjects= ApachePOIExcel.readXL2Hash("PageObjects.xlsx");
	}

	//Navigation Steps
	@Given("I navigate to ClaimCenter (.+) login page")
	public void navigate_to(String link)
	{
		link= "login."+link;
		predef.navigate_toURL(link);
	}


	@And("^I enter a (.+) id into (.+) field$")
	public void iEnterSuperuserIntoUsernameField(String data,String field) throws Throwable {
		String[] pageObject= DataUtils.getPageObject("Login",field);
		predef.enter_text(ApachePOIExcel.getXlData().get(getXLSheetNameFromClass()).get(data.toLowerCase()).get("value"),field);
	}

	@Then("^I click (.+) button$")
	public void iClickLoginButton(String field) throws Throwable {
		field= "login."+field;
		predef.click(field);
	}
	@Then("^I fill the (.+) page with (.*) data$")
	public void fillLoginPage(String pageName,String inputDataKey){
		Map<String,Object> tMap= YAMLReader.getMasterDataMap();
		String key=pageName+"."+inputDataKey;
		Map<String,Object> newMap=TestDataHandler.traverseToMap(key.toUpperCase(),tMap);
		TestDataHandler.injectInputData(newMap,pageName.toUpperCase());
	}

}