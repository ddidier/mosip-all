package io.mosip.kernel.masterdata.test.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.masterdata.constant.BlocklistedWordsErrorCode;
import io.mosip.kernel.masterdata.constant.LocationErrorCode;
import io.mosip.kernel.masterdata.dto.ApplicationDto;
import io.mosip.kernel.masterdata.dto.BiometricAttributeDto;
import io.mosip.kernel.masterdata.dto.BiometricTypeDto;
import io.mosip.kernel.masterdata.dto.BlockListedWordsRequest;
import io.mosip.kernel.masterdata.dto.BlocklistedWordListRequestDto;
import io.mosip.kernel.masterdata.dto.BlocklistedWordsDto;
import io.mosip.kernel.masterdata.dto.DocumentCategoryDto;
import io.mosip.kernel.masterdata.dto.DocumentTypeDto;
import io.mosip.kernel.masterdata.dto.ExceptionalHolidayDto;
import io.mosip.kernel.masterdata.dto.LanguageDto;
import io.mosip.kernel.masterdata.dto.LocationDto;
import io.mosip.kernel.masterdata.dto.LocationHierarchyLevelDto;
import io.mosip.kernel.masterdata.dto.LocationHierarchyLevelResponseDto;
import io.mosip.kernel.masterdata.dto.TemplateDto;
import io.mosip.kernel.masterdata.dto.WeekDaysResponseDto;
import io.mosip.kernel.masterdata.dto.WorkingDaysResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.ApplicationResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.BiometricAttributeResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.BiometricTypeResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.BlockListedWordsResponse;
import io.mosip.kernel.masterdata.dto.getresponse.DocumentCategoryResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.ExceptionalHolidayResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.LanguageResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.LocationHierarchyDto;
import io.mosip.kernel.masterdata.dto.getresponse.LocationHierarchyResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.LocationResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.ResgistrationCenterStatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.StatusResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.TemplateResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.ValidDocumentTypeResponseDto;
import io.mosip.kernel.masterdata.dto.getresponse.WeekDaysDto;
import io.mosip.kernel.masterdata.dto.postresponse.CodeResponseDto;
import io.mosip.kernel.masterdata.dto.postresponse.PostLocationCodeResponseDto;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.response.ColumnCodeValue;
import io.mosip.kernel.masterdata.dto.response.FilterResponseCodeDto;
import io.mosip.kernel.masterdata.dto.response.LocationPostResponseDto;
import io.mosip.kernel.masterdata.dto.response.LocationPutResponseDto;
import io.mosip.kernel.masterdata.entity.Holiday;
import io.mosip.kernel.masterdata.entity.IdType;
import io.mosip.kernel.masterdata.entity.Location;
import io.mosip.kernel.masterdata.entity.RegistrationCenter;
import io.mosip.kernel.masterdata.entity.id.CodeAndLanguageCodeID;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.DynamicFieldRepository;
import io.mosip.kernel.masterdata.repository.HolidayRepository;
import io.mosip.kernel.masterdata.repository.IdTypeRepository;
import io.mosip.kernel.masterdata.repository.RegistrationCenterRepository;
import io.mosip.kernel.masterdata.service.ApplicationService;
import io.mosip.kernel.masterdata.service.BiometricAttributeService;
import io.mosip.kernel.masterdata.service.BiometricTypeService;
import io.mosip.kernel.masterdata.service.BlocklistedWordsService;
import io.mosip.kernel.masterdata.service.DeviceService;
import io.mosip.kernel.masterdata.service.DeviceSpecificationService;
import io.mosip.kernel.masterdata.service.DeviceTypeService;
import io.mosip.kernel.masterdata.service.DocumentCategoryService;
import io.mosip.kernel.masterdata.service.DocumentTypeService;
import io.mosip.kernel.masterdata.service.DynamicFieldService;
import io.mosip.kernel.masterdata.service.ExceptionalHolidayService;
import io.mosip.kernel.masterdata.service.HolidayService;
import io.mosip.kernel.masterdata.service.LanguageService;
import io.mosip.kernel.masterdata.service.LocationHierarchyService;
import io.mosip.kernel.masterdata.service.LocationService;
import io.mosip.kernel.masterdata.service.MachineService;
import io.mosip.kernel.masterdata.service.MachineSpecificationService;
import io.mosip.kernel.masterdata.service.MachineTypeService;
import io.mosip.kernel.masterdata.service.RegWorkingNonWorkingService;
import io.mosip.kernel.masterdata.service.RegistrationCenterService;
import io.mosip.kernel.masterdata.service.RegistrationCenterTypeService;
import io.mosip.kernel.masterdata.service.TemplateFileFormatService;
import io.mosip.kernel.masterdata.service.TemplateService;
import io.mosip.kernel.masterdata.service.ZoneService;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import io.mosip.kernel.masterdata.utils.AuditUtil;
import io.mosip.kernel.masterdata.utils.LocalDateTimeUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
public class MasterdataControllerTest {

	// private static final String JSON_STRING_RESPONCE =
	// "{\"uinLength\":24,\"numberOfWrongAttemptsForOtp\":5,\"accountFreezeTimeoutInHours\":10,\"mobilenumberlength\":10,\"archivalPolicy\":\"arc_policy_2\",\"tokenIdLength\":23,\"restrictedNumbers\":[\"8732\",\"321\",\"65\"],\"registrationCenterId\":\"KDUE83CJ3\",\"machineId\":\"MCBD3UI3\",\"supportedLanguages\":[\"eng\",\"hnd\",\"ara\",\"deu\",\"FRN\"],\"tspIdLength\":24,\"otpTimeOutInMinutes\":2,\"notificationtype\":\"SMS|EMAIL\",\"pridLength\":32,\"vidLength\":32}";

