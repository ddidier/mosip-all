package io.mosip.opencrvs.service;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.opencrvs.dto.BaseEventRequest;
import io.mosip.opencrvs.dto.DecryptedEventDto;
import io.mosip.opencrvs.util.*;
import org.json.JSONObject;
import org.json.JSONException;

import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.Async;

import io.mosip.commons.packet.dto.Document;
import io.mosip.commons.packet.dto.PacketInfo;
import io.mosip.commons.packet.dto.packet.PacketDto;
import io.mosip.commons.packet.exception.PacketCreatorException;
import io.mosip.commons.packet.facade.PacketWriter;
import io.mosip.kernel.core.exception.BaseCheckedException;
import io.mosip.kernel.core.exception.BaseUncheckedException;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.core.util.FileUtils;

import io.mosip.opencrvs.constant.ApiName;
import io.mosip.opencrvs.constant.Constants;
import io.mosip.opencrvs.constant.LoggingConstants;
import io.mosip.opencrvs.dto.ReceiveDto;
import io.mosip.opencrvs.error.ErrorCode;

@Service
public class Receiver {

	private static Logger LOGGER = LogUtil.getLogger(Receiver.class);

	@Value("${IDSchema.Version}")
	private String idschemaVersion;

	@Value("${opencrvs.birth.process.type}")
	private String birthPacketProcessType;

	@Value("${mosip.opencrvs.kafka.consumer.poll.interval.ms}")
	private long pollIntervalMs;

	@Value("${opencrvs.audit.app.name}")
	private String auditAppName;
	@Value("${opencrvs.audit.app.id}")
	private String auditAppId;

	@Value("${object.store.base.location}")
	private String objectStoreBaseLocation;
	@Value("${packet.manager.account.name}")
	private String packetManagerAccountName;

	@Value("${opencrvs.center.id}")
	private String centerId;
	@Value("${opencrvs.machine.id}")
	private String machineId;

	@Autowired
	Environment env;

	private Map<Double, String> idschemaCache = new HashMap<>();

	@Autowired
	private SyncAndUploadService syncUploadEncryptionService;

	@Autowired
	private PacketWriter packetWriter;

	@Autowired
	private Producer producer;

	@Autowired
	private RestUtil restUtil;

	@Autowired
	private KafkaUtil kafkaUtil;

	@Autowired
	private JdbcUtil jdbcUtil;

	@Autowired
	private OpencrvsCryptoUtil opencrvsCryptoUtil;

	@Autowired
	private OpencrvsDataUtil opencrvsDataUtil;

	@Autowired
	private RestTemplate selfTokenRestTemplate;

	private ObjectMapper kafkaMessageToJavaMapper = null;

	@Async
	public void receive(){
		while (true) {
			ConsumerRecords<String, String> records = kafkaUtil.consumerPoll(Duration.ofMillis(pollIntervalMs));
			if (records.count()>1) {
				LOGGER.debug(LoggingConstants.SESSION, LoggingConstants.ID, "RECEIVER", "RECEIVED RECORDS. Number: " + records.count());
			}
			for (ConsumerRecord<String, String> record : records) {
				try {
					LOGGER.info(LoggingConstants.SESSION, LoggingConstants.ID, "txn_id - "+record.key(), "Received transaction");
					DecryptedEventDto preProcessResult = preProcess(record.key(),record.value());
					LOGGER.debug(LoggingConstants.SESSION, LoggingConstants.ID,"txn_id - "+record.key(),"PreProcessResult : " + preProcessResult);
					createAndUploadPacket(record.key(), preProcessResult);
				} catch (BaseCheckedException e){
					LOGGER.error(LoggingConstants.FORMATTER_PREFIX, LoggingConstants.SESSION, LoggingConstants.ID, "txn_id - "+record.key(), "Error while processing transaction. Sending to reproduce", e);
					producer.reproduce(record.key(),record.value());
				} catch (Exception e){
					LOGGER.error(LoggingConstants.FORMATTER_PREFIX, LoggingConstants.SESSION, LoggingConstants.ID, "txn_id - "+record.key(), "Error while processing transaction. ", e);
				}
			}
		}
	}

	public String generateRegistrationId(String centerId, String machineId){
		String apiNameRidGeneration = env.getProperty(ApiName.RIDGENERATION);
		String response = selfTokenRestTemplate.getForObject(apiNameRidGeneration+"/"+centerId+"/"+machineId,String.class);
		try{
			JSONObject responseJson = new JSONObject(response);
			return responseJson.getJSONObject("response").getString("rid");
		}
		catch(JSONException je){
			LOGGER.error(LoggingConstants.SESSION,LoggingConstants.ID,"generate RID", ErrorCode.JSON_PROCESSING_EXCEPTION.getErrorMessage());
			throw ErrorCode.JSON_PROCESSING_EXCEPTION.throwUnchecked(je);
		}
	}

	public String generateDefaultRegistrationId(){
		return generateRegistrationId(centerId, machineId);
	}

