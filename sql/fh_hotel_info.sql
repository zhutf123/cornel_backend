create table fh_hotel_info (
  id             serial primary key ,
  supplier_id    integer      not null ,
  hotel_city     varchar(64)  not null ,
  hotel_seq      varchar(64)  not null ,
  hotel_name     varchar(128) not null ,
  hotel_address  text         not null ,
  hotel_tel      varchar(16)  not null ,
  hotel_star     integer ,
  hotel_grade integer not null ,
  hotel_landmark varchar(128) ,
  status         smallint     not null default 0 ,
  create_time    timestamptz  not null ,
  operate_time   timestamptz  not null default current_timestamp ,
  operator       integer      not null ,
  lowest_price   integer ,
  start_period   varchar(16) ,
  end_period     varchar(16) ,
  exclusive_product boolean not null default false
);

comment on table fh_hotel_info is '酒店信息表';
comment on column fh_hotel_info.id is '自增主键';
comment on column fh_hotel_info.supplier_id is '供应商Id';
comment on column fh_hotel_info.hotel_city is '酒店所在城市';
comment on column fh_hotel_info.hotel_seq is '酒店sequence';
comment on column fh_hotel_info.hotel_name is '酒店名称';
comment on column fh_hotel_info.hotel_address is '酒店地址';
comment on column fh_hotel_info.hotel_tel is '酒店电话';
comment on column fh_hotel_info.hotel_star is '酒店星级';
comment on column fh_hotel_info.hotel_grade is '酒店档次';
comment on column fh_hotel_info.hotel_landmark is '酒店地标';
comment on column fh_hotel_info.status is '状态(-1: 删除 0:下架 1:上架)';
comment on column fh_hotel_info.create_time is '创建时间';
comment on column fh_hotel_info.operate_time is '操作时间';
comment on column fh_hotel_info.operator is '操作者qunarId';
comment on column fh_hotel_info.lowest_price is '最低报价';
comment on column fh_hotel_info.start_period is '有效期开始时间';
comment on column fh_hotel_info.end_period is '有效期结束时间';
comment on column fh_hotel_info.exclusive_product is '独家酒店';