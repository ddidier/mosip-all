package io.mosip.testrig.residentui.testcase;

import org.openqa.selenium.By;
import org.testng.annotations.Test;
import io.mosip.testrig.residentui.utility.BaseClass;
import io.mosip.testrig.residentui.utility.Commons;

@Test
public class ManageMyVid extends BaseClass {

	@Test
	public void manageMyVid() throws Exception {
		LoginTest.loginTest();
		Commons.click( driver, By.id("uinservices/managemyvid"));
		Commons.click( driver, By.id("Perpetual"));
		Commons.click( driver, By.id("vidWarningBtn"));
		Commons.click( driver, By.id("dismissBtn"));
		Commons.click( driver, By.xpath("(//*[@id='download0'])[1]"));
		Commons.click( driver, By.id("NoBtn"));		
		Commons.click( driver, By.id("Temporary"));
		Commons.click( driver, By.id("vidWarningBtn"));
		Commons.assertCheck(By.id("messagePopup"),"verify if Your VID has been successfully created against the Event ID");
		Commons.click( driver, By.id("dismissBtn"));
		Commons.click( driver, By.xpath("(//*[@id='download0'])[2]"));
		Commons.click( driver, By.id("vidDownloadBtn"));
		Commons.assertCheck(By.id("messagePopup"),"verify if Your request to download the VID card has been successfully processed");
		Commons.click( driver, By.id("dismissBtn"));
		Commons.click( driver, By.xpath("(//*[@id='delete0'])[2]"));
		Commons.click( driver, By.id("vidDeleteBtn"));
		Commons.assertCheck(By.id("dismissBtn"),"Your VID has been successfully deleted against the Event ID");	
		Commons.click( driver, By.id("dismissBtn"));
		Commons.click( driver, By.id("OneTimeUse"));
		Commons.click( driver, By.id("vidWarningBtn"));
		Commons.assertCheck(By.id("messagePopup"),"verify if Your VID has been successfully created against the Event ID");
		Commons.click( driver, By.id("dismissBtn"));
		Commons.click( driver, By.xpath("(//*[@id='download0'])[2]"));
		Commons.click( driver, By.id("vidDownloadBtn"));
		Commons.assertCheck(By.id("messagePopup"),"verify if Your request to download the VID card has been successfully processed");
		Commons.click( driver, By.id("dismissBtn"));
		Commons.click( driver, By.xpath("(//*[@id='delete0'])[2]"));
		Commons.click( driver, By.id("vidDeleteBtn"));
		Commons.assertCheck(By.id("messagePopup"),"Your VID has been successfully deleted against the Event ID");
		Commons.click( driver, By.id("dismissBtn"));
		    
	}

}
