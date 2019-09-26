create table fh_resource (
  id serial primary key ,
  img_url varchar(128) not null ,
  file_name text ,
  source_type integer not null ,
  supplier_id integer not null ,
  file_size bigint not null,
  create_time timestamptz not null
);

comment on table fh_resource is '机酒资源表';
comment on column fh_resource.id is '自增主键';
comment on column fh_resource.img_url is '图片url';
comment on column fh_resource.file_name is '原始文件名';
comment on column fh_resource.source_type is '资源类型(1:机票 2:酒店)';
comment on column fh_resource.supplier_id is '供应商id';
comment on column fh_resource.file_size is '图片大小';
comment on column fh_resource.create_time is '创建时间';

create index fh_resource_img_url_idx
on public.fh_resource
(img_url);
comment on index public.fh_resource_img_url_idx is 'imgUrl索引';