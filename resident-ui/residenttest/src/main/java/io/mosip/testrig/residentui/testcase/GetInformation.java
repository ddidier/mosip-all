package io.mosip.testrig.residentui.testcase;

import org.openqa.selenium.By;
import org.testng.annotations.Test;
import io.mosip.testrig.residentui.service.BaseTestCase;
import io.mosip.testrig.residentui.utility.BaseClass;
import io.mosip.testrig.residentui.utility.Commons;

@Test(groups = "GI")

public class GetInformation extends BaseClass {

	@Test(groups = "GI")
	public void getInformation() throws Exception {
		int totalHierarchyLevels=BaseTestCase.getHierarchyNumbers();
		Commons.click( driver, By.id("getInformation"));
		for(int i=0;i<totalHierarchyLevels;i++) {
			Commons.dropdown( driver, By.id("locationType"), By.id("hierarchyLevel"+i));
			Commons.enter( driver, By.id("searchLocationBox"),  BaseTestCase.GethierarchyName(i));
			Commons.click( driver, By.id("regCenterSearchBtn"));
			Commons.click( driver, By.id("regCentersDownloadBtn"));
			Commons.assertCheck(By.id("regCentersDownloadBtn"),"verify if registation center is downloaded");

    }
  }
}
