package io.mosip.kernel.bio.converter.controller;

import io.mosip.kernel.bio.converter.constant.SourceFormatCode;
import io.mosip.kernel.bio.converter.constant.TargetFormatCode;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import io.mosip.kernel.bio.converter.dto.ConvertRequestDto;

import io.mosip.kernel.bio.converter.TestBootApplication;
import io.mosip.kernel.bio.converter.util.ConverterDataUtil;
import io.mosip.kernel.core.http.RequestWrapper;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
* Convert Handler Controller Test
*
*
* @author Janardhan B S
* @since 1.0.0
*/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConvertControllerTest {
	@Autowired
	public MockMvc mockMvc;

	private RequestWrapper<ConvertRequestDto> convertRequestDto = new RequestWrapper<ConvertRequestDto>();
	private ObjectMapper mapper;
	
	@Before	
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		convertRequestDto.setId ("sample-converter");
		convertRequestDto.setVersion ("1.0");
	}

	/*
	 * test Null or Empty ConvertRequestDto Null
	 */
	@Test
    @WithUserDetails("reg-officer")
	public void t001ConvertTest() throws Exception {
		String req=
		"{" +
			"\"values\": null," +
			"\"sourceFormat\":null," +
			"\"targetFormat\":null," +
			"\"sourceParameters\":{" +
				"\"key\":\"value\"" +
			"}," +
			"\"targetParameters\":{" +
				"\"key\":\"value\"" +
			"}" +
		"}";
		convertRequestDto.setRequest (mapper.readValue(req, ConvertRequestDto.class));
		
		ConverterDataUtil.checkResponse(mockMvc.perform(post("/convert")
			.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convertRequestDto)))
			.andReturn(), 500,null,"MOS-CNV-500");
	}

	/*
	 * test Null or Empty ConvertRequestDto Values Null or Empty
	 */
	@Test
    @WithUserDetails("reg-officer")
	public void t002ConvertTest() throws Exception {
		String req=
		"{" +
			"\"values\":{" +
				"}," +
			"\"sourceFormat\":\"ISO19794_4_2011\"," +
			"\"targetFormat\":\"IMAGE/JPEG\"," +
			"\"sourceParameters\":{" +
				"\"key\":\"value\"" +
			"}," +
			"\"targetParameters\":{" +
				"\"key\":\"value\"" +
			"}" +
		"}";
		convertRequestDto.setRequest (mapper.readValue(req, ConvertRequestDto.class));
		
		ConverterDataUtil.checkResponse(mockMvc.perform(post("/convert").
			contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convertRequestDto)))
			.andReturn(), 500,null,"MOS-CNV-500");
	}

	/*
	 * test Null or Empty ConvertRequestDto Source Null
	 */
	@Test
    @WithUserDetails("reg-officer")
	public void t0030ConvertTest() throws Exception {
		String req=
		"{" +
			"\"values\":{" +
				"\"Left Thumb\": \"values\""+
				"}," +
			"\"sourceFormat\":\" \"," +
			"\"targetFormat\":\"string\"," +
			"\"sourceParameters\":{" +
				"\"key\":\"value\"" +
				"}," +
			"\"targetParameters\":{" +
				"\"key\":\"value\"" +
				"}" +
		"}";

		convertRequestDto.setRequest (mapper.readValue(req, ConvertRequestDto.class));
		
		ConverterDataUtil.checkResponse(mockMvc.perform(post("/convert").
			contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convertRequestDto)))
			.andReturn(), 500,null,"MOS-CNV-500");
	}

	/*
	 * test Null or Empty ConvertRequestDto Source Value Wrong (ISO19794_4_2011, ISO19794_5_2011, ISO19794_6_2011)
	 */
	@Test
    @WithUserDetails("reg-officer")
	public void t0031ConvertTest() throws Exception {
		String req=
		"{" +
			"\"values\":{" +
				"\"Left Thumb\": \"values\""+
			"}," +
			"\"sourceFormat\":\"ISO19794_3_2011\"," +
			"\"targetFormat\":\"string\"," +
			"\"sourceParameters\":{" +
				"\"key\":\"value\"" +
			"}," +
			"\"targetParameters\":{" +
				"\"key\":\"value\"" +
			"}" +
		"}";
		convertRequestDto.setRequest (mapper.readValue(req, ConvertRequestDto.class));
		
		ConverterDataUtil.checkResponse(mockMvc.perform(post("/convert").
			contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convertRequestDto)))
			.andReturn(), 500,null,"MOS-CNV-003");
	}

	/*
	 * test Null or Empty ConvertRequestDto Target Value null 
	 */
	@Test
    @WithUserDetails("reg-officer")
	public void t0040ConvertTest() throws Exception {
		String req=
		"{" +
			"\"values\":{" +
				"\"Left Thumb\": \"values\""+
			"}," +
			"\"sourceFormat\":\"ISO19794_4_2011\"," +
			"\"targetFormat\":\"\"," +
			"\"sourceParameters\":{" +
				"\"key\":\"value\"" +
			"}," +
			"\"targetParameters\":{" +
				"\"key\":\"value\"" +
			"}" +
		"}";
		convertRequestDto.setRequest (mapper.readValue(req, ConvertRequestDto.class));
		
		ConverterDataUtil.checkResponse(mockMvc.perform(post("/convert").
			contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convertRequestDto)))
			.andReturn(), 500,null,"MOS-CNV-500");
	}

	/*
	 * test Null or Empty ConvertRequestDto Target Value Wrong (IMAGE/JPEG, IMAGE/PNG) Current Working
	 * Future Implementation (ISO19794_4_2011_JPEG, ISO19794_5_2011_JPEG, ISO19794_6_2011_JPEG, ISO19794_4_2011_PNG, ISO19794_5_2011_PNG, ISO19794_6_2011_PNG)
	 */
	@Test
    @WithUserDetails("reg-officer")
	public void t0041ConvertTest() throws Exception {
		String req=
		"{" +
			"\"values\":{" +
				"\"Left Thumb\": \"values\""+
			"}," +
			"\"sourceFormat\":\"ISO19794_4_2011\"," +
			"\"targetFormat\":\"IMAGE/JPEGLL\"," +
			"\"sourceParameters\":{" +
				"\"key\":\"value\"" +
			"}," +
			"\"targetParameters\":{" +
				"\"key\":\"value\"" +
			"}" +
		"}";
		convertRequestDto.setRequest (mapper.readValue(req, ConvertRequestDto.class));
		
		ConverterDataUtil.checkResponse(mockMvc.perform(post("/convert").
			contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convertRequestDto)))
			.andReturn(), 500,null,"MOS-CNV-004");
	}

	/*
	 * test Null or Empty ConvertRequestDto Request Value not null or empty
	 */
	@Test
    @WithUserDetails("reg-officer")
	public void t005ConvertTest() throws Exception {
		String req=
		"{" +
			"\"values\":{" +
				"\"Left Thumb\": \"\""+
			"}," +
			"\"sourceFormat\":\"ISO19794_4_2011\"," +
			"\"targetFormat\":\"IMAGE/JPEG\"," +
			"\"sourceParameters\":{" +
				"\"key\":\"value\"" +
			"}," +
			"\"targetParameters\":{" +
				"\"key\":\"value\"" +
			"}" +
		"}";
		convertRequestDto.setRequest (mapper.readValue(req, ConvertRequestDto.class));

		ConverterDataUtil.checkResponse(mockMvc.perform(post("/convert").
						contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convertRequestDto)))
				.andReturn(), 500,null,"MOS-CNV-005");
	}

	/*
	 * test Null or Empty ConvertRequestDto Request Value not base64UrlEncoded
	 */
	@Test
    @WithUserDetails("reg-officer")
	public void t006ConvertTest() throws Exception {
		String req=
		"{" +
			"\"values\":{" +
				"\"Left Thumb\": \"4rdHFh+HYoS8oLdVvbUzEVqB8Lvm7kSPnuwF0AAABYQ=\""+
			"}," +
			"\"sourceFormat\":\"ISO19794_4_2011\"," +
			"\"targetFormat\":\"IMAGE/JPEG\"," +
			"\"sourceParameters\":{" +
				"\"key\":\"value\"" +
			"}," +
			"\"targetParameters\":{" +
				"\"key\":\"value\"" +
			"}" +
		"}";
		convertRequestDto.setRequest (mapper.readValue(req, ConvertRequestDto.class));
		
		ConverterDataUtil.checkResponse(mockMvc.perform(post("/convert").
			contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convertRequestDto)))
			.andReturn(), 500,null,"MOS-CNV-006");
	}

	/*
	 * test Source not valid ISO ISO19794_4_2011
	 */
	@Test
    @WithUserDetails("reg-officer")
	public void t008ConvertTest() throws Exception {
		String req=
		"{" +
			"\"values\":{" +
				"\"Left Thumb\": \"aGVsbG8gaG93IGFyZSB5b3U\""+
			"}," +
			"\"sourceFormat\":\"ISO19794_4_2011\"," +
			"\"targetFormat\":\"IMAGE/JPEG\"," +
			"\"sourceParameters\":{" +
				"\"key\":\"value\"" +
			"}," +
			"\"targetParameters\":{" +
				"\"key\":\"value\"" +
			"}" +
		"}";
		convertRequestDto.setRequest (mapper.readValue(req, ConvertRequestDto.class));

		ConverterDataUtil.checkResponse(mockMvc.perform(post("/convert").
						contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convertRequestDto)))
				.andReturn(), 500, null, "MOS-CNV-008");
	}

	/*
	 * test Source not valid ISO ISO19794_5_2011
	 */
	@Test
    @WithUserDetails("reg-officer")
	public void t009ConvertTest() throws Exception {
		String req=
		"{" +
			"\"values\":{" +
				"\"Left Thumb\": \"aGVsbG8gaG93IGFyZSB5b3U\""+
			"}," +
			"\"sourceFormat\":\"ISO19794_5_2011\"," +
			"\"targetFormat\":\"IMAGE/JPEG\"," +
			"\"sourceParameters\":{" +
				"\"key\":\"value\"" +
			"}," +
			"\"targetParameters\":{" +
				"\"key\":\"value\"" +
			"}" +
		"}";
		convertRequestDto.setRequest (mapper.readValue(req, ConvertRequestDto.class));

		ConverterDataUtil.checkResponse(mockMvc.perform(post("/convert").
						contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convertRequestDto)))
				.andReturn(), 500,null,"MOS-CNV-009");
	}

	/*
	 * test Source not valid ISO ISO19794_6_2011
	 */
	@Test
    @WithUserDetails("reg-officer")
	public void t010ConvertTest() throws Exception {
		String req=
		"{" +
			"\"values\":{" +
				"\"Left Thumb\": \"aGVsbG8gaG93IGFyZSB5b3U\""+
			"}," +
			"\"sourceFormat\":\"ISO19794_6_2011\"," +
			"\"targetFormat\":\"IMAGE/JPEG\"," +
			"\"sourceParameters\":{" +
				"\"key\":\"value\"" +
			"}," +
			"\"targetParameters\":{" +
				"\"key\":\"value\"" +
			"}" +
		"}";
		convertRequestDto.setRequest (mapper.readValue(req, ConvertRequestDto.class));

		ConverterDataUtil.checkResponse(mockMvc.perform(post("/convert").
						contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convertRequestDto)))
				.andReturn(), 500, null,"MOS-CNV-010");
	}

	/*
	 * Target Format(ISO19794_6_2011_JPEG) Not Supported For the Given Source Format(ISO19794_6_2011)
	 */
	@Test
    @WithUserDetails("reg-officer")
	public void t011ConvertTest() throws Exception {
		String req=
		"{" +
			"\"values\":{" +
				"\"Left Thumb\": \"aGVsbG8gaG93IGFyZSB5b3U\""+
			"}," +
			"\"sourceFormat\":\"ISO19794_6_2011\"," +
			"\"targetFormat\":\"ISO19794_6_2011/JPEG\"," +
			"\"sourceParameters\":{" +
				"\"key\":\"value\"" +
			"}," +
			"\"targetParameters\":{" +
				"\"key\":\"value\"" +
			"}" +
		"}";
		convertRequestDto.setRequest (mapper.readValue(req, ConvertRequestDto.class));

		ConverterDataUtil.checkResponse(mockMvc.perform(post("/convert").
						contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convertRequestDto)))
				.andReturn(), 500,null,"MOS-CNV-004");
	}

	/*
	 * get Finger (ISO19794_4_2011) to JPEG
	 */
	@Test
    @WithUserDetails("reg-officer")
	public void t012ConvertTest() throws Exception {
		FileInputStream fis = new FileInputStream("src/test/resources/finger.txt");
		String bioData = IOUtils.toString(fis, StandardCharsets.UTF_8);
		String req=
		"{" +
			"\"values\":{" +
				"\"Left IndexFinger\": \""+ bioData +"\""+
			"}," +
			"\"sourceFormat\":\"ISO19794_4_2011\"," +
			"\"targetFormat\":\"IMAGE/JPEG\"," +
			"\"sourceParameters\":{" +
				"\"key\":\"value\"" +
			"}," +
			"\"targetParameters\":{" +
				"\"key\":\"value\"" +
			"}" +
		"}";
		convertRequestDto.setRequest (mapper.readValue(req, ConvertRequestDto.class));

		ConverterDataUtil.checkResponse(mockMvc.perform(post("/convert").
						contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convertRequestDto)))
				.andReturn(), 200, SourceFormatCode.ISO19794_4_2011, TargetFormatCode.IMAGE_JPEG.getCode ());
	}

	/*
	 * get Finger (ISO19794_4_2011) to PNG
	 */
	@Test
    @WithUserDetails("reg-officer")
	public void t0121ConvertTest() throws Exception {
		FileInputStream fis = new FileInputStream("src/test/resources/finger.txt");
		String bioData = IOUtils.toString(fis, StandardCharsets.UTF_8);
		String req=
		"{" +
			"\"values\":{" +
				"\"Left IndexFinger\": \""+ bioData +"\""+
			"}," +
			"\"sourceFormat\":\"ISO19794_4_2011\"," +
			"\"targetFormat\":\"IMAGE/PNG\"," +
			"\"sourceParameters\":{" +
				"\"key\":\"value\"" +
			"}," +
			"\"targetParameters\":{" +
				"\"key\":\"value\"" +
			"}" +
		"}";
		convertRequestDto.setRequest (mapper.readValue(req, ConvertRequestDto.class));

		ConverterDataUtil.checkResponse(mockMvc.perform(post("/convert").
						contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convertRequestDto)))
				.andReturn(), 200, SourceFormatCode.ISO19794_4_2011, TargetFormatCode.IMAGE_PNG.getCode ());
	}

	/*
	 * get Face (ISO19794_5_2011) to JPEG
	 */
	@Test
    @WithUserDetails("reg-officer")
	public void t013ConvertTest() throws Exception {
		FileInputStream fis = new FileInputStream("src/test/resources/face.txt");
		String bioData = IOUtils.toString(fis, StandardCharsets.UTF_8);
		String req=
		"{" +
			"\"values\":{" +
				"\"Face\": \""+ bioData +"\""+
			"}," +
			"\"sourceFormat\":\"ISO19794_5_2011\"," +
			"\"targetFormat\":\"IMAGE/PNG\"," +
			"\"sourceParameters\":{" +
				"\"key\":\"value\"" +
			"}," +
			"\"targetParameters\":{" +
				"\"key\":\"value\"" +
			"}" +
		"}";
		convertRequestDto.setRequest (mapper.readValue(req, ConvertRequestDto.class));

		ConverterDataUtil.checkResponse(mockMvc.perform(post("/convert").
						contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convertRequestDto)))
				.andReturn(), 200, SourceFormatCode.ISO19794_5_2011, TargetFormatCode.IMAGE_PNG.getCode ());
	}

	/*
	 * get Face (ISO19794_5_2011) to PNG
	 */
	@Test
    @WithUserDetails("reg-officer")
	public void t0131ConvertTest() throws Exception {
		FileInputStream fis = new FileInputStream("src/test/resources/face.txt");
		String bioData = IOUtils.toString(fis, StandardCharsets.UTF_8);
		String req=
		"{" +
			"\"values\":{" +
				"\"Face\": \""+ bioData +"\""+
				"}," +
			"\"sourceFormat\":\"ISO19794_5_2011\"," +
			"\"targetFormat\":\"IMAGE/PNG\"," +
			"\"sourceParameters\":{" +
				"\"key\":\"value\"" +
			"}," +
			"\"targetParameters\":{" +
				"\"key\":\"value\"" +
			"}" +
		"}";
		convertRequestDto.setRequest (mapper.readValue(req, ConvertRequestDto.class));

		ConverterDataUtil.checkResponse(mockMvc.perform(post("/convert").
						contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convertRequestDto)))
				.andReturn(), 200, SourceFormatCode.ISO19794_5_2011, TargetFormatCode.IMAGE_PNG.getCode ());
	}

	/*
	 * get Iris (ISO19794_6_2011) to JPEG
	 */
	@Test
    @WithUserDetails("reg-officer")
	public void t014ConvertTest() throws Exception {
		FileInputStream fis = new FileInputStream("src/test/resources/iris.txt");
		String bioData = IOUtils.toString(fis, StandardCharsets.UTF_8);

		String req=
		"{" +
			"\"values\":{" +
				"\"Left Iris\": \""+ bioData +"\""+
			"}," +
			"\"sourceFormat\":\"ISO19794_6_2011\"," +
			"\"targetFormat\":\"IMAGE/JPEG\"," +
			"\"sourceParameters\":{" +
				"\"key\":\"value\"" +
			"}," +
			"\"targetParameters\":{" +
				"\"key\":\"value\"" +
			"}" +
		"}";
		convertRequestDto.setRequest (mapper.readValue(req, ConvertRequestDto.class));

		ConverterDataUtil.checkResponse(mockMvc.perform(post("/convert").
						contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convertRequestDto)))
				.andReturn(), 200, SourceFormatCode.ISO19794_6_2011, TargetFormatCode.IMAGE_JPEG.getCode ());
	}

	/*
	 * get Iris (ISO19794_6_2011) to PNG
	 */
	@Test
    @WithUserDetails("reg-officer")
	public void t0141ConvertTest() throws Exception {
		FileInputStream fis = new FileInputStream("src/test/resources/iris.txt");
		String bioData = IOUtils.toString(fis, StandardCharsets.UTF_8);

		String req=
		"{" +
			"\"values\":{" +
				"\"Left Iris\": \""+ bioData +"\""+
				"}," +
			"\"sourceFormat\":\"ISO19794_6_2011\"," +
			"\"targetFormat\":\"IMAGE/PNG\"," +
			"\"sourceParameters\":{" +
				"\"key\":\"value\"" +
			"}," +
			"\"targetParameters\":{" +
				"\"key\":\"value\"" +
			"}" +
		"}";
		convertRequestDto.setRequest (mapper.readValue(req, ConvertRequestDto.class));

		ConverterDataUtil.checkResponse(mockMvc.perform(post("/convert").
						contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(convertRequestDto)))
				.andReturn(), 200, SourceFormatCode.ISO19794_6_2011, TargetFormatCode.IMAGE_PNG.getCode ());
	}
}
