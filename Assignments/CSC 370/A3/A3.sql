/* Jory Anderson V00843894
 * CSC 370 A3 - Fall 2017
 */

--Q1
CREATE TABLE Classes(
	class VARCHAR(50),
	type1 CHAR(2),
	country VARCHAR(30),
	numGuns INTEGER,
	bore INTEGER,
	displacement INTEGER
);

CREATE TABLE Ships(
	name VARCHAR(50),
	class VARCHAR(50),
	launched INTEGER
);

CREATE TABLE Battles(
	name VARCHAR(50),
	date_fought VARCHAR(20)
);

CREATE TABLE Outcomes(
	ship1 VARCHAR(50),
	battle VARCHAR(30),
	result1 VARCHAR(10)
);

INSERT INTO Classes VALUES
	('Bismarck','bb','Germany',8,15,42000);
INSERT INTO Classes VALUES
	('Kongo','bc','Japan',8,14,32000);
INSERT INTO Classes VALUES
	('North Carolina','bb','USA',9,16,37000);
INSERT INTO Classes VALUES
	('Renown','bc','Gt. Britain',6,15,32000);
INSERT INTO Classes VALUES
	('Revenge','bb','Gt. Britain',8,15,29000);
INSERT INTO Classes VALUES
	('Tennessee','bb','USA',12,14,32000);
INSERT INTO Classes VALUES
	('Yamato','bb','Japan',9,18,65000);
INSERT INTO Ships VALUES
	('California','Tennessee',1921);
INSERT INTO Ships VALUES
	('Haruna','Kongo',1915);
INSERT INTO Ships VALUES
	('Hiei','Kongo',1914);
INSERT INTO Ships VALUES
	('Iowa','Iowa',1943);
INSERT INTO Ships VALUES
	('Kirishima','Kongo',1914);
INSERT INTO Ships VALUES
	('Kongo','Kongo',1913);
INSERT INTO Ships VALUES
	('Missouri','Iowa',1944);
INSERT INTO Ships VALUES
	('Musashi','Yamato',1942);
INSERT INTO Ships VALUES
	('New Jersey','Iowa',1943);
INSERT INTO Ships VALUES
	('North Carolina','North Carolina',1941);
INSERT INTO Ships VALUES
	('Ramilles','Revenge',1917);
INSERT INTO Ships VALUES
	('Renown','Renown',1916);
INSERT INTO Ships VALUES
	('Repulse','Renown',1916);
INSERT INTO Ships VALUES
	('Resolution','Revenge',1916);
INSERT INTO Ships VALUES
	('Revenge','Revenge',1916);
INSERT INTO Ships VALUES
	('Royal Oak','Revenge',1916);
INSERT INTO Ships VALUES
	('Royal Sovereign','Revenge',1916);
INSERT INTO Ships VALUES
	('Tennessee','Tennessee',1920);
INSERT INTO Ships VALUES
	('Washington','North Carolina',1941);
INSERT INTO Ships VALUES
	('Wisconsin','Iowa',1944);
INSERT INTO Ships VALUES
	('Yamato','Yamato',1941);
INSERT INTO Battles VALUES
	('North Atlantic','27-May-1941');
INSERT INTO Battles VALUES
	('Guadalcanal','15-Nov-1942');
INSERT INTO Battles VALUES
	('Surigao Strait','25-Oct-1944');
INSERT INTO Battles VALUES
	('North Cape','26-Dec-1943');
INSERT INTO Outcomes VALUES
	('Bismarck','North Atlantic', 'sunk');
INSERT INTO Outcomes VALUES
	('California','Surigao Strait', 'ok');
INSERT INTO Outcomes VALUES
	('Duke of York','North Cape', 'ok');
INSERT INTO Outcomes VALUES
	('Fuso','Surigao Strait', 'sunk');
INSERT INTO Outcomes VALUES
	('Hood','North Atlantic', 'sunk');
INSERT INTO Outcomes VALUES
	('King George V','North Atlantic', 'ok');
INSERT INTO Outcomes VALUES
	('Kirishima','Guadalcanal', 'sunk');
INSERT INTO Outcomes VALUES
	('Prince of Wales','North Atlantic', 'damaged');
INSERT INTO Outcomes VALUES
	('Rodney','North Atlantic', 'ok');
INSERT INTO Outcomes VALUES
	('Scharnhorst','North Cape', 'sunk');
INSERT INTO Outcomes VALUES
	('South Dakota','Guadalcanal', 'ok');