	private static final String JSON_STRING_RESPONSE = "{\r\n" + "\"registrationConfiguration\":\r\n"
			+ "							{\"keyValidityPeriodPreRegPack\":\"3\",\"smsNotificationTemplateRegCorrection\":\"OTP for your request is $otp\",\"defaultDOB\":\"1-Jan\",\"smsNotificationTemplateOtp\":\"OTP for your request is $otp\",\"supervisorVerificationRequiredForExceptions\":\"true\",\"keyValidityPeriodRegPack\":\"3\",\"irisRetryAttempts\":\"10\",\"fingerprintQualityThreshold\":\"120\",\"multifactorauthentication\":\"true\",\"smsNotificationTemplateUpdateUIN\":\"OTP for your request is $otp\",\"supervisorAuthType\":\"password\",\"maxDurationRegPermittedWithoutMasterdataSyncInDays\":\"10\",\"modeOfNotifyingIndividual\":\"mobile\",\"emailNotificationTemplateUpdateUIN\":\"Hello $user the OTP is $otp\",\"maxDocSizeInMB\":\"150\",\"emailNotificationTemplateOtp\":\"Hello $user the OTP is $otp\",\"emailNotificationTemplateRegCorrection\":\"Hello $user the OTP is $otp\",\"faceRetry\":\"12\",\"noOfFingerprintAuthToOnboardUser\":\"10\",\"smsNotificationTemplateLostUIN\":\"OTP for your request is $otp\",\"supervisorAuthMode\":\"IRIS\",\"operatorRegSubmissionMode\":\"fingerprint\",\"officerAuthType\":\"password\",\"faceQualityThreshold\":\"25\",\"gpsDistanceRadiusInMeters\":\"3\",\"automaticSyncFreqServerToClient\":\"25\",\"maxDurationWithoutMasterdataSyncInDays\":\"7\",\"loginMode\":\"bootable dongle\",\"irisQualityThreshold\":\"25\",\"retentionPeriodAudit\":\"3\",\"fingerprintRetryAttempts\":\"234\",\"emailNotificationTemplateNewReg\":\"Hello $user the OTP is $otp\",\"passwordExpiryDurationInDays\":\"3\",\"emailNotificationTemplateLostUIN\":\"Hello $user the OTP is $otp\",\"blockRegistrationIfNotSynced\":\"10\",\"noOfIrisAuthToOnboardUser\":\"10\",\"smsNotificationTemplateNewReg\":\"OTP for your request is $otp\"},\r\n"
			+ "\r\n" + "\"globalConfiguration\":\r\n"
			+ "						{\"mosip.kernel.crypto.symmetric-algorithm-name\":\"AES\",\"mosip.kernel.virus-scanner.port\":\"3310\",\"mosip.kernel.email.max-length\":\"50\",\"mosip.kernel.email.domain.ext-max-lenght\":\"7\",\"mosip.kernel.rid.sequence-length\":\"5\",\"mosip.kernel.uin.uin-generation-cron\":\"0 * * * * *\",\"mosip.kernel.rid.centerid-length\":\"5\",\"mosip.kernel.email.special-char\":\"!#$%&'*+-\\/=?^_`{|}~.\",\"mosip.kernel.rid.requesttime-length\":\"14\",\"mosip.kernel.vid.length.sequence-limit\":\"3\",\"mosip.kernel.keygenerator.asymmetric-key-length\":\"2048\",\"mosip.kernel.uin.min-unused-threshold\":\"100000\",\"mosip.kernel.prid.sequence-limit\":\"3\",\"auth.role.prefix\":\"ROLE_\",\"mosip.kernel.email.domain.ext-min-lenght\":\"2\",\"auth.server.validate.url\":\"http:\\/\\/localhost:8091\\/auth\\/validate_token\",\"mosip.kernel.machineid.length\":\"4\",\"mosip.supported-languages\":\"eng,ara,fra,hin,deu\",\"mosip.kernel.prid.length\":\"14\",\"auth.header.name\":\"Authorization\",\"mosip.kernel.crypto.asymmetric-algorithm-name\":\"RSA\",\"mosip.kernel.phone.min-length\":\"9\",\"mosip.kernel.uin.length\":\"10\",\"mosip.kernel.virus-scanner.host\":\"104.211.209.102\",\"mosip.kernel.email.min-length\":\"7\",\"mosip.kernel.rid.machineid-length\":\"5\",\"mosip.kernel.prid.repeating-block-limit\":\"3\",\"mosip.kernel.vid.length.repeating-block-limit\":\"2\",\"mosip.kernel.rid.length\":\"29\",\"mosip.kernel.phone.max-length\":\"15\",\"mosip.kernel.prid.repeating-limit\":\"2\",\"mosip.kernel.uin.restricted-numbers\":\"786,666\",\"mosip.kernel.email.domain.special-char\":\"-\",\"mosip.kernel.vid.length.repeating-limit\":\"2\",\"mosip.kernel.registrationcenterid.length\":\"4\",\"mosip.kernel.phone.special-char\":\"+ -\",\"mosip.kernel.uin.uins-to-generate\":\"200000\",\"mosip.kernel.vid.length\":\"16\",\"mosip.kernel.tokenid.length\":\"36\",\"mosip.kernel.uin.length.repeating-block-limit\":\"2\",\"mosip.kernel.tspid.length\":\"4\",\"mosip.kernel.tokenid.sequence-limit\":\"3\",\"mosip.kernel.uin.length.repeating-limit\":\"2\",\"mosip.kernel.uin.length.sequence-limit\":\"3\",\"mosip.kernel.keygenerator.symmetric-key-length\":\"256\",\"mosip.kernel.data-key-splitter\":\"#KEY_SPLITTER#\"}\r\n"
			+ "}";

	@Autowired
	public MockMvc mockMvc;
	
		@MockBean
	private PublisherClient<String,EventModel,HttpHeaders> publisher;

	@MockBean
	AuditUtil aditUtil;

	@Qualifier("restTemplateConfig")
	@MockBean
	private RestTemplate restTemplate;

	@MockBean
	private BiometricTypeService biometricTypeService;

	private BiometricTypeDto biometricTypeDto1 = new BiometricTypeDto();
	private BiometricTypeDto biometricTypeDto2 = new BiometricTypeDto();

	private List<BiometricTypeDto> biometricTypeDtoList = new ArrayList<>();
	private BiometricTypeResponseDto biometricTypeResponseDto;

	@MockBean
	private ApplicationService applicationService;

	private ApplicationDto applicationDto = new ApplicationDto();

	private List<ApplicationDto> applicationDtoList = new ArrayList<>();

	@MockBean
	private BiometricAttributeService biometricAttributeService;

	@MockBean
	private RegWorkingNonWorkingService regWorkingNonWorkingService;

	@MockBean
	private ExceptionalHolidayService exceptionalHolidayService;

	// private final String BIOMETRIC_ATTRIBUTE_EXPECTED = "{
	// \"biometricattributes\": [ { \"code\": \"iric_black\", \"name\": \"black\",
	// \"description\": null, \"isActive\": true},{\"code\": \"iric_brown\",
	// \"name\": \"brown\", \"description\": null,\"isActive\": true } ] }";

	private List<BiometricAttributeDto> biometricattributes;

	private DocumentCategoryDto documentCategoryDto1;
	private DocumentCategoryDto documentCategoryDto2;

	private List<DocumentCategoryDto> documentCategoryDtoList = new ArrayList<>();

	@MockBean
	private DocumentCategoryService documentCategoryService;
	private DocumentCategoryResponseDto documentCategoryResponseDto = new DocumentCategoryResponseDto();

	@MockBean
	private DocumentTypeService documentTypeService;
	
	@MockBean
	private DeviceService deviceService;
	
	@MockBean
	private DeviceSpecificationService deviceSpecificationService;
	
	@MockBean
	private DeviceTypeService deviceTypeService;
	
	@MockBean
	private DynamicFieldService dynamicFieldService;

	// private final String DOCUMENT_TYPE_EXPECTED = "{ \"documents\": [ { \"code\":
	// \"addhar\", \"name\": \"adhar card\", \"description\": \"Uid\", \"langCode\":
	// \"eng\"}, { \"code\": \"residensial\", \"name\": \"residensial_prof\",
	// \"description\": \"document for residential prof\", \"langCode\": \"eng\" } ]
	// }";

	ValidDocumentTypeResponseDto validDocumentTypeResponseDto = null;

	List<DocumentTypeDto> documentTypeDtos = null;

	@MockBean
	private IdTypeRepository repository;

	private IdType idType;
	private BlocklistedWordsDto blocklistedWordsDto;
	private BlockListedWordsRequest blockListedWordsRequest;

	private BlockListedWordsResponse blockListedWordsResponse;

	@MockBean
	private LanguageService languageService;

	// private static final String LANGUAGE_JSON_STRING = "{ \"languages\": [ {
	// \"code\": \"hin\", \"name\": \"hindi\", \"family\": \"hindi\",
	// \"nativeName\": \"hindi\" } ]}";

	private LanguageResponseDto respDto;
	private List<LanguageDto> languages;
	private LanguageDto hin;

	private static String LOCATION_JSON_EXPECTED_POST = null;
	LocationHierarchyDto locationHierarchyDto = null;
	@MockBean
	private LocationService locationService;

	@MockBean
	private MachineService machineService;

	@MockBean
	private MachineSpecificationService machineSpecificationService;

	@MockBean
	private MachineTypeService machineTypeService;

	@MockBean
	private LocationHierarchyService locationHierarchyService;

	LocationDto locationDto = null;
	LocationResponseDto locationResponseDto = null;
	List<Object[]> locObjList = null;
	LocationHierarchyResponseDto locationHierarchyResponseDto = null;
	PostLocationCodeResponseDto locationCodeDto = null;
	LocationHierarchyLevelResponseDto locationHierarchyLevelResponseDto = null;
	LocationHierarchyLevelDto locationHierarchyLevelDto = null;

	@MockBean
	private HolidayRepository holidayRepository;
	@MockBean
	private RegistrationCenterRepository registrationCenterRepository;

	@MockBean
	private RegistrationCenterService registrationCenterService;
	
	@MockBean
	RegistrationCenterTypeService registrationCenterTypeService;

	@MockBean
	HolidayService holidayService;

	private RegistrationCenter registrationCenter;
	private List<Holiday> holidays;

	private ApplicationResponseDto applicationResponseDto = new ApplicationResponseDto();

