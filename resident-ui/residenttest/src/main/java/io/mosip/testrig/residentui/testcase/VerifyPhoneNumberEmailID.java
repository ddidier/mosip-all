package io.mosip.testrig.residentui.testcase;

import static org.testng.Assert.assertFalse;

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import io.mosip.testrig.residentui.kernel.util.ConfigManager;
import io.mosip.testrig.residentui.utility.BaseClass;
import io.mosip.testrig.residentui.utility.Commons;
import io.mosip.testrig.residentui.utility.MockSMTPListener;
import io.mosip.testrig.residentui.utility.TestRunner;


public class VerifyPhoneNumberEmailID extends BaseClass {



	@Test(priority=0)
	public void VerifyEmailIDAndPhoneWIthInvalidVid() throws IOException, InterruptedException {
		Commons.click( driver, By.id("verifyPhoneNumber/EmailID"));
		Commons.click( driver, By.id("emailChannelBtn-button"));
		Commons.enter( driver, By.id("uin"), "6534906793542111");
		Commons.click( driver, By.id("sendOtpBtn"));
		assertFalse(Commons.isDisplayed(driver,By.id("submitOtpBtn")),"Verify submit otp button with invalid otp");
		Commons.click( driver, By.id("dismissBtn"));
		Commons.click( driver, By.id("phoneChannelBtn"));
		Commons.enter( driver, By.id("uin"), "6534906793542111");
		Commons.click( driver, By.id("sendOtpBtn"));
		assertFalse(Commons.isDisplayed(driver,By.id("submitOtpBtn")),"Verify submit otp button with invalid otp");
		Commons.click( driver, By.id("dismissBtn"));
		Commons.enter( driver, By.id("uin"), "6534906793542111");
		Commons.click( driver, By.id("sendOtpBtn"));
		assertFalse(Commons.isDisplayed(driver,By.id("submitOtpBtn")),"Verify submit otp button with invalid otp");

	}

	@Test(priority=2)
	public void VerifyEmailIDWIthInvalidOtp() throws Exception {
		String externalemail = ConfigManager.getexternalemail();
		Commons.click( driver, By.id("verifyPhoneNumber/EmailID"));
		Commons.click( driver, By.id("emailChannelBtn-button"));
		Commons.enter( driver, By.id("uin"), TestRunner.perpetualVid);
		Commons.click( driver, By.id("sendOtpBtn"));
		String otp = MockSMTPListener.getOtp(externalemail);
		Commons.enter( driver, By.id("otp-input"), otp + "23");
		Commons.click( driver, By.id("submitOtpBtn"));
		Commons.click( driver, By.id("dismissBtn"));
		Commons.click( driver, By.id("backBtn"));
		Commons.click( driver, By.id("emailChannelBtn-button"));
		Commons.enter( driver, By.id("uin"), TestRunner.perpetualVid);
		Commons.click( driver, By.id("sendOtpBtn"));
		Commons.click( driver, By.id("submitOtpBtn"));
		assertFalse(Commons.isDisplayed(driver,By.id("dismissBtn")),"Verify submit otp button without enter otp");

	}

	@Test(priority=3)
	public void VerifyEmailIDAndPhone() throws Exception {
		String externalemail = ConfigManager.getexternalemail();
		Commons.click( driver, By.id("verifyPhoneNumber/EmailID"));
		Commons.click( driver, By.id("emailChannelBtn-button"));
		Commons.enter( driver, By.id("uin"), TestRunner.perpetualVid);
		Commons.click( driver, By.id("sendOtpBtn"));
		String otp = MockSMTPListener.getOtp(externalemail);
		Commons.enter( driver, By.id("otp-input"), otp);
		Commons.click( driver, By.id("submitOtpBtn"));
		Commons.click( driver, By.id("dismissBtn"));
	}
}