INSERT INTO Outcomes VALUES
	('West Virginia','Surigao Strait', 'ok');
INSERT INTO Outcomes VALUES
	('Yamashiro','Surigao Strait', 'sunk');
	
	
--Q2.1	
SELECT Ships."NAME" 
FROM Ships, CLASSES
WHERE Ships."CLASS" = Classes."CLASS" AND Classes.DISPLACEMENT>3500 AND Ships.LAUNCHED >= 1921;

--Q2.2
SELECT Outcomes."SHIP1", Classes."DISPLACEMENT", Classes."NUMGUNS"
FROM Outcomes FULL OUTER JOIN Ships ON Outcomes.SHIP1 = Ships.NAME FULL OUTER JOIN Classes USING ("CLASS")
WHERE Outcomes."BATTLE" = 'Guadalcanal';

--Q2.3
SELECT NAME FROM Ships
UNION
SELECT SHIP1 FROM Outcomes;

--Q2.4
SELECT t1.COUNTRY FROM 
	(SELECT COUNTRY, count(type1) FROM CLASSES
	WHERE type1 = 'bb'
	GROUP BY COUNTRY) t1
JOIN
	(SELECT COUNTRY, count(type1) FROM CLASSES
	WHERE type1 = 'bc'
	GROUP BY COUNTRY) t2 ON t1.COUNTRY = t2.COUNTRY;
	
--Q2.5
CREATE VIEW OutcomesWithDate AS
	SELECT Outcomes.SHIP1, Outcomes.BATTLE, Outcomes.RESULT1, Battles.DATE_FOUGHT
	FROM Outcomes JOIN Battles ON Outcomes.battle = Battles.NAME;
	
SELECT t1.SHIP1 FROM
	 (SELECT SHIP1, DATE_FOUGHT 
	 FROM OutcomesWithDate
	 WHERE RESULT1 = 'damaged') t1
JOIN
	 (SELECT SHIP1, DATE_FOUGHT 
	 FROM OutcomesWithDate) t2 
ON t1.SHIP1 = t2.SHIP1
WHERE to_date(t1.DATE_FOUGHT, 'DD-MON-YY') < to_date(t2.DATE_FOUGHT, 'DD-MON-YY');

--Q2.6
SELECT COUNTRY
FROM Classes, SHIPS
WHERE Classes.CLASS = SHIPS.CLASS AND NUMGUNS IN (
	SELECT MAX(NUMGUNS)
	FROM CLASSES)
GROUP BY COUNTRY;

--Q2.7
SELECT ships.name, bore, numguns
FROM SHIPS, CLASSES c1
WHERE SHIPS.CLASS = c1.CLASS AND NUMGUNS IN 
	(	SELECT max(numguns)
		FROM CLASSES
		WHERE bore = c1.bore
		GROUP BY bore
)
ORDER BY BORE DESC;

--Q2.8
CREATE VIEW ClassesWith3Ships AS
	SELECT Class 
	FROM Classes 
	WHERE 3 <= (
		SELECT COUNT(Ships.NAME) 
		FROM Ships, Classes 
		WHERE Ships.CLASS = Classes.CLASS);

SELECT ClassesWith3Ships.Class, COUNT(result1) 
FROM Outcomes, Ships, ClassesWith3Ships
WHERE Outcomes.SHIP1 = Ships.NAME AND ClassesWith3Ships.Class = Ships.CLASS AND RESULT1 = 'sunk'
GROUP BY ClassesWith3Ships.Class;

DROP VIEW ClassesWith3Ships;

--Q3.1
INSERT INTO Classes VALUES ('Vittorio Veneto', 'bb', 'Italy', NULL, 15, 41000);
INSERT INTO Ships VALUES ('Vittorio Veneto', 'Vittorio Veneto', 1940);
INSERT INTO Ships VALUES ('Italia', 'Vittorio Veneto', 1940);
INSERT INTO Ships VALUES ('Roma', 'Vittorio Veneto', 1942);

--Q3.2
DELETE FROM Classes WHERE Class IN (
	SELECT Classes.Class 
	FROM Ships FULL OUTER JOIN Classes ON Ships.CLASS = Classes.CLASS
	GROUP BY Classes.CLASS
	HAVING COUNT(Ships.NAME) < 3
);

