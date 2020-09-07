package Practice.Automation;

import static org.testng.Assert.assertTrue;

import org.apache.commons.collections.map.HashedMap;
import org.testng.annotations.Test;

import com.project.utill.CommonFunction;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Unit test for simple App.
 */
public class Add_Promise extends BaseClass {
	@Test
	public void simpleTestClass() {
		String testCaseName = this.getClass().getSimpleName();

		try {
			HashedMap testData = CommonFunction.getTestData("testDataPromiseApp.xlsx", "TS_01", "Add_Promise");
			homePage = verifyPageLoad();
			CommonFunction.extendReportSaveScreen(testCaseName, driver);
			homePage.enterDetails(driver, testData, testCaseName);
			PromisesLogPage promisesLogPage = homePage.navigateToPromisesLogPage(driver, testCaseName);
			LogPromisePage logPromisePage = promisesLogPage.navigateToLogPromisePage(driver, testData, testCaseName);
			logPromisePage.enterDetails(driver, testData, testCaseName);
			assertTrue(logPromisePage.validaterecordEnter(driver, testData, testCaseName));

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block

			BaseClass.test.log(LogStatus.ERROR, e.getMessage());
			assertTrue(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block

			BaseClass.test.log(LogStatus.ERROR, e.getMessage());
			assertTrue(false);
		}
	}
}
