
---烘干塔报价表增加修改记录
alter table quote_info add column front_value jsonb;
COMMENT ON COLUMN "quote_info"."front_value" IS '变更前的值';


alter table company_info add column user_id varchar(40);
comment on column company_info.user_id is '绑定用户id';

alter table company_info add column license_url varchar(128);
comment on column company_info.license_url is '证件url';


alter table dry_tower add column company_id varchar(40);
comment on column dry_tower.company_id is '公司id';

ALTER TABLE user_info ALTER COLUMN id_type SET DEFAULT 1;


insert into company_info(company_id,company_name,user_id)
values('c274e276-46b3-413c-8e02-ea5417a12663','刘刚','29f2c648-6152-4742-982f-0a55cf3ff86a');


update dry_tower set company_id ='c274e276-46b3-413c-8e02-ea5417a12663',company='刘刚' where id=8;
