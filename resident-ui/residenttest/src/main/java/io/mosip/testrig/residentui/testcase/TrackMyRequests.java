package io.mosip.testrig.residentui.testcase;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.mosip.testrig.residentui.kernel.util.ConfigManager;
import io.mosip.testrig.residentui.utility.BaseClass;
import io.mosip.testrig.residentui.utility.Commons;

@Test
public class TrackMyRequests extends BaseClass{
	
	@Test(priority = 1)
	public void trackMyRequest() throws Exception {
		String Eid="";
		LoginTest.loginTest();
		Commons.click(driver, By.id("uinservices/managemyvid"));
		Commons.click(driver, By.id("Temporary"));
		Commons.click(driver, By.id("vidWarningBtn"));
		Commons.wait(2000);
		String eid	= driver.findElement(By.className("pop-up-header")).getText();
		System.out.println(eid);
		Eid = eid.replaceAll("[^0-9]", "");
		System.out.println(Eid);
		Commons.assertCheckString(Eid," verify if Your VID has been successfully created against the Event ID");
		Commons.click(driver, By.id("dismissBtn"));
		driver.navigate().back();
		Commons.click(driver, By.id("uinservices/trackservicerequest"));
		Commons.enter(driver, By.id("appIdValue"), Eid);
		Commons.clickWebelement(driver, By.id("getEIDStatusbtn"));
		Commons.assertCheck(By.id("downloadAcknowledgementbtn")," verify if Track the status of an event ID (EID) associated with the logged-in UIN/ VID. You can also view the detailed information about the entered EID.");
		Commons.click(driver, By.id("downloadAcknowledgementbtn"));
	}

	public void TrackMyRequestsInvalidEId() throws Exception {
		LoginTest.loginTest();
		Commons.click(driver, By.id("uinservices/trackservicerequest"));
		Commons.enter(driver, By.id("appIdValue"), data+"345");
		Commons.click(driver, By.id("getEIDStatusbtn"));
		Commons.click(driver, By.id("dismissBtn"));
	}

	public void TrackMyRequestsWithDiffEId() throws Exception {
		String tempEID=ConfigManager.gettempEID();
		LoginTest.loginTest();
		Commons.click(driver, By.id("uinservices/trackservicerequest"));
		Commons.enter(driver, By.id("appIdValue"), tempEID);
		Commons.click(driver, By.id("getEIDStatusbtn"));
		Commons.click(driver, By.id("dismissBtn"));
	}
}
