create table sub_task
(
	id serial not null,
	taskId varchar(40),
	start_time timestamp,
	end_time timestamp,
	lorry_num int,
	status int default 1,
	undist_num int,
	sub_task_id varchar(40)
);
