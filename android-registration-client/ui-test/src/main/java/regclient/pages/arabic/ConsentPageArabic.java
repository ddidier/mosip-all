package regclient.pages.arabic;

import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import regclient.api.FetchUiSpec;
import regclient.page.ConsentPage;
import regclient.page.DemographicDetailsPage;
import regclient.page.RegistrationTasksPage;
import regclient.pages.english.DemographicDetailsPageEnglish;
import regclient.pages.english.RegistrationTasksPageEnglish;



public class ConsentPageArabic extends ConsentPage {

	@AndroidFindBy(accessibility = "أُبلغ")
	private WebElement informedButton;
	
	@AndroidFindBy(accessibility = "يلغي")
	private WebElement cancelButton;

	public ConsentPageArabic(AppiumDriver driver) {
		super(driver);
	}

	@SuppressWarnings("deprecation")
	public boolean isConsentPageDisplayed() {
			return isElementDisplayed(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().descriptionContains(\"" + FetchUiSpec.getScreenTitle("consentdet") + "\"))"));		
	}

	@SuppressWarnings("deprecation")
	public boolean isCheckBoxReadable() {
		return isElementDisplayed (findElementWithRetry(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().descriptionContains(\"" + FetchUiSpec.getValueUsingId("consent") + "\"))")));
	}
	
	public  boolean isInformedButtonEnabled() {
		return isElementEnabled(informedButton);
	}

	public  DemographicDetailsPage clickOnInformedButton() {
		clickOnElement(informedButton);
		return new DemographicDetailsPageEnglish(driver);
	}


	public RegistrationTasksPage clickOnCancelButton() {
		clickOnElement(cancelButton);
		return new  RegistrationTasksPageEnglish(driver);
	}
	
	@SuppressWarnings("deprecation")
	public boolean updateUINTitleDisplayed() {
		return isElementDisplayed (findElementWithRetry(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().descriptionContains(\"" + FetchUiSpec.getTitleUsingId("UPDATE") + "\"))")));
	}
	
}
