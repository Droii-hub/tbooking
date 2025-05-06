create table airport(
    iata    varchar(3) primary key CHECK (iata ~ '^[A-Z]{3}$'),
    name    varchar(100) not null,
    location    varchar(255) not null
);