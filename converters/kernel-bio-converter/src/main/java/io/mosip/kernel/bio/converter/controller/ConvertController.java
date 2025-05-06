package io.mosip.kernel.bio.converter.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.kernel.bio.converter.dto.ConvertRequestDto;
import io.mosip.kernel.bio.converter.service.IConverterApi;
import io.mosip.kernel.bio.converter.service.impl.ConverterServiceImpl;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.kernel.core.http.ResponseWrapper;

/**
* Convert Handler Controller
*
*
* @author Janardhan B S
* @since 1.0.0
*/

@RestController
public class ConvertController {
	/**
	* Service instance {@link ConverterServiceImpl}
	*/
	@Autowired
	private IConverterApi converterService;
	
	/**
	* Verifies mapping of input public key with any machine.
	* if valid returns corresponding keyIndex.
	*
	* @param convertRequestDto
	* @return
	*/
	@ResponseFilter
	@PostMapping(value = "/convert", produces = "application/json")
	public ResponseWrapper<Map<String, String>> convert(
		@RequestBody @Valid RequestWrapper<ConvertRequestDto> convertRequestDto) throws Exception {
		ResponseWrapper<Map<String, String>> responseDto = new ResponseWrapper<Map<String, String>>();

		responseDto.setResponse (converterService.convert(convertRequestDto.getRequest().getValues(),
			convertRequestDto.getRequest().getSourceFormat(),
			convertRequestDto.getRequest().getTargetFormat(),
			convertRequestDto.getRequest().getSourceParameters(),
			convertRequestDto.getRequest().getTargetParameters()));

		return responseDto;
	}
}
