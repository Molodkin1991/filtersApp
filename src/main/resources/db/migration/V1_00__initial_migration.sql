CREATE TABLE search_filter
(
    id   SERIAL,
    name varchar(256),
    primary key (id)
);

CREATE TABLE filter_condition
(
    id               SERIAL,
    name             varchar(256),
    search_criteria  varchar(256),
    condition_value  varchar(256),
    search_filter_id int,
    primary key (id),
    FOREIGN KEY (search_filter_id)
        REFERENCES search_filter (id)
);
