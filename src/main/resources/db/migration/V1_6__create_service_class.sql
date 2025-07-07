create table service_class(
    id int primary key,
    class varchar(100) not null
);

insert into service_class values
(1, 'Первый'),
(2, 'Бизнес'),
(3, 'Эконом');