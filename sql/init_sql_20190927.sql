
DROP TABLE IF EXISTS "user_info";
CREATE TABLE "user_info" (
"id" serial PRIMARY KEY,
"user_id" varchar(50),
"open_id" varchar(50)[],
"name" varchar(40),
"gender" varchar(10),
"birthday" varchar(20),
"head_img" varchar(128),
"nick_name" varchar(64),
"id_type" integer,
"id_card" varchar(64),
"term_validity" varchar(20),
"mobile" varchar(64)[],
"lorry" varchar(64)[],
"order_nums" integer,
"score" numeric(10,2),
"distance" numeric(10,2),
"punish" jsonb,
"urgent_name_f" varchar(64),
"urgent_mobile_f" varchar(64),
"urgent_name_s" varchar(64),
"urgent_mobile_s" varchar(64),
"status" integer default 1,
"ext_info" hstore,
"last_login_time" timestamptz(6) default now(),
"create_time" timestamptz(6) default now(),
"operate_time" timestamptz(6) default now(),
"role" integer
)
WITH (OIDS=FALSE)
;
COMMENT ON COLUMN "user_info"."user_id" IS '用户ID,具有唯一性';
COMMENT ON COLUMN "user_info"."open_id" IS 'openid,存在一个用户对应多个openID';
COMMENT ON COLUMN "user_info"."name" IS '姓名';
COMMENT ON COLUMN "user_info"."gender" IS '性别';
COMMENT ON COLUMN "user_info"."car_type" IS '证件类型';
COMMENT ON COLUMN "user_info"."head_img" IS '头像';
COMMENT ON COLUMN "user_info"."nick_name" IS '昵称';
COMMENT ON COLUMN "user_info"."id_card" IS '证件号';
COMMENT ON COLUMN "user_info"."term_validity" IS '证件有效期';
COMMENT ON COLUMN "user_info"."mobile" IS '电话';
COMMENT ON COLUMN "user_info"."lorry" IS '车辆信息';
COMMENT ON COLUMN "user_info"."order_nums" IS '订单数量';
COMMENT ON COLUMN "user_info"."score" IS '积分';
COMMENT ON COLUMN "user_info"."distance" IS '总货运里程';
COMMENT ON COLUMN "user_info"."punish" IS '触发信息';
COMMENT ON COLUMN "user_info"."urgent_name_f" IS '紧急联系人1姓名';
COMMENT ON COLUMN "user_info"."urgent_mobile_f" IS '紧急联系人1电话';
COMMENT ON COLUMN "user_info"."urgent_name_s" IS '紧急联系人2姓名';
COMMENT ON COLUMN "user_info"."urgent_mobile_s" IS '紧急联系人2电话';
COMMENT ON COLUMN "user_info"."status" IS '状态 1:有效  2无效 3接单中';
COMMENT ON COLUMN "user_info"."ext_info" IS '扩展信息';
COMMENT ON COLUMN "user_info"."last_login_time" IS '最后登录时间';
COMMENT ON COLUMN "user_info"."role" IS '1 司机,2 烘干塔 3 港口 4 系统管理员  5 操作员';



DROP TABLE IF EXISTS "lorry_info";
CREATE TABLE "lorry_info" (
"id" serial PRIMARY KEY,
"lorry_type" varchar(40),
"company" varchar(40),
"weight" numeric(10,2),
"carry_weight" numeric(10,2),
"over_carry_weight" numeric(10,2),
"length" numeric(10,2),
"width" numeric(10,2),
"buy_time" timestamptz(6) default now(),
"mileage" numeric(10,2),
"plate_number" varchar(40),
"frame_number" varchar(128),
"id_type" integer,
"id_card" varchar(64),
"status" integer default 1,
"ext_info" hstore,
"create_time" timestamptz(6) default now(),
"operate_time" timestamptz(6) default now(),
"default_flag" integer default 0,
"unit_weight" varchar (10)
)
WITH (OIDS=FALSE)
;
COMMENT ON COLUMN "lorry_info"."lorry_type" IS '车型';
COMMENT ON COLUMN "lorry_info"."company" IS '品牌';
COMMENT ON COLUMN "lorry_info"."weight" IS '车重';
COMMENT ON COLUMN "lorry_info"."carry_weight" IS '载重';
COMMENT ON COLUMN "lorry_info"."over_carry_weight" IS '超载载重';
COMMENT ON COLUMN "lorry_info"."length" IS '长度';
COMMENT ON COLUMN "lorry_info"."width" IS '宽度';
COMMENT ON COLUMN "lorry_info"."buy_time" IS '购置日期';
COMMENT ON COLUMN "lorry_info"."mileage" IS '里程';
COMMENT ON COLUMN "lorry_info"."plate_number" IS '车牌号';
COMMENT ON COLUMN "lorry_info"."frame_number" IS '车架号';
COMMENT ON COLUMN "lorry_info"."id_type" IS '车主证件类型';
COMMENT ON COLUMN "lorry_info"."id_card" IS '车主证件号';
COMMENT ON COLUMN "lorry_info"."status" IS '状态 1:有效  2无效';
COMMENT ON COLUMN "lorry_info"."default_flag" IS '默认车辆标志位 1表明该车辆是该名车主的默认车辆，0表示其他';
COMMENT ON COLUMN "lorry_info"."unit_weight" IS '车辆载重单位';




