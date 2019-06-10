package seleniumutils.applicationlayer.applicationstepdefinitions;

import cucumber.api.java.en.Then;
import org.openqa.selenium.WebDriver;
import seleniumutils.frameworklayer.frameworkstepdefinitions.GWOOTBReusableStepDefinitions;
import seleniumutils.reusablestepdefinitions.ReusableStepDefinitions;

import java.util.HashMap;

//import info.seleniumcucumber.applicationstepdefinitions.PredefinedStepDefinitions;

public class FNOLLossDetailsPageStepDefinitions {
	protected WebDriver driver;
	protected HashMap<String,HashMap<String,HashMap<String, String>>> pageObjects;
	static ReusableStepDefinitions predef= new ReusableStepDefinitions();

	public String getXLSheetNameFromClass(){
		return this.getClass().getSimpleName().replaceAll("StepDefinitions","").toLowerCase();
	}
	public FNOLLossDetailsPageStepDefinitions() {
		//this.driver = DriverUtil.getDefaultDriver();
		//this.pageObjects= ApachePOIExcel.readXL2Hash("PageObjects.xlsx");
	}
	@Then("^I fill the fnol loss details page and continue")
	public void fillFNOLLossDetailsPage()throws Throwable{
		GWOOTBReusableStepDefinitions.gw_select_option_from_dropdown("1","index","fnollossdetails.loss_cause");
//		predef.double_click("fnollossdetails.loss_cause");
//		predef.enter_text("Collision at Uncontrolled Intersection", "fnollossdetails.loss_cause");
		predef.double_click("fnollossdetails.loss_location_type");
		GWOOTBReusableStepDefinitions.gw_select_option_from_dropdown("2","index","fnollossdetails.loss_location_type");
		//predef.enter_text_from_input_data("fnollossdetails.ma_loss_address", "fnollossdetails.loss_location_type");
		predef.click("fnollossdetails.next");
	}
}
