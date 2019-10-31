CREATE TABLE role_info (
id serial PRIMARY KEY,
name varchar(40),
role_id varchar(40),
status integer default 1,
ext_info hstore,
authority varchar(40)[],
create_time timestamptz(6) default now(),
operate_time timestamptz(6) default now()
)