DROP TABLE IF EXISTS "order_info";
CREATE TABLE "order_info" (
"id" serial PRIMARY KEY,
"task_id" varchar(40),
"order_id" varchar(40),
"lorry_id" integer,
"user_id" varchar(40),
"distance" numeric(10,2),
"unit_distance" varchar(40),
"unit_weight" varchar(40),
"carry_weight" numeric(10,2),
"order_weight" numeric(10,2),
"succ_weight" numeric(10,2),
"overweight" integer,
"accept_time" timestamptz(6),
"start_time" timestamptz(6),
"must_finish_time" timestamptz(6),
"send_out_time" timestamptz(6),
"estimate_finish_time" timestamptz(6),
"finish_time" timestamptz(6),
"send_out_code" varchar(10),
"send_out_user_id" varchar(40) [],
"receive_code" varchar(10),
"receiver_user_id" varchar(40) [],
"status" integer default 1,
"unexpect" text,
"cancel_time" timestamptz(6),
"cancel_reason" text,
"ext_info" hstore,
"create_time" timestamptz(6) default now(),
"operate_time" timestamptz(6) default now(),
"verify_code" varchar(100),
"receive_time" varchar (40),
"delivery_receive_time" timestamptz(6),
"let_out_time" timestamptz(6)
)
WITH (OIDS=FALSE)
;

COMMENT ON COLUMN "order_info"."task_id" IS '任务编号';
COMMENT ON COLUMN "order_info"."order_id" IS '订单ID';
COMMENT ON COLUMN "order_info"."lorry_id" IS '车辆id';
COMMENT ON COLUMN "order_info"."user_id" IS '货物提供方id';
COMMENT ON COLUMN "order_info"."user_id" IS '货物提供方id';
COMMENT ON COLUMN "order_info"."unit" IS '运送单位';
COMMENT ON COLUMN "order_info"."distance" IS '距离';
COMMENT ON COLUMN "order_info"."unit_distance" IS '距离单位';
COMMENT ON COLUMN "order_info"."carry_weight" IS '运送重量';
COMMENT ON COLUMN "order_info"."order_weight" IS '订单重量';
COMMENT ON COLUMN "order_info"."succ_weight" IS '送达重量';
COMMENT ON COLUMN "order_info"."overweight" IS '是否超重';
COMMENT ON COLUMN "order_info"."unit_weight" IS '运送单位';
COMMENT ON COLUMN "order_info"."accept_time" IS '接单时间';
COMMENT ON COLUMN "order_info"."start_time" IS '开始时间';
COMMENT ON COLUMN "order_info"."must_finish_time" IS '要求送达时间';
COMMENT ON COLUMN "order_info"."estimate_finish_time" IS '预计送达时间';
COMMENT ON COLUMN "order_info"."send_out_time" IS '出货时间';
COMMENT ON COLUMN "order_info"."send_out_code" IS '出货码';
COMMENT ON COLUMN "order_info"."send_out_user_id" IS '出货操作人id';
COMMENT ON COLUMN "order_info"."finish_time" IS '送达时间';
COMMENT ON COLUMN "order_info"."receive_code" IS '接货码';
COMMENT ON COLUMN "order_info"."receiver_user_id" IS '接货人id';
COMMENT ON COLUMN "order_info"."unexpect" IS '意外情况说明';
COMMENT ON COLUMN "order_info"."cancel_time" IS '取消时间';
COMMENT ON COLUMN "order_info"."cancel_reason" IS '取消原因';
COMMENT ON COLUMN "order_info"."ext_info" IS '扩展信息';
COMMENT ON COLUMN "order_info"."status" IS '状态 1:有效  2无效';
COMMENT ON COLUMN "order_info"."send_out_user_id" IS '出货人列表';
COMMENT ON COLUMN "order_info"."verify_code" IS '出货验证码';
COMMENT ON COLUMN "order_info"."receive_time" IS '司机到货物提供方接货时间';
COMMENT ON COLUMN "order_info"."delivery_receive_time" IS '接货人操作接货接货时间';
COMMENT ON COLUMN "order_info"."let_out_time" IS '卸货完成时间';



