package env;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.ErrorHandler;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import seleniumutils.methods.GlobalProperties;
import org.apache.log4j.Logger;

import java.io.File;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Created by tom on 24/02/17.
 */

public class DriverUtil implements Log4j{
    public static long DEFAULT_WAIT = 10;
    protected static WebDriver driver;

    private static String CHROMEDRIVERPROPERTY = "webdriver.chrome.driver";
    private static String FIREFOXDRIVERPROPERTY = "webdriver.gecko.driver";
    private static String IEDRIVERPROPERTY = "webdriver.ie.driver";

    public static String BROWSER = GlobalProperties.getConfigProperties().get("browser");
    private static String DRIVERPATH = System.getProperty("user.dir") + "\\webdrivers";
    private static String HEADLESS = GlobalProperties.getConfigProperties().get("headless");
    private static String TAKES_SCREENSHOT = GlobalProperties.getConfigProperties().get("takes_screenshot");
    private static String CHROME_BROWSERVERSION = GlobalProperties.getConfigProperties().get("chrome_browserversion");
    private static Boolean CHROMEUSEAUTOMATIONEXTENSION = GlobalProperties.getConfigProperties().get("chromeuseAutomationExtension").equals("true");
    private static String CHROME_OS = GlobalProperties.getConfigProperties().get("chrome_os");
    private static String CHROME_OS_VERSION = GlobalProperties.getConfigProperties().get("chrome_os_version");
    private static String BROWSERSTACK_DEBUG = GlobalProperties.getConfigProperties().get("chrome_browserstack_debug");
    private static String CHROME_BUILD = GlobalProperties.getConfigProperties().get("chrome_build");
    private static Boolean ACCESSIBILITY = GlobalProperties.getConfigProperties().get("accessibility").equals("true");

    private static String INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS = GlobalProperties.getConfigProperties().get("introduce_flakiness_by_ignoring_security_domains");
    private static Boolean IGNOREPROTECTEDMODESETTINGS = GlobalProperties.getConfigProperties().get("ignoreProtectedModeSettings").equals("true");
    private static Boolean IGNOREZOOMSETTING = GlobalProperties.getConfigProperties().get("ignoreZoomSetting").equals("true");
    private static String INITIALBROWSERURL = GlobalProperties.getConfigProperties().get("initialBrowserUrl");
    private static Boolean ENABLEPERSISTENTHOVER = GlobalProperties.getConfigProperties().get("enablePersistentHover").equals("true");
    private static Boolean ENABLEELEMENTCACHECLEANUP = GlobalProperties.getConfigProperties().get("enableElementCacheCleanup").equals("true");
    private static Boolean REQUIREWINDOWFOCUS = GlobalProperties.getConfigProperties().get("requireWindowFocus").equals("true");
    private static int BROWSERATTACHTIMEOUT = Integer.parseInt(GlobalProperties.getConfigProperties().get("browserAttachTimeout"));
    private static Boolean IE_FORCECREATEPROCESSAPI = GlobalProperties.getConfigProperties().get("ie_forceCreateProcessApi").equals("true");
    private static String IE_BROWSERCOMMANDLINESWITCHES = GlobalProperties.getConfigProperties().get("ie_browserCommandLineSwitches");
    private static Boolean IE_USEPERPROCESSPROXY = GlobalProperties.getConfigProperties().get("ie_usePerProcessProxy").equals("true");
    private static Boolean IE_ENSURECLEANSESSION = GlobalProperties.getConfigProperties().get("ie_ensureCleanSession").equals("true");
    private static String LOGFILE = GlobalProperties.getConfigProperties().get("logFile");
    private static String LOGLEVEL = GlobalProperties.getConfigProperties().get("logLevel");
    private static String HOST = GlobalProperties.getConfigProperties().get("host");
    private static String EXTRACTPATH = GlobalProperties.getConfigProperties().get("extractPath");
    private static Boolean SILENT = GlobalProperties.getConfigProperties().get("silent").equals("true");
    private static Boolean IE_SETPROXYBYSERVER = GlobalProperties.getConfigProperties().get("ie_setProxyByServer").equals("true");

