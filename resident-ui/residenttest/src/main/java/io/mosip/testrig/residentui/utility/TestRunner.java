package io.mosip.testrig.residentui.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import io.mosip.testrig.residentui.fw.util.AdminTestUtil;
import io.mosip.testrig.residentui.kernel.util.ConfigManager;
import io.mosip.testrig.residentui.kernel.util.KeycloakUserManager;
import io.mosip.testrig.residentui.service.BaseTestCase;
import io.mosip.testrig.residentui.testcase.*;


public class TestRunner {
	static TestListenerAdapter tla = new TestListenerAdapter();

	public static String jarUrl = TestRunner.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	public static String uin="";
	public static String perpetualVid="";
	public static String onetimeuseVid="";
	public static String temporaryVid="";
	protected static MockSMTPListener mockSMTPListener = null;
	static TestNG testNg;

	public static void main(String[] args) throws Exception {
		AdminTestUtil.initialize();	
		mockSMTPListener = new MockSMTPListener();
		mockSMTPListener.run();
		String identityGenManual=ConfigManager.getidentityGenManual();
		if(identityGenManual.equals("yes")) {
			uin=ConfigManager.getuin();
			perpetualVid=ConfigManager.getperpetualvid();
			onetimeuseVid=ConfigManager.getonetimevid();
			temporaryVid=ConfigManager.gettemporaryvid();
		}else {
			uin = AdminTestUtil.generateUIN();

			if (uin != null) {
				perpetualVid = AdminTestUtil.generateVID(uin, "perpetual");
				onetimeuseVid = AdminTestUtil.generateVID(uin, "onetimeuse");
				temporaryVid= AdminTestUtil.generateVID(uin, "temporary");
			}
		}

		startTestRunner();
	}



