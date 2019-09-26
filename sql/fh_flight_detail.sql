create table fh_flight_detail (
  id serial primary key ,
  flight_id integer not null ,
  flight_type integer not null default 0,
  seq integer not null default 1,
  transfer_days integer,
  flight_no varchar(16)not null ,
  dep_area varchar(64)not null ,
  arr_area varchar(64)not null ,
  dep_time varchar(16) not null ,
  arr_time varchar(16)not null ,
  dep_airport varchar(128) not null ,
  dep_airport_code varchar(4)not null ,
  dep_terminal varchar(128),
  arr_airport varchar(128)not null ,
  arr_airport_code varchar(4)not null ,
  arr_terminal varchar(128),
  air_line varchar(128)not null ,
  plane_type varchar(64)not null ,
  flight_duration varchar(16),
  is_stopover boolean not null default false ,
  stopover_airport varchar(128),
  stop_time_hour integer,
  stop_time_minute integer,
  status integer not null default 1,
  operate_time timestamp(6)
);

create unique index fh_flight_detail_id_type_seq_status_idx
on public.fh_flight_detail
  using btree
  (flight_id,flight_type,seq,status)
  where status = 1;
comment on index public.fh_flight_detail_id_type_seq_status_idx is 'flightId_flightType_seq_status唯一索引';

comment on table fh_flight_detail is '机票航线舱位表';
comment on column fh_flight_detail.flight_id is '航线id';
comment on column fh_flight_detail.flight_type is '航程类别（1：单程 2：往返）';
comment on column fh_flight_detail.flight_type is '去程 1： 回程 （默认是单程）';
comment on column fh_flight_detail.seq is '第几班中转航班（默认没有中转，则为1）';
comment on column fh_flight_detail.transfer_days is '跨天 （0：当日 1：次日 2：2日 3:3日）';
comment on column fh_flight_detail.flight_no is '航班号';
comment on column fh_flight_detail.dep_area is '出发地';
comment on column fh_flight_detail.arr_area is '目的地';
comment on column fh_flight_detail.dep_time is '起飞时间（格式 HH：mm）';
comment on column fh_flight_detail.arr_time is '到达时间（格式 HH：mm）';
comment on column fh_flight_detail.dep_airport is '起飞机场';
comment on column fh_flight_detail.dep_airport_code is '起飞机场三字码';
comment on column fh_flight_detail.dep_terminal is '起飞航站楼';
comment on column fh_flight_detail.arr_airport is '到达机场';
comment on column fh_flight_detail.arr_airport_code is '到达机场三字码';
comment on column fh_flight_detail.arr_terminal is '到达机场航站楼';
comment on column fh_flight_detail.air_line is '航司';
comment on column fh_flight_detail.plane_type is '机型';
comment on column fh_flight_detail.flight_duration is '飞行时长(单位：分钟)';
comment on column fh_flight_detail.is_stopover is '是否经停';
comment on column fh_flight_detail.stopover_airport is '经停机场(有经停时必填)';
comment on column fh_flight_detail.stop_time_hour is '经停时长 - 小时';
comment on column fh_flight_detail.stop_time_minute is '经停时长 - 分钟';
comment on column fh_flight_detail.status is '有效状态 （0：无效 1：有效）';
comment on column fh_flight_detail.operate_time  is '操作时间';