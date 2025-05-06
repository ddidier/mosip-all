/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.mosip.certify.mosipid.integration.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
public class IdaVcExchangeRequest {

	@NotNull
	private String vcAuthToken;

	/** The Variable to hold value of Credential Subject Id */
	@NotNull
	private String credSubjectId;

	/** The Variable to hold value of VC Format type */
	@NotNull
	private String vcFormat;

	/** The Variable to hold value of list of user selected locales */
	private List<String> locales;

	private Map<String, Object> metadata;

	private String id;
	
	private String version;
	
	private String individualId;
	
	private String transactionID;
	
	private String requestTime;
	
	private CredentialDefinitionDTO credentialsDefinition;

}
