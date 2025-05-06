package io.mosip.kernel.bio.converter.service;

import java.util.Map;

import io.mosip.kernel.bio.converter.exception.ConversionException;

/**
 * Converter API Interface for the services.
 * 
 * @author Janardhan B S
 * @since 1.0.0
 */
public interface IConverterApi {
	  /**
	  *
	  * @param values Base64 URL encoded values with identifier keys.
	  *              Eg: {
	  *                     "Left MiddleFinger" : "<base64 URL encoded BDB>",
	  *                     "Left LittleFinger" : "<base64 URL encoded BDB>"
	  *                   },
	  *                 sourceFormat : "ISO19794_4_2011"                               
	  *
	  *               Eg:  { "Face" : "<base64 URL encoded BDB>" }, 
	  *                 sourceFormat : "ISO19794_5_2011"
	  *                       
	  *                 Eg:  { "Left" : "<base64 URL encoded BDB>" } , 
	  *                     sourceFormat : "ISO19794_6_2011"
	  *
	  *               Eg: { "dateOfBirth" : "<base64 URL encoded date>" }
	  *               , sourceFormat : "YYYY/mm/DD"
	  *
	  *               Eg: { "proofOfException" : "<base64 URL encoded data>" }, sourceFormat : "application/pdf"
	  *
	  * @param sourceFormat input value mime type, if not supported, ConversionException is thrown
	  * @param targetFormat output value mime type, if not supported, ConversionException is thrown
	  * @param sourceParameters Provided source value/format related parameters to be considered during conversion. Unknown parameters are ignored.
	  * @param targetParameters parameters to be considered during conversion to target format. Unknown parameters are ignored.
	  * @return converted Base64 URL encoded values w.r.t targetFormat for the input identifier keys
	  * @throws ConversionException
	  *
	  * ErrorCodes:
	  * MOS-CNV-001 Conversion source format not supported
	  * MOS-CNV-002 Conversion target format not supported
	  * MOS-CNV-003 Invalid source value
	  * MOS-CNV-500 Technical Error
	  */
	  Map<String, String> convert(Map<String, String> values, String sourceFormat, String targetFormat, Map<String, String> sourceParameters, Map<String, String> targetParameters) throws ConversionException;	  
	}