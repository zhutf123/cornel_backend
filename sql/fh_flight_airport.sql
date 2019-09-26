create table fh_flight_airport (
  id serial primary key ,
  airport varchar(128) not null,
  airport_code varchar(4) not null,
	airport_english_name varchar(128) not null,
  city varchar(128) not null,
  city_code varchar(4) not null,
  city_english_name varchar(128) not null,
  parent_name varchar(64) not null,
  continent varchar(64) not null
);

comment on table fh_flight_airport is '机酒机场对照表';
comment on column fh_flight_airport.airport is '机场名';
comment on column fh_flight_airport.airport_code is '机场三字码';
comment on column fh_flight_airport.airport_code is '机场英文名';
comment on column fh_flight_airport.airport is '城市名';
comment on column fh_flight_airport.airport is '城市三字码';
comment on column fh_flight_airport.airport_code is '城市英文名';
comment on column fh_flight_airport.parent_name is '父地区';
comment on column fh_flight_airport.continent is '国家/大洲';

--初始化数据见pmo325