    private static String PHANTOMJS_ELEMENT_SCROLL_BEHAVIOR = GlobalProperties.getConfigProperties().get("phantomjs_element_scroll_behavior");
    private static String PHANTOMJS_TAKES_SCREENSHOT = GlobalProperties.getConfigProperties().get("phantomjs_takes_screenshot");
    private static String PHANTOMJS_ENABLE_PROFILING_CAPABILITY = GlobalProperties.getConfigProperties().get("phantomjs_enable_profiling_capability");
    private static String PHANTOMJS_HAS_NATIVE_EVENTS = GlobalProperties.getConfigProperties().get("phantomjs_has_native_events");
    private static String PHANTOMJS_PAGE_SETTINGS_USERAGENT = GlobalProperties.getConfigProperties().get("phantomjs_page_settings_useragent");
    private static String PHANTOMJS_WEB_SECURITY = GlobalProperties.getConfigProperties().get("phantomjs_web-security");
    private static String PHANTOMJS_IGNORE_SSL_ERRORS = GlobalProperties.getConfigProperties().get("phantomjs_ignore-SSL-ERRORS");
    private static String PHANTOMJS_LOGLEVEL = GlobalProperties.getConfigProperties().get("phantomjs_loglevel");
    private static Boolean VIDEORESULTS = GlobalProperties.getConfigProperties().get("record_test_video").equals("true");
    private static int IMPLICIT_WAIT_DURATION_SEC = Integer.parseInt(GlobalProperties.getConfigProperties().get("implicit_wait_duration_sec"));
    private static int PAGE_LOAD_WAIT_DURATION_SEC = Integer.parseInt(GlobalProperties.getConfigProperties().get("page_load_wait_duration_sec"));
    //private static String CHROME_BROWSERVERSION=GlobalProperties.getConfigProperties().get("chrome_browserversion");

    protected static WebDriverWait wait;

