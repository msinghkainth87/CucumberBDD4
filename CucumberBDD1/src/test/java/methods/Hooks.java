package methods;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.apache.commons.io.FileUtils;
import seleniumutils.methods.ApachePOIExcel;
import seleniumutils.methods.GlobalProperties;
import seleniumutils.methods.YAMLReader;
import seleniumutils.methods.helpers.PageObjectGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Hooks {

	@Before
	public void pageObjectInitialize()throws Exception{
		GlobalProperties globalProperties = new GlobalProperties();
		PageObjectGenerator.pageObjects = ApachePOIExcel.readXL2FlatHash("src\\test\\resources\\"+globalProperties.getConfigProperties().get("application").toLowerCase()+"PageObjects.xlsx");
		if(globalProperties.getConfigProperties().get("input_data_type").equalsIgnoreCase("YAML"))
			YAMLReader.readAllYAMLData();
		String env = globalProperties.getConfigProperties().get("input_data_type");

		//TestDataHandler.testData=ApachePOIExcel.readXL2FlatHash("InputTestData.xlsx");
		//ApachePOIExcel.setXlData("googlePageObjects.xlsx");
	}
//
//	@After
//	public void beforeScenario(Scenario scenario) {
//		System.out.println("In hooks");
//		System.out.println(scenario.getName());
//		System.out.println(scenario.getStatus());
//		generateReport("target/cucumber-html-reports");
//
//	}

	private static void generateReport(String karateOutputPath) {
		Collection<File> jsonFiles = FileUtils.listFiles(new File(karateOutputPath), new String[] {"json"}, true);
		List jsonPaths = new ArrayList(jsonFiles.size());
		for (File file :jsonFiles) {
			jsonPaths.add(file.getAbsolutePath());
		}
		Configuration config = new Configuration(new File("target"), "CucumberBDD");
		//config.addClassifications("Environment", System.getProperty("karate.env"));
		ReportBuilder reportBuilder = new ReportBuilder(jsonPaths, config);
		reportBuilder.generateReports();

	}


}