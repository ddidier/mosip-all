package io.mosip.kernel.bio.converter.dto;

import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
* @author Janardhan B S
* @since 1.0.0
*/
@Data
public class ConvertRequestDto {
    @NotNull(message = "Values code can not be null")
    @Size(min=1, message = "Minimum one entry required")
	public Map<String, String> values;
    @NotNull(message = "SourceFormat code can not be null")
    @NotBlank(message = "SourceFormat code can not be blank")
    @NotEmpty(message = "SourceFormat code can not be empty")
    public String sourceFormat;
    @NotNull(message = "TargetFormat code can not be null")
    @NotBlank(message = "TargetFormat code can not be blank")
    @NotEmpty(message = "TargetFormat code can not be empty")
    public String targetFormat;
    public Map<String, String> sourceParameters;
    public Map<String, String> targetParameters;
}
