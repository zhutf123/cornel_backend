CREATE table fh_hotel_price (
  id SERIAL PRIMARY KEY ,
  hotel_id integer not null ,
  room_id integer not null ,
  use_date timestamptz not null ,
  status smallint default 1 ,
  price integer not null default 0 ,
  stock integer not null ,
  selled_count integer not null default 0,
  operate_time timestamptz not null default current_timestamp
);

comment on table fh_hotel_price is '酒店房型表';
comment on column fh_hotel_price.id is '自增主键';
comment on column fh_hotel_price.hotel_id is '关联酒店Id';
comment on column fh_hotel_price.room_id is '关联房型Id';
comment on column fh_hotel_price.status is '有效状态(0:无效 1:有效)';
comment on column fh_hotel_price.use_date is '售卖日期';
comment on column fh_hotel_price.price is '报价';
comment on column fh_hotel_price.stock is '库存';
comment on column fh_hotel_price.selled_count is '已售数';
comment on column fh_hotel_price.operate_time is '操作时间';