Environment : Java 8
Usage :
-Chạy EurekaServer
-Chạy FileServer ( tầm 5 instance gì đó )
-Chạy GateWay

URL xem các instance đang chạy trên Eureka server : localhost:8083

test request : 
get : localhost:8080/test

test websocket :
mở file test.html, bấm connect, sau đó gọi api /test

sql DDL
create table public.ip_address
(
    id             serial
        constraint ip_address_pk
            primary key,
    address        varchar(25) not null,
    created_at     timestamp,
    last_action_at timestamp
);

alter table public.ip_address
    owner to postgres;

create table public.file
(
    id             serial
        constraint file_pk
            primary key,
    uploader_id    integer
        constraint file_ip_address_id_fk
            references public.ip_address,
    size           double precision,
    status         varchar(10),
    created_at     timestamp,
    name           varchar(100),
    download_count integer,
    note           varchar(50),
    path           varchar(100)
);

alter table public.file
    owner to postgres;

create table public.action_log
(
    id      serial
        constraint action_log_pk
            primary key,
    file_id integer
        constraint action_log_file_id_fk
            references public.file,
    action  varchar(50),
    time    timestamp,
    ip_id   integer
        constraint action_log_ip_address_id_fk
            references public.ip_address,
    comment varchar(100),
    state   varchar(10)
);

comment on column public.action_log.ip_id is 'ip_id';

alter table public.action_log
    owner to postgres;

