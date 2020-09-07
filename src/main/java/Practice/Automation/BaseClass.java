package Practice.Automation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.Automation.Extend.Reporter;
import com.project.utill.CommonFunction;
import com.project.utill.LoggingClass;
import com.relevantcodes.extentreports.LogStatus;

import io.github.bonigarcia.wdm.WebDriverManager;
import parallelExecution.Extend.Interface.ITestReporter;

/**
 * Hello world!
 *
 */
@Listeners(parallelExecution.Extend.Report.SuiteListener.class)
public class BaseClass {
	protected WebDriverWait wait;
	// ATUTestRecorder recorder;
	/**
	 * To Read the environment details
	 */
	// private ScreenRecorder screenRecorder;
	String screenshotBasePath;
	String logBasePath;
	String logFile;
	String gecoDriverPath;
	String permenetscreenshotPath;
	public Logger logger = Logger.getLogger(BaseClass.class);

	public WebDriver driver;
	/*
	 * public static ExtentReports extent; public static ExtentTest test;
	 */
	public static String browserValue = "";
	public static File f;
	// public static ITestReporter test;
	public LoginPage homePage;
	public static ITestReporter test;
	public BaseClass() {

		// TODO Auto-generated constructor stub
		try {

			PropertyConfigurator.configure(new File(".").getCanonicalPath() + File.separator + "src" + File.separator
					+ "test" + File.separator + "resource" + File.separator + "log4j.properties");

			// Location for Screen shot
			screenshotBasePath = new File(".").getCanonicalPath() + File.separator + "Reports" + File.separator + "Temp"
					+ File.separator + "screenshots";

			permenetscreenshotPath = new File(".").getCanonicalPath() + File.separator + "test-output" + File.separator
					+ "screenshots";
			// Instantiating logger
			logFile = new File(".").getCanonicalPath() + File.separator + "Reports" + File.separator
					+ "LowLevelReport.log";
			FileOutputStream writer = new FileOutputStream(logFile);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	protected LoginPage verifyPageLoad() throws ClassNotFoundException, InterruptedException {

		// Initializing the objects
		LoggingClass.getLogger().info("Verifying home page loaded ");
		homePage = PageFactory.initElements(driver, LoginPage.class);

		// Verify page is present or not.
		 homePage.verifyPage(driver);
		LoggingClass.getLogger().info(this.getClass().getSimpleName() + " Login Page is sucessfully loaded");
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " Login Page is sucessfully loaded");
		LoggingClass.getLogger().info(this.getClass().getSimpleName() + " Login Page factory  Initiated ");
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " Login Page factory  Initiated ");
		return homePage;

	}
	@BeforeSuite
	public void reporting() {
		try {
			File index = new File(new File(".").getCanonicalPath() + File.separator + "Reports" + File.separator
					+ "Report" + File.separator + "screenshots");
			String[] directories = index.list(new FilenameFilter() {
				public boolean accept(File current, String name) {
					return new File(current, name).isDirectory();
				}
			});
			for (String f : directories) {
				CommonFunction.deleteFile(new File(new File(".").getCanonicalPath() + File.separator + "Reports"
						+ File.separator + "Report" + File.separator + "screenshots" + File.separator + f));
			}

			CommonFunction.deleteFile(new File(
					new File(".").getCanonicalPath() + File.separator + "Reports" + File.separator + "Report"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {

		}

	}
	@BeforeTest
	public void classSetUp() {

		test = Reporter.getTestReporter();
	}
	@Parameters("browser")
	@BeforeMethod
	public void setup(@Optional("chrome") String browser, Method caller) {
		test.startTest(CommonFunction.getTestCaseName(this.getClass().getCanonicalName()), "");
		boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString()
				.indexOf("-agentlib:jdwp") > 0;
		try {
			if (isDebug) {
				Runtime.getRuntime().exec("taskkill /F /IM " + CommonFunction.getEnv("GeckoDriver"));
				Runtime.getRuntime().exec("taskkill /F /IM " + CommonFunction.getEnv("ChromeDriver"));
				// Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		LoggingClass.getLogger()
				.info("Starting Test Case:" + CommonFunction.getTestCaseName(this.getClass().getCanonicalName())
						+ "<a name=**" + CommonFunction.getTestCaseName(this.getClass().getCanonicalName())
						+ "></a>**");
		test.log(LogStatus.INFO,
				"Starting Test Case:" + CommonFunction.getTestCaseName(this.getClass().getCanonicalName())
						+ "<a name=**" + CommonFunction.getTestCaseName(this.getClass().getCanonicalName())
						+ "></a>**");
		String homePageUrl = CommonFunction.getEnv("loginurl");
		try {
			browserValue = browser;
			if (browser.equalsIgnoreCase("firefox")) {
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				firefoxOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

				firefoxOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
				firefoxOptions.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
				Proxy proxy = new Proxy();
				proxy.setProxyType(Proxy.ProxyType.AUTODETECT);
				firefoxOptions.setCapability(CapabilityType.PROXY, proxy);
				firefoxOptions.setCapability("acceptInsecureCerts", true);
				firefoxOptions.setCapability("marionette", false);
				// firefoxOptions.setHeadless(true);

				try {
					WebDriverManager.firefoxdriver().setup();
					/* Instantiating driver */
					driver = new FirefoxDriver(firefoxOptions);
				} catch (Exception e) {

				}
			} else if (browser.equalsIgnoreCase("chrome")) {
				Proxy proxy = new Proxy();
				proxy.setProxyType(Proxy.ProxyType.AUTODETECT);
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				chromeOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
				chromeOptions.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
				chromeOptions.setCapability(CapabilityType.PROXY, proxy);
				chromeOptions.setCapability("acceptInsecureCerts", true);
				chromeOptions.setCapability("marionette", true);
				try {
					WebDriverManager.chromedriver().setup();
					driver = new ChromeDriver(chromeOptions);
				} catch (Exception e) {
					gecoDriverPath = new File(".").getCanonicalPath() + File.separator + "src" + File.separator + "test"
							+ File.separator + "resource" + File.separator + "lib" + File.separator
							+ CommonFunction.getEnv("ChromeDriver");
					System.setProperty("webdriver.chrome.driver", gecoDriverPath);
					driver = new ChromeDriver(chromeOptions);
				}
			}

		} catch (Exception e) {
		}
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(250, TimeUnit.SECONDS);

		/* Navigating to home page */
		driver.get(homePageUrl);

		/* Maximize the window */

		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		
	}
	@AfterMethod
	public final void tearDown(ITestResult result) throws IOException {
		try {
			LoggingClass.getLogger().info("The test result for " + this.getClass().getSimpleName() + " is "
					+ (boolean) (result.getStatus() == 1));
			test.log(LogStatus.INFO, "The test result for " + this.getClass().getSimpleName() + " is "
					+ (boolean) (result.getStatus() == 1));

			if (!result.isSuccess()) {
				test.log(LogStatus.FAIL, this.getClass().getSimpleName() + " was Failled ");
				LoggingClass.getLogger().info(this.getClass().getSimpleName() + " was Failled ");

			} else if (result.isSuccess()) {
				test.log(LogStatus.PASS, this.getClass().getSimpleName() + " test case is passed");
			} else {
				test.log(LogStatus.SKIP, this.getClass().getSimpleName() + " test case was Skipped");
			}
		} catch (Exception e) {

		} finally {
			test.endTest();
			Reporter.flushReport();
			driver.close();

		}

	}
	@AfterSuite
	public final void tearsuite(ITestContext testContext) throws IOException {
		try {
			String dateTimeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
			FileUtils.copyFile(new File(logFile),
					new File(logBasePath + File.separator + " " + dateTimeStamp + ".log"));
			Runtime.getRuntime().exec("taskkill /F /IM " + CommonFunction.getEnv("GeckoDriver"));
			FileUtils.copyDirectory(
					new File(new File(".").getCanonicalPath() + File.separator + "Reports" + File.separator + "Report"),
					new File(new File(".").getCanonicalPath() + File.separator + "Reports" + File.separator
							+ "Report backUp" + File.separator + testContext.getSuite().getName() + File.separator
							+ "Report" + dateTimeStamp));
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} finally {

			// extent.close();
		}
	}

}
