create table cars(id uuid primary key, manufacturer text NOT NULL, model text NOT NULL, production_year int NOT NULL, vin text unique NOT NULL, added_at date NOT NULL);

create table check_ups(id uuid primary key, worker_name text not null, price real NOT NULL, performed_at timestamp NOT NULL, car_id uuid NOT NULL references cars(id));