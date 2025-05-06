package io.mosip.testrig.residentui.testcase;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import io.mosip.testrig.residentui.utility.BaseClass;
import io.mosip.testrig.residentui.utility.Commons;

@Test
public class ShareMyData extends BaseClass {

	@Test(groups = "SMD")
	public void shareMyData() throws Exception {
		LoginTest.loginTest();

		Commons.clickWebelement( driver, By.id("uinservices/sharewithpartner"));
		try {
			Commons.clickWebelement( driver, By.id("fullName"));
		}catch(Exception e) {
			Commons.clickWebelement( driver, By.id("name"));

		}
		Commons.clickWebelement( driver, By.id("dateOfBirth"));
		Commons.clickWebelement( driver, By.id("UIN"));
		Commons.dropdown( driver, By.id("partnerDetails"));
		Commons.enter( driver, By.id("sharingReasonPlaceholder"), data);
		Commons.clickWebelement( driver, By.id("shareBtn"));
		Commons.clickWebelement( driver, By.id("confirmShareInfo"));
		Commons.clickWebelement( driver, By.id("shareInfoBtn"));
		Commons.assertCheck(By.id("messagePopup"),"verify if The data chosen by you has been successfully shared with the chosen partner against the Event Id");
		Commons.clickWebelement( driver, By.id("dismissBtn"));

		Commons.clickWebelement( driver, By.id("uinservices/sharewithpartner"));
		try {
			Commons.clickWebelement( driver, By.id("fullName"));
		}catch(Exception e) {
			Commons.clickWebelement( driver, By.id("name"));

		}
		Commons.clickWebelement( driver, By.id("dateOfBirth"));
		Commons.clickWebelement( driver, By.id("UIN"));
		Commons.clickWebelement( driver, By.id("shareBtn"));
		Commons.clickWebelement( driver, By.id("dismissBtn"));

		// without partner fullName
		Commons.enter( driver, By.id("sharingReasonPlaceholder"), data);
		Commons.clickWebelement( driver, By.id("shareBtn"));
		Commons.clickWebelement( driver, By.id("dismissBtn"));

		// without purpose
		driver.findElement(By.id("sharingReasonPlaceholder")).clear();
		Commons.clickWebelement( driver, By.id("shareBtn"));
		Commons.clickWebelement( driver, By.id("dismissBtn"));

		// all with valid
		Commons.clickWebelement( driver, By.id("perpetualVID"));
		Commons.clickWebelement( driver, By.id("phone"));
		Commons.clickWebelement( driver, By.id("email"));
		Commons.clickWebelement( driver, By.id("fullAddress"));
		Commons.clickWebelement( driver, By.id("gender"));
		Commons.clickWebelement( driver, By.id("photo"));
		Commons.dropdown( driver, By.id("partnerDetails"));
		Commons.enter( driver, By.id("sharingReasonPlaceholder"), data);
		Commons.clickWebelement( driver, By.id("shareBtn"));
		Commons.clickWebelement( driver, By.id("confirmShareInfo"));
		Commons.clickWebelement( driver, By.id("shareInfoBtn"));
		Commons.clickWebelement( driver, By.id("dismissBtn"));

		Commons.clickWebelement( driver, By.id("uinservices/sharewithpartner"));
		try {
			Commons.clickWebelement( driver, By.id("fullName"));
		}catch(Exception e) {
			Commons.clickWebelement( driver, By.id("name"));

		}
		Commons.clickWebelement( driver, By.id("dateOfBirth"));
		Commons.clickWebelement( driver, By.id("UIN"));
		Commons.clickWebelement( driver, By.id("perpetualVID"));
		Commons.clickWebelement( driver, By.id("phone"));
		Commons.clickWebelement( driver, By.id("email"));
		Commons.clickWebelement( driver, By.id("fullAddress"));
		Commons.clickWebelement( driver, By.id("gender"));
		Commons.clickWebelement( driver, By.id("photo"));
		Commons.clickWebelement( driver, By.id("shareBtn"));
		Commons.assertCheck(By.id("messagePopup"),"verify if The data chosen by you has been successfully shared with the chosen partner against the Event Id");
		Commons.clickWebelement( driver, By.id("dismissBtn"));

		Commons.enter( driver, By.id("sharingReasonPlaceholder"), data);
		Commons.clickWebelement( driver, By.id("shareBtn"));
		Commons.assertCheck(By.id("messagePopup"),"verify if The data chosen by you has been successfully shared with the chosen partner against the Event Id");
		Commons.clickWebelement( driver, By.id("dismissBtn"));
		driver.findElement(By.id("sharingReasonPlaceholder")).clear();

		Commons.dropdown( driver, By.id("partnerDetails"));
		Commons.clickWebelement( driver, By.id("shareBtn"));
		Commons.assertCheck(By.id("messagePopup"),"verify if The data chosen by you has been successfully shared with the chosen partner against the Event Id");
		Commons.clickWebelement( driver, By.id("dismissBtn"));

	}
}
