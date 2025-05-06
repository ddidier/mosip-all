package io.mosip.testrig.residentui.testcase;


import org.openqa.selenium.By;
import org.testng.annotations.Test;

import io.mosip.testrig.residentui.fw.util.AdminTestUtil;
import io.mosip.testrig.residentui.kernel.util.ConfigManager;
import io.mosip.testrig.residentui.utility.BaseClass;
import io.mosip.testrig.residentui.utility.Commons;
import io.mosip.testrig.residentui.utility.MockSMTPListener;
import io.mosip.testrig.residentui.utility.TestRunner;


@Test(groups="UMD")
public class UpdateMyData extends BaseClass {

	public void updateMyDataName() throws Exception {
		LoginTest.loginTest();
		Commons.clickWebelement( driver, By.id("uinservices/updatedemographic"));
		String name = AdminTestUtil.getValueFromAuthActuator("json-property", "name");
		name = name.replace("[", "").replace("]", "").replace("\"", "");
		String[] names=name.split(",");
		for(String Name:names) {
			Commons.enter( driver, By.id(Name+BaseClass.envsupportlang()), Commons.generateRandomAlphabetString());
		}	
		Commons.dropdown( driver, By.id("gender"));
		Commons.enter( driver, By.id("proofOfIdentityvalue"), data);
		Commons.dropdown( driver, By.id("proofOfIdentity"));
		Commons.uploadImage( driver, By.id("fileDropRef"),
				TestRunner.getResourcePath() + "//BulkUploadFiles//tonyId.png");
		Commons.wait(2000);
		Commons.clickWebelement( driver, By.id("previewBtn"));
		Commons.clickWebelement( driver, By.id("submitBtn"));
		Commons.clickWebelement( driver, By.id("confirmUpdateData"));
		Commons.clickWebelement( driver, By.id("updateMyDataBtn"));	
		Commons.assertCheck(By.id("messagePopup"),"verify if Your update has been successfully updated against the Event");
		Commons.clickWebelement( driver, By.id("dismissBtn"));
	}


	public void updateMyDataAddress() throws Exception {
		Commons.wait(ConfigManager.packetUpdateWait()*60000);	
		LoginTest.loginTest();
		Commons.clickWebelement( driver, By.id("uinservices/updatedemographic"));
		Commons.clickWebelement( driver, By.xpath("//div[@id='mat-tab-label-0-1']"));
		Commons.enter( driver, By.id("addressLine1"+BaseClass.envsupportlang()),  Commons.generateRandomAlphabetString());
		Commons.dropdown( driver, By.id("Region"+BaseClass.envsupportlang()));
		Commons.dropdown( driver, By.id("Province"+BaseClass.envsupportlang()));
		Commons.dropdown( driver, By.id("City"+BaseClass.envsupportlang()));
		Commons.dropdown( driver, By.id("Zone"+BaseClass.envsupportlang()));
		Commons.dropdown( driver, By.id("Postal Code"));
		Commons.dropdown( driver, By.id("proofOfAddress"));
		Commons.enter( driver, By.id("proofOfAddressvalue"), data);
		Commons.uploadImage( driver, By.id("fileAddRef"),
				TestRunner.getResourcePath() + "//BulkUploadFiles//tonyId.png");
		Commons.wait(2000);
		Commons.clickWebelement( driver, By.id("previewBtn"));
		Commons.clickWebelement( driver, By.id("submitBtn"));
		Commons.clickWebelement( driver, By.id("confirmUpdateData"));
		Commons.clickWebelement( driver, By.id("updateMyDataBtn"));
		Commons.assertCheck(By.id("messagePopup"),"verify if Your update has been successfully updated against the Event");
		Commons.clickWebelement( driver, By.id("dismissBtn"));

	}
	
	public void UpdateDataWithEmail() throws Exception {
		Commons.wait(ConfigManager.packetUpdateWait()*60000);
		String tempemail1 = ConfigManager.gettempemail1();
		LoginTest.loginTest();
		Commons.click( driver, By.id("uinservices/updatedemographic"));
		Commons.clickWebelement( driver, By.xpath("//div[@id='mat-tab-label-0-2']"));
		Commons.enter( driver, By.id("email"), tempemail1);	
		Commons.clickWebelement( driver, By.id("sendOTPemail"));
		String otp = MockSMTPListener.getOtp(tempemail1);
		System.out.println(otp);
		Commons.enter( driver, By.id("otp-input"), otp);
		Commons.clickWebelement( driver, By.id("submitOtpBtn"));
		Commons.assertCheck(By.id("messagePopup"),"verify if Your Email ID has been successfully updated against the Event");
		Commons.clickWebelement( driver, By.id("dismissBtn"));

	}
	
	public void UpdateDataWithLanguage() throws Exception {
		Commons.wait(ConfigManager.packetUpdateWait()*60000);
		LoginTest.loginTest();
		Commons.click( driver, By.id("uinservices/updatedemographic"));
		Commons.clickWebelement( driver, By.xpath("//div[@id='mat-tab-label-0-3']"));
		Commons.dropdown( driver, By.id("preferredLang"));
		Commons.clickWebelement( driver, By.id("submit"));
		Commons.assertCheck(By.id("messagePopup"),"verify if Your Language has been successfully updated against the Event");

	}

	public void UpdateDataWithoutAddressLine() throws Exception {
		Commons.wait(ConfigManager.packetUpdateWait()*60000);
		LoginTest.loginTest();
		Commons.clickWebelement( driver, By.id("uinservices/updatedemographic"));
		Commons.clickWebelement( driver, By.xpath("//div[@id='mat-tab-label-0-1']"));
		Commons.dropdown( driver, By.id("Region"+BaseClass.envsupportlang()));
		Commons.dropdown( driver, By.id("Province"+BaseClass.envsupportlang()));
		Commons.dropdown( driver, By.id("City"+BaseClass.envsupportlang()));
		Commons.dropdown( driver, By.id("Zone"+BaseClass.envsupportlang()));
		Commons.dropdown( driver, By.id("Postal Code"));
		Commons.dropdown( driver, By.id("proofOfAddress"));
		Commons.enter( driver, By.id("proofOfAddressvalue"), data);
		Commons.uploadImage( driver, By.id("fileAddRef"),
				TestRunner.getResourcePath() + "//BulkUploadFiles//tonyId.png");
		Commons.wait(2000);
		Commons.clickWebelement( driver, By.id("previewBtn"));
		Commons.clickWebelement( driver, By.id("submitBtn"));
		Commons.clickWebelement( driver, By.id("confirmUpdateData"));
		Commons.clickWebelement( driver, By.id("updateMyDataBtn"));
		Commons.assertCheck(By.id("messagePopup"),"verify if Your update has been successfully updated against the Event");
		Commons.clickWebelement( driver, By.id("dismissBtn"));

	}

}
