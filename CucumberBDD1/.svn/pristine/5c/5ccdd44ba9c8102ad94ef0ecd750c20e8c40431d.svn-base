package seleniumutils.applicationlayer.applicationstepdefinitions;

import cucumber.api.java.en.Then;
import org.openqa.selenium.WebDriver;
import seleniumutils.frameworklayer.frameworkstepdefinitions.GWOOTBReusableStepDefinitions;
import seleniumutils.reusablestepdefinitions.ReusableStepDefinitions;

import java.util.HashMap;

//import info.seleniumcucumber.applicationstepdefinitions.PredefinedStepDefinitions;

public class FNOLBasicInformationPageStepDefinitions {
	protected WebDriver driver;
	protected HashMap<String,HashMap<String,HashMap<String, String>>> pageObjects;
	static ReusableStepDefinitions predef= new ReusableStepDefinitions();

	public String getXLSheetNameFromClass(){
		return this.getClass().getSimpleName().replaceAll("StepDefinitions","").toLowerCase();
	}
	public FNOLBasicInformationPageStepDefinitions() {
		//this.driver = DriverUtil.getDefaultDriver();
		//this.pageObjects= ApachePOIExcel.readXL2Hash("PageObjects.xlsx");
	}
	@Then("^I fill the fnol basic information page and continue")
	public void fillFNOLBasicInformationPage()throws Throwable{

		//predef.double_click("fnolbasicinformation.reported_by_name");
		//predef.click("fnolbasicinformation.reported_by_name");
		GWOOTBReusableStepDefinitions.gw_select_option_from_dropdown("1","index","fnolbasicinformation.reported_by_name");
//		Thread.sleep(2000);
//		predef.enter_key("ARROW_DOWN", "fnolbasicinformation.reported_by_name");
//		Thread.sleep(2000);
//		predef.enter_key("ARROW_DOWN", "fnolbasicinformation.reported_by_name");
//		Thread.sleep(2000);
//		predef.enter_key("ENTER", "fnolbasicinformation.reported_by_name");
//		Thread.sleep(2000);
		//predef.enter_text("mark WILLIAMS", "fnolbasicinformation.reported_by_name");
		predef.click("fnolbasicinformation.involved_vehicle_checkbox");
		Thread.sleep(2000);
		predef.click("fnolbasicinformation.next");
	}
}