--Q3.3 (Done At The End)
--Q4
CREATE TABLE Exceptions(
	row_id ROWID,
	owner VARCHAR2(30),
	table_name VARCHAR2(30),
	CONSTRAINT VARCHAR2(30)
);

--Q4.1
ALTER TABLE Classes ADD CONSTRAINT classes_pk PRIMARY KEY(class);
ALTER TABLE Ships ADD CONSTRAINT ship_to_classes_fk FOREIGN KEY(class) REFERENCES Classes(class)
	EXCEPTIONS INTO Exceptions; 
	
SELECT Ships.*, CONSTRAINT FROM Ships, Exceptions
WHERE Ships.rowid = Exceptions.row_id;

DELETE FROM Ships WHERE class IN (
	SELECT CLASS
	FROM Ships, Exceptions
	WHERE Ships.rowid = Exceptions.row_id
);

ALTER TABLE Ships ADD CONSTRAINT ship_to_classes_fk
FOREIGN KEY(class) REFERENCES Classes(class);

--Q4.2
ALTER TABLE Battles ADD CONSTRAINT battles_PK PRIMARY KEY(NAME);
ALTER TABLE Outcomes ADD CONSTRAINT outcomes_to_battles_fk FOREIGN KEY(BATTLE) REFERENCES Battles(NAME)
	EXCEPTIONS INTO Exceptions;
	
--Q4.3
ALTER TABLE Ships ADD CONSTRAINT shipname_pk PRIMARY KEY(name);
ALTER TABLE Outcomes ADD CONSTRAINT Outcome_to_Ships FOREIGN KEY(ship1) REFERENCES Ships(name)
	EXCEPTIONS INTO EXCEPTIONS;

SELECT OUTCOMES.*, CONSTRAINT FROM Outcomes, Exceptions
WHERE Outcomes.rowid = Exceptions.row_id;

DELETE FROM Outcomes WHERE SHIP1 IN
	(SELECT SHIP1 
	FROM OUTCOMES, Exceptions
	WHERE Outcomes.rowid = Exceptions.row_id
);

ALTER TABLE Outcomes ADD CONSTRAINT outcomes_to_ships_fk FOREIGN KEY(SHIP1) REFERENCES SHIPS(NAME);

--Q4.4
ALTER TABLE Classes ADD CHECK (bore <= 16);

--Q4.5
ALTER TABLE Classes ADD CONSTRAINT Classes_CheckGuns CHECK (NUMGUNS <= 9 OR (NUMGUNS > 9 AND bore <= 14));

--Q4.6
CREATE OR REPLACE VIEW OutcomesView AS
	SELECT ship1, battle, result1
	FROM Outcomes O
	WHERE NOT EXISTS (
		SELECT *
		FROM Ships S, Battles B
		WHERE S.name=O.ship1 AND O.battle=B.name AND
			  S.launched > TO_NUMBER(TO_CHAR(B.date_fought, 'yyyy'))
	)
WITH CHECK OPTION;

INSERT INTO OutcomesView (ship1, battle, result1)
VALUES('Musashi', 'North Atlantic','ok');

--Q4.7

CREATE OR REPLACE VIEW ShipsVIEW AS
	SELECT * FROM Ships WHERE NOT EXISTS (
		SELECT Ships.name, Ships.launched, Ships2.name, Ships2.launched
		FROM Ships, Ships Ships2
		WHERE Ships2.name = Ships.CLASS AND Ships.launched < Ships2.launched
	)
WITH CHECK OPTION;

INSERT INTO ShipsView VALUES('Something', 'Vittorio Veneto', 1939);

--Q4.8
CREATE OR REPLACE VIEW ShipBattleView AS
	SELECT * FROM Battles
	WHERE NOT EXISTS (
		SELECT Ship1, Date_Fought
		FROM OUTCOMES, BATTLES
		WHERE OUTCOMES.BATTLE = BATTLES.NAME AND result1 = 'sunk'
		GROUP BY SHIP1, DATE_FOUGHT
		HAVING DATE_FOUGHT != MAX(DATE_FOUGHT)
	)
WITH CHECK OPTION;

--Q3.3
UPDATE CLASSES
SET bore = bore*2.5

--Cleanup
DROP TABLE Exceptions;
DROP TABLE Outcomes;
DROP TABLE Battles;
DROP TABLE Ships;
DROP TABLE Classes;
DROP VIEW ShipBattleView;
DROP VIEW ShipsVIEW;
DROP VIEW OutcomesVIEW;
DROP VIEW OutcomesWithDate;