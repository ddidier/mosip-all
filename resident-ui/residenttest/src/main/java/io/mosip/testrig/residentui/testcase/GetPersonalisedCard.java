package io.mosip.testrig.residentui.testcase;

import org.openqa.selenium.By;
import org.testng.annotations.Test;
import io.mosip.testrig.residentui.utility.BaseClass;
import io.mosip.testrig.residentui.utility.Commons;

@Test
public class GetPersonalisedCard extends BaseClass {

	@Test
	public void getPersonalisedCard() throws Exception {
		LoginTest.loginTest();
		Commons.click( driver, By.id("uinservices/personalisedcard"));
		Commons.click( driver, By.id("fullName"));
		Commons.clickWebelement( driver, By.id("dateOfBirth"));
		Commons.clickWebelement( driver, By.id("UIN"));
		Commons.click( driver, By.id("downloadFileBtn"));
		Commons.assertCheck(By.id("messagePopup"),"verify if personalisedcard is downloaded");

		Commons.click( driver, By.id("dismissBtn"));
		Commons.click( driver, By.id("uinservices/personalisedcard"));
		Commons.click( driver, By.id("fullName"));
		Commons.clickWebelement( driver, By.id("dateOfBirth"));
		Commons.clickWebelement( driver, By.id("UIN"));
		Commons.clickWebelement( driver, By.id("perpetualVID"));
		Commons.clickWebelement( driver, By.id("phone"));
		Commons.click( driver, By.id("downloadFileBtn"));
		Commons.assertCheck(By.id("messagePopup"),"verify if personalisedcard is downloaded");

		Commons.click( driver, By.id("dismissBtn"));
		Commons.click( driver, By.id("uinservices/personalisedcard"));
		Commons.clickWebelement( driver, By.id("fullName"));
		Commons.clickWebelement( driver, By.id("dateOfBirth"));
		Commons.clickWebelement( driver, By.id("UIN"));
		Commons.clickWebelement( driver, By.id("perpetualVID"));
		Commons.clickWebelement( driver, By.id("phone"));
		Commons.clickWebelement( driver, By.id("email"));
		Commons.clickWebelement( driver, By.id("fullAddress"));
		Commons.clickWebelement( driver, By.id("gender"));
		Commons.clickWebelement( driver, By.id("photo"));
		Commons.click( driver, By.id("downloadFileBtn"));
		Commons.assertCheck(By.id("messagePopup"),"verify if personalisedcard is downloaded");

		Commons.click( driver, By.id("dismissBtn"));
		Commons.click( driver, By.id("uinservices/personalisedcard"));
		Commons.clickWebelement( driver, By.id("fullName"));
		Commons.clickWebelement( driver, By.id("dateOfBirth"));
		Commons.clickWebelement( driver, By.id("UIN"));
		Commons.clickWebelement( driver, By.id("perpetualVID"));
		Commons.clickWebelement( driver, By.id("phone"));
		Commons.clickWebelement( driver, By.id("email"));
		Commons.clickWebelement( driver, By.id("fullAddress"));
		Commons.clickWebelement( driver, By.id("gender"));
		Commons.clickWebelement( driver, By.id("photo"));
		Commons.click( driver, By.id("downloadFileBtn"));
		Commons.assertCheck(By.id("messagePopup"),"verify if personalisedcard is downloaded");

		Commons.click( driver, By.id("dismissBtn"));
		
	}

}
