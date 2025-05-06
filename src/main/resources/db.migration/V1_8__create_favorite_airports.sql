create table favorite_airports(
    passenger_id bigint primary key references passenger(id) on delete cascade,
    first_airport varchar(3) references airport(iata),
    second_airport varchar(3) references airport(iata),
    third_airport varchar(3) references airport(iata)
)