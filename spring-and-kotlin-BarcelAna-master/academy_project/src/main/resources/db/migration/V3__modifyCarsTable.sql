alter table cars
drop column manufacturer;

alter table cars
drop column model;

alter table cars
add car_model_id uuid;