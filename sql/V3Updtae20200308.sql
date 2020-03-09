
    alter table quote_info add create_time timestamp default now();
    comment on column quote_info.create_time is '下单时间';

alter table quote_info add review_user varchar(40);

alter table quote_infoadd warehouse_time timestamp;
comment on column quote_info.review_user is '审核人员ID';

comment on column quote_info.warehouse_time is '烘干塔货物入库时间';


-- auto-generated definition
create table admin_user
(
    id        serial      not null,
    user_id   varchar(40) not null,
    user_name varchar(20),
    mobile    varchar(15)[],
    status    integer default 1
);

comment on column admin_user.id is '自增ID';

comment on column admin_user.user_id is 'userID';

comment on column admin_user.user_name is '名字';

comment on column admin_user.mobile is '手机号';

comment on column admin_user.status is '状态0 无效 1有效';


create unique index admin_user_id_uindex
    on admin_user (id);

alter table quote_info
	add warehouse_time timestamp;
comment on column quote_info.warehouse_time is '货物入库时间';


alter table system_quote
	add notice varchar(200) [];
	comment on column system_quote.notice is '报价注意事项';

-- auto-generated definition
create table special_quote
(
    id                 serial not null,
    quote_user_id       varchar(40),
    quote_id            varchar(40),
    create_time         timestamp   default now(),
    update_time         timestamp   default now(),
    status              integer     default 1,
    target_user_id      varchar(40),
    target_tower_id     varchar(40),
    commodity_id        varchar(40),
    unit_price          varchar(10) default '元'::character varying,
    unit_weight         varchar(10),
    min_shipment_weight numeric(10, 2),
    quote               numeric(10, 2)
);

comment on table special_quote is '单独定价表';

comment on column special_quote.id is ' 自增ID';

comment on column special_quote.quote_user_id is '报价人';

comment on column special_quote.quote_id is '报价ID';

comment on column special_quote.create_time is '创建时间';

comment on column special_quote.update_time is '更新时间';

comment on column special_quote.status is '状态0 无效 1有效a';

comment on column special_quote.target_tower_id is '烘干塔ID';

comment on column special_quote.commodity_id is '商品ID';

comment on column special_quote.unit_price is '价格单位';

comment on column special_quote.unit_weight is '重量单位';

comment on column special_quote.min_shipment_weight is '最小出货量';

comment on column special_quote.quote is '价格';



