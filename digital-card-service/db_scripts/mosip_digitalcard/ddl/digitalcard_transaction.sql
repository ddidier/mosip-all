-- -------------------------------------------------------------------------------------------------
-- Database Name: mosip_digitalcard
-- Table Name 	: digitalcard.digitalcard_transaction
-- Purpose    	: Credential: The credential share is a functional service that interacts with the ID Repository and collects the user attributes for printing.
--           
-- Create By   	: Dhanendra Sahu
-- Created Date	: Aug-2020
-- 
-- Modified Date        Modified By         Comments / Remarks
-- ------------------------------------------------------------------------------------------

-- ------------------------------------------------------------------------------------------
-- object: digitalcard.digitalcard_transaction | type: TABLE --
-- DROP TABLE IF EXISTS digitalcard.digitalcard_transaction CASCADE;
CREATE TABLE digitalcard.digitalcard_transaction(
	rid character varying(36) NOT NULL,
	credential_id character varying(36),
	uin_salted_hash character varying,
	status_code character varying(32) NOT NULL,
	datashareurl character varying(256),
	issuancedate timestamp,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean DEFAULT FALSE,
	del_dtimes timestamp,
	status_comment character varying(512),
	CONSTRAINT pk_digitaltrn_rid PRIMARY KEY (rid)

);
-- ddl-end --
COMMENT ON TABLE digitalcard.digitalcard_transaction IS 'DigitalCard: The DigitalCard share is a functional service that interacts with the ID Repository and collects the user attributes for printing';
-- ddl-end --
COMMENT ON COLUMN digitalcard.digitalcard_transaction.rid IS 'RID:  rid is registration id';
-- ddl-end --
COMMENT ON COLUMN digitalcard.digitalcard_transaction.credential_id IS 'Credential Id: Credential id generated when distribute credential';
-- ddl-end --
COMMENT ON COLUMN digitalcard.digitalcard_transaction.uin_salted_hash IS 'Request: Request json of credential request genrator';
-- ddl-end --
COMMENT ON COLUMN digitalcard.digitalcard_transaction.status_code IS 'Status Code: Contains status of request';
-- ddl-end --
COMMENT ON COLUMN digitalcard.digitalcard_transaction.datashareurl IS 'Datashare URL: Credential data url';
-- ddl-end --
COMMENT ON COLUMN digitalcard.digitalcard_transaction.issuancedate IS 'Issuance Date: Credential issue date';
-- ddl-end --
COMMENT ON COLUMN digitalcard.digitalcard_transaction.cr_by IS 'Created By : ID or name of the user who create / insert record';
-- ddl-end --
COMMENT ON COLUMN digitalcard.digitalcard_transaction.cr_dtimes IS 'Created DateTimestamp : Date and Timestamp when the record is created/inserted';
-- ddl-end --
COMMENT ON COLUMN digitalcard.digitalcard_transaction.upd_by IS 'Updated By : ID or name of the user who update the record with new values';
-- ddl-end --
COMMENT ON COLUMN digitalcard.digitalcard_transaction.upd_dtimes IS 'Updated DateTimestamp : Date and Timestamp when any of the fields in the record is updated with new values.';
-- ddl-end --
COMMENT ON COLUMN digitalcard.digitalcard_transaction.is_deleted IS 'IS_Deleted : Flag to mark whether the record is Soft deleted.';
-- ddl-end --
COMMENT ON COLUMN digitalcard.digitalcard_transaction.del_dtimes IS 'Deleted DateTimestamp : Date and Timestamp when the record is soft deleted with is_deleted=TRUE';
-- ddl-end --
