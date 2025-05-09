package io.mosip.kernel.masterdata.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.mosip.kernel.masterdata.validator.FilterType;
import io.mosip.kernel.masterdata.validator.FilterTypeEnum;
import io.mosip.kernel.masterdata.validator.StringFormatter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Class for Device Type DTO
 * 
 * @author Megha Tanga
 * @since 1.0.0
 *
 */

@Data
public class DeviceTypeDto {


	@NotNull
	@StringFormatter(min = 1, max = 36)
	@ApiModelProperty(value = "code", required = true, dataType = "java.lang.String")
	private String code;

	@Deprecated
	private String langCode;

	@FilterType(types = { FilterTypeEnum.EQUALS, FilterTypeEnum.STARTSWITH, FilterTypeEnum.CONTAINS })
	@NotNull
	@StringFormatter(min = 1, max = 64)
	@ApiModelProperty(value = "name", required = true, dataType = "java.lang.String")
	private String name;

	@Size(min = 0, max = 128)
	@ApiModelProperty(value = "description", required = true, dataType = "java.lang.String")
	private String description;

	@NotNull
	@ApiModelProperty(value = "isActive", required = true, dataType = "java.lang.Boolean")
	private Boolean isActive;
}
