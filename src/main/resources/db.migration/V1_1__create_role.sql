create table role(
    id BIGINT PRIMARY KEY,
    role VARCHAR(100) NOT NULL
);

insert into role values
(1, 'Администратор'),
(2, 'Пользователь');