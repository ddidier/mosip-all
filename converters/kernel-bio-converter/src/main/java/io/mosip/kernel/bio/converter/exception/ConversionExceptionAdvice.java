package io.mosip.kernel.bio.converter.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.kernel.bio.converter.constant.ConverterErrorCode;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.EmptyCheckUtils;

/**
 * ConversionHandlerControllerAdvice for capturing the errors build the response wrapper around services errors
 * and give ResponseEntity .
 * @author Janardhan B S
 * @since 1.0.0
 */
@RestControllerAdvice
public class ConversionExceptionAdvice {
	@Autowired
	private ObjectMapper objectMapper;
	
	@ExceptionHandler(value = { Exception.class, RuntimeException.class, ConversionException.class })
	public ResponseEntity<ResponseWrapper<ServiceError>> defaultServiceErrorHandler(HttpServletRequest request,
			Exception e) throws IOException {
		ResponseWrapper<ServiceError> responseWrapper = setErrors(request);
		ServiceError error = null;
		if (e instanceof ConversionException)
		{
			ConversionException conversionException = (ConversionException)e;
			error = new ServiceError(conversionException.getErrorCode(), conversionException.getMessage());
		}
		else
		{
			error = new ServiceError(ConverterErrorCode.TECHNICAL_ERROR_EXCEPTION.getErrorCode(), e.getMessage());
		}
		responseWrapper.getErrors().add(error);
		ExceptionUtils.logRootCause(e);
		return new ResponseEntity<ResponseWrapper<ServiceError>>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseWrapper<ServiceError> setErrors(HttpServletRequest httpServletRequest) throws IOException {
		ResponseWrapper<ServiceError> responseWrapper = new ResponseWrapper<ServiceError>();
		String requestBody = null;
		if (httpServletRequest instanceof ContentCachingRequestWrapper) {
			requestBody = new String(((ContentCachingRequestWrapper) httpServletRequest).getContentAsByteArray());
		}
		if (EmptyCheckUtils.isNullEmpty(requestBody)) {
			return responseWrapper;
		}
		JsonNode reqNode = objectMapper.readTree(requestBody);
		responseWrapper.setId(reqNode.path("id").asText());
		responseWrapper.setVersion(reqNode.path("version").asText());
		return responseWrapper;
	}
}