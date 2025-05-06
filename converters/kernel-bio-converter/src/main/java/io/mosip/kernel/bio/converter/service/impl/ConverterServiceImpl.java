package io.mosip.kernel.bio.converter.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.jnbis.api.model.Bitmap;
import org.jnbis.internal.WsqDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.mosip.biometrics.util.CommonUtil;
import io.mosip.biometrics.util.ConvertRequestDto;
import io.mosip.biometrics.util.face.FaceBDIR;
import io.mosip.biometrics.util.face.FaceDecoder;
import io.mosip.biometrics.util.face.ImageDataType;
import io.mosip.biometrics.util.finger.FingerBDIR;
import io.mosip.biometrics.util.finger.FingerDecoder;
import io.mosip.biometrics.util.finger.FingerImageCompressionType;
import io.mosip.biometrics.util.iris.ImageFormat;
import io.mosip.biometrics.util.iris.IrisBDIR;
import io.mosip.biometrics.util.iris.IrisDecoder;
import io.mosip.kernel.bio.converter.constant.ConverterErrorCode;
import io.mosip.kernel.bio.converter.constant.SourceFormatCode;
import io.mosip.kernel.bio.converter.constant.TargetFormatCode;
import io.mosip.kernel.bio.converter.exception.ConversionException;
import io.mosip.kernel.bio.converter.service.IConverterApi;

import static io.mosip.kernel.bio.converter.constant.ConverterErrorCode.INVALID_TARGET_EXCEPTION;
import static io.mosip.kernel.bio.converter.constant.ConverterErrorCode.NOT_SUPPORTED_COMPRESSION_TYPE;

/**
 * This class implements handling conversion of ISO format to JPEG or PNG Image format 
 * 
 * @author Janardhan B S
 * @since 1.0.0
 * 
 */
@Service
public class ConverterServiceImpl implements IConverterApi 
{	
	@Autowired
    public ConverterServiceImpl() {
    }
	
	@Override
	public Map<String, String> convert(Map<String, String> values, String sourceFormat, String targetFormat,
		Map<String, String> sourceParameters, Map<String, String> targetParameters) throws ConversionException {
		ConverterErrorCode errorCode = null;
		Map<String, String> targetValues = new HashMap<String, String> ();
		
		SourceFormatCode sourceCode = SourceFormatCode.fromCode(sourceFormat);
		TargetFormatCode targetCode = TargetFormatCode.fromCode(targetFormat);
		for (Map.Entry<String,String> entry : values.entrySet())
		{
			String targetValue = null;
			String isoData = entry.getValue();
			if (isoData == null || isoData.trim ().length () == 0)
			{
				errorCode = ConverterErrorCode.SOURCE_CAN_NOT_BE_EMPTY_OR_NULL_EXCEPTION;
				throw new ConversionException (errorCode.getErrorCode(), errorCode.getErrorMessage ());
			}

			switch (sourceCode)
			{
				// FINGER ISO can have JP2000 or WSQ
				case ISO19794_4_2011:
					targetValue = convertFingerIsoToImageType(sourceCode, entry.getValue(), targetCode, targetParameters);
					break;
				// FACE ISO can have JP2000 
				case ISO19794_5_2011:
					targetValue = convertFaceIsoToImageType(sourceCode, entry.getValue(), targetCode, targetParameters);
					break;
				// IRIS ISO can have JP2000 
				case ISO19794_6_2011:
					targetValue = convertIrisIsoToImageType(sourceCode, entry.getValue(), targetCode, targetParameters);
					break;
				default:
					errorCode = ConverterErrorCode.INVALID_SOURCE_EXCEPTION;
					throw new ConversionException (errorCode.getErrorCode(), errorCode.getErrorMessage());
			}
			targetValues.put(entry.getKey(), targetValue);
		}
		return targetValues;
	}

