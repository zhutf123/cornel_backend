-- 主要的改动
alter table quote_info
	add wet_weight numeric(10,2);

comment on column quote_info.wet_weight is '湿粮重量';

alter table quote_info
	add loan_id varchar(40);

comment on column quote_info.loan_id is '贷款ID ';

alter table quote_info
	add cargo_status int default 1;

comment on column quote_info.cargo_status is '货物状态1现货,2等待货物入库';


create table loan_info
(
	id serial not null,
	loan_id VARCHAR(40),
	price numeric(10,2),
	interest numeric(10,2),
	apply_user_id varchar(40),
	apply_time TIMESTAMP,
	lending_time timestamp,
	est_repay_time timestamp,
	actual_repay_time timestamp,
	interest_total numeric(10,2),
	repay_price numeric(10,2),
	review_user_id varchar(40),
	create_time TIMESTAMP default now(),
	update_time timestamp default now(),
	status int
);

comment on table loan_info is '贷款表';

comment on column loan_info.id is '自增ID';

comment on column loan_info.price is '贷款金额';

comment on column loan_info.interest is '利息';

comment on column loan_info.apply_user_id is '贷款人';

comment on column loan_info.apply_time is '请款日期';

comment on column loan_info.lending_time is '放款日期';

comment on column loan_info.est_repay_time is '预计还款日期';

comment on column loan_info.actual_repay_time is '实际还款日期';

comment on column loan_info.interest_total is '利息总和';

comment on column loan_info.repay_price is '还款金额';

comment on column loan_info.review_user_id is '审批人';

comment on column loan_info.create_time is '创建时间';

comment on column loan_info.update_time is '更新时间';

comment on column loan_info.status is '状态0 已取消 1待审核 2审核通过 3审核拒绝';

create unique index loan_info_id_uindex
	on loan_info (id);

alter table loan_info
	add constraint loan_info_pk
		primary key (id);

alter table quote_info
	add review_opt hstore;