DROP TABLE IF EXISTS "task_info";
CREATE TABLE "task_info" (
"id" serial PRIMARY KEY,
"title" varchar(256),
"task_id" varchar(40),
"product" varchar(128),
"weight" numeric(10,2),
"unit" varchar(40),
"unaccept_weight" numeric(10,2),
"undist_weight" numeric(10,2),
"start_time" timestamptz(6),
"end_time" timestamptz(6),
"unit_cost_time" timestamptz(6),
"dep" varchar(40),
"arr" varchar(40),
"dep_gis" geometry,
"arr_gis" geometry,
"distance" numeric(10,2),
"unit_distance" varchar (10),
"unit_weight_price" numeric(10,2),
"estimate_price" numeric(10,2),
"level" integer,
"status" integer default 1,
"ext_info" hstore,
"load_lorry_unit" integer,
"load_time_unit" integer,
"create_time" timestamptz(6) default now(),
"operate_time" timestamptz(6) default now(),
"subtask_time" json,
"receiver_user_id" varchar(50)[],
"unit_price" varchar(10),
"unit_weight" varchar (20),
"service_mobile" varchar (20),
"receiver_mobile" varchar(64)[],
"supplier_mobile" varchar(64)[]
)
WITH (OIDS=FALSE)
;


COMMENT ON COLUMN "task_info"."title" IS '标题';
COMMENT ON COLUMN "task_info"."product" IS '产品名称';
COMMENT ON COLUMN "task_info"."weight" IS '重量';
COMMENT ON COLUMN "task_info"."unit" IS '单位';
COMMENT ON COLUMN "task_info"."unaccept_weight" IS '未接单量';
COMMENT ON COLUMN "task_info"."undist_weight" IS '未派发量';
COMMENT ON COLUMN "task_info"."start_time" IS '订单开始时间';
COMMENT ON COLUMN "task_info"."end_time" IS '订单结束时间';
COMMENT ON COLUMN "task_info"."unit_cost_time" IS '单位任务耗时';
COMMENT ON COLUMN "task_info"."dep" IS '出发地';
COMMENT ON COLUMN "task_info"."arr" IS '到达地';
COMMENT ON COLUMN "task_info"."distance" IS '距离';
COMMENT ON COLUMN "task_info"."unit_distance" IS '距离单位km';

COMMENT ON COLUMN "task_info"."unit_weight_price" IS '单位重量价格';
COMMENT ON COLUMN "task_info"."estimate_price" IS '预期收益';
COMMENT ON COLUMN "task_info"."level" IS '任务级别';
COMMENT ON COLUMN "task_info"."status" IS '状态';
COMMENT ON COLUMN "task_info"."ext_info" IS '扩展信息';
COMMENT ON COLUMN "task_info"."load_lorry_unit" IS '单位装载车辆数,如可同时装载两量车';
COMMENT ON COLUMN "task_info"."load_time_unit" IS '单位装载时间，装载一顿耗时';
COMMENT ON COLUMN "task_info"."operation_detail" IS '单位装载时间，装载一顿耗时';
COMMENT ON COLUMN "task_info"."subtask_time" IS '出货时间安排    [{"time": "2019-09-12 12:00-14:00","num": 2}, {"time": "2019-09-13 12:00-14:00","num": 2}]';

