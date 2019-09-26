create table fh_hotel_op_log (
  id serial primary key ,
  hotel_id integer not null ,
  operator_id integer not null ,
  operator varchar(64) not null ,
  op_type  integer not null ,
  product_type integer     not null ,
  content text not null ,
  operate_time timestamptz not null default current_timestamp
);

comment on table fh_hotel_op_log is '机酒产品操作记录表';
comment on column fh_hotel_op_log.id is '自增主键';
comment on column fh_hotel_op_log.hotel_id is '酒店Id';
comment on column fh_hotel_op_log.operator_id is '操作者qunarId';
comment on column fh_hotel_op_log.operator is '操作者名字';
comment on column fh_hotel_op_log.op_type is '操作类型(1:保存 2:更新 3:删除)';
comment on column fh_hotel_op_log.product_type is '操作产品分类(1:酒店 2:房型 3:房型报价)';
comment on column fh_hotel_op_log.content is '操作内容';
comment on column fh_hotel_op_log.operate_time is '操作时间';
