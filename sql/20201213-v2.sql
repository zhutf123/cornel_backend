
---烘干塔报价表增加修改记录
alter table quote_info add column front_value jsonb;
COMMENT ON COLUMN "quote_info"."front_value" IS '变更前的值';


alter table company_info add column user_id varchar(40);
comment on column company_info.user_id is '绑定用户id';

alter table company_info add column license_url varchar(40);
comment on column company_info.license_url is '证件url';


