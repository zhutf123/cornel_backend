create table fh_wish_resource (
  id serial primary key ,
  wish_order_id integer not null ,
  amount bigint not null ,
  origin_order_id integer,
  res_type integer not null ,
  res_seq varchar(64) not null ,
  res_status integer not null default 1 ,
  res_info json not null ,
  checked boolean default false,
  change_price float ,
  create_time timestamptz(6) not null
);

comment on table fh_wish_resource is '意向单资源信息表';
comment on column fh_wish_resource.wish_order_id is '意向单id';
comment on column fh_wish_resource.amount is '资源价格';
comment on column fh_wish_resource.origin_order_id is '对应的原始资源订单号 (机票没有原订单Id)';
comment on column fh_wish_resource.res_type is '资源类型。1:机票 2:酒店';
comment on column fh_wish_resource.res_seq is '资源序列 对于机票（1去程 2回程） 对于酒店 hotel_seq';
comment on column fh_wish_resource.res_status is '资源状态  （-1:删除 1:正常 2:变价 3:售罄 4:失败）';
comment on column fh_wish_resource.res_info is '资源信息';
comment on column fh_wish_resource.checked is '一键生单时是否选择了该资源';
comment on column fh_wish_resource.change_price is '资源变价数目(不是变价时为空)';
comment on column fh_wish_resource.create_time is '创建时间';