	@MockBean
	private TemplateService templateService;

	private CodeAndLanguageCodeID codeAndLanguageCodeId;

	@MockBean
	private BlocklistedWordsService blocklistedWordsService;

	private List<TemplateDto> templateDtoList = new ArrayList<>();

	@MockBean
	private TemplateFileFormatService templateFileFormatService;
	
	@MockBean
	private ZoneService zoneService;
	
	@MockBean
	LocalDateTimeUtil localDateTimeUtil;

	@MockBean
	DynamicFieldRepository dynamicFieldRepository;

	private ObjectMapper mapper;

	WeekDaysResponseDto weekDaysResponseDto = new WeekDaysResponseDto();
	WorkingDaysResponseDto workingDaysResponseDto = new WorkingDaysResponseDto();
	ExceptionalHolidayResponseDto exceptionalHolidayResponseDto = new ExceptionalHolidayResponseDto();

	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		doNothing().when(aditUtil).auditRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(JSON_STRING_RESPONSE);
		biometricTypeSetup();

		applicationSetup();

		biometricAttributeSetup();

		documentCategorySetup();

		documentTypeSetup();

		idTypeSetup();

		locationSetup();
		locationHierarchyLevelSetup();

		registrationCenterController();
		blockListedWordSetUp();
		templateSetup();

		templateFileFormatSetup();

		regWorkingDaySetup();

