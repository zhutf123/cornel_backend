-- auto-generated definition
create table buyer_info
(
    id                  serial      not null
        constraint buyer_info_pk
            primary key,
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

-- 货品信息表
create table cargo_info
(
    id              serial not null
        constraint cargo_id_pk
            primary key,
    weight          numeric(10, 2),
    unit_weight     varchar(5),
    location_id     varchar(40),
    parent_cargo_id varchar(40),
    deal_time       timestamp,
    price           numeric(10, 2),
    commodity_id    varchar(40),
    status          integer,
    store_id        varchar(40),
    cargo_id        varchar(40)
);

comment on table cargo_info is '货品信息表';

comment on column cargo_info.id is '自增ID';

comment on column cargo_info.weight is '重量';

comment on column cargo_info.unit_weight is '单位';

comment on column cargo_info.location_id is '货物当前位置';

comment on column cargo_info.parent_cargo_id is '父货物ID';

comment on column cargo_info.deal_time is '成交时间';

comment on column cargo_info.price is '成交单价';

comment on column cargo_info.commodity_id is '商品ID';

comment on column cargo_info.status is '状态';

comment on column cargo_info.store_id is '从个库存来的';


create unique index cargo_id_id_uindex
    on cargo_info (id);

-- 公司信息表
create table company_info
(
    id           serial       not null
        constraint company_info_pk
            primary key,
    company_id   varchar(40)  not null,
    company_name varchar(200) not null,
    create_time  timestamp default now(),
    update_time  timestamp,
    status       integer   default 1,
    contact_user varchar(40),
    contact_tel  varchar(20)[]
);

comment on table company_info is '公司信息';

comment on column company_info.id is '自增ID';

comment on column company_info.company_id is '公司ID';

comment on column company_info.company_name is '公司名称';

comment on column company_info.create_time is '创建时间';

comment on column company_info.update_time is '更新时间';

comment on column company_info.status is '状态 1 有效 0无效';

comment on column company_info.contact_user is '联系人';

comment on column company_info.contact_tel is '联系电话';


create unique index company_info_id_uindex
    on company_info (id);

-- 地理位置表
create table location_info
(
    id              serial not null
        constraint location_info_pk
            primary key,
    location_id     varchar(40),
    location        varchar(200),
    location_gis    varchar(30),
    status          integer,
    location_area   varchar(200),
    location_detail varchar(200)
);

comment on table location_info is '地理位置表';

comment on column location_info.id is '位置ID';

comment on column location_info.location is '位置';

comment on column location_info.location_gis is '经纬度';


create unique index location_info_id_uindex
    on location_info (id);

create unique index location_info_location_location_area_location_detail_uindex
    on location_info (location, location_area, location_detail);
-- 系统报价表
create table offer_sheet
(
    id             serial      not null
        constraint offer_sheet_pk
            primary key,
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

alter table offer_sheet
	add target_user_id varchar(40);

comment on column offer_sheet.target_user_id is '针对特定用户的报价，如果为空则是全局报价 否则是针对特定买家的报价';


-- 求购表
create table purchase_info
(
    id                  serial not null
        constraint purchase_info_pk
            primary key,
    commodity_id        varchar(40),
    buyer_id            varchar(40),
    price               numeric(10, 2),
    weight              numeric(10, 2),
    unit_price          varchar(5) default '元'::character varying,
    unit_weight         varchar(5) default '吨'::character varying,
    receive_location_id varchar(40),
    receive_start_time  timestamp,
    receive_end_time    timestamp,
    status              smallint,
    create_time         timestamp  default now(),
    contact_user_name   varchar(20),
    mobile              varchar(20),
    purchase_id         varchar(40)
);

comment on table purchase_info is '求购信息表';

comment on column purchase_info.id is '自增ID';

comment on column purchase_info.commodity_id is '求购商品';

comment on column purchase_info.buyer_id is '买家ID';

comment on column purchase_info.price is '单价';

comment on column purchase_info.weight is '求购数量';

comment on column purchase_info.unit_price is '价格单位';

comment on column purchase_info.unit_weight is '重量单位';

comment on column purchase_info.receive_location_id is '收货地址';

comment on column purchase_info.receive_start_time is '收货起始时间';

comment on column purchase_info.receive_end_time is '收货截止时间';

comment on column purchase_info.status is '0 无效 1有效';

comment on column purchase_info.create_time is '创建时间';

comment on column purchase_info.contact_user_name is '联系人';

comment on column purchase_info.mobile is '联系电话';

comment on column purchase_info.purchase_id is '求购ID';


create unique index purchase_info_id_uindex
    on purchase_info (id);


-- 售卖订单表
-- auto-generated definition
create table sale_order
(
    id                 serial not null
        constraint sell_order_pk
            primary key,
    status             integer,
    cargo_id           varchar(40),
    buyer_id           varchar(40),
    order_time         timestamp default now(),
    receive_start_time timestamp,
    payment_type       varchar(40),
    payment_status     integer,
    freight_price      numeric(10, 2),
    commodity_price    numeric(10, 2),
    order_price        numeric(10, 2),
    unit_price         varchar(5),
    weight             numeric(10, 2),
    unit_weight        varchar(5),
    waybill_id         varchar(40)[],
    purchase_id        varchar(40),
    contact_user_name  varchar(20),
    mobile             varchar(20),
    receive_location   varchar(20),
    from_location      varchar(20),
    order_id           varchar(40),
    offer_id           varchar(40),
    receive_end_time   timestamp
);

comment on table sale_order is '订单表';

comment on column sale_order.id is '自增ID';

comment on column sale_order.status is '订单状态';

comment on column sale_order.cargo_id is '货品ID';

comment on column sale_order.buyer_id is '买家ID';

comment on column sale_order.order_time is '下单时间';

comment on column sale_order.receive_start_time is '预计送达起始时间';

comment on column sale_order.payment_type is '付款方式';

comment on column sale_order.payment_status is '付款状态 0 待支付 1支付完成';

comment on column sale_order.freight_price is '运费';

comment on column sale_order.commodity_price is '商品价格';

comment on column sale_order.order_price is '订单的总金额=freigh_price+commodity_price';

comment on column sale_order.unit_price is '价格单位';

comment on column sale_order.weight is '重量';

comment on column sale_order.unit_weight is '重量单位';

comment on column sale_order.waybill_id is '运单列表';

comment on column sale_order.purchase_id is '对应的求购信息 为空则说明是购买的系统挂出的订单';

comment on column sale_order.contact_user_name is '联系人';

comment on column sale_order.mobile is '联系电话';

comment on column sale_order.receive_location is '收货地址';

comment on column sale_order.from_location is '发货地';

comment on column sale_order.order_id is '订单ID';

comment on column sale_order.offer_id is '报价ID';

comment on column sale_order.receive_end_time is '预计收货截止时间';


create unique index sell_order_id_uindex
    on sale_order (id);


-- 运输方式表
create table transport_type
(
    id             serial not null,
    transport_type varchar(20),
    transport_id   varchar(40),
    status         smallint default 1
);

comment on table transport_type is '运输方式表';

comment on column transport_type.id is '自增ID';

comment on column transport_type.transport_type is '运输方式 货运、空运、水运';

comment on column transport_type.transport_id is 'uuid';

comment on column transport_type.status is '0 无效 1有效';


create unique index transport_type_id_uindex
    on transport_type (id);

-- auto-generated definition
create table waybill_info
(
    id               serial not null,
    carrier_id       varchar(40),
    transport_id     varchar(40),
    from_location_id varchar(40),
    to_location_id   varchar(40),
    start_time       timestamp,
    end_time         timestamp,
    weight           numeric(10, 2),
    unit_weight      varchar(5),
    price            numeric(10, 2),
    unit_price       varchar(5),
    cargo_id         varchar(40),
    parent_cargo_id  varchar(40),
    status           integer,
    update_time      timestamp
);

comment on table waybill_info is '运单信息';

comment on column waybill_info.id is '自增ID';

comment on column waybill_info.carrier_id is '承运人ID';

comment on column waybill_info.transport_id is '运输方式';

comment on column waybill_info.from_location_id is '起始地址';

comment on column waybill_info.to_location_id is '终止地址';

comment on column waybill_info.start_time is '开始运输时间';

comment on column waybill_info.end_time is '接货时间';

comment on column waybill_info.weight is '货物重量';

comment on column waybill_info.unit_weight is '重量单位';

comment on column waybill_info.price is '运费';

comment on column waybill_info.unit_price is '价格单位';

comment on column waybill_info.cargo_id is '货物ID';

comment on column waybill_info.parent_cargo_id is '父货物ID';

comment on column waybill_info.status is '状态 0待运输 1运输中 2 运输完成 3 取消';

comment on column waybill_info.update_time is '更新时间';


create unique index waybill_info_id_uindex
    on waybill_info (id);

