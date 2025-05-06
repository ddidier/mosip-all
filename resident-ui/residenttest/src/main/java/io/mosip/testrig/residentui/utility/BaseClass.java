package io.mosip.testrig.residentui.utility;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.mosip.testrig.residentui.kernel.util.ConfigManager;
import io.mosip.testrig.residentui.service.BaseTestCase;
import io.mosip.testrig.residentui.testcase.LoginTest;

public class BaseClass {
	private static final Logger logger = Logger.getLogger(BaseClass.class);
	protected static  WebDriver driver;
	protected Map<String, Object> vars;
	protected static JavascriptExecutor js;
	protected String langcode;
	public static LoginTest login;
	protected String envPath = ConfigManager.getiam_residentportal_path();
	protected String env = ConfigManager.getiam_apienvuser();
	protected String externalurl = System.getProperty("externalurl");
	protected String password = System.getProperty("password");
	protected String data = Commons.appendDate;
	public int countScenarioPassed=0;

	@BeforeMethod
	public void setUp() throws Exception {
		logger.info("Start set up");
		if(System.getProperty("os.name").equalsIgnoreCase("Linux") && ConfigManager.getDocker().equals("yes") ) {


			logger.info("Docker start");
			String configFilePath ="/usr/bin/chromedriver";
			System.setProperty("webdriver.chrome.driver", configFilePath);

		}else {
			WebDriverManager.chromedriver().setup();
			logger.info("window chrome driver start");
		}
		ChromeOptions options = new ChromeOptions();
		String headless=ConfigManager.getHeadless();
		if(headless.equalsIgnoreCase("yes")) {
			logger.info("Running is headless mode");
			options.addArguments("--headless", "--disable-gpu","--no-sandbox", "--window-size=1920x1080","--disable-dev-shm-usage");


		}


		driver = new ChromeDriver(options);
		js = (JavascriptExecutor) driver;
		vars = new HashMap<String, Object>();
		driver.get(envPath);
		logger.info("launch url --"+envPath);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();



		String language=ConfigManager.getloginlang();
		try {
			if(!language.equals("sin")) {
				Commons.dropdown( driver, By.id("languages"), By.id("lang"+language));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		login = new LoginTest();
	}
	@AfterMethod
	public void afterMethod(ITestResult result) {
		MockSMTPListener mockSMTPListener = new MockSMTPListener();
		mockSMTPListener.bTerminate = true;
		driver.quit();
	}

	public static String envsupportlang() {
		List<String> langs=BaseTestCase.getLanguageList();
		for(String lang:langs) {
			return lang;
		}
		return "";

	}
}
