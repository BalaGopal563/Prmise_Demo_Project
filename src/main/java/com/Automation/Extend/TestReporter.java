package com.Automation.Extend;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.model.ITest;

import parallelExecution.Extend.Interface.ITestReporter;


public class TestReporter implements ITestReporter {

	private Map<Long, ExtentTest> testReporters = new ConcurrentHashMap<Long, ExtentTest>();
	private ExtentReports reporter;

	public TestReporter(ExtentReports reporter) {
		this.reporter = reporter;
	}

	public void startTest(String testName) {
		Long threadId = Thread.currentThread().getId();
		if (!testReporters.containsKey(threadId))
			testReporters.put(threadId, reporter.startTest(testName));
	}

	public void startTest(String testName, String testDescription) {
		Long threadId = Thread.currentThread().getId();
		if (!testReporters.containsKey(threadId))
			testReporters.put(threadId, reporter.startTest(testName, testDescription));
	}

	public void endTest() {
		Long threadId = Thread.currentThread().getId();
		if (testReporters.containsKey(threadId)) {
			reporter.endTest(getTestReporter());
			testReporters.remove(threadId);
		}
	}

	@Override
	public void log(LogStatus logStatus, String stepName, String details) {
		getTestReporter().log(logStatus, stepName, details);
	}

	@Override
	public void log(LogStatus logStatus, String details) {
		getTestReporter().log(logStatus, details);
	}

	@Override
	public void log(LogStatus logStatus, String stepName, Throwable t) {
		getTestReporter().log(logStatus, stepName, t);
	}

	@Override
	public void log(LogStatus logStatus, Throwable t) {
		getTestReporter().log(logStatus, t);
	}

	@Override
	public void setDescription(String description) {
		getTestReporter().setDescription(description);
	}

	@Override
	public String getDescription() {
		return getTestReporter().getDescription();
	}

	@Override
	public void setStartedTime(Date startedTime) {
		getTestReporter().setStartedTime(startedTime);
	}

	@Override
	public Date getStartedTime() {
		return getTestReporter().getStartedTime();
	}

	@Override
	public void setEndedTime(Date endedTime) {
		getTestReporter().setEndedTime(endedTime);
	}

	@Override
	public Date getEndedTime() {
		return getTestReporter().getEndedTime();
	}

	@Override
	public String addScreenCapture(String imgPath) {
		return getTestReporter().addScreenCapture(imgPath);
	}

	@Override
	public String addBase64ScreenShot(String base64) {
		return getTestReporter().addBase64ScreenShot(base64);
	}

	@Override
	public String addScreencast(String screencastPath) {
		return getTestReporter().addScreencast(screencastPath);
	}

	@Override
	public ExtentTest assignCategory(String... categories) {
		return getTestReporter().assignCategory(categories);
	}

	@Override
	public ExtentTest assignAuthor(String... authors) {
		return getTestReporter().assignAuthor(authors);
	}

	@Override
	public ExtentTest appendChild(ExtentTest node) {
		return getTestReporter().appendChild(node);
	}

	@Override
	public LogStatus getRunStatus() {
		return getTestReporter().getRunStatus();
	}

	@Override
	public ITest getTest() {
		return getTestReporter().getTest();
	}

	private ExtentTest getTestReporter() {
		Long threadId = Thread.currentThread().getId();
		if (testReporters.containsKey(threadId)) {
			return testReporters.get(threadId);
		}
		throw new Error("Trying to get an extent test which is not yet created. ThreadId: " + threadId);
	}

	
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		// TODO Auto-generated method stub
		 for (ISuite suite : suites) {
	            Map<String, ISuiteResult> result = suite.getResults();
	  
	            for (ISuiteResult r : result.values()) {
	                ITestContext context = r.getTestContext();
	  
	                buildTestNodes(context.getPassedTests(), LogStatus.PASS);
	                buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
	                buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
	            }
	        }
		
	}
	 private void buildTestNodes(IResultMap tests, LogStatus status) {
	        ExtentTest test;
	  
	        if (tests.size() > 0) {
	            for (ITestResult result : tests.getAllResults()) {
	                test = reporter.startTest(result.getMethod().getMethodName());
	  
	               /* test.getTest().startedTime = getTime(result.getStartMillis());
	                test.getTest().endedTime = getTime(result.getEndMillis());*/
	  
	                for (String group : result.getMethod().getGroups())
	                    test.assignCategory(group);
	  
	                String message = "Test " + status.toString().toLowerCase() + "ed";
	  
	                if (result.getThrowable() != null)
	                    message = result.getThrowable().getMessage();
	  
	                test.log(status, message);
	  
	                reporter.endTest(test);
	            }
	        }
	    }
}