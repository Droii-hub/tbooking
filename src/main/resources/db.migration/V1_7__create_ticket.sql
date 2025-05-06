create table ticket(
    flight_id bigint not null references flight(id),
    seat int not null,
    primary key (flight_id, seat),
    service_class_id int not null references service_class(id),
    baggage_allowance varchar(100),
    passenger_id bigint not null references passenger(id)
);