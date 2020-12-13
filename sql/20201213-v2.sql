
---烘干塔报价表增加修改记录
alter table quote_info add column front_value jsonb;
COMMENT ON COLUMN "quote_info"."front_value" IS '变更前的值';

