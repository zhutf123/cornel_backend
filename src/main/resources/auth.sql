create table notify_info
(
  id serial not null,
  task_id varchar(40) not null,
  job_no int,
  job_status int default 0,
  operation_time json,
  create_time timestamptz(6) default now(),
  expire_time timestamptz(6) default now(),
  order_id varchar(40)
);