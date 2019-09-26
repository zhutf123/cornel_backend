create table fh_flight_price (
  id serial primary key ,
  flight_id integer not null,
  flight_cabin_id integer not null,
  stock integer not null,
  adult_price integer not null,
  child_price integer,
  selled_count integer not null default 0,
  status integer not null default 1,
  use_date timestamptz(6) not null,
  operate_time  timestamptz(6)
);

create unique index fh_flight_price_id_cabinid_usedate_status_idx
on public.fh_flight_price
  using btree
  (flight_id,flight_cabin_id,use_date,status)
  where status = 1;
comment on index public.fh_flight_price_id_cabinid_usedate_status_idx is 'flightId_flightCabinId_useDate_status唯一索引';


comment on table fh_flight_price is '机票航线报价表';
comment on column fh_flight_price.flight_id is '航线id';
comment on column fh_flight_price.flight_cabin_id is  '舱位id';
comment on column fh_flight_price.use_date is '报价日期';
comment on column fh_flight_price.stock is  '库存';
comment on column fh_flight_price.adult_price is '成人价';
comment on column fh_flight_price.child_price is '儿童价';
comment on column fh_flight_price.selled_count is '已售数';
comment on column fh_flight_price.status is '状态 (0：无效 1：有效)';
comment on column fh_flight_price.operate_time is '操作时间';
