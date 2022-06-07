INSERT INTO search_filter (id, name)
VALUES (1, 'First Filter');
INSERT INTO search_filter (id, name)
VALUES (2, 'Second Filter');

INSERT INTO filter_condition
    (name, search_criteria, condition_value, search_filter_id)
VALUES ('AMOUNT', 'MORE', '4', 1);
INSERT INTO filter_condition
    (name, search_criteria, condition_value, search_filter_id)
VALUES ('TITLE', 'CONTAINS', 'MEOW', 1);
INSERT INTO filter_condition
(name, search_criteria, condition_value, search_filter_id)
VALUES ('DATE', 'BEFORE', '2022-06-06', 2);