COMMENT ON COLUMN "task_info"."consignee_userid" IS '接货人ID';
COMMENT ON COLUMN "task_info"."unit_price" IS '价格单位';
COMMENT ON COLUMN "task_info"."service_mobile" IS '客服电话';
COMMENT ON COLUMN "task_info"."receiver_mobile" IS '收货人电话';
COMMENT ON COLUMN "task_info"."supplier_mobile" IS '出货人电话';


create table sub_task
(
	id serial not null,
	task_id varchar(40),
	start_time timestamp,
	end_time timestamp,
	lorry_num int,
	status int default 1,
	undist_num int,
	sub_task_id varchar(40)
);

comment on column sub_task.id is '自增ID';

comment on column sub_task.taskId is '主任务ID';

comment on column sub_task.start_time is '起始时间';

comment on column sub_task.end_time is '截止时间';

comment on column sub_task.lorry_num is '该时间段可接受几辆车';

comment on column sub_task.status is '1有效0无效';

comment on column sub_task.undist_num is '未派发数量';

comment on column sub_task.sub_task_id is '拆分子任务的ID';





DROP TABLE IF EXISTS "lorry_gis_info";
CREATE TABLE "lorry_gis_info" (
"id" serial PRIMARY KEY,
"order_id" integer,
"lorry_id" integer,
"user_id" integer,
"gis" geometry,
"status" integer default 1,
"ext_info" hstore,
"create_time" timestamptz(6) default now(),
"operate_time" timestamptz(6) default now()
)
WITH (OIDS=FALSE)
;

COMMENT ON COLUMN "lorry_gis_info"."order_id" IS '订单id';
COMMENT ON COLUMN "lorry_gis_info"."gis" IS '订单id';
COMMENT ON COLUMN "lorry_gis_info"."lorry_id" IS '订单id';
COMMENT ON COLUMN "lorry_gis_info"."user_id" IS '用户id';
COMMENT ON COLUMN "lorry_gis_info"."status" IS '1 有效 2 无效';



DROP TABLE IF EXISTS "order_operation_log";
CREATE TABLE "order_operation_log" (
"id" serial PRIMARY KEY,
"order_id" varchar(40),
"mark" text,
"operator" integer,
"status" integer default 1,
"ext_info" hstore,
"create_time" timestamptz(6) default now(),
"operate_time" timestamptz(6) default now()
)
WITH (OIDS=FALSE)
;
COMMENT ON COLUMN "order_operation_log"."order_id" IS '订单id';
COMMENT ON COLUMN "order_operation_log"."mark" IS '操作内容';
COMMENT ON COLUMN "order_operation_log"."operator" IS '操作人id';
COMMENT ON COLUMN "order_operation_log"."status" IS '1 有效  2 无效';



DROP TABLE IF EXISTS "acl_info";
CREATE TABLE "acl_info" (
"id" serial PRIMARY KEY,
"name" varchar(40),
"code" varchar(40),
"url" varchar(256),
"module" integer,
"status" integer default 1,
"ext_info" hstore,
"create_time" timestamptz(6) default now(),
"operate_time" timestamptz(6) default now()
)
WITH (OIDS=FALSE)
;
COMMENT ON COLUMN "acl_info"."name" IS '名称';
COMMENT ON COLUMN "acl_info"."code" IS '标记';
COMMENT ON COLUMN "acl_info"."url" IS '链接';
COMMENT ON COLUMN "acl_info"."module" IS '模块类型 1 菜单 2按钮';
COMMENT ON COLUMN "acl_info"."status" IS '1 有效  2 无效';


DROP TABLE IF EXISTS "role_info";
CREATE TABLE "role_info" (
"id" serial PRIMARY KEY,
"name" varchar(40),
"role_id" varchar(40),
"status" integer default 1,
"ext_info" hstore,
"acl_code" varchar(40),
"create_time" timestamptz(6) default now(),
"operate_time" timestamptz(6) default now()
)
WITH (OIDS=FALSE)
;
COMMENT ON COLUMN "role_info"."name" IS '角色名称';
COMMENT ON COLUMN "role_info"."status" IS '1 有效  2 无效';
COMMENT ON COLUMN "role_info"."acl_code" IS '角色对应的权限';
COMMENT ON COLUMN "role_info"."role_id" IS '角色id';