	private String convertFingerIsoToImageType(SourceFormatCode sourceCode, String isoData, TargetFormatCode targetCode, 
		Map<String, String> targetParameters) throws ConversionException {
		ConverterErrorCode errorCode = ConverterErrorCode.TECHNICAL_ERROR_EXCEPTION;

		ConvertRequestDto requestDto = new ConvertRequestDto();
		requestDto.setModality("Finger");
		requestDto.setVersion(sourceCode.getCode());

		try {
			requestDto.setInputBytes(CommonUtil.decodeURLSafeBase64 (isoData));
		} catch (Exception e) {
			errorCode = ConverterErrorCode.SOURCE_NOT_VALID_BASE64URLENCODED_EXCEPTION;
			throw new ConversionException (errorCode.getErrorCode(), e.getLocalizedMessage());
		}
		
		FingerBDIR bdir;
		int inCompressionType = -1;		
		byte [] inImageData = null;
		try {
			bdir = FingerDecoder.getFingerBDIR(requestDto);

			inCompressionType = bdir.getCompressionType();		
			inImageData = bdir.getImage();
		} catch (Exception e) {
			errorCode = ConverterErrorCode.SOURCE_NOT_VALID_FINGER_ISO_FORMAT_EXCEPTION;
			throw new ConversionException (errorCode.getErrorCode(), e.getLocalizedMessage());
		}

		BufferedImage outImage = null;
		byte [] outImageData = null; 
		switch(inCompressionType)
		{
			case FingerImageCompressionType.JPEG_2000_LOSSY:
			case FingerImageCompressionType.JPEG_2000_LOSS_LESS:
				try {
					outImage = ImageIO.read(new ByteArrayInputStream(inImageData));
					// change here outImage width, height, dpi here based on targetParameters 
				} catch (IOException e) {
					errorCode = ConverterErrorCode.COULD_NOT_READ_ISO_IMAGE_DATA_EXCEPTION;					
					throw new ConversionException (errorCode.getErrorCode(), e.getLocalizedMessage());
				}
				outImageData = convertBufferedImageToBytes(targetCode, outImage);
				break;
			case FingerImageCompressionType.WSQ:
				WsqDecoder decoder = new WsqDecoder ();
				Bitmap bitmap = decoder.decode(inImageData);
				outImage = CommonUtil.convert(bitmap);
				// change here outImage width, height, dpi here based on targetParameters 
				outImageData = convertBufferedImageToBytes(targetCode, outImage);
				break;
			default:
				throw new ConversionException (NOT_SUPPORTED_COMPRESSION_TYPE.getErrorCode(), NOT_SUPPORTED_COMPRESSION_TYPE.getErrorMessage());
		}
		if (outImageData != null)
		{
			return CommonUtil.encodeToURLSafeBase64(outImageData);
		}
		throw new ConversionException (errorCode.getErrorCode(), errorCode.getErrorMessage());
	}

	private String convertFaceIsoToImageType(SourceFormatCode sourceCode, String isoData, TargetFormatCode targetCode, 
		Map<String, String> targetParameters) throws ConversionException {
		ConverterErrorCode errorCode = ConverterErrorCode.TECHNICAL_ERROR_EXCEPTION;

		ConvertRequestDto requestDto = new ConvertRequestDto();
		requestDto.setModality("Face");
		requestDto.setVersion(sourceCode.getCode());
		try {
			requestDto.setInputBytes(CommonUtil.decodeURLSafeBase64 (isoData));
		} catch (Exception e) {
			errorCode = ConverterErrorCode.SOURCE_NOT_VALID_BASE64URLENCODED_EXCEPTION;
			throw new ConversionException (errorCode.getErrorCode(), e.getLocalizedMessage());
		}

		FaceBDIR bdir;
		int inImageDataType = -1;
		byte [] inImageData = null;
		try {
			bdir = FaceDecoder.getFaceBDIR(requestDto);
			
			inImageDataType = bdir.getImageDataType();
			inImageData = bdir.getImage();
		} catch (Exception e) {
			errorCode = ConverterErrorCode.SOURCE_NOT_VALID_FACE_ISO_FORMAT_EXCEPTION;
			throw new ConversionException (errorCode.getErrorCode(), e.getLocalizedMessage());
		}

		BufferedImage outImage = null;
		byte [] outImageData = null; 
		switch(inImageDataType)
		{
			case ImageDataType.JPEG2000_LOSSY:
			case ImageDataType.JPEG2000_LOSS_LESS:
				try {
					outImage = ImageIO.read(new ByteArrayInputStream(inImageData));
					// change here outImage width, height, dpi here based on targetParameters 
				} catch (IOException e) {
					errorCode = ConverterErrorCode.COULD_NOT_READ_ISO_IMAGE_DATA_EXCEPTION;					
					throw new ConversionException (errorCode.getErrorCode(), e.getLocalizedMessage());
				}
				outImageData = convertBufferedImageToBytes(targetCode, outImage);
				break;
			default:
				throw new ConversionException (NOT_SUPPORTED_COMPRESSION_TYPE.getErrorCode(), NOT_SUPPORTED_COMPRESSION_TYPE.getErrorMessage());
		}
		if (outImageData != null)
		{
			return CommonUtil.encodeToURLSafeBase64(outImageData);
		}
		throw new ConversionException (errorCode.getErrorCode(), errorCode.getErrorMessage());
	}