	public static void startTestRunner() throws Exception {

		File homeDir = null;
		TestNG runner = new TestNG();
		if(!ConfigManager.gettestcases().equals("")) {
			XmlSuite suite = new XmlSuite();
			suite.setName("RESIDENT-UI-AUTOMATION-RUN");
			suite.addListener("io.mosip.testrig.residentui.utility.EmailableReport");

			XmlClass getInformation = new XmlClass("io.mosip.testrig.residentui.testcase.GetInformation");
			XmlClass getMyUIN = new XmlClass("io.mosip.testrig.residentui.testcase.GetMyUIN");
			XmlClass GetPersonalisedCard = new XmlClass("io.mosip.testrig.residentui.testcase.GetPersonalisedCard");
			XmlClass loginTest = new XmlClass("io.mosip.testrig.residentui.testcase.LoginTest");
			XmlClass manageMyVid = new XmlClass("io.mosip.testrig.residentui.testcase.ManageMyVid");
			XmlClass SecureMyId = new XmlClass("io.mosip.testrig.residentui.testcase.SecureMyId");
			XmlClass shareMyData = new XmlClass("io.mosip.testrig.residentui.testcase.ShareMyData");
			XmlClass trackMyRequest = new XmlClass("io.mosip.testrig.residentui.testcase.TrackMyRequests");
			XmlClass updateMyDataName = new XmlClass("io.mosip.testrig.residentui.testcase.UpdateMyData");
			XmlClass VerifyEmailIDWIthInvalidVid = new XmlClass("io.mosip.testrig.residentui.testcase.VerifyPhoneNumberEmailID");
			XmlClass viewMyHistory = new XmlClass("io.mosip.testrig.residentui.testcase.ViewMyHistory");


			List<XmlClass> classes = new ArrayList<>();
			String[] Scenarionames=ConfigManager.gettestcases().split(",");
			for(String test:Scenarionames) {
				String Scenarioname=test.toLowerCase();

				if(Scenarioname.contains("getinformation"))
					classes.add(getInformation);

				if(Scenarioname.contains("getmyuin")) {
					classes.add(updateMyDataName);
					classes.add(getMyUIN);
				}

				if(test.equals("getPersonalisedCard"))
					classes.add(GetPersonalisedCard);

				if(Scenarioname.contains("logintest"))
					classes.add(loginTest);

				if(test.equals("manageMyVid"))
					classes.add(manageMyVid);

				if(test.equals("secureMyId"))
					classes.add(SecureMyId);

				if(test.equals("shareMyData"))
					classes.add(shareMyData);

				if(Scenarioname.contains("trackmyrequest"))
					classes.add(trackMyRequest);

				if(Scenarioname.contains("update"))
					classes.add(updateMyDataName);

				if(Scenarioname.contains("verifyemailid"))
					classes.add(VerifyEmailIDWIthInvalidVid);

				if(test.equals("viewMyHistory"))
					classes.add(viewMyHistory);

			}


			XmlTest test = new XmlTest(suite);
			test.setName("MyTest");
			test.setXmlClasses(classes);

			List<XmlSuite> suites = new ArrayList<>();
			suites.add(suite);

			runner.setXmlSuites(suites);

		}else {
			List<String> suitefiles = new ArrayList<String>();
			String os = System.getProperty("os.name");

			if (checkRunType().contains("IDE") || os.toLowerCase().contains("windows") == true) {
				homeDir = new File(getResourcePath() + "/testngFile");

			} else {
				homeDir = new File(getResourcePath() + "/testngFile");

			}

			for (File file : homeDir.listFiles()) {
				if (file.getName().toLowerCase() != null) {
					suitefiles.add(file.getAbsolutePath());
				}
			}

			runner.setTestSuites(suitefiles);

		}

		String language=ConfigManager.getloginlang();

		if(language.equals("sin")) {
			language="";
		}
		System.getProperties().setProperty("testng.outpur.dir", "testng-report");
		runner.setOutputDirectory("testng-report");
		System.getProperties().setProperty("emailable.report2.name", "RESIDENT-" + BaseTestCase.environment + "-"
				+ language + "-run-" + System.currentTimeMillis() + "-report.html");


		runner.run();

		MockSMTPListener mockSMTPListener = new MockSMTPListener();
		mockSMTPListener.bTerminate = true;

		System.exit(0);

	}

	public static String getGlobalResourcePath() {
		if (checkRunType().equalsIgnoreCase("JAR")) {
			return new File(jarUrl).getParentFile().getAbsolutePath().toString();
		} else if (checkRunType().equalsIgnoreCase("IDE")) {
			String path = new File(TestRunner.class.getClassLoader().getResource("").getPath()).getAbsolutePath()
					.toString();
			if (path.contains("test-classes"))
				path = path.replace("test-classes", "classes");
			return path;
		}
		return "Global Resource File Path Not Found";
	}

	public static String getResourcePath() {
		if (checkRunType().equalsIgnoreCase("JAR")) {
			return new File(jarUrl).getParentFile().getAbsolutePath().toString()+"/resources/";
		} else if (checkRunType().equalsIgnoreCase("IDE")) {
			String path = System.getProperty("user.dir") +  System.getProperty("path.config");

			//	String path = new File(TestRunner.class.getClassLoader().getResource("").getPath()).getAbsolutePath()
			//				.toString();
			if (path.contains("test-classes"))
				path = path.replace("test-classes", "classes");
			return path;
		}
		return "Global Resource File Path Not Found";
	}

	public static String checkRunType() {
		if (TestRunner.class.getResource("TestRunner.class").getPath().toString().contains(".jar"))
			return "JAR";
		else
			return "IDE";
	}
	public static String GetKernalFilename(){
		String path = System.getProperty("env.user");
		String kernalpath=null;
		if(System.getProperty("env.user")==null || System.getProperty("env.user").equals("")) {
			kernalpath="Kernel.properties";

		}else {
			kernalpath="Kernel_"+path+".properties";
		}
		return kernalpath;
	}

}
