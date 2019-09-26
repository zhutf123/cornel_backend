

DROP TABLE IF EXISTS fh_order_info_sp CASCADE;
CREATE TABLE fh_order_info_sp(
    id            serial   primary key,
    order_id      character varying(64)        NOT NULL,
    ticket_id     character varying(100)        ,
    qunar_order_id      character varying(100)        NOT NULL,
    dep_supplier_id          int4        NOT NULL,
    dep_flight_id     int4        NOT NULL,
    dep_flight_cabin_id    int4        NOT NULL,
    arr_supplier_id          int4        NOT NULL,
    arr_flight_id     int4        NOT NULL,
    arr_flight_cabin_id    int4        NOT NULL,
    trip_type     int2  NOT NULL,
    adult_num    int4 ,
    child_num    int4 ,
    dep_date	timestamptz 	NOT NULL,
    arr_date	timestamptz 	NOT NULL,
    totle_price     numeric(9)   NOT NULL,
    status      int2  NOT NULL,
    ext_info      text NOT NULL,
    customer_name      character varying(50),
    customer_phone     character varying(20),
    customer_email     character varying(50),
    create_time     timestamptz DEFAULT now() NOT NULL,
    operate_time   timestamptz DEFAULT now() NOT NULL,
    refund_info text 
);
COMMENT ON TABLE public.fh_order_info_sp IS '供应商订单简表';
COMMENT ON COLUMN public.fh_order_info_sp.id IS '自增id';
COMMENT ON COLUMN public.fh_order_info_sp.order_id IS '订单号';
COMMENT ON COLUMN public.fh_order_info_sp.ticket_id IS '票号';
COMMENT ON COLUMN public.fh_order_info_sp.qunar_order_id IS '去哪主单号';
COMMENT ON COLUMN public.fh_order_info_sp.dep_supplier_id IS '去程供应商Id';
COMMENT ON COLUMN public.fh_order_info_sp.dep_flight_id IS '去程航线id';
COMMENT ON COLUMN public.fh_order_info_sp.dep_flight_cabin_id IS '去程航线仓位id';
COMMENT ON COLUMN public.fh_order_info_sp.arr_supplier_id IS '回程供应商Id';
COMMENT ON COLUMN public.fh_order_info_sp.arr_flight_id IS '回程航线id';
COMMENT ON COLUMN public.fh_order_info_sp.arr_flight_cabin_id IS '回程航线仓位id';
COMMENT ON COLUMN public.fh_order_info_sp.trip_type IS '航线类型 （1：单程  2：往返）';
COMMENT ON COLUMN public.fh_order_info_sp.adult_num IS '成人数';
COMMENT ON COLUMN public.fh_order_info_sp.child_num IS '孩童数';
COMMENT ON COLUMN public.fh_order_info_sp.dep_date IS '出发日期';
COMMENT ON COLUMN public.fh_order_info_sp.arr_date IS '返程日期';
COMMENT ON COLUMN public.fh_order_info_sp.totle_price IS '总价';
COMMENT ON COLUMN public.fh_order_info_sp.status IS '订单状态 (1、生单占座，2、订单取消，3、出票，4、退款)';
COMMENT ON COLUMN public.fh_order_info_sp.ext_info IS '生单详细信息';
COMMENT ON COLUMN public.fh_order_info_sp.customer_name IS '顾客姓名';
COMMENT ON COLUMN public.fh_order_info_sp.customer_phone IS '顾客电话';
COMMENT ON COLUMN public.fh_order_info_sp.customer_email IS '顾客邮箱';
COMMENT ON COLUMN public.fh_order_info_sp.create_time IS '创建时间';
COMMENT ON COLUMN public.fh_order_info_sp.operate_time IS '操作时间';
COMMENT ON COLUMN public.fh_order_info_sp.refund_info IS '退款信息';
