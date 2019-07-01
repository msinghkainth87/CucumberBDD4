package methods;

import com.intuit.karate.Config;
import com.intuit.karate.cucumber.CucumberRunner;
import com.intuit.karate.cucumber.KarateStats;
import cucumber.api.CucumberOptions;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cucumber.api.junit.Cucumber;
import seleniumutils.methods.GlobalProperties;
//import cucumber.api.testng.AbstractTestNGCucumberTests;

//comment @RunWith and uncomment above import and the RunCukesTestNG class if you are running with TestNG

@CucumberOptions(tags={"@conversion_run1"},
		//Built-In report
		//plugin = {"pretty","html:target/cucumberHtmlReport.html","json:target/cucumberJSONReportKarate.json", "com.cucumber.listener.ExtentCucumberFormatter:target/report.html"},
		//cucumber_pro
		//plugin = {"io.cucumber.pro.JsonReporter12:default"},
		//extent_reports uncomment the @afterclass method in junit
		//plugin = {"com.sitture.ExtentFormatter:output/extent-report/index.html", "html:output/html-report"},
		//pretty:target/cucumber-json-report.json
		features = {"src/test/resources"},
		glue = {"com.intuit.karate","seleniumutils.reusablestepdefinitions", "seleniumutils.applicationlayer","seleniumutils.frameworklayer","methods"}
)

public class RunCukeTest {
	@Test
    public void testParallel() {
		//Set the Webservice Env
		GlobalProperties globalProperties = new GlobalProperties();
		System.setProperty("karate.env",globalProperties.getConfigProperties().get("karate_env"));
		System.setProperty("karate.path",System.getProperty("user.dir"));
        String karateOutputPath = "target";
        KarateStats stats = CucumberRunner.parallel(getClass(), 2, karateOutputPath);
    }
	@CucumberOptions(tags={""},
			plugin = {"pretty","html:target/cucumberHtmlReport.html","json:target/cucumberJSONReportUI.json", "com.cucumber.listener.ExtentCucumberFormatter:target/report.html"},
			features = {"src/test/resources"},
			glue = {"seleniumutils.reusablestepdefinitions", "seleniumutils.applicationlayer","seleniumutils.frameworklayer","methods"}
	)
	@RunWith(Cucumber.class)
	public static class SubTestWithRunner {

	}

	@AfterClass
	public static void beforeScenario() throws Exception {
		System.out.println("MainTest subRunner()");
		JUnitCore.runClasses(SubTestWithRunner.class);
		generateReport("target");
		//if(GlobalProperties.getConfigProperties().get("record_test_video").equals("true"))
			//VideoRecord.stopRecording();
	}
	private static void generateReport(String karateOutputPath) {
		Collection<File> jsonFiles = FileUtils.listFiles(new File(karateOutputPath), new String[]{"json"}, true);
		jsonFiles.remove(new File("target/cucumber-html-reports/aXe.json"));
		jsonFiles.remove(new File("target/cucumber-html-reports/htmlcs.json"));
		jsonFiles.remove(new File("target/cucumber-html-reports/testAccessibility.json"));
		jsonFiles.remove(new File("target/classes/Transformation.json"));
		jsonFiles.remove(new File("target/test-classes/Transformation.json"));
		jsonFiles.remove(new File("target/classes/PositionTranformation.json"));
		jsonFiles.remove(new File("target/test-classes/PositionTranformation.json"));
		List jsonPaths = new ArrayList(jsonFiles.size());
		for (File file : jsonFiles) {
			jsonPaths.add(file.getAbsolutePath());
		}
		Configuration config = new Configuration(new File("target"), "CucumberBDD");
		config.addClassifications("Environment", System.getProperty("karate.env"));
		ReportBuilder reportBuilder = new ReportBuilder(jsonPaths, config);
		reportBuilder.generateReports();
	}
}

/*TestNG
@Test
public class RunCukesTestNG extends AbstractTestNGCucumberTests{

}*/

