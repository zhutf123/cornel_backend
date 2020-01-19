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
	url varchar(50)
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


