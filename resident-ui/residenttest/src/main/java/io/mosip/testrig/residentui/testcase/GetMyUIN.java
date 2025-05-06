package io.mosip.testrig.residentui.testcase;

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.annotations.Test;
import io.mosip.testrig.residentui.kernel.util.ConfigManager;
import io.mosip.testrig.residentui.utility.BaseClass;
import io.mosip.testrig.residentui.utility.Commons;
import io.mosip.testrig.residentui.utility.MockSMTPListener;
import io.mosip.testrig.residentui.utility.TestRunner;

@Test(groups = "GMU",dependsOnGroups="UMD")
public class GetMyUIN extends BaseClass{

	String vid =TestRunner.perpetualVid;
	@Test(priority=0)
	public void getMyUIN() throws Exception {
		String externalemail = ConfigManager.getexternalemail();
		Commons.click( driver, By.id("getMyUIN"));
		Commons.enter( driver, By.id("aidValue"), vid);//
		Commons.click( driver, By.id("getUinSendOtpBtn"));
		String otp = MockSMTPListener.getOtp(externalemail);
		Commons.enter( driver, By.id("otp-input"), otp);
		Commons.click( driver, By.xpath("//button[@id='getUinsubmitBtn']"));
		Commons.assertCheck(By.id("messagePopup"),"verify if Your UIN card has been successfully downloaded against the Event ID");
		Commons.click( driver, By.id("dismissBtn"));			
	}

	@Test(priority=1)
	public void getMyUINWithoutAID() throws InterruptedException, IOException {
		Commons.click( driver, By.id("getMyUIN"));
		Commons.click(driver, By.id("getUinSendOtpBtn"));
	}

	@Test(priority=2)
	public void getMyUINWithInvalidOtp() throws Exception {
		String externalemail = ConfigManager.getexternalemail();
		Commons.click( driver, By.id("getMyUIN"));
		Commons.enter(driver, By.id("aidValue"), vid);
		Commons.click(driver, By.id("getUinSendOtpBtn"));
		String otp = MockSMTPListener.getOtp(externalemail);
		Commons.enter(driver, By.id("otp-input"), otp+"56");
		Commons.click(driver, By.xpath("//button[@id='getUinsubmitBtn']"));
		Commons.click(driver, By.id("dismissBtn"));

	}

	@Test(priority=3)
	public void getMyUINWithExpiredOtp() throws Exception {
		Commons.click( driver, By.id("getMyUIN"));//id
		Commons.enter(driver, By.id("aidValue"), vid);//
		Commons.click(driver, By.id("getUinSendOtpBtn"));
		Commons.enter(driver, By.id("otp-input"),"12345");
		Commons.click(driver, By.xpath("//button[@id='getUinsubmitBtn']"));
		Commons.click(driver, By.id("dismissBtn"));

	}

	@Test(priority=4)
	public void getMyUINWithoutOtp() throws Exception {
		Commons.click( driver, By.id("getMyUIN"));//id
		Commons.enter(driver, By.id("aidValue"), vid);
		Commons.click(driver, By.id("getUinSendOtpBtn"));
		Commons.click(driver, By.xpath("//button[@id='getUinsubmitBtn']"));

	}
}
