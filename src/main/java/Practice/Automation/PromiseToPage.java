package Practice.Automation;

import java.text.SimpleDateFormat;

import org.apache.commons.collections.map.HashedMap;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.project.utill.CommonFunction;
import com.project.utill.LoggingClass;
import com.relevantcodes.extentreports.LogStatus;

public class PromiseToPage {

	@FindBy(how = How.XPATH, using = "//select[@name='cboEmp']")
	private WebElement cboEmp;

	@FindBy(how = How.XPATH, using = "//*[@name='txtStartDate']")
	private WebElement txtStartDate;

	@FindBy(how = How.XPATH, using = "//*[@name='btnSearch']")
	private WebElement btnSearch;

	@FindBy(how = How.XPATH, using = "//*[@name='btnReset']")
	private WebElement btnReset;

	@FindBy(how = How.XPATH, using = "//*[@name='btnShowAll']")
	private WebElement btnShowAll;

	public boolean checkCurrentRecord(WebDriver driver, HashedMap testData, String testCaseName) {
		boolean result = false;
		CommonFunction.clickButton(driver, btnSearch, "Search Button");
		result = CommonFunction.verifyElementPresent(driver,
				"//*[text()='" + testData.get("PROMISE_LOG").toString() + "']", "XPATH", 20);
		if (result) {
			BaseClass.test.log(LogStatus.INFO, "Record Found");
			LoggingClass.getLogger().info("Record Found");
			result = true;
		} else {
			BaseClass.test.log(LogStatus.INFO, "Record Not Found");
			LoggingClass.getLogger().info("Record Not Found");
		}
		return result;
	}

	public void enterDetails(WebDriver driver, HashedMap testData, String testCaseName) {
		BaseClass.test.log(LogStatus.INFO, "**** Login Page Screen before Entering the Details ****");
		CommonFunction.extendReportSaveScreen(testCaseName, driver);
		try {
			String dateTimeStamp = new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());

			String dropdownValue = testData.get("PROMISE_FROM").toString();
			String promiseLog = dateTimeStamp;
			CommonFunction.selectDropDown(driver, cboEmp, dropdownValue);
			txtStartDate.sendKeys(promiseLog);
			BaseClass.test.log(LogStatus.INFO, "**** Login Page Screen after Entering the Details ****");
			CommonFunction.extendReportSaveScreen(testCaseName, driver);
		}

		catch (NullPointerException e) {
			BaseClass.test.log(LogStatus.WARNING, "Please check Test Data");
			LoggingClass.getLogger().info("Please check Test Data");
		}
	}

}
