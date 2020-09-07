package Practice.Automation;

import java.text.SimpleDateFormat;

import org.apache.commons.collections.map.HashedMap;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.project.utill.CommonFunction;
import com.project.utill.LoggingClass;
import com.relevantcodes.extentreports.LogStatus;

public class LogPromisePage {

	@FindBy(how = How.XPATH, using = "//select[@name='cboEmp']")
	private WebElement cboEmp;

	@FindBy(how = How.XPATH, using = "//*[@name='txtPromise']")
	private WebElement txtPromise;

	@FindBy(how = How.ID, using = "btnSubmit")
	private WebElement btnSubmit;

	@FindBy(how = How.XPATH, using = "//*[@name='btnReset']")
	private WebElement btnReset;

	@FindBy(how = How.XPATH, using = "//*[@name='btnCancel']")
	private WebElement btnCancel;

	@FindBy(how = How.XPATH, using = "//font[@color='#FF0000']")
	private WebElement successMessage;

	public boolean validaterecordEnter(WebDriver driver, HashedMap testData, String testCaseName) {
		boolean result = false;
		try {
			String errormess = testData.get("TEXT_MESSAGE").toString();
			CommonFunction.clickButton(driver, btnSubmit, "Submit Button");
			CommonFunction.extendReportSaveScreen(testCaseName, driver);
			result = CommonFunction.verifyElementText(driver, successMessage, errormess);
		} catch (NullPointerException e) {
			BaseClass.test.log(LogStatus.WARNING, "Please check Test Data");
			LoggingClass.getLogger().info("Please check Test Data");
		}
		return result;
	}

	public void enterDetails(WebDriver driver, HashedMap testData, String testCaseName) {
		BaseClass.test.log(LogStatus.INFO, "**** Login Page Screen before Entering the Details ****");
		CommonFunction.extendReportSaveScreen(testCaseName, driver);
		try {
			String dateTimeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

			String dropdownValue = testData.get("PROMISE_FROM").toString();
			String promiseLog = testData.get("PROMISE_LOG").toString() + " Time: " + dateTimeStamp;
			testData.put("PROMISE_LOG", promiseLog);
			CommonFunction.selectDropDown(driver, cboEmp, dropdownValue);
			txtPromise.sendKeys(promiseLog);
			BaseClass.test.log(LogStatus.INFO, "**** Login Page Screen after Entering the Details ****");
			CommonFunction.extendReportSaveScreen(testCaseName, driver);
		}

		catch (NullPointerException e) {
			BaseClass.test.log(LogStatus.WARNING, "Please check Test Data");
			LoggingClass.getLogger().info("Please check Test Data");
		}
	}

	public PromiseToPage naviageToPromiseToPage(WebDriver driver, String testCaseName) {
		CommonFunction.clickButton(driver, btnSubmit, "Submit Button");
		PromiseToPage promiseToPage = PageFactory.initElements(driver, PromiseToPage.class);
		return promiseToPage;
	}

}
