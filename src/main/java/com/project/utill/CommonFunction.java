package com.project.utill;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import Practice.Automation.BaseClass;

public class CommonFunction {
	InputStream inputStream;
	String result = "";

	static Properties prop = new Properties();

	@SuppressWarnings("deprecation")
	public static void clickButton(WebDriver driver, WebElement el, String name) {
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(20, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			wait.until(ExpectedConditions.elementToBeClickable(el));

			el.click();
			LoggingClass.getLogger().info("Element " + name + " is  clicked on UI");
			BaseClass.test.log(LogStatus.INFO, "Element " + name + " is  clicked on UI");

		} catch (TimeoutException e) {

			LoggingClass.getLogger().info("Element " + name + " is not clickable on UI");
			BaseClass.test.log(LogStatus.INFO, "Element " + name + " is not clickable on UI");
		}
	}

	public static void selectDropDown(WebDriver driver, WebElement element, String dropDownValue) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOf(element));
			LoggingClass.getLogger().info("Element " + element + " is present on current Page");
			BaseClass.test.log(LogStatus.INFO, "Element " + element + " is present on current Page");

			Select select = new Select(element);
			select.selectByVisibleText(dropDownValue);

		} catch (TimeoutException Except) {

			LoggingClass.getLogger().info("Element  " + element + " is Not visible after 120 sec of page load");
			BaseClass.test.log(LogStatus.INFO, "Element  " + element + " is Not visible after 120 sec of page load");
		} catch (Exception Except) {
			LoggingClass.getLogger().info("Element  " + element + " is Not visible after 120 sec of page load");
			BaseClass.test.log(LogStatus.INFO, "Element  " + element + " is Not visible after 120 sec of page load");
		}

	}

	public static boolean verifyElement(WebDriver driver, WebElement element, int sec, String xpath) {
		boolean[] result = new boolean[] { true };

		try {
			WebDriverWait wait = new WebDriverWait(driver, sec);
			wait.until(ExpectedConditions.visibilityOf(element));
			LoggingClass.getLogger().info("Element " + element + " is present on current Page");
			BaseClass.test.log(LogStatus.INFO, "Element " + element + " is present on current Page");

		} catch (TimeoutException Except) {

			LoggingClass.getLogger().info("Element  " + element + " is Not visible after 120 sec of page load");
			BaseClass.test.log(LogStatus.INFO, "Element  " + element + " is Not visible after 120 sec of page load");
			List<WebElement> li = driver.findElements(By.xpath(xpath));
			if (!(li.size() > 0)) {
				result[0] = false;
			}
			for (WebElement s : li) {
				result[0] = verifyElement(driver, s);
				if (result[0]) {
					break;
				}
			}
		} catch (Exception Except) {
			LoggingClass.getLogger().info("Element  " + element + " is Not visible after 120 sec of page load");
			BaseClass.test.log(LogStatus.INFO, "Element  " + element + " is Not visible after 120 sec of page load");
			result[0] = false;
		}

		return result[0];

	}

	public static String getInputText(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		if (verifyElement(driver, element)) {
			String detail = element.getText();
			return detail;
		}

		return "";
	}

	public static HashedMap getTestData(String fileName, String sheetName, String testCaseId)
			throws FileNotFoundException, IOException {
		XSSFRow row = null;
		XSSFCell cell = null;
		try {
			String filePath = new File(".").getCanonicalPath() + File.separator + "src" + File.separator + "test"
					+ File.separator + "resource" + File.separator + "testData" + File.separator + fileName;

			// Establish connection to work sheet

			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(filePath));
			XSSFSheet sheet = wb.getSheet(sheetName);
			Hashtable<String, Integer> excelrRowColumnCount = new Hashtable<String, Integer>();
			excelrRowColumnCount = findRowColumnCount(sheet, excelrRowColumnCount);

			// function call to find excel header fields
			Hashtable<String, Integer> excelHeaders = new Hashtable<String, Integer>();
			excelHeaders = readExcelHeaders(sheet, excelHeaders, excelrRowColumnCount);
			HashedMap data = new HashedMap();
			ArrayList<String> header = new ArrayList<String>();
			ArrayList<String> matcher = new ArrayList<String>();

			// Get all header
			row = sheet.getRow(0);
			if (row != null) {
				for (int c = 0; c < excelrRowColumnCount.get("ColumnCount"); c++) {
					cell = sheet.getRow(0).getCell(c);
					if (cell != null) {
						String temp = convertXSSFCellToString(row.getCell(c));
						header.add(temp);
					}
				}
			}

			// Get test data set
			for (int r = 1; r < excelrRowColumnCount.get("RowCount"); r++) {
				row = sheet.getRow(r);
				if (row != null) {
					XSSFCell tempCell = sheet.getRow(r).getCell(0);
					if (tempCell != null) {
						String tcID = convertXSSFCellToString(row.getCell(0));
						if (tcID.equalsIgnoreCase(testCaseId)) {
							matcher.add(tcID);
							for (int c = 1; c < excelrRowColumnCount.get("ColumnCount"); c++) {
								cell = sheet.getRow(r).getCell(c);
								String temp = convertXSSFCellToString(row.getCell(c));
								matcher.add(temp);
							}
						}
					}
				}
			}

			// Add all the test data to a Map
			for (int i = 0; i < matcher.size(); i++) {
				matcher.size();
				data.put(header.get(i), matcher.get(i));
			}
			wb.close();
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String convertXSSFCellToString(XSSFCell cell) {
		String cellValue = null;
		if (cell != null) {
			cellValue = cell.toString();
			cellValue = cellValue.trim();
		} else {
			cellValue = "";
		}
		return cellValue;
	}

	private static Hashtable<String, Integer> findRowColumnCount(XSSFSheet sheet,
			Hashtable<String, Integer> rowColumnCount) {

		XSSFRow row = null;
		int rows;
		rows = sheet.getPhysicalNumberOfRows();
		int cols = 0;
		int tmp = 0;
		int counter = 0;
		String temp = null;

		for (int i = 0; i < 10 || i < rows; i++) {
			row = sheet.getRow(i);
			if (row != null) {
				temp = convertXSSFCellToString(row.getCell(0));
				if (!temp.equals("")) {
					counter++;
				}
				tmp = sheet.getRow(i).getPhysicalNumberOfCells();
				if (tmp > cols)
					cols = tmp;
			}
		}

		rowColumnCount.put("RowCount", counter);
		rowColumnCount.put("ColumnCount", cols);

		return rowColumnCount;
	}

	private static Hashtable<String, Integer> readExcelHeaders(XSSFSheet sheet, Hashtable<String, Integer> excelHeaders,
			Hashtable<String, Integer> rowColumnCount) {

		XSSFRow row = null;
		XSSFCell cell = null;
		for (int r = 0; r < rowColumnCount.get("RowCount"); r++) {
			row = sheet.getRow(r);

			if (row != null) {
				for (int c = 0; c < rowColumnCount.get("ColumnCount"); c++) {
					cell = row.getCell(c);
					if (cell != null) {
						excelHeaders.put(cell.toString(), c);
					}
				}
				break;
			}
		}
		return excelHeaders;
	}

	public static boolean verifyElementText(WebDriver driver, WebElement element, String title) {
		boolean result = true;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.visibilityOf(element));
			String elementText = CommonFunction.getInputText(element, driver);
			if (elementText.trim().equalsIgnoreCase(title.trim())) {
				LoggingClass.getLogger().info("Element with text  " + title + " is Present");
				BaseClass.test.log(LogStatus.INFO, "Element with text  " + title + " is Present");
			} else {
				LoggingClass.getLogger().error("Expected Text: " + elementText + " Actual Text: " + title);

				BaseClass.test.log(LogStatus.ERROR, "Expected Text: " + elementText + " Actual Text: " + title);
				result = false;
			}
		} catch (TimeoutException Except) {
			result = false;

			LoggingClass.getLogger().error("Element  " + element + " is Not visible after 60 sec of page load");
			BaseClass.test.log(LogStatus.INFO, "Element  " + element + " is Not visible after 60 sec of page load");
		}
		return result;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean verifyElementPresent(WebDriver driver, String element,
			String type, int time) {
		boolean result = true;

		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(time, TimeUnit.SECONDS)
					.pollingEvery((long) .1, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
			if (type.equalsIgnoreCase("XPATH")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated((By
						.xpath(element))));
			} else if (type.equalsIgnoreCase("ID")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated((By
						.id(element))));
			}

		} catch (org.openqa.selenium.NoSuchElementException e) {
			LoggingClass.getLogger().info(
					"Element  " + element + " is Not Present");
			BaseClass.test.log(LogStatus.INFO, "Element  " + element
					+ " is Not Present");
			result = false;
			return result;
		} catch (TimeoutException Except) {
			LoggingClass.getLogger().info(
					"Element  " + element
							+ " is Not visible after 120 sec of page load");
			BaseClass.test.log(LogStatus.INFO, "Element  " + element
					+ " is Not visible after 120 sec of page load");
			result = false;
			return result;
		}
		LoggingClass.getLogger().info("Element  " + element + " is Present");
		BaseClass.test.log(LogStatus.INFO, "Element  " + element
				+ " is Present");
		return result;

	}

	public static boolean verifyElement(WebDriver driver, WebElement element) {
		boolean result = true;

		try {
			WebDriverWait wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.visibilityOf(element));
			LoggingClass.getLogger().info("Element " + element + " is present on current Page");
			BaseClass.test.log(LogStatus.INFO, "Element " + element + " is present on current Page");

		} catch (TimeoutException Except) {

			LoggingClass.getLogger().info("Element  " + element + " is Not visible after 120 sec of page load");
			BaseClass.test.log(LogStatus.INFO, "Element  " + element + " is Not visible after 120 sec of page load");
			result = false;
		} catch (Exception Except) {

			LoggingClass.getLogger().info("Element  " + element + " is Not visible after 120 sec of page load");
			BaseClass.test.log(LogStatus.INFO, "Element  " + element + " is Not visible after 120 sec of page load");
			result = false;
		}

		return result;

	}

	public static void extendReportSaveScreen(String testCaseName, WebDriver driver) {
		// TODO Auto-generated method stub
		String TC = testCaseName;
		String dateTimeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
		String destFile;
		try {
			String filename = "Image" + dateTimeStamp + ".png";
			File scrFile = takeScreenshot(driver);
			destFile = new File(".").getCanonicalPath() + File.separator + "Reports" + File.separator + "Report"
					+ File.separator + "screenshots" + File.separator + TC + File.separator + filename;
			FileUtils.copyFile(scrFile, new File(destFile));

			BaseClass.test.log(LogStatus.INFO,
					BaseClass.test.addScreenCapture("screenshots" + File.separator + TC + File.separator + filename));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			LoggingClass.getLogger().error("Unable to save screen: " + e.toString());
		}
	}

	public static File takeScreenshot(WebDriver driver) {
		// get the driver

		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		return scrFile;
	}

	public String getPropValues() throws IOException {

		try {

			String propFileName = "config.properties";
			inputStream = new FileInputStream(new File(".").getCanonicalPath() + File.separator + "src" + File.separator
					+ "test" + File.separator + "resource" + File.separator + propFileName);
			// inputStream =
			// getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property  file '" + propFileName + "' not found in the classpath");
			}

		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return result;

	}

	public static void deleteFile(File file) {

		String[] myFiles;
		// checking giving file is directory or not
		if (file.isDirectory()) {
			// list all file of directory
			myFiles = file.list();
			for (int i = 0; i < myFiles.length; i++) {
				File myFile = new File(file, myFiles[i]);
				if (!myFile.isDirectory())
					// deleting file
					myFile.delete();
			}
		}
	}

	public static String getProperty(String value) {
		// TODO Auto-generated method stub
		String PropertiesValue = null;
		CommonFunction cf = new CommonFunction();
		try {
			cf.getPropValues();
			if (prop.getProperty(value) == null) {
				LoggingClass.getLogger().error("looks: " + value + " value is missing in properties file");
				PropertiesValue = "null";
			} else {
				PropertiesValue = prop.getProperty(value);
			}
		}

		catch (NullPointerException e) {
			// TODO Auto-generated catch block
			LoggingClass.getLogger().error("looks: " + value + "  is  missing in properties file");

		} catch (IOException e) {

		}

		return PropertiesValue;
	}

	public static String getEnv(String string) {

		// TODO Auto-generated method stub
		String value = "";
		if (System.getProperty(string) != null) {
			value = System.getProperty(string);
		} else {
			value = getProperty(string);
		}
		return value;
	}

	public static String getTestCaseName(String name) {
		// TODO Auto-generated method stub
		String tcNam = null;
		if (!name.equals(null)) {
			tcNam = name.substring(name.lastIndexOf(".") + 1);
		}
		return tcNam;
	}

}
