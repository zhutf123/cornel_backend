create table fh_wish_order (
  id serial primary key ,
  main_order_id integer not null ,
  issue_type integer[] not null ,
  issue_phase integer not null ,
  origin_res_status json ,
  origin_person_num integer ,
  origin_day_num integer ,
  dep_area varchar(256) not null,
  arr_area varchar(256) not null,
  take_off_date timestamptz(6) not null ,
  own_status integer not null default 0 ,
  owner varchar(64) ,
  own_time timestamptz(6) ,
  order_status integer ,
  create_time timestamptz(6) not null ,
  operator integer ,
  operate_time timestamptz(6) not null default current_timestamp ,
  message_id varchar(40)
);

comment on table fh_wish_order is '意向单总表';
comment on column fh_wish_order.main_order_id is '原订单id, 关联main_order表的主键Id';
comment on column fh_wish_order.issue_type is '问题原因 (1:机票变价 2:机票售罄 3:酒店变价 4:酒店售罄 5:机票占座失败 6:酒店生单失败 7:机票出票失败 8:酒店确认失败)';
comment on column fh_wish_order.issue_phase is '问题阶段(1:生单 2:支付 3:机票占座 4:酒店生单 5:酒店确认 6:机票出票)';
comment on column fh_wish_order.origin_res_status is '原始资源失败状态 订单Id/失败原因';
comment on column fh_wish_order.origin_day_num is '原始订单出行天数';
comment on column fh_wish_order.dep_area is '原始出发地';
comment on column fh_wish_order.arr_area is '原始目的地';
comment on column fh_wish_order.take_off_date is '原始出行日期';
comment on column fh_wish_order.own_status is '认领状态 （0 未认领 1 已认领）';
comment on column fh_wish_order.owner is '认领人';
comment on column fh_wish_order.own_time is '认领时间';
comment on column fh_wish_order.order_status is '意向单状态 (1: 未代客下单　２：已下单　３：已弃单)';
comment on column fh_wish_order.create_time is '创建时间';
comment on column fh_wish_order.operator is '操作人 (原始订单下单失败后,自动创建一条意向单,默认是system)';
comment on column fh_wish_order.operate_time is '最后操作时间';
comment on column fh_wish_order.message_id is '创建意向单的qmq消息Id';
