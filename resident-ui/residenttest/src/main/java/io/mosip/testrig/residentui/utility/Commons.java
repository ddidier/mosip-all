package io.mosip.testrig.residentui.utility;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Random;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import io.mosip.testrig.residentui.kernel.util.ConfigManager;


public class Commons extends BaseClass{
	private static final Logger logger = Logger.getLogger(Commons.class);

	public static String appendDate="0"+getDateTime();

	public static String getDateTime(){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

	public  static void click(WebDriver driver, By by) throws IOException, InterruptedException {
		logger.info("Clicking " + by );
		wait(1000);
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			wait.until(ExpectedConditions.elementToBeClickable(by));		
			driver.findElement(by).click();
		}catch (Exception e) {
			try {		
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", driver.findElement(by));
			}catch (Exception exception) {
				Reporter.log("<p><img src='data:image/png;base64," + Screenshot.ClickScreenshot(driver) + "' width='900' height='450'/></p>");
				throw exception;  // Re-throw the caught exception
			}
		}
	}

	public static void clickWebelement(WebDriver driver, By by) throws IOException {
		logger.info("Clicking " + by);

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", element);

		}catch (Exception e) {
			try {		
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", driver.findElement(by));
			}catch (Exception exception) {
				Reporter.log("<p><img src='data:image/png;base64," + Screenshot.ClickScreenshot(driver) + "' width='900' height='450'/></p>");
				throw exception;  // Re-throw the caught exception
			}
		}
	}


	public static void enter(WebDriver driver, By by,String value) throws IOException {
		logger.info("Entering " + by +value);
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			driver.findElement(by).clear();
			driver.findElement(by).sendKeys(value);
		}catch (Exception e) {
			try {			
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", driver.findElement(by));
			}catch (Exception exception) {
				Reporter.log("<p><img src='data:image/png;base64," + Screenshot.ClickScreenshot(driver) + "' width='900' height='450'/></p>");
				throw exception;  // Re-throw the caught exception
			}
		}
	}

	public static void uploadImage(WebDriver driver, By by,String path) throws IOException {
		logger.info("Entering " + by +path);
		try {
			wait(1000);
			driver.findElement(by).sendKeys(path);
		}catch (Exception e) {
			try {			
				wait(1000);
				driver.findElement(by).sendKeys(path);
			}catch (Exception exception) {
				Reporter.log("<p><img src='data:image/png;base64," + Screenshot.ClickScreenshot(driver) + "' width='900' height='450'/></p>");
				throw exception;  // Re-throw the caught exception
			}
		}
	}

	public static void dropdown(WebDriver driver, By by) throws Exception {
		logger.info("Selecting DropDown Index Zero Value " + by);

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
			WebElement dropdownElement = wait.until(ExpectedConditions.elementToBeClickable(by));
			dropdownElement.click();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[aria-owns]")));
			String att = dropdownElement.getAttribute("aria-owns");
			String[] list = att.split(" ");
			if (list.length > 0) {
				click(driver, By.id(list[0]));
			} else {
				throw new NoSuchElementException("Dropdown list not found.");
			}
		} catch (Exception e) {
			try {
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
				WebElement dropdownElement = wait.until(ExpectedConditions.elementToBeClickable(by));
				dropdownElement.click();
				wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[aria-owns]")));
				String att = dropdownElement.getAttribute("aria-owns");
				String[] list = att.split(" ");
				if (list.length > 0) {
					click(driver, By.id(list[0]));
				} else {
					throw new NoSuchElementException("Dropdown list not found.");
				}

			}catch (Exception exception) {
				Reporter.log("<p><img src='data:image/png;base64," + Screenshot.ClickScreenshot(driver) + "' width='900' height='450'/></p>");
				throw exception;  // Re-throw the caught exception
			}
		}
	}

	public static void dropdown(WebDriver driver, By by,By value) throws IOException, InterruptedException	{
		logger.info("Selecting DropDown By Value " + by +value ); 
		click(driver,by);
		click(driver,value);		
	}


	public static void dropdown(WebDriver driver, By by, String value) {
		logger.info("Selecting DropDown By Value " + by + value);
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
			wait.until(ExpectedConditions.elementToBeClickable(by));
			click(driver, by);
			String val = "'" + value + "'";
			By valueBy = By.xpath("//span[contains(text()," + val + ")]");

			wait.until(ExpectedConditions.elementToBeClickable(valueBy));
			click(driver, valueBy);
		} catch (Exception e) {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", driver.findElement(by));		
			String val = "'" + value + "'";
			By valueBy = By.xpath("//span[contains(text()," + val + ")]");
			executor.executeScript("arguments[0].click();", driver.findElement(valueBy));			
		}
	}

	public static String generateRandomAlphabetString() {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		Random random = new Random();
		StringBuilder stringBuilder = new StringBuilder(15);

		for (int i = 0; i < 15; i++) {
			int index = random.nextInt(alphabet.length());
			stringBuilder.append(alphabet.charAt(index));
		}

		return stringBuilder.toString();
	}

	public static boolean isDisplayed(WebDriver driver ,By by) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(ExpectedConditions.presenceOfElementLocated(by));			
			return driver.findElement(by).isDisplayed();
		}catch(Exception e) {
			return false;
		}
	}

	public static void wait(int wait) {
		try {
			Thread.sleep(wait);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void assertCheck(By by,String message) throws IOException {
		try {
			if(!ConfigManager.getimagename().equals("0.9.0")) {
			wait(2000);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
			boolean isDisplayed = Commons.isDisplayed(driver, by);
			Assert.assertTrue(isDisplayed,message);
			}

		}catch (Exception te) {
			Reporter.log("<p><img src='data:image/png;base64," + Screenshot.ClickScreenshot(driver) + "' width='900' height='450'/></p>");
			throw te;

		}  catch (AssertionError e) {
			Reporter.log("<p><img src='data:image/png;base64," + Screenshot.ClickScreenshot(driver) + "' width='900' height='450'/></p>");
			throw e;
		}
	}

	public static void assertCheckString(String text,String message) throws IOException {
		try {
			if(!ConfigManager.getimagename().equals("0.9.0")) {
			wait(2000);
			Assert.assertNotNull(text,message);
			}

		} catch (AssertionError e) {
			Reporter.log("<p><img src='data:image/png;base64," + Screenshot.ClickScreenshot(driver) + "' width='900' height='450'/></p>");
			throw e;
		}
	}
}
