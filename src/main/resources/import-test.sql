DROP TABLE IF EXISTS t_fruits;
CREATE TABLE t_fruits
(
    id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
    name VARCHAR (255) NOT NULL, 
    description VARCHAR(255), 
    PRIMARY KEY (id)
);
INSERT INTO t_fruits (id, name, description) VALUES (1000, 'Piña', 'Es grande y sabe bien');
INSERT INTO t_fruits (id, name, description) VALUES (2000, 'Cereza', 'Es rojita y pequeñita');
