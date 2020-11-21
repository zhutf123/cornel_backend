-- 照片信息表
DROP TABLE IF EXISTS "img_list";
create table img_list
(
	id serial not null,
	img_id varchar(40),
	img_desc varchar(50),
	status int,
	bind_id varchar(40),
	bind_type int,
	create_time timestamp default now(),
	url varchar(100)
);

comment on column img_list.id is '自增ID';

comment on column img_list.img_id is '图片ID';

comment on column img_list.img_desc is '图片描述';

comment on column img_list.status is '图片状态 0删除 1使用中';

comment on column img_list.bind_id is '绑定ID 绑定的车或者人';

comment on column img_list.bind_type is '绑定的车或者人 0 是车辆有关的照片 1是人有关的照片';

comment on column img_list.create_time is '上传时间';

comment on column img_list.url is '图片地址';

create unique index img_list_id_uindex on img_list (id);
create index img_list_type_id_desc on img_list (bind_type,bind_id,img_desc);

alter table img_list
	add constraint img_list_type_id_desc_pk
		unique (bind_type,bind_id,img_desc);


-- 货车类型表
DROP TABLE IF EXISTS "car_type";
create table car_type
(
	id serial not null,
	name varchar(20),
	car_desc varchar(200),
	create_time timestamp default now(),
	status int default 1
);

comment on table car_type is '车型表';

comment on column car_type.id is '自增ID';

comment on column car_type.name is '名称';

comment on column car_type.desc is '描述';

comment on column car_type.create_time is '创建时间';

comment on column car_type.status is '1 可用 0不可用';
-- 修改车辆表
alter table lorry_info add car_lice_owner varchar(20);
comment on column lorry_info.car_lice_owner is '车辆行驶证归属人 本人、他人、公司';
alter table lorry_info add engine_no varchar(40);
comment on column lorry_info.engine_no is '发动机编号';
alter table lorry_info add user_id varchar(40);
comment on column lorry_info.user_id is '用户ID';
alter table lorry_info add lorry_id varchar(40);
comment on column lorry_info.lorry_id is '车辆ID';

alter table order_info alter column lorry_id type varchar(50) using lorry_id::varchar(50);