		exceptionalHolidaySetUp();

	}

	public void regWorkingDaySetup() {

		List<WeekDaysDto> WeekDaysResponseList = new ArrayList<>();
		WeekDaysDto weekDaysDto = new WeekDaysDto();
		WeekDaysResponseList.add(weekDaysDto);
	}

	public void exceptionalHolidaySetUp() {
		List<ExceptionalHolidayDto> exceptionalHolidayList = new ArrayList<>();
		ExceptionalHolidayDto exceptionalHolidayDto = new ExceptionalHolidayDto();
		exceptionalHolidayList.add(exceptionalHolidayDto);
	}

	private void templateSetup() {
		TemplateDto templateDto = new TemplateDto();

		templateDto.setId("3");
		templateDto.setName("Email template");
		templateDto.setFileFormatCode("xml");
		templateDto.setTemplateTypeCode("EMAIL");
		templateDto.setLangCode("HIN");
		templateDtoList.add(templateDto);
	}

	private void templateFileFormatSetup() {
		codeAndLanguageCodeId = new CodeAndLanguageCodeID();
		codeAndLanguageCodeId.setCode("xml");
		codeAndLanguageCodeId.setLangCode("FRE");
	}

	private void registrationCenterController() {
		LocalDateTime specificDate = LocalDateTime.of(2018, Month.JANUARY, 1, 10, 10, 30);
		LocalDate date = LocalDate.of(2018, Month.NOVEMBER, 7);
		registrationCenter = new RegistrationCenter();
		Location location = new Location();
		location.setCode("KAR_59");
		registrationCenter.setAddressLine1("7th Street");
		registrationCenter.setAddressLine2("Lane 2");
		registrationCenter.setAddressLine3("Mylasandra-560001");
		registrationCenter.setIsActive(true);
		registrationCenter.setCenterTypeCode("PAR");
		registrationCenter.setContactPhone("987654321");
		registrationCenter.setCreatedBy("John");
		registrationCenter.setCreatedDateTime(specificDate);
		registrationCenter.setHolidayLocationCode("KAR");
		registrationCenter.setLocationCode("LOC");
		registrationCenter.setId("REG_CR_001");
		registrationCenter.setLangCode("eng");
		registrationCenter.setWorkingHours("9");
		registrationCenter.setLatitude("12.87376");
		registrationCenter.setLongitude("12.76372");
		registrationCenter.setName("RV Niketan REG CENTER");

		holidays = new ArrayList<>();

		Holiday holiday = new Holiday();
		holiday.setHolidayId(1);
		// holiday.setHolidayId(new HolidayID("KAR", date, "eng", "Diwali"));
		holiday.setLocationCode("KAR");
		holiday.setHolidayDate(date);
		holiday.setLangCode("eng");
		holiday.setHolidayName("Diwali");
		holiday.setCreatedBy("John");
		holiday.setCreatedDateTime(specificDate);
		holiday.setHolidayDesc("Diwali");
		holiday.setIsActive(true);

		holidays.add(holiday);
	}

	LocationPostResponseDto locationPostResponseDto = null;
	LocationPutResponseDto locationPutResponseDto = null;

	private void locationHierarchyLevelSetup() {
		List<LocationHierarchyLevelDto> locationHierarchyDtos = new ArrayList<>();
		
		locationHierarchyLevelDto = new LocationHierarchyLevelDto();
		locationHierarchyLevelDto.setHierarchyLevel((short) 0);
		locationHierarchyLevelDto.setHierarchyLevelName("Country");
		locationHierarchyLevelDto.setIsActive(true);
		locationHierarchyLevelDto.setLangCode("eng");

		locationHierarchyDtos.add(locationHierarchyLevelDto);
		locationHierarchyLevelDto.setHierarchyLevel((short) 1);
		locationHierarchyLevelDto.setHierarchyLevelName("Region");
		locationHierarchyLevelDto.setIsActive(true);
		locationHierarchyLevelDto.setLangCode("eng");
		locationHierarchyDtos.add(locationHierarchyLevelDto);

		locationHierarchyLevelResponseDto = new LocationHierarchyLevelResponseDto();
		locationHierarchyLevelResponseDto.setLocationHierarchyLevels(locationHierarchyDtos);
	}

	private void locationSetup() {
		List<LocationDto> locationHierarchies = new ArrayList<>();
		List<LocationHierarchyDto> locationHierarchyDtos = new ArrayList<>();

		locationResponseDto = new LocationResponseDto();
		locationDto = new LocationDto();
		locationDto.setCode("IND");
		locationDto.setName("INDIA");
		locationDto.setHierarchyLevel((short) 0);
		locationDto.setHierarchyName(null);
		locationDto.setParentLocCode(null);
		locationDto.setLangCode("HIN");

		locationDto.setIsActive(true);
		locationHierarchies.add(locationDto);
		locationDto.setCode("KAR");
		locationDto.setName("KARNATAKA");
		locationDto.setHierarchyLevel((short) 1);
		locationDto.setHierarchyName("STATE");
		locationDto.setParentLocCode("IND");
		locationDto.setLangCode("eng");
		locationDto.setIsActive(true);

		locationDto.setIsActive(true);
		locationHierarchies.add(locationDto);
		locationResponseDto.setLocations(locationHierarchies);
		LocationHierarchyDto locationHierarchyDto = new LocationHierarchyDto();
		locationHierarchyDto.setLocationHierarchylevel((short) 1);
		locationHierarchyDto.setLocationHierarchyName("COUNTRY");
		locationHierarchyDtos.add(locationHierarchyDto);
		locationHierarchyResponseDto = new LocationHierarchyResponseDto();
		locationHierarchyResponseDto.setLocations(locationHierarchyDtos);

		locationCodeDto = new PostLocationCodeResponseDto();
		locationCodeDto.setCode("TN");
		locationCodeDto.setLangCode("eng");
		RequestWrapper<LocationDto> requestDto = new RequestWrapper<>();
		requestDto.setId("mosip.create.location");
		requestDto.setVersion("1.0.0");
		requestDto.setRequest(locationDto);

		locationPostResponseDto = new LocationPostResponseDto();
		locationPostResponseDto.setCode("KAR");
		locationPostResponseDto.setName("KARNATAKA");
		locationPostResponseDto.setHierarchyLevel((short) 1);
		locationPostResponseDto.setHierarchyName("STATE");
		locationPostResponseDto.setParentLocCode("IND");
		locationPostResponseDto.setLangCode("eng");
		locationPostResponseDto.setIsActive(true);

		locationPutResponseDto = new LocationPutResponseDto();
		locationPutResponseDto.setCode("KAR");
		locationPutResponseDto.setName("KARNATAKA");
		locationPutResponseDto.setHierarchyLevel((short) 1);
		locationPutResponseDto.setHierarchyName("STATE");
		locationPutResponseDto.setParentLocCode("IND");
		locationPutResponseDto.setLangCode("eng");
		locationPutResponseDto.setIsActive(true);

		try {
			LOCATION_JSON_EXPECTED_POST = mapper.writeValueAsString(requestDto);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	private void idTypeSetup() {
		idType = new IdType();
		idType.setIsActive(true);
		idType.setCreatedBy("testCreation");
		idType.setLangCode("eng");
		idType.setCode("POA");
		idType.setDescr("Proof Of Address");
	}

	private void documentTypeSetup() {
		documentTypeDtos = new ArrayList<DocumentTypeDto>();
		DocumentTypeDto documentType = new DocumentTypeDto();
		documentType.setCode("addhar");
		documentType.setName("adhar card");
		documentType.setDescription("Uid");
		documentType.setLangCode("eng");

		documentTypeDtos.add(documentType);
		DocumentTypeDto documentType1 = new DocumentTypeDto();
		documentType1.setCode("residensial");
		documentType1.setName("residensial_prof");
		documentType1.setDescription("document for residential prof");
		documentType1.setLangCode("eng");
		documentTypeDtos.add(documentType1);
		validDocumentTypeResponseDto = new ValidDocumentTypeResponseDto(documentTypeDtos);
	}

	private void documentCategorySetup() {
		documentCategoryDto1 = new DocumentCategoryDto();
		documentCategoryDto1.setCode("101");
		documentCategoryDto1.setName("POI");
		documentCategoryDto1.setLangCode("eng");

		documentCategoryDto2 = new DocumentCategoryDto();
		documentCategoryDto2.setCode("102");
		documentCategoryDto2.setName("POR");
		documentCategoryDto2.setLangCode("eng");

		documentCategoryDtoList.add(documentCategoryDto1);
		documentCategoryDtoList.add(documentCategoryDto2);
	}

	private void biometricAttributeSetup() {
		biometricattributes = new ArrayList<>();
		BiometricAttributeDto biometricAttribute = new BiometricAttributeDto();
		biometricAttribute.setCode("iric_black");
		biometricAttribute.setName("black");
		biometricAttribute.setDescription(null);
		biometricAttribute.setIsActive(true);
		biometricattributes.add(biometricAttribute);
		BiometricAttributeDto biometricAttribute1 = new BiometricAttributeDto();
		biometricAttribute1.setCode("iric_brown");
		biometricAttribute1.setName("brown");
		biometricAttribute.setDescription(null);
		biometricAttribute1.setIsActive(true);
		biometricattributes.add(biometricAttribute1);
		new BiometricAttributeResponseDto(biometricattributes);
	}

	private void applicationSetup() {
		applicationDto.setCode("101");
		applicationDto.setName("pre-registeration");
		applicationDto.setDescription("Pre-registration Application Form");
		applicationDto.setLangCode("eng");

		applicationDtoList.add(applicationDto);

		codeAndLanguageCodeId = new CodeAndLanguageCodeID();
		codeAndLanguageCodeId.setCode("101");
		codeAndLanguageCodeId.setLangCode("eng");
	}

	private void biometricTypeSetup() {
		biometricTypeDto1.setCode("1");
		biometricTypeDto1.setName("DNA MATCHING");
		biometricTypeDto1.setDescription(null);

		biometricTypeDto2.setCode("3");
		biometricTypeDto2.setName("EYE SCAN");
		biometricTypeDto2.setDescription(null);

		biometricTypeDtoList.add(biometricTypeDto1);
		biometricTypeDtoList.add(biometricTypeDto2);

		biometricTypeResponseDto = new BiometricTypeResponseDto();
		biometricTypeResponseDto.setBiometrictypes(biometricTypeDtoList);

		codeAndLanguageCodeId = new CodeAndLanguageCodeID();
		codeAndLanguageCodeId.setCode("1");
		codeAndLanguageCodeId.setLangCode("DNA MATCHING");
	}

	private void blockListedWordSetUp() {
		blocklistedWordsDto = new BlocklistedWordsDto();
		blocklistedWordsDto.setLangCode("TST");
		blocklistedWordsDto.setIsActive(true);
		blocklistedWordsDto.setDescription("Test");
		blocklistedWordsDto.setWord("testword");
		blockListedWordsRequest = new BlockListedWordsRequest();
		blockListedWordsRequest.setBlocklistedword(blocklistedWordsDto);
		blockListedWordsResponse = new BlockListedWordsResponse();
		blockListedWordsResponse.setLangCode("TST");
		blockListedWordsResponse.setWord("testword");

	}

	// -------------------------------BiometricTypeControllerTest--------------------------
	@Test
	@WithUserDetails("zonal-admin")
	public void fetchAllBioMetricTypeTest() throws Exception {
		Mockito.when(biometricTypeService.getAllBiometricTypes()).thenReturn(biometricTypeResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/biometrictypes")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithUserDetails("zonal-admin")
	public void fetchAllBiometricTypeUsingLangCodeTest() throws Exception {
		Mockito.when(biometricTypeService.getAllBiometricTypesByLanguageCode(Mockito.anyString()))
				.thenReturn(biometricTypeResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/biometrictypes/eng"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void fetchBiometricTypeUsingCodeAndLangCode() throws Exception {
		BiometricTypeResponseDto biometricTypeResponseDto = new BiometricTypeResponseDto();
		List<BiometricTypeDto> biometricTypeDtos = new ArrayList<>();
		biometricTypeDtos.add(biometricTypeDto1);
		biometricTypeResponseDto.setBiometrictypes(biometricTypeDtos);
		Mockito.when(biometricTypeService.getBiometricTypeByCodeAndLangCode(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(biometricTypeResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/biometrictypes/1/eng"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void addBiometricTypeTest() throws Exception {
		Mockito.when(biometricTypeService.createBiometricType(Mockito.any())).thenReturn(codeAndLanguageCodeId);

		mockMvc.perform(MockMvcRequestBuilders.post("/biometrictypes").contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
						+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
						+ "    \"code\": \"1\",\n" + "    \"description\": \"string\",\n" + "    \"isActive\": true,\n"
						+ "    \"langCode\": \"eng\",\n" + "    \"name\": \"Abc\"\n" + "  }\n" + "}"))

				.andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void addBiometricTypeLanguageValidationTest() throws Exception {
		Mockito.when(biometricTypeService.createBiometricType(Mockito.any())).thenReturn(codeAndLanguageCodeId);

		mockMvc.perform(MockMvcRequestBuilders.post("/biometrictypes").contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
						+ "  \"requesttime\": \"2018-12-17T07:22:22.233Z\",\n" + "  \"request\": {\n"
						+ "    \"code\": \"1\",\n" + "    \"description\": \"string\",\n" + "    \"isActive\": true,\n"
						+ "    \"langCode\": \"akk\",\n" + "    \"name\": \"Abc\"\n" + "  }\n" + "}"))

				.andExpect(status().isOk());
	}

	// -------------------------------ApplicationControllerTest--------------------------//
	@Test
	@WithUserDetails("zonal-admin")
	public void fetchAllApplicationTest() throws Exception {
		applicationResponseDto.setApplicationtypes(applicationDtoList);
		Mockito.when(applicationService.getAllApplication()).thenReturn(applicationResponseDto);

		mockMvc.perform(MockMvcRequestBuilders.get("/applicationtypes"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void fetchAllApplicationUsingLangCodeTest() throws Exception {
		applicationResponseDto.setApplicationtypes(applicationDtoList);
		Mockito.when(applicationService.getAllApplicationByLanguageCode(Mockito.anyString()))
				.thenReturn(applicationResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/applicationtypes/eng"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void fetchApplicationUsingCodeAndLangCode() throws Exception {
		List<ApplicationDto> applicationDtos = new ArrayList<>();
		applicationDtos.add(applicationDto);
		applicationResponseDto.setApplicationtypes(applicationDtos);
		Mockito.when(applicationService.getApplicationByCodeAndLanguageCode(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(applicationResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/applicationtypes/101/eng"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void addApplicationTest() throws Exception {
		Mockito.when(applicationService.createApplication(Mockito.any())).thenReturn(codeAndLanguageCodeId);

		mockMvc.perform(MockMvcRequestBuilders.post("/applicationtypes").contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
						+ "  \"requesttime\": \"2018-12-17T07:15:06.724Z\",\n" + "  \"request\": {\n"
						+ "    \"code\": \"101\",\n" + "    \"description\": \"Pre-registration Application Form\",\n"
						+ "    \"isActive\": true,\n" + "    \"langCode\": \"eng\",\n"
						+ "    \"name\": \"pre-registeration\"\n" + "  }\n" + "}"))

				.andExpect(status().isOk());
	}

	// -------------------------------BiometricAttributeControllerTest--------------------------

	@Test
	@WithUserDetails("test")
	public void testGetBiometricAttributesByBiometricType() throws Exception {

		Mockito.when(biometricAttributeService.getBiometricAttribute(Mockito.anyString(), Mockito.anyString()))
				.thenReturn((biometricattributes));

		mockMvc.perform(MockMvcRequestBuilders.get("/getbiometricattributesbyauthtype/eng/iric"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.response.biometricattributes[0].name", is("black")));

	}

	@Test
	@WithUserDetails("test")
	public void testBiometricTypeBiometricAttributeNotFoundException() throws Exception {
		Mockito.when(biometricAttributeService.getBiometricAttribute(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new DataNotFoundException("KER-MAS-00000",
						"No biometric attributes found for specified biometric code type and language code"));
		mockMvc.perform(MockMvcRequestBuilders.get("/getbiometricattributesbyauthtype/eng/face"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void testBiometricTypeFetchException() throws Exception {
		Mockito.when(biometricAttributeService.getBiometricAttribute(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new MasterDataServiceException("KER-DOC-00000", "exception duringfatching data from db"));
		mockMvc.perform(MockMvcRequestBuilders.get("/getbiometricattributesbyauthtype/eng/iric"))
				.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
	}

	// -------------------------------DocumentCategoryControllerTest--------------------------
	@Test
	@WithUserDetails("zonal-admin")
	public void fetchAllDocumentCategoryTest() throws Exception {
		documentCategoryResponseDto.setDocumentcategories(documentCategoryDtoList);
		Mockito.when(documentCategoryService.getAllDocumentCategory()).thenReturn(documentCategoryResponseDto);

		mockMvc.perform(MockMvcRequestBuilders.get("/documentcategories"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void fetchAllDocumentCategoryUsingLangCodeTest() throws Exception {
		documentCategoryResponseDto.setDocumentcategories(documentCategoryDtoList);
		Mockito.when(documentCategoryService.getAllDocumentCategoryByLaguageCode(Mockito.anyString()))
				.thenReturn(documentCategoryResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/documentcategories/eng"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void fetchDocumentCategoryUsingCodeAndLangCode() throws Exception {
		List<DocumentCategoryDto> documentCategoryDtos = new ArrayList<>();
		documentCategoryDtos.add(documentCategoryDto1);
		documentCategoryResponseDto.setDocumentcategories(documentCategoryDtos);
		Mockito.when(
				documentCategoryService.getDocumentCategoryByCodeAndLangCode(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(documentCategoryResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/documentcategories/101/eng"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithUserDetails("test")
	public void updateDocumentCategoryStatusTest() throws Exception {

		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Document Categories");
		Mockito.when(documentCategoryService.updateDocumentCategory(Mockito.anyString(), Mockito.anyBoolean()))
				.thenReturn(dto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/documentcategories").characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON).param("code", "ABC")
				.param("isActive", "true");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	// -------------------------------DocumentTypeControllerTest--------------------------
	@Test
	@WithUserDetails("resident")
	public void testGetDoucmentTypesForDocumentCategoryAndLangCode() throws Exception {

		Mockito.when(documentTypeService.getAllValidDocumentType(Mockito.anyString(), Mockito.anyString()))
				.thenReturn((documentTypeDtos));
		mockMvc.perform(MockMvcRequestBuilders.get("/documenttypes/poa/eng"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.response.documents[0].code", is("addhar")));

	}

	@Test
	@WithUserDetails("resident")
	public void testDocumentTypeNotFoundException() throws Exception {
		Mockito.when(documentTypeService.getAllValidDocumentType(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new DataNotFoundException("KER-DOC-10001",
						"No documents found for specified document category code and language code"));
		mockMvc.perform(MockMvcRequestBuilders.get("/documenttypes/poc/eng"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithUserDetails("test")
	public void updateDocumentTypeStatusTest() throws Exception {

		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Document Types");
		Mockito.when(documentTypeService.updateDocumentType(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(dto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/documenttypes").characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON).param("code", "ABC")
				.param("isActive", "true");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void testDocumentTypeFetchException() throws Exception {
		Mockito.when(documentTypeService.getAllValidDocumentType(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new MasterDataServiceException("KER-DOC-10000", "exception during fatching data from db"));
		mockMvc.perform(MockMvcRequestBuilders.get("/documenttypes/poc/eng"))
				.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
	}
	// -------------------------------IdTypesControllerTest--------------------------

	@Test
	@WithUserDetails("zonal-admin")
	public void testIdTypeController() throws Exception {
		List<IdType> idTypeList = new ArrayList<>();
		idTypeList.add(idType);
		Mockito.when(repository.findByLangCode(anyString())).thenReturn(idTypeList);
		mockMvc.perform(get("/idtypes/{languagecode}", "eng")).andExpect(status().isOk());
	}

	// -------------------------------LanguageControllerTest--------------------------
	@Test
	@WithUserDetails("zonal-admin")
	public void testGetAllLanguages() throws Exception {
		loadSuccessData();
		Mockito.when(languageService.getAllLaguages()).thenReturn(respDto);

		mockMvc.perform(MockMvcRequestBuilders.get("/languages")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.response.languages[0].code", is("hin")));

	}

	@Test
	@WithUserDetails("zonal-admin")
	public void testGetAllLanguagesForLanguageNotFoundException() throws Exception {
		Mockito.when(languageService.getAllLaguages())
				.thenThrow(new DataNotFoundException("KER-MAS-0987", "No Language found"));
		mockMvc.perform(MockMvcRequestBuilders.get("/languages")).andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	@WithUserDetails("test")
	public void testGetAllLanguagesForLanguageFetchException() throws Exception {
		Mockito.when(languageService.getAllLaguages())
				.thenThrow(new MasterDataServiceException("KER-MAS-0988", "Error occured while fetching language"));
		mockMvc.perform(MockMvcRequestBuilders.get("/languages"))
				.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
	}

	private void loadSuccessData() {
		respDto = new LanguageResponseDto();
		languages = new ArrayList<>();

		// creating language
		hin = new LanguageDto();
		hin.setCode("hin");
		hin.setName("hindi");
		hin.setFamily("hindi");
		hin.setNativeName("hindi");

		// adding language to list
		languages.add(hin);

		respDto.setLanguages(languages);

	}

	// -------------------------------LocationControllerTest--------------------------

	@Test
	@WithUserDetails("individual")
	public void testGetAllLocationHierarchy() throws Exception {

		Mockito.when(locationService.getLocationDetails(Mockito.anyString())).thenReturn(locationHierarchyResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/locations/eng")).andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	@WithUserDetails("test")
	public void testGetLocatonHierarchyByLocCodeAndLangCode() throws Exception {
		Mockito.doReturn(locationResponseDto).when(locationService).getLocationHierarchyByLangCode(Mockito.anyString(),
				Mockito.anyString());

		mockMvc.perform(MockMvcRequestBuilders.get("/locations/KAR/eng"))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	@WithUserDetails("individual")
	public void testGetAllLocationsNoRecordsFoundException() throws Exception {
		Mockito.when(locationService.getLocationDetails(Mockito.anyString()))
				.thenThrow(new MasterDataServiceException("1111111", "Error from database"));
		mockMvc.perform(MockMvcRequestBuilders.get("/locations/eng"))
				.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
	}

	@Test
	@WithUserDetails("individual")
	public void testGetAllLocationsDataBaseException() throws Exception {
		Mockito.when(locationService.getLocationDetails(Mockito.anyString()))
				.thenThrow(new DataNotFoundException("3333333", "Location Hierarchy does not exist"));
		mockMvc.perform(MockMvcRequestBuilders.get("/locations/eng")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void testGetLocationsByLangCodeAndLocCodeDataBaseException() throws Exception {
		Mockito.when(locationService.getLocationHierarchyByLangCode(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new MasterDataServiceException("1111111", "Error from database"));
		mockMvc.perform(MockMvcRequestBuilders.get("/locations/KAR/eng"))
				.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
	}

	@Test
	@WithUserDetails("test")
	public void testGetLocationsByLangCodeAndLocCodeNoRecordsFoundException() throws Exception {
		Mockito.when(locationService.getLocationHierarchyByLangCode(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new DataNotFoundException("3333333", "Location Hierarchy does not exist"));
		mockMvc.perform(MockMvcRequestBuilders.get("/locations/KAR/eng"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * @Test @WithUserDetails("test") public void testSaveLocationHierarchy() throws
	 *       Exception {
	 *       Mockito.when(locationService.createLocationHierarchy(Mockito.any())).thenReturn(locationCodeDto);
	 *       mockMvc.perform(MockMvcRequestBuilders.post("/locations").contentType(MediaType.APPLICATION_JSON)
	 *       .content(LOCATION_JSON_EXPECTED_POST)).andExpect(MockMvcResultMatchers.status().isOk());
	 *       }
	 * 
	 * @Test @WithUserDetails("test") public void
	 *       testNegativeSaveLocationHierarchy() throws Exception {
	 *       Mockito.when(locationService.createLocationHierarchy(Mockito.any()))
	 *       .thenThrow(new MasterDataServiceException("1111111", "Error from
	 *       database"));
	 *       mockMvc.perform(MockMvcRequestBuilders.post("/locations").contentType(MediaType.APPLICATION_JSON)
	 *       .content(LOCATION_JSON_EXPECTED_POST))
	 *       .andExpect(MockMvcResultMatchers.status().isInternalServerError()); }
	 * 
	 **/
	@Test
	@WithUserDetails("global-admin")
	public void testUpdateLocationDetails() throws Exception {
		Mockito.when(locationService.updateLocationDetails(Mockito.any())).thenReturn(locationPutResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.put("/locations").contentType(MediaType.APPLICATION_JSON)
				.content(LOCATION_JSON_EXPECTED_POST)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithUserDetails("global-admin")
	public void testUpdateLocationDetailsException() throws Exception {
		Mockito.when(locationService.updateLocationDetails(Mockito.any()))
				.thenThrow(new MasterDataServiceException("1111111", "Error from database"));
		mockMvc.perform(MockMvcRequestBuilders.put("/locations").contentType(MediaType.APPLICATION_JSON)
				.content(LOCATION_JSON_EXPECTED_POST))
				.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
	}

	@Test
	@WithUserDetails("individual")
	public void getImmediateChildrenTest() throws Exception {
		Mockito.when(locationService.getImmediateChildrenByLocCodeAndLangCode(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(locationResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/locations/immediatechildren/eng/KAR"))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	@WithUserDetails("individual")
	public void getImmediateChildrenServiceExceptionTest() throws Exception {
		Mockito.when(locationService.getImmediateChildrenByLocCodeAndLangCode(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new MasterDataServiceException("1111111", "Error from database"));
		mockMvc.perform(MockMvcRequestBuilders.get("/locations/immediatechildren/eng/KAR"))
				.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));

	}

	@Test
	@WithUserDetails("individual")
	public void getImmediateChildrenDataNotFoundExceptionTest() throws Exception {
		Mockito.when(locationService.getImmediateChildrenByLocCodeAndLangCode(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new DataNotFoundException("111111", "data not found"));
		mockMvc.perform(MockMvcRequestBuilders.get("/locations/immediatechildren/eng/KAR"))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	@WithUserDetails("test")
	public void testDeleteLocationDetails() throws Exception {
		Mockito.when(locationService.deleteLocationDetials(Mockito.anyString())).thenReturn(new CodeResponseDto());
		mockMvc.perform(MockMvcRequestBuilders.delete("/locations/KAR").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	@WithUserDetails("individual")
	public void getLocationDataByHierarchyNameSuccessTest() throws Exception {

		Mockito.when(locationService.getLocationDataByHierarchyName(Mockito.anyString()))
				.thenReturn(locationResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/locations/locationhierarchy/state"))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	@WithUserDetails("individual")
	public void dataNotfoundExceptionTest() throws Exception {

		Mockito.when(locationService.getLocationDataByHierarchyName(Mockito.anyString()))
				.thenThrow(new DataNotFoundException(LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorCode(),
						LocationErrorCode.LOCATION_NOT_FOUND_EXCEPTION.getErrorMessage()));
		mockMvc.perform(MockMvcRequestBuilders.get("/locations/locationhierarchy/123"))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	@WithUserDetails("individual")
	public void masterDataServiceExceptionTest() throws Exception {

		Mockito.when(locationService.getLocationDataByHierarchyName(Mockito.anyString()))
				.thenThrow(new MasterDataServiceException(LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorCode(),
						LocationErrorCode.LOCATION_FETCH_EXCEPTION.getErrorMessage()));
		mockMvc.perform(MockMvcRequestBuilders.get("/locations/locationhierarchy/123"))
				.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));

	}

	@Test
	@WithUserDetails("individual")
	public void getLocationDataByHierarchyLevelsSuccessTest() throws Exception {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		LocalDateTime dateTime = LocalDateTime.parse("2020-03-23T07:39:19.342Z", formatter);
		LocalDateTime currentTimestamp = LocalDateTime.now();
		
		Mockito.when(localDateTimeUtil.getLocalDateTimeFromTimeStamp(currentTimestamp, "2020-03-23T07:39:19.342Z")).thenReturn(dateTime);
		Mockito.when(locationHierarchyService.getLocationHierarchy(dateTime, currentTimestamp)).thenReturn(locationHierarchyLevelResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/locationHierarchyLevels"))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	/*
	 * //
	 * -------------------------------RegistrationCenterControllerTest--------------
	 * ------------
	 * 
	 * @Test public void testGetRegistraionCenterHolidaysSuccess() throws Exception
	 * {
	 * Mockito.when(registrationCenterRepository.findByIdAndLanguageCode(anyString()
	 * , anyString())) .thenReturn(registrationCenter);
	 * Mockito.when(holidayRepository.findAllByLocationCodeYearAndLangCode(anyString
	 * (), anyString(), anyInt())) .thenReturn(holidays); mockMvc.perform(get(
	 * "/getregistrationcenterholidays/{languagecode}/{registrationcenterid}/{year}",
	 * "eng", "REG_CR_001", 2018)).andExpect(status().isOk()); }
	 * 
	 * @Test public void testGetRegistraionCenterHolidaysNoRegCenterFound() throws
	 * Exception { mockMvc.perform(get(
	 * "/getregistrationcenterholidays/{languagecode}/{registrationcenterid}/{year}",
	 * "eng", "REG_CR_001", 2017)).andExpect(status().isNotFound()); }
	 * 
	 * @Test public void
	 * testGetRegistraionCenterHolidaysRegistrationCenterFetchException() throws
	 * Exception {
	 * Mockito.when(registrationCenterRepository.findByIdAndLanguageCode(anyString()
	 * , anyString())) .thenThrow(DataRetrievalFailureException.class);
	 * mockMvc.perform(get(
	 * "/getregistrationcenterholidays/{languagecode}/{registrationcenterid}/{year}",
	 * "eng", "REG_CR_001", 2017)).andExpect(status().isInternalServerError()); }
	 * 
	 * @Test public void testGetRegistraionCenterHolidaysHolidayFetchException()
	 * throws Exception {
	 * Mockito.when(registrationCenterRepository.findByIdAndLanguageCode(anyString()
	 * , anyString())) .thenReturn(registrationCenter);
	 * Mockito.when(holidayRepository.findAllByLocationCodeYearAndLangCode(anyString
	 * (), anyString(), anyInt())) .thenThrow(DataRetrievalFailureException.class);
	 * mockMvc.perform(get(
	 * "/getregistrationcenterholidays/{languagecode}/{registrationcenterid}/{year}",
	 * "eng", "REG_CR_001", 2018)).andExpect(status().isInternalServerError()); }
	 */

	// -------------------------------TemplateControllerTest--------------------------
	@Test
	@WithUserDetails("global-admin")
	public void getAllTemplateByTest() throws Exception {
		TemplateResponseDto templateResponseDto = new TemplateResponseDto();
		templateResponseDto.setTemplates(templateDtoList);
		Mockito.when(templateService.getAllTemplate()).thenReturn(templateResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/templates")).andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("id-auth")
	public void getAllTemplateByLanguageCodeTest() throws Exception {
		TemplateResponseDto templateResponseDto = new TemplateResponseDto();
		templateResponseDto.setTemplates(templateDtoList);
		Mockito.when(templateService.getAllTemplateByLanguageCode(Mockito.anyString())).thenReturn(templateResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/templates/HIN")).andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("individual")
	public void getAllTemplateByLanguageCodeAndTemplateTypeCodeTest() throws Exception {
		TemplateResponseDto templateResponseDto = new TemplateResponseDto();
		templateResponseDto.setTemplates(templateDtoList);
		Mockito.when(templateService.getAllTemplateByLanguageCodeAndTemplateTypeCode(Mockito.anyString(),
				Mockito.anyString())).thenReturn(templateResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/templates/HIN/EMAIL")).andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("resident")
	public void getAllTemplateByTemplateTypeCodeTest() throws Exception {
		TemplateResponseDto templateResponseDto = new TemplateResponseDto();
		templateResponseDto.setTemplates(templateDtoList);
		Mockito.when(templateService.getAllTemplateByTemplateTypeCode(Mockito.anyString()))
				.thenReturn(templateResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/templates/templatetypecodes/EMAIL")).andExpect(status().is(200));
	}
	
	@Test
	@WithUserDetails("test")
	public void updateTemplateStatusTest() throws Exception {

		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Templates");
		Mockito.when(templateService.updateTemplates(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(dto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/templates").characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON).param("id", "html")
				.param("isActive", "true");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	// -----------------------------TemplateFileFormatControllerTest------------------------

	@Test
	@WithUserDetails("test")
	public void createTemplateFileFormatTest() throws Exception {
		Mockito.when(templateFileFormatService.createTemplateFileFormat(Mockito.any()))
				.thenReturn(codeAndLanguageCodeId);
		mockMvc.perform(MockMvcRequestBuilders.post("/templatefileformats").contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
						+ "  \"requesttime\": \"2018-12-17T07:19:33.655Z\",\n" + "  \"request\": {\n"
						+ "    \"code\": \"xml\",\n" + "    \"description\": \"string\",\n"
						+ "    \"isActive\": true,\n" + "    \"langCode\": \"eng\"\n" + "  }\n" + "}"))

				.andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void createTemplateFileFormatLanguageCodeValidatorTest() throws Exception {
		Mockito.when(templateFileFormatService.createTemplateFileFormat(Mockito.any()))
				.thenReturn(codeAndLanguageCodeId);
		mockMvc.perform(MockMvcRequestBuilders.post("/templatefileformats").contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + "  \"id\": \"string\",\n" + "  \"version\": \"string\",\n"
						+ "  \"requesttime\": \"2018-12-17T07:19:33.655Z\",\n" + "  \"request\": {\n"
						+ "    \"code\": \"xml\",\n" + "    \"description\": \"string\",\n"
						+ "    \"isActive\": true,\n" + "    \"langCode\": \"xxx\"\n" + "  }\n" + "}"))

				.andExpect(status().isOk());
	}
	
	@Test
	@WithUserDetails("test")
	public void updateFileFormatStatusTest() throws Exception {

		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Templates File Formats");
		Mockito.when(templateFileFormatService.updateTemplateFileFormat(Mockito.anyString(), Mockito.anyBoolean()))
				.thenReturn(dto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/templatefileformats").characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON).param("code", "12345")
				.param("isActive", "true");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void validateWordsTest() throws Exception {
		List<String> words = new ArrayList<>();
		words.add("test");

		BlocklistedWordListRequestDto blocklistedWordListRequestDto = new BlocklistedWordListRequestDto();
		blocklistedWordListRequestDto.setBlocklistedwords(words);
		RequestWrapper<BlocklistedWordListRequestDto> requestWrapper = new RequestWrapper<>();
		requestWrapper.setRequest(blocklistedWordListRequestDto);

		Mockito.when(blocklistedWordsService.validateWord(words)).thenReturn(true);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/blocklistedwords/words")
				.characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestWrapper));

		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.response.code", is("Valid")));
	}

	@Test
	@WithUserDetails("test")
	public void validateWordsFalseTest() throws Exception {
		List<String> words = new ArrayList<>();
		words.add("test");

		BlocklistedWordListRequestDto blocklistedWordListRequestDto = new BlocklistedWordListRequestDto();
		blocklistedWordListRequestDto.setBlocklistedwords(words);
		RequestWrapper<BlocklistedWordListRequestDto> requestWrapper = new RequestWrapper<>();
		requestWrapper.setRequest(blocklistedWordListRequestDto);

		Mockito.when(blocklistedWordsService.validateWord(words)).thenReturn(false);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/blocklistedwords/words")
				.characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestWrapper));
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.response.code", is("Invalid")));
	}
	
	@Test
	@WithUserDetails("test")
	public void updateBlockListedWordStatusTest() throws Exception {

		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for BlocklistedWords");
		Mockito.when(blocklistedWordsService.updateBlockListedWordStatus(Mockito.anyString(), Mockito.anyBoolean()))
				.thenReturn(dto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/blocklistedwords").characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON).param("word", "ABC")
				.param("isActive", "true");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void updateDeviceStatusTest() throws Exception {

		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Devices");
		Mockito.when(deviceService.updateDeviceStatus(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(dto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/devices").characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON).param("id", "ABC")
				.param("isActive", "true");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void updateDeviceSpecificationStatusTest() throws Exception {

		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Device Specification");
		Mockito.when(deviceSpecificationService.updateDeviceSpecification(Mockito.anyString(), Mockito.anyBoolean()))
				.thenReturn(dto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/devicespecifications").characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON).param("id", "ABC")
				.param("isActive", "true");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void updateDeviceTypeStatusTest() throws Exception {

		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Device Type");
		Mockito.when(deviceTypeService.updateDeviceType(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(dto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/devicetypes").characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON).param("code", "ABC")
				.param("isActive", "true");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void updateAllDynamicFieldStatusTest() throws Exception {

		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Dynamic Fields");
		Mockito.when(dynamicFieldRepository.updateAllDynamicFieldIsActive(Mockito.anyString(), Mockito.anyBoolean(), Mockito.any(),
				Mockito.anyString())).thenReturn(1);
		Mockito.when(dynamicFieldService.updateDynamicFieldStatus(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(dto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/dynamicfields/all").characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON).param("fieldName", "ABC")
				.param("isActive", "true");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void updateDynamicFieldStatusTest() throws Exception {

		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Dynamic Fields");
		Mockito.when(dynamicFieldService.updateDynamicFieldValueStatus(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(dto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/dynamicfields").characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON).param("id", "ABC")
				.param("isActive", "true");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void validateWordsExceptionTest() throws Exception {
		List<String> words = new ArrayList<>();
		words.add("test");

		BlocklistedWordListRequestDto blocklistedWordListRequestDto = new BlocklistedWordListRequestDto();
		blocklistedWordListRequestDto.setBlocklistedwords(words);
		RequestWrapper<BlocklistedWordListRequestDto> requestWrapper = new RequestWrapper<>();
		requestWrapper.setRequest(blocklistedWordListRequestDto);

		Mockito.when(blocklistedWordsService.validateWord(Mockito.any()))
				.thenThrow(new MasterDataServiceException(
						BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_FETCH_EXCEPTION.getErrorCode(),
						BlocklistedWordsErrorCode.BLOCKLISTED_WORDS_FETCH_EXCEPTION.getErrorMessage()));

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/blocklistedwords/words")
				.characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestWrapper));
		mockMvc.perform(requestBuilder).andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
	}

	// --------------------------Registration
	// center-validatetimeStamp------------------//

	@Test
	@WithUserDetails("reg-processor")
	public void validateTimestampWithRegistrationCenter() throws Exception {
		ResgistrationCenterStatusResponseDto resgistrationCenterStatusResponseDto = new ResgistrationCenterStatusResponseDto();
		resgistrationCenterStatusResponseDto.setStatus("Accepted");
		Mockito.when(registrationCenterService.validateTimeStampWithRegistrationCenter(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString())).thenReturn(resgistrationCenterStatusResponseDto);

		mockMvc.perform(get("/registrationcenters/validate/1/eng/2017-12-12T17:59:59.999Z")).andExpect(status().isOk());

	}

	@Test
	@WithUserDetails("reg-processor")
	public void validateTimestampWithRegistrationCenterMasterDataExceptionTest() throws Exception {
		ResgistrationCenterStatusResponseDto resgistrationCenterStatusResponseDto = new ResgistrationCenterStatusResponseDto();
		resgistrationCenterStatusResponseDto.setStatus("Accepted");
		Mockito.when(registrationCenterService.validateTimeStampWithRegistrationCenter(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new MasterDataServiceException("11111", "Database exception"));

		mockMvc.perform(get("/registrationcenters/validate/1/eng/2017-12-12T17:59:59.999Z"))
				.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));

	}

	@Test
	@WithUserDetails("reg-processor")
	public void validateTimestampWithRegistrationCenterDataNotFoundExceptionTest() throws Exception {
		ResgistrationCenterStatusResponseDto resgistrationCenterStatusResponseDto = new ResgistrationCenterStatusResponseDto();
		resgistrationCenterStatusResponseDto.setStatus("Accepted");
		Mockito.when(registrationCenterService.validateTimeStampWithRegistrationCenter(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new DataNotFoundException("11111", "Data not found exception"));

		mockMvc.perform(get("/registrationcenters/validate/1/eng/2017-12-12T17:59:59.999Z")).andExpect(status().isOk());

	}
	
	@Test
	@WithUserDetails("test")
	public void updateRegistrationCenterAdminStatusTest() throws Exception {

		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Registration Centers");
		Mockito.when(registrationCenterService.updateRegistrationCenter(Mockito.anyString(), Mockito.anyBoolean()))
				.thenReturn(dto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/registrationcenters").characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON).param("id", "10002")
				.param("isActive", "true");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}
	
	@Test
	@WithUserDetails("test")
	public void updateHolidayStatusTest() throws Exception {

		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Holiday");
		Mockito.when(
				holidayService.updateHolidayStatus(Mockito.anyString(), Mockito.anyBoolean()))
				.thenReturn(dto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/holidays")
				.characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).param("holidayId", "10005").param("isActive", "true");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("global-admin")
	public void updateLanguageStatusTest() throws Exception {

		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Language");
		Mockito.when(languageService.updateLanguageStatus(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(dto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/languages").characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON).param("code", "ara")
				.param("isActive", "true");
		mockMvc.perform(requestBuilder).andExpect(status().is(200));
	}

	@Test
	@WithUserDetails("test")
	public void updateLocationStatusTest() throws Exception {

		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Location");
		Mockito.when(locationService.updateLocationStatus(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(dto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/locations").characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON).param("code", "NTH")
				.param("isActive", "true");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void updateMachineStatusTest() throws Exception {

		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Machine");
		Mockito.when(machineService.updateMachineStatus(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(dto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/machines").characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON).param("id", "11008")
				.param("isActive", "true");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void updateMachineSpecificationStatusTest() throws Exception {

		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for MachineSpecification");
		Mockito.when(
				machineSpecificationService.updateMachineSpecificationStatus(Mockito.anyString(), Mockito.anyBoolean()))
				.thenReturn(dto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/machinespecifications")
				.characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).param("id", "11009").param("isActive", "true");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void updateMachineTypeStatusTest() throws Exception {

		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for MachineType");
		Mockito.when(machineTypeService.updateMachineTypeStatus(Mockito.anyString(), Mockito.anyBoolean()))
				.thenReturn(dto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/machinetypes").characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON).param("code", "AGB")
				.param("isActive", "true");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("test")
	public void updateRegistrationCenterTypeStatusTest() throws Exception {

		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for Registration Center Types");
		Mockito.when(
				registrationCenterTypeService.updateRegistrationCenterType(Mockito.anyString(), Mockito.anyBoolean()))
				.thenReturn(dto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/registrationcentertypes")
				.characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).param("code", "BCC").param("isActive", "true");
		mockMvc.perform(requestBuilder).andExpect(status().is(200));
	}

	// -----------------------------WorkingDayControllerTest------------------------
	@Test
	@WithUserDetails("reg-processor")
	public void weekDaysControllerTest() throws Exception {

		Mockito.when(regWorkingNonWorkingService.getWeekDaysList("10001", "eng")).thenReturn(weekDaysResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/weekdays/10001/eng")).andExpect(status().isOk());

	}

	@Test
	@WithUserDetails("reg-processor")
	public void workDaysControllerTest() throws Exception {

		Mockito.when(regWorkingNonWorkingService.getWorkingDays("10001", "101")).thenReturn(workingDaysResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/workingdays/10001/101")).andExpect(status().isOk());

	}
	@Test
	@WithUserDetails("reg-processor")
	public void updateWorkDaysStatusControllerTest() throws Exception {

		StatusResponseDto dto = new StatusResponseDto();
		dto.setStatus("Status updated successfully for workingDays");
		Mockito.when(regWorkingNonWorkingService.updateWorkingDaysStatus(Mockito.anyString(), Mockito.anyBoolean()))
				.thenReturn(dto);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/workingdays").characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON).param("code", "ABC")
				.param("isActive", "true");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}
	@Test
	@WithUserDetails("reg-processor")
	public void workDaysControllerByLangCodeTest() throws Exception {

		Mockito.when(regWorkingNonWorkingService.getWorkingDays("eng")).thenReturn(workingDaysResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/workingdays/eng")).andExpect(status().isOk());

	}

	// -----------------------------ExceptionalHolidayControllerTest------------------------
	@Test
	@WithUserDetails("reg-processor")
	public void exceptionalHolidayControllerTest() throws Exception {

		Mockito.when(exceptionalHolidayService.getAllExceptionalHolidays("10001", "eng"))
				.thenReturn(exceptionalHolidayResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/exceptionalholidays/10001/eng")).andExpect(status().isOk());

	}
	
	@Test
	@WithUserDetails("test")
	public void zoneFilterValuesTest() throws Exception {

		FilterValueDto filterValueDto = new FilterValueDto();
		List<FilterDto> filters = new ArrayList<FilterDto>();
		FilterDto filterDto = new FilterDto();
		filterDto.setColumnName("name");
		filterDto.setText("SAFi");
		filterDto.setType("unique");
		filters.add(filterDto);
		filterValueDto.setFilters(filters);
		filterValueDto.setLanguageCode("eng");

		FilterResponseCodeDto filterResponseCodeDto = new FilterResponseCodeDto();
		List<ColumnCodeValue> ResponseFilters = new ArrayList<ColumnCodeValue>();
		ColumnCodeValue codeValue = new ColumnCodeValue();
		codeValue.setFieldCode("MRS");
		codeValue.setFieldID("name");
		codeValue.setFieldValue("Marrakesh-Safi");
		ResponseFilters.add(codeValue);
		filterResponseCodeDto.setFilters(ResponseFilters);

		RequestWrapper<FilterValueDto> requestWrapper = new RequestWrapper<>();
		requestWrapper.setRequest(filterValueDto);

		Mockito.when(zoneService.zoneFilterValues(Mockito.any())).thenReturn(filterResponseCodeDto);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/zones/filtervalues").characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(requestWrapper));
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

}