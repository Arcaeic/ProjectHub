# Creating Tables
Created Thursday 14 September 2017

#### Create Table
	CREATE TABLE Studios (
	name VARCHAR(20),
	website VARCHAR(255)
	);

	CREATE TABLE Movies (
	title VARCHAR(50),
	year INT,
	length INT,
	rating CHAR(2),
	studioname VARCHAR(20)
	);


	CREATE TABLE Stars (
	name VARCHAR(20) Primary Key,
	gender CHAR(1),
	birthyear INT,
	birthplace VARCHAR(40)
	);


