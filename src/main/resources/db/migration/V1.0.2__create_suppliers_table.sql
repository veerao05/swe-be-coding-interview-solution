CREATE TABLE getyourguide.supplier
(
    id      INT PRIMARY KEY,
    name    VARCHAR(255),
    address VARCHAR(255),
    zip     VARCHAR(20),
    city    VARCHAR(100),
    country VARCHAR(100)
);

INSERT INTO getyourguide.supplier (id, name, address, zip, city, country)
VALUES (1, 'John Doe', '123 Main St', '12345', 'Anytown', 'USA'),
       (2, 'Jane Doe', '456 Main St', '12345', 'Anytown', 'USA'),
       (3, 'Charlie Doe', '678 Main St', '12345', 'Anytown', 'USA'),
       (200, 'Jackie Chan', '789 Main St', '10000', 'Hong Kong', 'China'),
       (250, 'Ion Popescu', 'Str. Veseliei, Nr. 4', '253445', 'Bucharest', 'Romania');

