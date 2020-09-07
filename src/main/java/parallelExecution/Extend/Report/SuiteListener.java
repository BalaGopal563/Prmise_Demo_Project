package parallelExecution.Extend.Report;

import java.io.File;
import java.io.IOException;


import org.testng.ISuite;
import org.testng.ISuiteListener;

import com.Automation.Extend.Reporter;


public class SuiteListener implements ISuiteListener {

	public static String OUTPUT_FOLDER = null;

	@Override
	public void onFinish(ISuite arg0) {
		Reporter.closeReport();
	}

	@Override
	public void onStart(ISuite arg0) {
		// Start the report here
		try {
			OUTPUT_FOLDER=new File(".").getCanonicalPath() + File.separator
					+ "Reports" + File.separator + "Report" + File.separator;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Reporter.createReporter(OUTPUT_FOLDER + "HtmlReport.html", true);
	}
}