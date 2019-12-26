
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


司机信息
   姓名  性别  生日  证件类型  证件号  驾驶证证件有效期  联系电话[]  常用车辆信息[]  接单数量  积分  运送总里程  处罚情况{}
   紧急联系人1姓名  紧急联系人1电话  紧急联系人2姓名  紧急联系人2电话  司机状态

"valid" boolean,
"valid_number" varchar(6),
"open_id" varchar(128)


车辆基础信息
  车型  品牌  自重  载重  长度  宽度 状态

车辆信息
   车型id  购置日期  当前行驶里程  车牌  车架号  车主证件号  车主证件类型  车辆状态

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


订单信息
   任务id 车辆id  司机id  是否超载  运送重量  送达重量  抢单时间  任务开始时间  要求送达时间  出货时间
   预计送达时间  送达时间 司机出货码  仓库收货码 订单状态  意外情况说明
   取消时间  取消原因


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



任务信息
  任务id  任务标题  货品名称  任务总重量  未被领取的任务重量  任务开始时间  任务结束时间  拆分任务执行时长
  出发地名称  送达地名称  出发地gps  送达地gps  里程信息
  货品单价  平均收益  紧急级别  任务状态

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
"unit_price" varchar(10)
)
WITH (OIDS=FALSE)
;

--经纬图查询按照str返回
--select ST_AsText(dep_gis) from task_info limit 1;
--update task_info set dep_gis=point(116.456011,40.14013)::geometry

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

 拆分子任务的信息

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




定位信息
  车辆id  司机id gis信息  gis更新时间


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


订单操作记录

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


权限表 角色表   用户角色 用户权限表

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




