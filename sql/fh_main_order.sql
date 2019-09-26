alter table main_order add column from_wishorder boolean;
alter table main_order add column from_wishorder_id integer ;
alter table main_order add column origin_main_order_id integer ;
alter table main_order add column origin_display_id varchar(256) ;

comment on column main_order.from_wishorder is '是否由意向单生成';
comment on column main_order.from_wishorder_id is '意向单Id';
comment on column main_order.origin_main_order_id is '原失败订单Id';
comment on column main_order.origin_display_id is '原失败订单display_id';

alter table flight_order add column resource_id integer;
alter table hotel_order add column resource_id integer;
alter table hotel_order add column person_per_room integer;
comment on column flight_order.resource_id is '关联意向单资源Id';
comment on column hotel_order.resource_id is '关联意向单资源Id';
comment on column hotel_order.person_per_room IS '房间容纳人数';