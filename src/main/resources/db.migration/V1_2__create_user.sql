create table booking_user (
    id  BIGSERIAL primary key,
    email VARCHAR(255) NOT NULL UNIQUE CHECK (email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$'),
    password VARCHAR(255) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    patronymic VARCHAR(100),
    role_id INT NOT NULL REFERENCES role(id) DEFAULT 2,
    last_enter TIMESTAMP,
    blocked BOOLEAN DEFAULT false
);