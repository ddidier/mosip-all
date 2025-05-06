//package io.mosip.testrig.residentui.testcase;
//
//import java.util.ArrayList;
//
//import org.openqa.selenium.By;
//import org.testng.annotations.Test;
//
//import io.mosip.testrig.residentui.kernel.util.ConfigManager;
//import io.mosip.testrig.residentui.utility.Commons;
//import io.mosip.testrig.residentui.utility.MockSMTPListener;
//import io.mosip.testrig.residentui.utility.ResidentBaseClass;
//
//public class BookinganAppointment extends ResidentBaseClass {
//
//	@Test
//	public void bookinganAppointment () throws Exception {
////		String externalemail = ConfigManager.getexternalemail();
////		Commons.click(test,driver, By.id("bookingAnAppointment"));
////		ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
////		driver.switchTo().window(tabs.get(1));
////		if(ConfigManager.getloginlang().equalsIgnoreCase("fra")) 
////			Commons.dropdown(test, driver, By.id("mat-select-0"),"français");
////		else if (ConfigManager.getloginlang().equalsIgnoreCase("ara")) 
////			Commons.dropdown(test, driver, By.id("mat-select-0"),"العَرَبِيَّة‎");
////		else 
////			Commons.dropdown(test, driver, By.id("mat-select-0"),"English");
////		Commons.enter(test, driver, By.id("inputFieldContact"), Commons.getDateTime());
////		Commons.click(test,driver, By.xpath("//*[text()=' Send OTP ']"));
////		Commons.enter(test, driver, By.id("inputFieldContact"), Commons.getDateTime()+ConfigManager.gettempemail());
////		Commons.click(test,driver, By.xpath("//*[text()=' Send OTP ']"));
////		Commons.enter(test, driver, By.id("inputFieldOTP"),"123456");
////		Commons.click(test,driver, By.xpath("//*[text()=' Verify ']"));
////		Commons.click(test,driver, By.xpath("//*[text()=' OK ']"));
////		Commons.enter(test, driver, By.id("inputFieldOTP"), MockSMTPListener.getOtp(externalemail));
////		Thread.sleep(180000);
////		Commons.click(test,driver, By.xpath("//*[text()=' Send OTP ']"));
////		Commons.enter(test, driver, By.id("inputFieldOTP"), MockSMTPListener.getOtp(externalemail));
////		Commons.click(test,driver, By.xpath("//*[text()=' Verify ']"));
////		Commons.click(test,driver, By.id("mat-checkbox-2"));
////		Commons.click(test,driver, By.xpath("//*[text()=' Submit ']"));
////		Commons.click(test,driver, By.id("mat-checkbox-4-input"));
////		Commons.click(test,driver, By.xpath("//*[text()=' Accept ']"));
////		Commons.enter(test, driver, By.id("mat-input-0"),"abc"+Commons.getDateTime());
////		Commons.enter(test, driver, By.id("mat-input-1"),"abc"+Commons.getDateTime());
////		Commons.enter(test, driver, By.id("mat-input-3"),"20");
////		Commons.dropdown(test, driver, By.id("mat-select-0"));
////		Thread.sleep(11111);
//
//
//
//
//	}
//}
