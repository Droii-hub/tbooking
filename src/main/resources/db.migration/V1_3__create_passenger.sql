create table passenger (
    id bigserial primary key,
    user_id bigint not null references booking_user(id),
    surname VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    patronymic VARCHAR(100),
    male boolean default true,
    birth_date date not null,
    passport_series int not null,
    passport_number int not null,
    passport_source varchar(255) not null,
    passport_issue_date date not null
);