	private String convertIrisIsoToImageType(SourceFormatCode sourceCode, String isoData, TargetFormatCode targetCode, 
		Map<String, String> targetParameters) throws ConversionException {
		ConverterErrorCode errorCode = ConverterErrorCode.TECHNICAL_ERROR_EXCEPTION;

		ConvertRequestDto requestDto = new ConvertRequestDto();
		requestDto.setModality("Iris");
		requestDto.setVersion(sourceCode.getCode());
		try {
			requestDto.setInputBytes(CommonUtil.decodeURLSafeBase64 (isoData));
		} catch (Exception e) {
			errorCode = ConverterErrorCode.SOURCE_NOT_VALID_BASE64URLENCODED_EXCEPTION;
			throw new ConversionException (errorCode.getErrorCode(), e.getLocalizedMessage());
		}

		int inImageFormat = -1;
		byte [] inImageData = null;
		IrisBDIR bdir;
		try {
			bdir = IrisDecoder.getIrisBDIR(requestDto);

			inImageFormat = bdir.getImageFormat();
			inImageData = bdir.getImage();
		} catch (Exception e) {
			errorCode = ConverterErrorCode.SOURCE_NOT_VALID_IRIS_ISO_FORMAT_EXCEPTION;
			throw new ConversionException (errorCode.getErrorCode(), e.getLocalizedMessage());
		}

		BufferedImage outImage = null;
		byte [] outImageData = null; 
		switch(inImageFormat)
		{
			case ImageFormat.MONO_JPEG2000:
				try {
					outImage = ImageIO.read(new ByteArrayInputStream(inImageData));
					// change here outImage width, height, dpi here based on targetParameters 
				} catch (IOException e) {
					errorCode = ConverterErrorCode.COULD_NOT_READ_ISO_IMAGE_DATA_EXCEPTION;					
					throw new ConversionException (errorCode.getErrorCode(), e.getLocalizedMessage());
				}
				outImageData = convertBufferedImageToBytes(targetCode, outImage);
				break;
			default:
				throw new ConversionException (NOT_SUPPORTED_COMPRESSION_TYPE.getErrorCode(), NOT_SUPPORTED_COMPRESSION_TYPE.getErrorMessage());
		}
		if (outImageData != null)
		{
			return CommonUtil.encodeToURLSafeBase64(outImageData);
		}
		throw new ConversionException (errorCode.getErrorCode(), errorCode.getErrorMessage());
	}
	
	private byte[] convertBufferedImageToBytes(TargetFormatCode targetCode, BufferedImage outImage) {
		switch (targetCode)
		{
			case IMAGE_JPEG:
				return CommonUtil.convertBufferedImageToJPEGBytes(outImage);
			case IMAGE_PNG:
				return CommonUtil.convertBufferedImageToPNGBytes(outImage);
			default:
				throw new ConversionException (INVALID_TARGET_EXCEPTION.getErrorCode(), INVALID_TARGET_EXCEPTION.getErrorMessage ());
		}	
	}
}
