package io.mosip.authentication.hotlistfilter.impl;

import static io.mosip.authentication.core.constant.IdAuthCommonConstants.BIO_PATH;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;

import io.mosip.authentication.authfilter.exception.IdAuthenticationFilterException;
import io.mosip.authentication.authfilter.spi.IMosipAuthFilter;
import io.mosip.authentication.common.service.transaction.manager.IdAuthSecurityManager;
import io.mosip.authentication.common.service.util.AuthTypeUtil;
import io.mosip.authentication.core.constant.IdAuthenticationErrorConstants;
import io.mosip.authentication.core.exception.IdAuthenticationBusinessException;
import io.mosip.authentication.core.hotlist.dto.HotlistDTO;
import io.mosip.authentication.core.indauth.dto.AuthRequestDTO;
import io.mosip.authentication.core.indauth.dto.BioIdentityInfoDTO;
import io.mosip.authentication.core.indauth.dto.IdentityInfoDTO;
import io.mosip.authentication.core.spi.hotlist.service.HotlistService;
import io.mosip.kernel.core.hotlist.constant.HotlistIdTypes;
import io.mosip.kernel.core.hotlist.constant.HotlistStatus;

/**
 * The Class DeviceProviderHotlistFilterImpl - implementation of auth filter for
 * validating hotlisted device providers in the authentication request.
 * 
 * @author Loganathan Sekar
 */
public class DeviceProviderHotlistFilterImpl implements IMosipAuthFilter {
	
	/** The hotlist service. */
	@Autowired
	private HotlistService hotlistService;	

	/**
	 * Inits the.
	 */
	public void init() throws IdAuthenticationFilterException {
	}

	/**
	 * Test method that executes predicate test condition on the given arguments
	 *
	 * @param authRequest  the auth request
	 * @param identityData the identity data
	 * @param properties   the properties
	 * @throws IdAuthenticationBusinessException 
	 */
	public void validate(AuthRequestDTO authRequest, Map<String, List<IdentityInfoDTO>> identityData,
			Map<String, Object> properties) throws IdAuthenticationFilterException {
		validateHotlistedIds(authRequest);
	}
	
	/**
	 * Checks if is device provider hotlisted.
	 *
	 * @param biometrics the biometrics
	 * @param errors     the errors
	 * @throws IdAuthenticationFilterException 
	 */
	protected void isDeviceProviderHotlisted(List<BioIdentityInfoDTO> biometrics) throws IdAuthenticationFilterException {
		if (Objects.nonNull(biometrics) && !biometrics.isEmpty()) {
			OptionalInt indexOpt = IntStream.range(0, biometrics.size()).filter(index -> {
				HotlistDTO hotlistStatus = hotlistService.getHotlistStatus(
						IdAuthSecurityManager.generateHashAndDigestAsPlainText(biometrics.get(0).getData().getDigitalId().getDp()
								.concat(biometrics.get(0).getData().getDigitalId().getDpId()).getBytes()),
						HotlistIdTypes.DEVICE_PROVIDER);
				return hotlistStatus.getStatus().contentEquals(HotlistStatus.BLOCKED);
			}).findFirst();
			if(indexOpt.isPresent()) {
				throw new IdAuthenticationFilterException(IdAuthenticationErrorConstants.IDVID_DEACTIVATED_BLOCKED.getErrorCode(),
							String.format(IdAuthenticationErrorConstants.IDVID_DEACTIVATED_BLOCKED.getErrorMessage(),
									String.format(BIO_PATH, indexOpt.getAsInt(), HotlistIdTypes.DEVICE_PROVIDER)));
			}
			
		}
	}

	/**
	 * Validate hotlisted ids.
	 *
	 * @param errors         the errors
	 * @param authRequestDto the auth request dto
	 * @throws IdAuthenticationFilterException 
	 */
	private void validateHotlistedIds(AuthRequestDTO authRequestDto) throws IdAuthenticationFilterException {
		if (AuthTypeUtil.isBio(authRequestDto)) {
			isDeviceProviderHotlisted(authRequestDto.getRequest().getBiometrics());
		}
	}
	
}
