alter table system_quote add target_user_id varchar(40) [];

alter table quote_info add create_time timestamp default now();

comment on column quote_info.create_time is '下单时间';
alter table quote_info add review_user varchar(40);
comment on column quote_info.review_user is '审核人员ID';
