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

public class LoginPage {

	@FindBy(how = How.XPATH, using = "//img[@title='Clarion Promise']")
	private WebElement pageTitle;

	@FindBy(how = How.XPATH, using = "//input[@name='txtUsername']")
	private WebElement userName;

	@FindBy(how = How.XPATH, using = "//input[@name='txtPassword']")
	private WebElement password;

	@FindBy(how = How.XPATH, using = "//input[@value='Login']")
	private WebElement loginButton;

	@FindBy(how = How.XPATH, using = "//font[@color='red']")
	private WebElement errorMessage;

	public void verifyPage(WebDriver driver) {
		if (CommonFunction.verifyElement(driver, pageTitle)) {
			BaseClass.test.log(LogStatus.INFO, "**** Login Page Loaded *****");
			LoggingClass.getLogger().info("**** Login Page Loaded *****");
		} else {
			BaseClass.test.log(LogStatus.INFO, "**** Login Page Loading is Failed *****");
			LoggingClass.getLogger().info("**** Login Page Loading is Failed *****");
		}
	}
	
	public PromisesLogPage navigateToPromisesLogPage(WebDriver driver,String testCaseName){
		CommonFunction.clickButton(driver, loginButton, "Login Button");
		PromisesLogPage pl=PageFactory.initElements(driver,PromisesLogPage.class);
		return pl;
	}

	public boolean validateBlackFileds(WebDriver driver, HashedMap testData, String testCaseName) {
		boolean result = false;
		try {
			String errormess = testData.get("ERROR_MESSAGE").toString();
			CommonFunction.clickButton(driver, loginButton, "Login Button");
			CommonFunction.extendReportSaveScreen(testCaseName, driver);
			result = CommonFunction.verifyElementText(driver, errorMessage, errormess);
		} catch (NullPointerException e) {
			BaseClass.test.log(LogStatus.WARNING, "Please check Test Data");
			LoggingClass.getLogger().info("Please check Test Data");
		}
		return result;
	}
	
	public boolean validateNamePassword(WebDriver driver, HashedMap testData, String testCaseName) {
		boolean result = false;
		if (CommonFunction.verifyElement(driver, userName)) {
			BaseClass.test.log(LogStatus.INFO, "**** Login Page Loaded *****");
			LoggingClass.getLogger().info("**** Login Page Loaded *****");
			result=true;
		} else {
			BaseClass.test.log(LogStatus.INFO, "**** Login Page Loading is Failed *****");
			LoggingClass.getLogger().info("**** Login Page Loading is Failed *****");
		}
		return result;
	}



	public void enterDetails(WebDriver driver, HashedMap testData, String testCaseName) {
		BaseClass.test.log(LogStatus.INFO, "**** Login Page Screen before Entering the Details ****");
		CommonFunction.extendReportSaveScreen(testCaseName, driver);
		try {
			String userNam = testData.get("USER_NAME").toString();
			String passWord = testData.get("PASSWORD").toString();
			userName.sendKeys(userNam);
			password.sendKeys(passWord);
			BaseClass.test.log(LogStatus.INFO, "**** Login Page Screen after Entering the Details ****");
			CommonFunction.extendReportSaveScreen(testCaseName, driver);
		}

		catch (NullPointerException e) {
			BaseClass.test.log(LogStatus.WARNING, "Please check Test Data");
			LoggingClass.getLogger().info("Please check Test Data");
		}
	}

}
