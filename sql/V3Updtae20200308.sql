alter table system_quote add target_user_id varchar(40) [];

alter table quote_info add create_time timestamp default now();

comment on column quote_info.create_time is '下单时间';
alter table quote_info add review_user varchar(40);
comment on column quote_info.review_user is '审核人员ID';
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

alter table admin_user
    owner to postgres;

create unique index admin_user_id_uindex
    on admin_user (id);

alter table quote_info
	add warehouse_time timestamp;
comment on column quote_info.warehouse_time is '货物入库时间';

