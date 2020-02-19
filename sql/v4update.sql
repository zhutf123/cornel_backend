-- 买家表
create table buyer_info
(
    id                  serial      not null,
    user_id             varchar(40) not null,
    user_name           varchar(20),
    id_card             varchar(20),
    id_type             smallint  default 0,
    mobile              varchar(20)[],
    company_id          varchar(40),
    create_time         timestamp default now(),
    update_time         timestamp default now(),
    frequently_location varchar(40)[],
    status              integer,
    default_location    varchar(40)
);

comment on table buyer_info is '买家信息';

comment on column buyer_info.id is '自增ID';

comment on column buyer_info.user_id is 'userid';

comment on column buyer_info.user_name is '用户名';

comment on column buyer_info.id_card is '身份证号';

comment on column buyer_info.id_type is '证件类型 0 身份证';

comment on column buyer_info.mobile is '手机号';

comment on column buyer_info.company_id is '公司ID';

comment on column buyer_info.create_time is '创建时间';

comment on column buyer_info.update_time is '更新时间';

comment on column buyer_info.frequently_location is '常用地址';

comment on column buyer_info.status is '0 无效 1有效';

comment on column buyer_info.default_location is '默认收货地址';

create unique index buyer_info_id_uindex
    on buyer_info (id);

-- 系统报价表
create table offer_sheet
(
    id             serial      not null,
    commodity_id   varchar(40) not null,
    location_id    varchar(40),
    price          numeric(10, 2),
    unit_price     varchar(5) default '元/吨'::character varying,
    status         smallint,
    create_time    timestamp,
    offer_id       varchar(40),
    notice         varchar(200)[],
    min_buy_weight numeric(10, 2),
    unit_weight    varchar(5)
);

comment on table offer_sheet is '系统对买家的报价';

comment on column offer_sheet.id is '自增ID';

comment on column offer_sheet.commodity_id is '商品ID';

comment on column offer_sheet.location_id is '发货地';

comment on column offer_sheet.price is '价格';

comment on column offer_sheet.unit_price is '价格单位元/吨';

comment on column offer_sheet.status is '0无效 1有效';

comment on column offer_sheet.create_time is '创建时间';

comment on column offer_sheet.offer_id is '报价ID';

comment on column offer_sheet.notice is '注意事项';

comment on column offer_sheet.unit_weight is '重量单位';

create unique index offer_sheet_id_uindex
    on offer_sheet (id);



