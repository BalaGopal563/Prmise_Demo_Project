package Practice.Automation;

import org.apache.commons.collections.map.HashedMap;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.project.utill.CommonFunction;
import com.project.utill.LoggingClass;
import com.relevantcodes.extentreports.LogStatus;

public class PromisesLogPage {

	@FindBy(how = How.XPATH, using = "//a[text()='Log Promise']")
	private WebElement logPromise;

	@FindBy(how = How.XPATH, using = "//a[text()='Promises From']")
	private WebElement promissesFrom;

	@FindBy(how = How.XPATH, using = "//a[text()='My Account']")
	private WebElement myAccount;

	@FindBy(how = How.XPATH, using = "//a[text()='Promises To']")
	private WebElement promissesTo;

	@FindBy(how = How.XPATH, using = "//*[@class='lightgrey']/b[text()='HOME']")
	private WebElement home;

	@FindBy(how = How.XPATH, using = "//*[@class='lightgrey']/b[text()='LOGOUT']")
	private WebElement logout;

	public LogPromisePage navigateToLogPromisePage(WebDriver driver, HashedMap testData, String testCaseName) {

		CommonFunction.clickButton(driver, logPromise, "Log Promise");
		LogPromisePage logPromisePage = PageFactory.initElements(driver, LogPromisePage.class);
		return logPromisePage;
	}

	public LoginPage navigateToLoginPage(WebDriver driver, HashedMap testData, String testCaseName) {

		CommonFunction.clickButton(driver, logout, "Logout button");
		LoginPage logPromisePage = PageFactory.initElements(driver, LoginPage.class);
		return logPromisePage;
	}

	public boolean verifyLogIn(WebDriver driver, HashedMap testData, String testCaseName) {
		boolean result = false;
		CommonFunction.extendReportSaveScreen(testCaseName, driver);
		if (CommonFunction.verifyElement(driver, home) && CommonFunction.verifyElement(driver, logout)) {
			BaseClass.test.log(LogStatus.INFO, "**** Successfully Logged In ****");
			LoggingClass.getLogger().info("**** Successfully Logged In ****");
			result = true;
		} else {
			BaseClass.test.log(LogStatus.ERROR, "**** LogIn Failed ****");
			LoggingClass.getLogger().error("**** LogIn Failed ****");
		}
		return result;
	}

}
