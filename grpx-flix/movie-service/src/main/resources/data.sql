DROP TABLE IF EXISTS movie;
CREATE TABLE movie AS
SELECT *
FROM CSVREAD('classpath:movies.csv');