	public DecryptedEventDto preProcess(String key, String value) throws BaseCheckedException{
		if(kafkaMessageToJavaMapper==null){
			kafkaMessageToJavaMapper = new ObjectMapper();
			kafkaMessageToJavaMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
		BaseEventRequest request;
		try{
			request = kafkaMessageToJavaMapper.readValue(value, BaseEventRequest.class);
		} catch(JsonProcessingException je) {
			LOGGER.error(LoggingConstants.FORMATTER_PREFIX, LoggingConstants.SESSION, LoggingConstants.ID,"txn_id - "+key,"kafka message value - json error - ", je);
			throw ErrorCode.JSON_PROCESSING_EXCEPTION.throwChecked(je);
		}

		String decrypted;
		try{
			byte[] encryptedData = CryptoUtil.decodePlainBase64(request.getData());
			decrypted = new String(opencrvsCryptoUtil.decrypt(encryptedData));
		} catch(BaseCheckedException e) {
			LOGGER.error(LoggingConstants.FORMATTER_PREFIX, LoggingConstants.SESSION, LoggingConstants.ID,"txn_id - "+key,"Unable to decrypt ", e);
			throw e;
		}
		try{
			return kafkaMessageToJavaMapper.readValue(decrypted, DecryptedEventDto.class);
		} catch(JsonProcessingException je) {
			LOGGER.error(LoggingConstants.FORMATTER_PREFIX, LoggingConstants.SESSION, LoggingConstants.ID,"txn_id - "+key,"Decrypted String - json error", je);
			throw ErrorCode.JSON_PROCESSING_EXCEPTION.throwChecked(je);
		}
	}

	public void createAndUploadPacket(String txnId, DecryptedEventDto requestBody) throws BaseCheckedException {
		if(!jdbcUtil.ifTxnExists(txnId)){
			jdbcUtil.createBirthTransaction(txnId);
		}
		else if("Uploaded".equals(jdbcUtil.getBirthStatus(txnId))){
			LOGGER.info(LoggingConstants.SESSION, LoggingConstants.ID, "txn_id - "+txnId, "Packet Already Uploaded.");
			return;
		}

		// make some receivedto out of requestbody
		ReceiveDto request ;
		try {
			request = opencrvsDataUtil.buildIdJson(requestBody);
		}
		catch(Exception e){
			LOGGER.error(LoggingConstants.FORMATTER_PREFIX, LoggingConstants.SESSION,LoggingConstants.ID,"build IdJson Request", ErrorCode.IDJSON_BUILD_EXCEPTION.getErrorMessage(), e);
			throw ErrorCode.IDJSON_BUILD_EXCEPTION.throwUnchecked(e);
		}

		try{
			String ridObtained = jdbcUtil.getBirthRid(txnId);
			if (ridObtained==null || ridObtained.isEmpty()){
				String receivedRid = opencrvsDataUtil.getRidFromBody(requestBody);
				request.setRid( (receivedRid==null || receivedRid.isEmpty()) ? generateDefaultRegistrationId() : receivedRid);
				jdbcUtil.updateBirthRidAndStatus(txnId,request.getRid(),"RID Generated");
			} else {
				request.setRid(ridObtained);
			}
		}
		catch(Exception e){
			LOGGER.error(LoggingConstants.FORMATTER_PREFIX, LoggingConstants.SESSION,LoggingConstants.ID,"generate RID", ErrorCode.RID_GENERATE_EXCEPTION.getErrorMessage(), e);
			throw ErrorCode.RID_GENERATE_EXCEPTION.throwUnchecked(e);
		}

		// BaseUncheckedException only till this point

		LOGGER.info(LoggingConstants.SESSION,LoggingConstants.ID,request.getRid(),"Started Packet Creation");

		File file = null;

		try {
			Map<String, String> idMap = new HashMap<>();
			JSONObject demoJsonObject = new JSONObject(request.getIdentityJson());
			JSONObject fieldsJson = demoJsonObject.getJSONObject(Constants.IDENTITY);
			Iterator<?> fields = fieldsJson.keys();

			while(fields.hasNext()){
				String key = (String)fields.next();
				Object value = fieldsJson.get(key);
				idMap.put(key, value == null ? null : value.toString());
			}

			// set demographic documents
			Map<String, Document> docsMap = new HashMap<>();
			//if (request.getProofOfAddress() != null && !request.getProofOfAddress().isEmpty())
			//	setDemographicDocuments(request.getProofOfAddress(), demoJsonObject, Constants.PROOF_OF_ADDRESS, docsMap);
			//if (request.getProofOfDateOfBirth() != null && !request.getProofOfDateOfBirth().isEmpty())
			//	setDemographicDocuments(request.getProofOfDateOfBirth(), demoJsonObject, Constants.PROOF_OF_DOB, docsMap);
			//if (request.getProofOfRelationship() != null && !request.getProofOfRelationship().isEmpty())
			//	setDemographicDocuments(request.getProofOfRelationship(), demoJsonObject, Constants.PROOF_OF_RELATIONSHIP, docsMap);
			if (request.getProofOfIdentity() != null && !request.getProofOfIdentity().isEmpty())
				setDemographicDocuments(request.getProofOfIdentity(), demoJsonObject, Constants.PROOF_OF_IDENTITY, docsMap);

			LOGGER.info(LoggingConstants.SESSION,LoggingConstants.ID,request.getRid(),"Passing the packet for creation, to PacketManager Library");

			jdbcUtil.updateBirthStatus(txnId,"Creating Packet");

			PacketDto packetDto = new PacketDto();
			// packetDto.setId(generateRegistrationId(request.centerId, request.machineId));
			packetDto.setId(request.getRid());
			packetDto.setSource(restUtil.getDefaultSource());
			packetDto.setProcess(birthPacketProcessType);
			packetDto.setSchemaVersion(idschemaVersion);
			packetDto.setSchemaJson(restUtil.getIdSchema(Double.valueOf(idschemaVersion),idschemaCache));
			LOGGER.debug(LoggingConstants.SESSION,LoggingConstants.ID,request.getRid(),"Received This schemaJson from API: " + packetDto.getSchemaJson());
			packetDto.setFields(idMap);
			packetDto.setDocuments(docsMap);
			packetDto.setMetaInfo(restUtil.getMetadata(birthPacketProcessType, request.getRid(),centerId,machineId, request.getOpencrvsBRN()));
			packetDto.setAudits(restUtil.generateAudit(packetDto.getId(),auditAppName,auditAppId));
			packetDto.setOfflineMode(false);
			packetDto.setRefId(centerId + "_" + machineId);
			List<PacketInfo> packetInfos = packetWriter.createPacket(packetDto);

			if (CollectionUtils.isEmpty(packetInfos) || packetInfos.iterator().next().getId() == null)
				throw new PacketCreatorException(ErrorCode.PACKET_CREATION_EXCEPTION.getErrorCode(), ErrorCode.PACKET_CREATION_EXCEPTION.getErrorMessage());

			jdbcUtil.updateBirthStatus(txnId,"Packet Created");

			LOGGER.info(LoggingConstants.SESSION,LoggingConstants.ID,request.getRid(),"Packet Created Successfully.");
			LOGGER.debug(LoggingConstants.SESSION,LoggingConstants.ID,request.getRid(),"Packet Created Successfully. Info: "+packetInfos);

			file = new File(objectStoreBaseLocation
					+ File.separator + packetManagerAccountName
					+ File.separator + packetInfos.iterator().next().getId() + ".zip");

			FileInputStream fis = new FileInputStream(file);

			byte[] packetZipBytes = IOUtils.toByteArray(fis);

			String packetCreatedDateTime = packetDto.getId().substring(packetDto.getId().length() - 14);
			String formattedDate = packetCreatedDateTime.substring(0, 8) + "T" + packetCreatedDateTime.substring(packetCreatedDateTime.length() - 6);
			String creationTime = LocalDateTime.parse(formattedDate, DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + ".000Z";

			LOGGER.debug(LoggingConstants.SESSION, LoggingConstants.ID, packetDto.getId(), "Receiver::createPacket()::packet created and sent for sync service");

			String packetGeneratorRes = syncUploadEncryptionService.uploadUinPacket(packetDto.getId(), packetDto.getRefId(), creationTime, birthPacketProcessType, packetZipBytes);

			jdbcUtil.updateBirthStatus(txnId,"Uploaded");

			LOGGER.info(LoggingConstants.SESSION, LoggingConstants.ID, packetDto.getId(), "Receiver::createPacket()::packet synced and uploaded");
		} catch (Exception e) {
			LOGGER.error(LoggingConstants.FORMATTER_PREFIX, LoggingConstants.SESSION,LoggingConstants.ID, request.getRid(),"Encountered error while create packet", e);
			if (e instanceof BaseCheckedException) {
				throw (BaseCheckedException) e;
			} else if (e instanceof BaseUncheckedException) {
				throw (BaseUncheckedException) e;
			} else {
				throw ErrorCode.UNKNOWN_EXCEPTION.throwChecked(e);
			}
		} finally {
			if (file != null && file.exists())
				FileUtils.forceDelete(file);
		}
	}
	private void setDemographicDocuments(String documentBytes, JSONObject demoJsonObject, String documentName, Map<String, Document> map){
		try{
			JSONObject identityJson = demoJsonObject.getJSONObject(Constants.IDENTITY);
			JSONObject documentJson = identityJson.getJSONObject(documentName);

			Document docDetailsDto = new Document();
			docDetailsDto.setDocument(CryptoUtil.decodeURLSafeBase64(documentBytes));
			docDetailsDto.setFormat((String) documentJson.get("format"));
			docDetailsDto.setValue((String) documentJson.get("value"));
			docDetailsDto.setType((String) documentJson.get("type"));
			map.put(documentName, docDetailsDto);
		}
		catch(JSONException je){
			throw ErrorCode.JSON_PROCESSING_EXCEPTION.throwUnchecked(je);
		}
	}
}
