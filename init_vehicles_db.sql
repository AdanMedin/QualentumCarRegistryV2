drop database if exists vehicles_db;

create database vehicles_db;

use vehicles_db;

create table brand (
id int primary key not null AUTO_INCREMENT,
name varchar(100) not null,
warranty int not null,
country varchar(100) not null
);

create table vehicle (
id int primary key not null AUTO_INCREMENT,
brand_id int not null,
model varchar(100) not null,
mileage int not null,
price double not null,
year int not null,
description varchar(300) not null,
colour varchar(50) not null,
fuel_type varchar(20) not null,
num_doors int not null,
constraint fk_brand_id_brand foreign key (brand_id) references brand (id) on delete cascade on update cascade
);