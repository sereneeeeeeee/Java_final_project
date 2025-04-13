CREATE TABLE movies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    year VARCHAR(10),
    overview TEXT,
    genres VARCHAR(255),         -- 暫時存成用逗號分隔的字串
    director VARCHAR(100),
    link VARCHAR(255),
    poster VARCHAR(255)
);

CREATE TABLE movie_actors (
    movie_id INT,
    actor_name VARCHAR(255),
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);

CREATE TABLE movie_genres (
    movie_id INT,
    genre_name VARCHAR(100),
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);