    public static WebDriver getDefaultDriver() {
        if (driver != null) {
            return driver;

        }

        //System.setProperty("webdriver.gecko.driver", "./geckodriver");
        if (VIDEORESULTS) {
            try {
                //VideoRecord.startRecording();
            } catch (Exception e) {
                e.printStackTrace();
                log.info(e.getMessage());
            }
        }
        driver = chooseDriver();
        driver.manage().timeouts().setScriptTimeout(DEFAULT_WAIT,
                TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT_DURATION_SEC, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_WAIT_DURATION_SEC, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        return driver;
    }

    /**
     * By default to web driver will be PhantomJS
     * <p>
     * Override it by passing -DWebDriver=Chrome to the command line arguments
     *
     * @return
     */
    private static WebDriver chooseDriver() {

        String preferredDriver = BROWSER;
        boolean headless = HEADLESS.equals("true");
        DesiredCapabilities capabilities = null;
        try {
            switch (preferredDriver.toLowerCase()) {
                case "ch":
                case "chrome":
                    //                    System.setProperty(CHROMEDRIVERPROPERTY, DRIVERPATH + "\\chromedriver.exe");
                    capabilities = setChromeDriverCapabilities();
                    final ChromeOptions chromeOptions = new ChromeOptions();
                    if (headless) {
                        chromeOptions.addArguments("--headless");
                    }
                    chromeOptions.setExperimentalOption("useAutomationExtension", CHROMEUSEAUTOMATIONEXTENSION);
                    chromeOptions.addArguments("disable-infobars");
                    capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                    System.out.println("********************* before driver created");
                    log.info("********************* before driver created");
                    ChromeDriverService service = new ChromeDriverService.Builder()
                            .usingDriverExecutable(new File(DRIVERPATH + "\\chromedriver.exe"))
                            .usingAnyFreePort()
                            .build();

                    //ChromeOptions options = new ChromeOptions();
                    chromeOptions.merge(capabilities);
                    ChromeDriver driver = new ChromeDriver(service, chromeOptions);
                    System.out.println("********************* after driver created");
                    log.info("********************* after driver created");
                    ErrorHandler handler = new ErrorHandler();
                    handler.setIncludeServerErrors(false);
                    driver.setErrorHandler(handler);
                    log.info("Chrome Driver Launched");
                    return driver;
                case "phantomjs":
                    DesiredCapabilities caps = new DesiredCapabilities();
                    caps.setJavascriptEnabled(true);
                    caps.setCapability("takesScreenshot", TAKES_SCREENSHOT);
                    PhantomJSDriverService servicePJS = new PhantomJSDriverService.Builder()
                            .usingAnyFreePort()
                            .usingPhantomJSExecutable(new File(DRIVERPATH + "\\phantomjs\\bin\\phantomjs.exe"))
                            .build();
                    log.info("PhantomJS Driver Launched");
                    return new PhantomJSDriver(servicePJS, caps);
                /*
                    System.setProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, DRIVERPATH+"\\phantomjs\\bin\\phantomjs.exe");
                    //capabilities = DesiredCapabilities.phantomjs();
                    capabilities.setJavascriptEnabled(true);
                    Capabilities caps = new DesiredCapabilities();
                    capabilities.setCapability("takesScreenshot", TAKES_SCREENSHOT);
    //				capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,DRIVERPATH+"\\phantomjs\\bin\\phantomjs.exe");
                    return new PhantomJSDriver(caps);
                    */
                case "ff":
                case "firefox":
                    //ProfilesIni allProfiles = new ProfilesIni();
                    //FirefoxProfile profile = allProfiles.getProfile("selenium");
                    //driver = new FirefoxDriver(profile);

                    System.setProperty(FIREFOXDRIVERPROPERTY, DRIVERPATH + "\\geckodriver.exe");
                    capabilities = DesiredCapabilities.firefox();
                    capabilities.setJavascriptEnabled(true);
                    capabilities.setCapability("takesScreenshot", GlobalProperties.getConfigProperties().get("takes_screenshot"));
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    //capabilities.s
                    if (headless) {
                        firefoxOptions.addArguments("-headless", "-safe-mode");
                    }
                    capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
                    log.info("Firefox Driver Launched");
                    return new FirefoxDriver();

                case "ie":
                case "internetexplorer":
                    System.setProperty(IEDRIVERPROPERTY, DRIVERPATH + "\\IEDriverServer.exe");
                    capabilities = DesiredCapabilities.internetExplorer();
                    capabilities.setJavascriptEnabled(true);
                    capabilities.setCapability("takesScreenshot", TAKES_SCREENSHOT);
                    InternetExplorerDriver IEDriver = new InternetExplorerDriver(setIEDriverCapabilities());
                    //this is to reset Zoom level to 100% for IE.

                    IEDriver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL, "0"));
                    log.info("Interner Explorer Driver Launched");
                    return IEDriver;
                case "safari":
                    return new SafariDriver();

                default:
                    //return new PhantomJSDriver(capabilities);
                    firefoxOptions = new FirefoxOptions();
                    //capabilities.s
                    if (headless) {
                        firefoxOptions.addArguments("-headless", "-safe-mode");
                    }
                    capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
                    final FirefoxDriver firefox = new FirefoxDriver();
                    log.info("Default Firefox Driver Launched");
                    return firefox;
            }

        } catch (Exception e) {
            log.error("Failed to Open Browser || Class:: DriverUtil || method::chooseDriver " + e.getMessage());
            Assert.fail("Failed to Open Browser|| Class:: DriverUtil || method::chooseDriver");
        }
     return driver;
    }



    public static DesiredCapabilities setChromeDriverCapabilities(){
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setJavascriptEnabled(true);
        if(ACCESSIBILITY) {
            log.info("Accessibility  Driver setting chosen for chrome");
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.BROWSER, Level.ALL);
            capabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        }
        capabilities.setCapability("takesScreenshot", TAKES_SCREENSHOT);
        return capabilities;
    }

    public static InternetExplorerOptions setIEDriverCapabilities(){
        InternetExplorerOptions ieOptions = new InternetExplorerOptions();
        ieOptions.setCapability("requireWindowFocus", true);
        ieOptions.setCapability("INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS",INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS);
        ieOptions.setCapability("ignoreProtectedModeSettings",IGNOREPROTECTEDMODESETTINGS);
        ieOptions.setCapability("ignoreZoomSetting",IGNOREZOOMSETTING);
        ieOptions.setCapability("initialBrowserUrl",INITIALBROWSERURL);
        ieOptions.setCapability("enablePersistentHover",ENABLEPERSISTENTHOVER);
        ieOptions.setCapability("enableElementCacheCleanup",ENABLEELEMENTCACHECLEANUP);
        ieOptions.setCapability("requireWindowFocus",REQUIREWINDOWFOCUS);
        ieOptions.setCapability("browserAttachTimeout",BROWSERATTACHTIMEOUT);
//		ieOptions.setCapability("ie.forceCreateProcessApi",IE_FORCECREATEPROCESSAPI);
//		ieOptions.setCapability("ie.browserCommandLineSwitches",IE_BROWSERCOMMANDLINESWITCHES);
//		ieOptions.setCapability("ie.usePerProcessProxy",IE_USEPERPROCESSPROXY);
        ieOptions.setCapability("ie.ensureCleanSession",IE_ENSURECLEANSESSION);
//		ieOptions.setCapability("logFile",LOGFILE);
//		ieOptions.setCapability("logLevel",LOGLEVEL);
//		ieOptions.setCapability("host",HOST);
//		ieOptions.setCapability("extractPath",EXTRACTPATH);
//		ieOptions.setCapability("silent",SILENT);
//		ieOptions.setCapability("ie.setProxyByServer",IE_SETPROXYBYSERVER);
        return ieOptions;
    }

    public static WebElement waitAndGetElementByCssSelector(WebDriver driver, String selector,
                                                            int seconds) {
        By selection = By.cssSelector(selector);
        return (new WebDriverWait(driver, seconds)).until( // ensure element is visible!
                ExpectedConditions.visibilityOfElementLocated(selection));
    }

    public static void waitForAjaxCall() {
        try {
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.MILLISECONDS);
            List<WebElement> ajaxelement = driver.findElements(By.xpath("//body[contains(@class,'x-masked')]"));
            if (ajaxelement.size() > 0) {
                System.out.println("Ajax call started"+ Instant.now());
                WebDriverWait wait1 = new WebDriverWait(driver, 4, 20);

                wait1.until(
                        ExpectedConditions.presenceOfElementLocated(By.xpath("//body[contains(@class,'x-masked')]")));
                // wait1=new WebDriverWait(driver,4,20);
                // wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//body[(@class='x-body
                // x-gecko x-layout-fit x-border-box x-container x-container-default')]")));
                // //GW8
                wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                        "//body[(@class='x-body x-webkit x-chrome x-border-layout-ct x-border-box x-container x-container-default')]"))); // GW9
                System.out.println("Ajax call ended"+Instant.now());
            } else {
            }
        } catch (Exception e) {
            System.out.println("No ajax call"+Instant.now());
             log.info("No AJAX CALL ");//e.printStackTrace();
        }
    }

    public static boolean waitForJSandJQueryToLoad() {

        WebDriverWait wait = new WebDriverWait(driver, 2);
        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {

                    return ((Long)((JavascriptExecutor)driver).executeScript("return jQuery.active") == 0);
                }
                catch (Exception e) {
                    // no jQuery present
                    log.error("no jQuery present"+ e.getMessage());
                    return true;
                }
                finally {
                    System.out.println("JQuery call ended"+ Instant.now());
                    log.info("JQuery call ended"+ Instant.now());
                }
            }
        };

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {

                try {
                    return ((JavascriptExecutor)driver).executeScript("return document.readyState")
                            .toString().equals("complete");
                } catch (Exception e) {log.info(e.getMessage());
                    return true;
                } finally {
                    System.out.println("JS call ended"+ Instant.now());
                    log.info("JS call ended"+ Instant.now());
                }
            }
        };

        return wait.until(jQueryLoad) && wait.until(jsLoad);
    }

    public static void closeDriver() {
        if (driver != null) {
            try {
                //driver.close(); // uncomment this and below line
                //driver.quit(); // fails in current geckodriver! TODO: Fixme
            } catch (NoSuchMethodError nsme) {log.info(nsme.getMessage()); // in case quit fails
            } catch (NoSuchSessionException nsse) {log.info(nsse.getMessage()); // in case close fails
            } catch (SessionNotCreatedException snce) {log.info(snce.getMessage());} // in case close fails
            driver = null;
        }
    }
}
