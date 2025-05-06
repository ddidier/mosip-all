/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.mosip.certify.mosipid.integration.service;


import io.mosip.kernel.signature.dto.JWTSignatureRequestDto;
import io.mosip.kernel.signature.dto.JWTSignatureResponseDto;
import io.mosip.kernel.signature.service.SignatureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;


@Service
@Slf4j
@ConditionalOnProperty(value = "mosip.certify.integration.vci-plugin", havingValue = "IdaVCIssuancePluginImpl")
public class HelperService {

    public static final String UTC_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String OIDC_PARTNER_APP_ID = "CERTIFY_PARTNER";
    private static Base64.Encoder urlSafeEncoder;

    static {
        urlSafeEncoder = Base64.getUrlEncoder().withoutPadding();
    }


    @Autowired
    private SignatureService signatureService;

    protected String getRequestSignature(String request) {
        JWTSignatureRequestDto jwtSignatureRequestDto = new JWTSignatureRequestDto();
        jwtSignatureRequestDto.setApplicationId(OIDC_PARTNER_APP_ID);
        jwtSignatureRequestDto.setReferenceId("");
        jwtSignatureRequestDto.setIncludePayload(false);
        jwtSignatureRequestDto.setIncludeCertificate(true);
        jwtSignatureRequestDto.setDataToSign(HelperService.b64Encode(request));
        JWTSignatureResponseDto responseDto = signatureService.jwtSign(jwtSignatureRequestDto);
        log.debug("Request signature ---> {}", responseDto.getJwtSignedData());
        return responseDto.getJwtSignedData();
    }

    protected static String getUTCDateTime() {
        return ZonedDateTime
                .now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern(UTC_DATETIME_PATTERN));
    }

    protected static String b64Encode(String value) {
        return urlSafeEncoder.encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

}
