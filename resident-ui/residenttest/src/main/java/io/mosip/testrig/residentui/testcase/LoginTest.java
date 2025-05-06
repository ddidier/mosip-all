package io.mosip.testrig.residentui.testcase;


import org.openqa.selenium.By;
import org.testng.annotations.Test;


import io.mosip.testrig.residentui.kernel.util.ConfigManager;
import io.mosip.testrig.residentui.utility.BaseClass;
import io.mosip.testrig.residentui.utility.Commons;
import io.mosip.testrig.residentui.utility.MockSMTPListener;
import io.mosip.testrig.residentui.utility.TestRunner;

@Test(groups = "LG")
public class LoginTest extends BaseClass {
	public static int run=0;

	@Test(priority = 0)
	public static void loginTest() throws Exception {

		String envPath = ConfigManager.getiam_residentportal_path();
		String otp = "";
		String externalemail = ConfigManager.getexternalemail();		driver.get(envPath);
		Commons.click( driver, By.xpath("//*[@id='UINservices']"));
		Commons.click( driver, By.id("login_with_otp"));
		Commons.enter( driver, By.id("Otp_mosip-vid"), TestRunner.perpetualVid);
		Commons.click( driver, By.id("get_otp"));
		if(!Commons.isDisplayed(driver, By.id("otp_verify_input"))) {
			Commons.wait(120000);
			Commons.click( driver, By.id("get_otp"));
		}
		otp = MockSMTPListener.getOtp(externalemail);
		System.out.println(otp);
		for (int i = 0; i <= otp.length() - 1; i++) {
			Commons.enter( driver, By.xpath("//*[@id='otp_verify_input']//div//input[" + (i + 1) + "]"),
					Character.toString(otp.charAt(i)));
		}	
		Commons.click( driver, By.id("verify_otp"));
		Commons.wait(2000);
		if(run==0) {
			Commons.click(driver, By.id("authorize_scope"));
			Commons.click(driver, By.id("voluntary_claims"));
			Commons.click(driver, By.id("continue"));
			Commons.click(driver, By.id("dismissBtn"));
			run=1;
		}else if(run==1) {
			Commons.click(driver, By.id("dismissBtn"));
		}
		Commons.assertCheck(By.id("uinservices/viewhistory"),"verify if uin services login using perpetual vid");

	}

	public static void loginTestWithTempraryVID() throws Exception {
		String envPath = ConfigManager.getiam_residentportal_path();

		String otp = "";
		String externalemail = ConfigManager.getexternalemail();
		driver.get(envPath);
		Commons.click( driver, By.xpath("//*[@id='UINservices']"));
		Commons.click( driver, By.id("login_with_otp"));
		Commons.enter( driver, By.id("Otp_mosip-vid"), TestRunner.temporaryVid);
		Commons.click( driver, By.id("get_otp"));
		otp = MockSMTPListener.getOtp(externalemail);
		System.out.println(otp);
		for (int i = 0; i <= otp.length() - 1; i++) {
			Commons.enter( driver, By.xpath("//*[@id='otp_verify_input']//div//input[" + (i + 1) + "]"),
					Character.toString(otp.charAt(i)));
		}
		Commons.click( driver, By.id("verify_otp"));
		Commons.wait(2000);
		if(Commons.isDisplayed(driver, By.id("dismissBtn")))
			Commons.click(driver, By.id("dismissBtn"));
		else {
			Commons.click(driver, By.id("authorize_scope"));
			Commons.click(driver, By.id("voluntary_claims"));
			Commons.click(driver, By.id("continue"));
			Commons.click(driver, By.id("dismissBtn"));
		}
		Commons.assertCheck(By.id("uinservices/viewhistory"),"verify if uin services login using tempoary vid");

	}

	public static void loginTestWithOneTimeVID() throws Exception {
		String envPath = ConfigManager.getiam_residentportal_path();

		String otp = "";
		String externalemail = ConfigManager.getexternalemail();
		driver.get(envPath);
		Commons.click( driver, By.xpath("//*[@id='UINservices']"));
		Commons.click( driver, By.id("login_with_otp"));
		Commons.enter( driver, By.id("Otp_mosip-vid"), TestRunner.onetimeuseVid);
		Commons.click( driver, By.id("get_otp"));

		otp = MockSMTPListener.getOtp(externalemail);

		System.out.println(otp);
		for (int i = 0; i <= otp.length() - 1; i++) {
			Commons.enter( driver, By.xpath("//*[@id='otp_verify_input']//div//input[" + (i + 1) + "]"),
					Character.toString(otp.charAt(i)));
		}
		Commons.click( driver, By.id("verify_otp"));
		Commons.wait(2000);
		if(Commons.isDisplayed(driver, By.id("dismissBtn")))
			Commons.click(driver, By.id("dismissBtn"));
		else {
			Commons.click(driver, By.id("authorize_scope"));
			Commons.click(driver, By.id("voluntary_claims"));
			Commons.click(driver, By.id("continue"));
			Commons.click(driver, By.id("dismissBtn"));
		}
		Commons.assertCheck(By.id("uinservices/viewhistory"),"verify if uin services login using one time vid");
	}


}