DROP TABLE IF EXISTS "user_role_info";
CREATE TABLE "user_role_info" (
"id" serial PRIMARY KEY,
"user_id" integer,
"role_id" varchar(40),
"status" integer default 1,
"ext_info" hstore,
"create_time" timestamptz(6) default now(),
"operate_time" timestamptz(6) default now()
)
WITH (OIDS=FALSE)
;
COMMENT ON COLUMN "user_role_info"."user_id" IS '用户id';
COMMENT ON COLUMN "user_role_info"."role" IS '角色id';
COMMENT ON COLUMN "user_role_info"."status" IS '1 有效  2 无效';


DROP TABLE IF EXISTS "user_acl_info";
CREATE TABLE "user_acl_info" (
"id" serial PRIMARY KEY,
"user_id" varchar(40),
"acl_code" varchar(40),
"status" integer default 1,
"ext_info" hstore,
"create_time" timestamptz(6) default now(),
"operate_time" timestamptz(6) default now()
)
WITH (OIDS=FALSE)
;
COMMENT ON COLUMN "user_acl_info"."user_id" IS '用户id';
COMMENT ON COLUMN "user_acl_info"."acl_id" IS '权限id';
COMMENT ON COLUMN "user_acl_info"."status" IS '1 有效  2 无效';





create table dist_order_info
(
  id serial not null,
  dist_id varchar(40),
  user_id varchar(40),
  task_id varchar(40) not null,
  job_no int,
  job_status int default 0,
  operation_time json,
  create_time timestamptz(6) default now(),
  expire_time timestamptz(6) default now(),
  order_id varchar(40),
  mobile varchar(40),
  dist_weight numeric(10,2)
);

comment on table dist_order_info is '发送通知信息表';
comment on column dist_order_info.dist_id is '派单ID';

comment on column dist_order_info.user_id is '车主的userID';

comment on column dist_order_info.id is '自增ID';

comment on column dist_order_info.task_id is '任务ID';

comment on column dist_order_info.job_no is '批次号';

comment on column dist_order_info.job_status is '发布任务的表示
0 待接单
1 接单
2 拒绝接单
3 接单后取消
4 超时未接单
5 接单后超时未去接受任务';

comment on column dist_order_info.operation_time is '操作时间';
comment on column dist_order_info.create_time is '下发短信时间';
comment on column dist_order_info.expire_time is '过期时间';
comment on column dist_order_info.order_id is '对应的订单ID。只有司机接单时才会生成这个订单ID';
comment on column dist_order_info.mobile is '派单短信的下发';
comment on column dist_order_info.dist_weight is '派单的重量';


-- 商品总类表
create table commodity_list
(
    id                   serial not null primary key,
    name                 varchar(200),
    commodity_properties hstore,
    commodity_id         varchar(40),
    status               integer default 1,
    system_flag          integer,
    bind_user_id         varchar(50)
);

comment on table commodity_list is '商品种类';

comment on column commodity_list.name is '商品名称 一级玉米';

comment on column commodity_list.commodity_properties is '商品属性';

comment on column commodity_list.commodity_id is '商品ID';

comment on column commodity_list.status is '有效标志位 1有效  0 无效';

comment on column commodity_list.system_flag is '1 系统商品；0 是用户自定义商品';

comment on column commodity_list.bind_user_id is '绑定这个报价的提出用户userID';





-- 烘干塔信息
-- auto-generated definition
create table dry_tower
(
    id              serial not null primary key,
    company         varchar(200),
    commodity_id    varchar(40)[],
    location        varchar(200),
    area            numeric(10, 2),
    shipment_weight numeric(10, 2),
    create_time     timestamp default now(),
    status          integer   default 1,
    capacity_store  numeric(10, 2),
    load_lorry_num  integer,
    load_lorry_cost numeric(5, 2),
    bind_user_id    varchar(40),
    tower_id        varchar(50),
    default_flag    integer,
    location_area   varchar(200),
    location_detail varchar(200),
    contact_user_id varchar(40)[],
    contacts_name   varchar(40),
    contact_mobile  varchar(20),
    user_id_card    varchar(50)
);

comment on table dry_tower is '烘干塔信息';

comment on column dry_tower.id is '自增ID';

comment on column dry_tower.company is '公司名称';

comment on column dry_tower.commodity_id is '主营品种';

comment on column dry_tower.location is '位置';

