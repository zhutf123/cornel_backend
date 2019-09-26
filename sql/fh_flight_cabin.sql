create table fh_flight_cabin (
  id serial primary key ,
  flight_id integer not null,
  cabin_code varchar(16) not null,
  cabin_type varchar(16) not null,
  create_time timestamptz(6) not null,
  operate_time timestamptz(6) not null
);

comment on table fh_flight_cabin is '机票航线舱位表';
comment on column fh_flight_cabin.flight_id is '航线Id';
comment on column fh_flight_cabin.cabin_code is  '航程代号';
comment on column fh_flight_cabin.cabin_type is  '舱位类型';