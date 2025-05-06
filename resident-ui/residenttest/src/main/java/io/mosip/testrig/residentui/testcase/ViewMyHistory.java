package io.mosip.testrig.residentui.testcase;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import io.mosip.testrig.residentui.utility.BaseClass;
import io.mosip.testrig.residentui.utility.Commons;

@Test
public class ViewMyHistory extends BaseClass {

	@Test(groups = "VMH")
	public void viewMyHistory() throws Exception {
		LoginTest.loginTest();
		Commons.clickWebelement( driver, By.id("uinservices/viewhistory"));
		Commons.clickWebelement( driver, By.id("fromPickertext"));
		Commons.clickWebelement( driver,By.xpath("//*[@id=\"mat-datepicker-0\"]/div/mat-month-view/table/tbody/tr[5]/td[4]"));
		Commons.clickWebelement( driver, By.id("searchbtn"));
		Commons.clickWebelement( driver, By.id("historyTypeBtn"));
		Commons.clickWebelement( driver, By.id("ALL"));
		Commons.clickWebelement( driver, By.id("searchbtn"));
		Commons.clickWebelement( driver, By.id("ALL"));
		Commons.clickWebelement( driver, By.id("AUTHENTICATION_REQUEST"));
		Commons.clickWebelement( driver, By.id("searchbtn"));
		Commons.clickWebelement( driver, By.id("AUTHENTICATION_REQUEST"));
		Commons.clickWebelement( driver, By.id("SERVICE_REQUEST"));
		Commons.clickWebelement( driver, By.id("searchbtn"));
		Commons.clickWebelement( driver, By.id("SERVICE_REQUEST"));
		Commons.clickWebelement( driver, By.id("DATA_UPDATE_REQUEST"));
		Commons.clickWebelement( driver, By.id("searchbtn"));
		Commons.clickWebelement( driver, By.id("DATA_UPDATE_REQUEST"));
		Commons.clickWebelement( driver, By.id("ID_MANAGEMENT_REQUEST"));
		Commons.clickWebelement( driver, By.id("searchbtn"));
		Commons.clickWebelement( driver, By.id("ID_MANAGEMENT_REQUEST"));
		Commons.clickWebelement( driver, By.id("DATA_SHARE_REQUEST"));
		Commons.clickWebelement( driver, By.id("searchbtn"));
		Commons.clickWebelement( driver, By.id("DATA_SHARE_REQUEST"));
		Commons.clickWebelement( driver, By.id("ALL"));
		
		driver.navigate().back();
		Commons.clickWebelement( driver, By.id("uinservices/viewhistory"));
		Commons.clickWebelement( driver, By.id("statusTypeBtn"));		
		Commons.click( driver, By.id("SUCCESS"));
		Commons.clickWebelement( driver, By.id("searchbtn"));
		Commons.clickWebelement( driver, By.id("SUCCESS"));
		Commons.clickWebelement( driver, By.id("IN_PROGRESS"));
		Commons.clickWebelement( driver, By.id("searchbtn"));
		Commons.clickWebelement( driver, By.id("IN_PROGRESS"));
		Commons.clickWebelement( driver, By.id("FAILED"));
		Commons.clickWebelement( driver, By.id("searchbtn"));
		Commons.clickWebelement( driver, By.id("downloadbtn"));

	}
}