comment on column dry_tower.area is '占地大小';

comment on column dry_tower.shipment_weight is '出货量';

comment on column dry_tower.create_time is '创建时间';

comment on column dry_tower.status is '状态 1 正在使用中 0 失效';

comment on column dry_tower.capacity_store is '库存能力';

comment on column dry_tower.load_lorry_num is '同时装载车次';

comment on column dry_tower.load_lorry_cost is '装载一车耗时';

comment on column dry_tower.bind_user_id is '绑定人';

comment on column dry_tower.tower_id is '烘干塔ID';

comment on column dry_tower.default_flag is '默认标志位 1 是默认 0是非默认';

comment on column dry_tower.location_area is '所在区域';

comment on column dry_tower.location_detail is '详细地址';

comment on column dry_tower.contact_user_id is '烘干塔联系人，一个塔可以对应多个联系人';


-- 报价表
-- auto-generated definition
create table quote_info
(
    id  serial not null primary key,
    commodity_id    varchar(40),
    quote           numeric(10, 2),
    unit_price      varchar(10),
    shipment_weight numeric(10, 2),
    unit_weight     varchar(10),
    start_time      timestamp,
    update_time     timestamp,
    status          integer default 1,
    "desc"          varchar(200),
    system_flag     integer,
    bargain_status  integer,
    user_id         varchar(50),
    location        varchar(200),
    user_name       varchar(100),
    quote_id        varchar(50),
    tower_id        varchar(50),
    mobile          varchar(200),
    end_time        timestamp
);

comment on table quote_info is '报价表格';

comment on column quote_info.id is '自增ID';

comment on column quote_info.commodity_id is '商品ID';

comment on column quote_info.quote is '报价金额';

comment on column quote_info.unit_price is '金额单位';

comment on column quote_info.shipment_weight is '出货量';

comment on column quote_info.unit_weight is '出货重量单位';

comment on column quote_info.start_time is '出仓时间';

comment on column quote_info.update_time is '更新时间';

comment on column quote_info.status is '报价的状态 1 有效 0 无效';

comment on column quote_info."desc" is '描述';

comment on column quote_info.system_flag is '是否是系统报价，1 是系统报价 0 是用户自定义的报价';

comment on column quote_info.bargain_status is '是否接受议价 0 不接受议价 1接受议价';

comment on column quote_info.user_id is '报价人';

comment on column quote_info.location is '出仓位置';

comment on column quote_info.user_name is '用户名';

comment on column quote_info.quote_id is '报价ID';

comment on column quote_info.tower_id is '烘干塔ID';

comment on column quote_info.mobile is '联系电话';

comment on column quote_info.end_time is '结束时间';


-- 系统报价表
-- auto-generated definition
create table system_quote
(
    id                  serial not null primary key,
    quote_id            varchar(40),
    commodity_id        varchar(40),
    quote               numeric(10, 2),
    unit_price          varchar(10),
    create_time         timestamp default now(),
    update_time         timestamp default now(),
    status              integer   default 1,
    unit_weight         varchar(10),
    min_shipment_weight numeric(10, 2)
);

comment on table system_quote is '系统品种报价表';

comment on column system_quote.id is '自增ID';

comment on column system_quote.quote_id is '报价ID';

comment on column system_quote.commodity_id is '商品ID';

comment on column system_quote.quote is '价格';

comment on column system_quote.unit_price is '价格单位  元/吨';

comment on column system_quote.unit_weight is '重量单位';

comment on column system_quote.min_shipment_weight is '最少出货量';

create table agreement_info
(
    id             serial not null primary key,
    agreement_id   varchar(50),
    status         integer   default 1,
    create_time    timestamp default now(),
    update_time    timestamp,
    agreement_name varchar(200),
    adapt          varchar(40)[],
    agreement      text
);

comment on table agreement_info is '协议表 各种协议';

comment on column agreement_info.id is '自增ID';

comment on column agreement_info.agreement_id is '协议ID';

comment on column agreement_info.status is '状态位 0 无效 1有效';

comment on column agreement_info.create_time is '创建时间';

comment on column agreement_info.update_time is '更新时间';

comment on column agreement_info.agreement_name is '协议名称';

comment on column agreement_info.adapt is '适用那些页面';

comment on column agreement_info.agreement is '协议内容';




