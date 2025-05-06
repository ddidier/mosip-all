package io.mosip.testrig.residentui.testcase;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import io.mosip.testrig.residentui.utility.BaseClass;
import io.mosip.testrig.residentui.utility.Commons;

@Test
public class SecureMyId extends BaseClass {

	@Test(groups = "SMI")
	public void secureMyId() throws Exception {
		LoginTest.loginTest();

		Commons.click( driver, By.id("uinservices/lockunlockauth"));
		Commons.click( driver, By.id("setAuthlockStatus1"));
		Commons.click( driver, By.id("updateAuthlockStatusBtn"));
		Commons.click( driver, By.id("lockunlockauthBtn"));
		Commons.assertCheck(By.id("messagePopup"),"Your request to change the Authentication type(s) has been saved successfully against the Event ID");
		Commons.click( driver, By.id("dismissBtn"));

		Commons.click( driver, By.id("uinservices/lockunlockauth"));
		Commons.click( driver, By.id("setAuthlockStatus1"));
		Commons.click( driver, By.id("updateAuthlockStatusBtn"));
		Commons.click( driver, By.id("lockunlockauthBtn"));
		Commons.assertCheck(By.id("messagePopup"),"Your request to change the Authentication type(s) has been saved successfully against the Event ID");
		Commons.click( driver, By.id("dismissBtn"));

		Commons.click( driver, By.id("uinservices/lockunlockauth"));
		Commons.click( driver, By.id("setAuthlockStatus2"));
		Commons.click( driver, By.id("updateAuthlockStatusBtn"));
		Commons.click( driver, By.id("lockunlockauthBtn"));
		Commons.assertCheck(By.id("messagePopup"),"Your request to change the Authentication type(s) has been saved successfully against the Event ID");
		Commons.click( driver, By.id("dismissBtn"));

		Commons.click( driver, By.id("uinservices/lockunlockauth"));
		Commons.click( driver, By.id("setAuthlockStatus2"));
		Commons.click( driver, By.id("updateAuthlockStatusBtn"));
		Commons.click( driver, By.id("lockunlockauthBtn"));
		Commons.assertCheck(By.id("messagePopup"),"Your request to change the Authentication type(s) has been saved successfully against the Event ID");
		Commons.click( driver, By.id("dismissBtn"));

		Commons.click( driver, By.id("uinservices/lockunlockauth"));
		Commons.click( driver, By.id("setAuthlockStatus3"));
		Commons.click( driver, By.id("updateAuthlockStatusBtn"));
		Commons.click( driver, By.id("lockunlockauthBtn"));
		Commons.assertCheck(By.id("messagePopup"),"Your request to change the Authentication type(s) has been saved successfully against the Event ID");
		Commons.click( driver, By.id("dismissBtn"));

		Commons.click( driver, By.id("uinservices/lockunlockauth"));
		Commons.click( driver, By.id("setAuthlockStatus3"));
		Commons.click( driver, By.id("updateAuthlockStatusBtn"));
		Commons.click( driver, By.id("lockunlockauthBtn"));
		Commons.assertCheck(By.id("messagePopup"),"Your request to change the Authentication type(s) has been saved successfully against the Event ID");
		Commons.click( driver, By.id("dismissBtn"));

		Commons.click( driver, By.id("uinservices/lockunlockauth"));
		Commons.click( driver, By.id("setAuthlockStatus4"));
		Commons.click( driver, By.id("updateAuthlockStatusBtn"));
		Commons.click( driver, By.id("lockunlockauthBtn"));
		Commons.assertCheck(By.id("messagePopup"),"Your request to change the Authentication type(s) has been saved successfully against the Event ID");
		Commons.click( driver, By.id("dismissBtn"));

		Commons.click( driver, By.id("uinservices/lockunlockauth"));
		Commons.click( driver, By.id("setAuthlockStatus4"));
		Commons.click( driver, By.id("updateAuthlockStatusBtn"));
		Commons.click( driver, By.id("lockunlockauthBtn"));
		Commons.assertCheck(By.id("messagePopup"),"Your request to change the Authentication type(s) has been saved successfully against the Event ID");
		Commons.click( driver, By.id("dismissBtn"));

		Commons.click( driver, By.id("uinservices/lockunlockauth"));
		Commons.click( driver, By.id("setAuthlockStatus5"));
		Commons.click( driver, By.id("updateAuthlockStatusBtn"));
		Commons.click( driver, By.id("lockunlockauthBtn"));
		Commons.assertCheck(By.id("messagePopup"),"Your request to change the Authentication type(s) has been saved successfully against the Event ID");
		Commons.click( driver, By.id("dismissBtn"));

		Commons.click( driver, By.id("uinservices/lockunlockauth"));
		Commons.click( driver, By.id("setAuthlockStatus5"));
		Commons.click( driver, By.id("updateAuthlockStatusBtn"));
		Commons.click( driver, By.id("lockunlockauthBtn"));
		Commons.assertCheck(By.id("messagePopup"),"Your request to change the Authentication type(s) has been saved successfully against the Event ID");
		Commons.click( driver, By.id("dismissBtn"));

	}
}