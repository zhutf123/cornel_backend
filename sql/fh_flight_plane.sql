create table fh_flight_plane (
  id serial PRIMARY key,
  plane_type varchar(16) not null,
  plane_name varchar(16) not null,
  status integer not null default 1,
  create_time timestamptz(6)
);

--数据量太小，直接全表扫描，不需要建立索引

comment on table fh_flight_plane is '机型对照表';
comment on column fh_flight_plane.plane_type is '机型代码';
comment on column fh_flight_plane.plane_name is '机型名称';
comment on column fh_flight_plane.status is '有效状态(0: 无效 1：有效)';

--初始化数据
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('319','空客319',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('33V','A330全商务舱客机',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('744','波音747',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('MA6','新舟60',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('346','空客340',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('763','波音767',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('336','空客330',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('735','波音737',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('330','空客330',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('333','空客330',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('332','空客330',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('E90','ERJ190',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('320A','空客320',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('757','波音757',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('ERJ','ERJ',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('73D','波音737',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('77W','波音777',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('JET','机型未定',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('33B','空客A330',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('75B','波音757',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('747','波音747',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('320','空客A320',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('32I','空客A320',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('323','空客320',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('733','波音737',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('321','空客A321',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('77A','波音777',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('340','空客340',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('737','波音737',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('325','空客320',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('739','波音737',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('738','波音737',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('767','波音767',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('32H','空客320',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('388','空客A380',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('734','波音737',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('73A','波音737',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('CR2','庞巴迪CRJ-200',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('73C','波音737',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('32A','空客320',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('73E','波音737',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('32G','空客320',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('73G','波音737',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('772','波音777',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('73H','波音737',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('73K','波音737',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('73J','波音737',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('73L','波音737',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('190','EMB-190',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('380','空客380',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('32S','空客320',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('752','波音757',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('73T','波音737',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('77L','波音777',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('773','波音777',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('32Z','空客320',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('32F','空客320',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('331','空客330',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('777','波音777',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('736','波音737',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('787','波音787',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('CR7','庞巴迪CRJ700',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('M90','MD90(麦道90)',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('CR9','庞巴迪CRJ900',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('788','波音787',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('76W','波音767',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('74E','波音747',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('33E','空客330',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('343','空客340',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('76A','波音767',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('33A','空客330',1,'2015-11-02 12:00');
insert into fh_flight_plane (plane_type,plane_name,status,create_time) values ('76E','波音767',1,'2015-11-02 12:00');