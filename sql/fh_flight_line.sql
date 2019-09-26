create table fh_flight_line (
  id serial primary key ,
  supplier_id integer not null ,
  trip_type integer not null default 1,
  dep_area_go varchar(64) not null ,
  dep_area_back  varchar(64) ,
  arr_area_go  varchar(64) not null ,
  arr_area_back  varchar(64) ,
  transfer_area_go  text not null,
  transfer_area_back  text,
  adult_airport_tax  integer not null ,
  adult_fuel_tax  integer not null ,
  adult_tax  integer not null ,
  adult_service_fee  numeric(10,2) not null ,
  child_airport_tax  integer not null ,
  child_fuel_tax  integer not null ,
  child_tax  integer not null ,
  child_service_fee  numeric(10,2) not null ,
  is_cancelable  boolean not null default false,
  cancel_policy  text ,
  reserve_days  integer,
  is_reaffirm  boolean not null default false,
  status  integer not null default 0,
  operator  integer not null ,
  create_time  timestamptz(6) ,
  operate_time  timestamptz(6),
  flight_no_go  varchar(16) ,
  flight_no_back varchar(16),
  lowest_price integer ,
  start_period varchar(16) ,
  end_period varchar(16),
  line_transfer_days INTEGER not null DEFAULT 0,
  travel_day_num integer,
  exclusive_product boolean not null default false
);

comment on table fh_flight_line is '机票航线舱位表';
comment on column fh_flight_line.supplier_id is '供应商id';
comment on column fh_flight_line.trip_type is  '航程类别（1：单程 2：往返）';
comment on column fh_flight_line.dep_area_go is  '去程出发地';
comment on column fh_flight_line.arr_area_go is   '去程目的地';
comment on column fh_flight_line.dep_area_back is '返程出发地,返程时必填';
comment on column fh_flight_line.arr_area_back is   '返程目的地,返程时必';
comment on column fh_flight_line.transfer_area_go is '程多航段中转地，逗号分隔';
comment on column fh_flight_line.transfer_area_back is '程多航段中转地，逗号分隔';
comment on column fh_flight_line.adult_airport_tax is  '成人机场建设费';
comment on column fh_flight_line.adult_fuel_tax is  '成人燃油税';
comment on column fh_flight_line.adult_tax is  '成人其他税费';
comment on column fh_flight_line.adult_service_fee is '成人服务费';
comment on column fh_flight_line.child_airport_tax is  '儿童机场建设费';
comment on column fh_flight_line.child_fuel_tax is  '儿童燃油税';
comment on column fh_flight_line.child_tax is  '儿童其他税费';
comment on column fh_flight_line.child_service_fee is   '儿童服务费';
comment on column fh_flight_line.is_cancelable is  '是否可以退改';
comment on column fh_flight_line.cancel_policy is  '退改规则';
comment on column fh_flight_line.reserve_days is '前预定天数';
comment on column fh_flight_line.is_reaffirm is  '是否需要二次确认';
comment on column fh_flight_line.status is  '状态位 （-1：删除 0：下架 1：上架）';
comment on column fh_flight_line.operator is  '操作者';
comment on column fh_flight_line.create_time is  '创建时间';
comment on column fh_flight_line.operate_time is  '操作时间';
comment on column fh_flight_line.flight_no_go is  '去程航班号';
comment on column fh_flight_line.flight_no_back is  '回程航班号';
comment on column fh_flight_line.lowest_price is  '最低价';
comment on column fh_flight_line.start_period is  '有效期结束时间';
comment on column fh_flight_line.end_period is  '有效期结束时间';
comment on column fh_flight_line.line_transfer_days IS '航线跨天（从起飞日期到回程时的起飞日期）';
comment on column fh_flight_line.travel_day_num IS '往返天数(单程为空)';
comment on column fh_flight_line.exclusive_product IS '独家政策';