CREATE TABLE tb_asteroid (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nasa_id VARCHAR(255),
    name VARCHAR(255),
    estimated_diameter_min_km DOUBLE,
    estimated_diameter_max_km DOUBLE,
    absolute_magnitude DOUBLE,
    is_potentially_dangerous BOOLEAN
);

CREATE TABLE tb_close_approach (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    asteroid_id BIGINT,
    approach_date DATE,
    miss_distance_km DOUBLE,
    relative_velocity_km_h DOUBLE,
    orbiting_body VARCHAR(255),
    risk_level INT,
    CONSTRAINT fk_close_approach_asteroid FOREIGN KEY (asteroid_id) REFERENCES tb_asteroid(id)
);

-- Tabela de junção exigida pelo Spring JPA no PUT/DELETE
CREATE TABLE tb_asteroid_close_approaches (
    asteroid_id BIGINT NOT NULL,
    close_approaches_id BIGINT NOT NULL,
    CONSTRAINT fk_taca_asteroid FOREIGN KEY (asteroid_id) REFERENCES tb_asteroid(id),
    CONSTRAINT fk_taca_close_approach FOREIGN KEY (close_approaches_id) REFERENCES tb_close_approach(id)
);

CREATE TABLE tb_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    username VARCHAR(255),
    password VARCHAR(255)
);
