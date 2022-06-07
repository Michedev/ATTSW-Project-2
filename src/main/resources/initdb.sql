CREATE TABLE IF NOT EXISTS Users (
    id serial PRIMARY KEY,
	username varchar(255),
	password varchar(255),
	email varchar(255)
);




CREATE TABLE IF NOT EXISTS Tasks (
    id serial PRIMARY KEY,
	title varchar(255),
	subtask1 varchar(255),
	subtask2 varchar(255),
	subtask3 varchar(255),
	id_user int,
	FOREIGN KEY (id_user) REFERENCES Users (id)
);