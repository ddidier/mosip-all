package io.mosip.kernel.masterdata.entity;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Neha
 * @since 1.0.0
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "template_file_format", schema = "master")
@EqualsAndHashCode(callSuper = true)
public class TemplateFileFormat extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1051422672381211978L;

	@Id
	@AttributeOverride(name = "code", column = @Column(name = "code", nullable = false))
	private String code;

	@Column(name = "lang_code", nullable = false, length = 3)
	private String langCode;

	@Column(name = "descr")
	private String description;

}
