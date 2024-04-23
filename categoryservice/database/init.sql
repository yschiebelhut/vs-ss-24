#
Initializes the Database with all necessary tables

CREATE TABLE category
(
    id   INT          NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX FK1mtsbur82frn64de7balymq9s ON product (category_id ASC);