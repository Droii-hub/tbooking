create table flight(
    id bigserial primary key,
    departure_airport varchar(3) not null references airport(iata),
    departure_time timestamp not null,
    arrival_airport varchar(3) not null references airport(iata),
    arrival_time timestamp not null,
    available_seats int not null,
    total_seats int not null,
    check (available_seats<=total_seats)
);