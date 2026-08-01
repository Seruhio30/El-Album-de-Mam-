CREATE TABLE memories (
                          id BIGINT PRIMARY KEY,
                          title VARCHAR(150) NOT NULL,
                          type VARCHAR(20) NOT NULL,
                          category VARCHAR(50) NOT NULL,
                          memory_date DATE NOT NULL,
                          place VARCHAR(100) NOT NULL,
                          file_path VARCHAR(255) NOT NULL,
                          thumbnail VARCHAR(255) NOT NULL,
                          description VARCHAR(500) NOT NULL
);