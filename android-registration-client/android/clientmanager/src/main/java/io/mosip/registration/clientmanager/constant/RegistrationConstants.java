package io.mosip.registration.clientmanager.constant;

import java.sql.Array;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegistrationConstants {

    public static final String COMMA = ",";
    public static final String EMPTY_STRING = "";
    public static final String AGE_GROUP = "ageGroup";
    public static final String AGE = "age";
    public static final String DEFAULT_AGE_GROUP = "adult";
    public static final String PROCESS_KEY = "_process";
    public static final String FLOW_KEY = "_flow";
    public static final String ID_SCHEMA_VERSION = "IDSchemaVersion";

    public static final String AUDIT_EXPORTED_TILL = "AuditExportedTill";
    public static final List<String> RIGHT_SLAB_ATTR = Arrays.asList("rightIndex","rightMiddle","rightRing","rightLittle");
    public static final List<String> LEFT_SLAB_ATTR = Arrays.asList("leftIndex","leftMiddle","leftRing","leftLittle");
    public static final List<String> THUMBS_ATTR = Arrays.asList("leftThumb", "rightThumb");
    public static final List<String> DOUBLE_IRIS_ATTR = Arrays.asList("leftEye", "rightEye");
    public static final List<String> FACE_ATTR = Arrays.asList("");
    public static final List<String> EXCEPTION_PHOTO_ATTR = Arrays.asList("unknown");

    public static String DEDUPLICATION_ENABLE_FLAG = "mosip.registration.mds.deduplication.enable.flag";
    public static final String ENABLE = "Y";
    public static final String DISABLE = "N";

    //SBI intents
    public static final String DISCOVERY_INTENT_ACTION = "io.sbi.device";
    public static final String D_INFO_INTENT_ACTION = ".Info";
    public static final String R_CAPTURE_INTENT_ACTION = ".rCapture";
    public static final String SBI_INTENT_REQUEST_KEY = "input";
    public static final String SBI_INTENT_RESPONSE_KEY = "response";

    //Global param keys
    public static final String MANDATORY_LANGUAGES_KEY = "mosip.mandatory-languages";
    public static final String OPTIONAL_LANGUAGES_KEY = "mosip.optional-languages";
    public static final String MAX_LANGUAGES_COUNT_KEY = "mosip.max-languages.count";
    public static final String MIN_LANGUAGES_COUNT_KEY = "mosip.min-languages.count";
    public static final String LEFT_SLAP_THRESHOLD_KEY = "mosip.registration.leftslap_fingerprint_threshold";
    public static final String RIGHT_SLAP_THRESHOLD_KEY = "mosip.registration.rightslap_fingerprint_threshold";
    public static final String THUMBS_THRESHOLD_KEY = "mosip.registration.thumbs_fingerprint_threshold";
    public static final String IRIS_THRESHOLD_KEY = "mosip.registration.iris_threshold";
    public static final String FACE_THRESHOLD_KEY = "mosip.registration.face_threshold";
    public static final String LEFT_SLAP_ATTEMPTS_KEY = "mosip.registration.num_of_fingerprint_retries";
    public static final String RIGHT_SLAP_ATTEMPTS_KEY = "mosip.registration.num_of_fingerprint_retries";
    public static final String THUMBS_ATTEMPTS_KEY = "mosip.registration.num_of_fingerprint_retries";
    public static final String IRIS_ATTEMPTS_KEY = "mosip.registration.num_of_iris_retries";
    public static final String FACE_ATTEMPTS_KEY = "mosip.registration.num_of_face_retries";
    public static final String SERVER_VERSION = "mosip.registration.server_version";
    public static final String PRIMARY_LANGUAGE = "mosip.primary-language";
    public static final String SECONDARY_LANGUAGE = "mosip.secondary-language";
    public static final String CONSENT_SCREEN_TEMPLATE_NAME = "mosip.registration.consent-screen-template-name";
    public static final String INDIVIDUAL_BIOMETRICS_ID = "mosip.registration.individual-biometrics-id";
    public static final String INTRODUCER_BIOMETRICS_ID = "mosip.registration.introducer-biometrics-id";
    public static final String INFANT_AGEGROUP_NAME = "mosip.registration.infant-agegroup-name";
    public static final String AGEGROUP_CONFIG = "mosip.registration.agegroup-config";
    public static final String ALLOWED_BIO_ATTRIBUTES = "mosip.registration.allowed-bioattributes";
    public static final String DEFAULT_APP_TYPE_CODE = "mosip.registration.default-app-type-code";

    public static final String RESPONSE = "response";
    public static final String ERRORS = "errors";
    public static final String ON_BOARD_AUTH_STATUS = "authStatus";
    public static final String USER_ON_BOARDING_THRESHOLD_NOT_MET_MSG = "USER_ON_BOARDING_THRESHOLD_NOT_MET_MSG";
    public static final String SUCCESS = "Success";
    public static final String FINGER = "FINGER";
    public static final String FACE = "FACE";
    public static final String IRIS = "IRIS";
    public static final String USER_ON_BOARDING_SUCCESS_MSG = "USER_ONBOARD_SUCCESS";
    public static final String USER_ON_BOARDING_ERROR_RESPONSE = "USER_ONBOARD_ERROR";

    //Audits
    public static final String REGISTRATION_SCREEN = "Registration: %s";
    public static final String AGE_GROUP_CONFIG = "mosip.regproc.packet.classifier.tagging.agegroup.ranges";
    public static final String APPLICANT_TYPE_MVEL_SCRIPT = "mosip.kernel.applicantType.mvel.file";
    public static final String RESPONSE_SIGNATURE_PUBLIC_KEY_APP_ID = "SERVER-RESPONSE";
    public static final String RESPONSE_SIGNATURE_PUBLIC_KEY_REF_ID = "SIGN-VERIFY";

    public static final String PRE_REGISTRATION_DUMMY_ID = "mosip.pre-registration.datasync.fetch.ids";
    public static final String VER = "1.0";
    public static final String PRE_REG_DAYS_LIMIT = "mosip.registration.pre_reg_no_of_days_limit";

    public static final String UI_SCHEMA_SUBTYPE_FULL_NAME = "name";
    public static final String UI_SCHEMA_SUBTYPE_EMAIL = "Email";
    public static final String UI_SCHEMA_SUBTYPE_PHONE = "Phone";
    public static final String SELECTED_HANDLES = "mosip.registration.default-selected-handle-fields";
    public static final String TEMPLATE_IMPORTANT_GUIDELINES = "mosip.registration.important_guidelines";
    public static final String PRE_REG_PACKET_LOCATION = "mosip.registration.registration_pre_reg_packet_location";
    public static final String FORGOT_PASSWORD_URL = "mosip.registration.reset_password_url";

    public static final String LEFT_LITTLE_FINGER = "Left LittleFinger";
    public static final String LEFT_RING_FINGER = "Left RingFinger";
    public static final String LEFT_MIDDLE_FINGER = "Left MiddleFinger";
    public static final String LEFT_INDEX_FINGER = "Left IndexFinger";
    public static final String RIGHT_LITTLE_FINGER = "Right LittleFinger";
    public static final String RIGHT_RING_FINGER = "Right RingFinger";
    public static final String RIGHT_MIDDLE_FINGER = "Right MiddleFinger";
    public static final String RIGHT_INDEX_FINGER = "Right IndexFinger";
    public static final String LEFT_THUMB = "Left Thumb";
    public static final String RIGHT_THUMB = "Right Thumb";
    public static final String RIGHT = "Right";
    public static final String LEFT = "Left";

}
