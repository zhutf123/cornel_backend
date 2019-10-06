CREATE TABLE user_acl_info (
id serial PRIMARY KEY,
user_id integer,
acl_id integer,
status integer default 1,
ext_info hstore,
create_time timestamptz(6) default now(),
operate_time timestamptz(6) default now()
)