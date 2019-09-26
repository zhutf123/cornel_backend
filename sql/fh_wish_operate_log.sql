create table fh_wish_operate_log (
  id serial primary key ,
  wish_order_id integer not null ,
  op_type integer not null ,
  op_desc varchar(256) not null ,
  operator integer not null ,
  operator_name varchar(32) not null,
  operate_time timestamptz(6) not null default current_timestamp
);

comment on table fh_wish_operate_log is '意向单操作记录表';
comment on column fh_wish_operate_log.wish_order_id is '意向单id';
comment on column fh_wish_operate_log.op_type is '0:意向单 1:机票 2:酒店 3:用户需求';
comment on column fh_wish_operate_log.op_desc is '操作描述';
comment on column fh_wish_operate_log.operator is '操作人';
comment on column fh_wish_operate_log.operator_name is '操作人真实姓名';
comment on column fh_wish_operate_log.operate_time is '操作时间';