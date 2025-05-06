-- -------------------------------------------------------------------------------------------------
-- Database Name	: mosip_keymgr
-- Release Version 	: 1.2.0
-- Purpose    		: Database Alter scripts for the release for Key Manager DB.       
-- Create By   		: Ram Bhatt
-- Created Date		: Nov-2021
-- 
-- Modified Date        Modified By         Comments / Remarks
-- -------------------------------------------------------------------------------------------------

\c mosip_keymgr sysadmin


ALTER TABLE keymgr.key_alias ADD COLUMN cert_thumbprint character varying(100);
ALTER TABLE keymgr.ca_cert_store ADD CONSTRAINT cert_thumbprint_unique UNIQUE (cert_thumbprint,partner_domain);

ALTER TABLE keymgr.key_alias ADD COLUMN uni_ident character varying(50);
ALTER TABLE keymgr.key_alias ADD CONSTRAINT uni_ident_const UNIQUE (uni_ident);

ALTER TABLE keymgr.key_policy_def ADD COLUMN pre_expire_days smallint;
ALTER TABLE keymgr.key_policy_def ADD COLUMN access_allowed character varying(1024);
----------------------------------------------------------------------------------------------------
