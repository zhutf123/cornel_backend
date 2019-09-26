CREATE table fh_hotel_room (
  id SERIAL PRIMARY KEY ,
  hotel_id integer not null ,
  room_name varchar(128) not null ,
  include_breakfast boolean default false ,
  room_breakfast integer ,
  room_area integer not null ,
  room_floor integer not null ,
  room_bed_type integer not null ,
  room_person_count integer NOT NULL ,
  room_broadband integer NOT NULL ,
  room_window boolean not null ,
  room_no_smoking boolean not null ,
  room_prepay integer not null ,
  room_img text not null ,
  remark text ,
  can_refund boolean default false ,
  refund_policy text ,
  room_largess text ,
  include_favourable boolean default false,
  room_favourable json ,
  operate_time timestamptz not null default current_timestamp
);

comment on table fh_hotel_room is '酒店房型表';
comment on column fh_hotel_room.id is '自增主键';
comment on column fh_hotel_room.hotel_id is '酒店id,对应于hotel_info的id';
comment on column fh_hotel_room.room_name is '房型名称';
comment on column fh_hotel_room.include_breakfast is '是否含早餐';
comment on column fh_hotel_room.room_breakfast is '早餐份数 (默认如果有值, 大于等于1)';
comment on column fh_hotel_room.room_area is '房间面积';
comment on column fh_hotel_room.room_floor is '房间所在楼层';
comment on column fh_hotel_room.room_bed_type is '床型(0:大床 1:双床 2:大/双床 3:三床 4:一单一双 5:单人床 6:上下铺 7:通铺 8:榻榻米 9:水床 10:圆床 11:拼床)';
comment on column fh_hotel_room.room_person_count is '入住人数（１-10）';
comment on column fh_hotel_room.room_broadband is '宽带(0:无 2:免费 3:收费 4:部分收费 5:部分有且收费 6:部分有且免费 7:部分有且部分收费)';
comment on column fh_hotel_room.room_window is '是否有窗';
comment on column fh_hotel_room.room_no_smoking is '是否禁烟';
comment on column fh_hotel_room.room_prepay is '支付类型(0:预付 1:现付)';
comment on column fh_hotel_room.room_img is '房型图片';
comment on column fh_hotel_room.remark is '说明,描述';
comment on column fh_hotel_room.can_refund is '是否可退款';
comment on column fh_hotel_room.refund_policy is '退款说明';
comment on column fh_hotel_room.room_largess is '赠送';
comment on COLUMN fh_hotel_room.include_favourable is '是否包含连住优惠';
comment on column fh_hotel_room.room_favourable is '连住优惠';
comment on column fh_hotel_room.operate_time is '操作时间';