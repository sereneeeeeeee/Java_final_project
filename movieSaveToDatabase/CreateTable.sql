CREATE TABLE movies (
    id INT AUTO_INCREMENT PRIMARY KEY, --表示這個欄位會自動編號
    title VARCHAR(255), --最多存 255 個字元
    year VARCHAR(10),
    overview TEXT, --儲存非常長的字串
    link VARCHAR(255),
    image TEXT
);

CREATE TABLE movie_actors (
    movie_id INT,               -- 對應 movies 表的 id
    actor_name VARCHAR(255),
    FOREIGN KEY (movie_id) REFERENCES movies(id) ---- 外鍵，連到 movies 的 id
);

CREATE TABLE movie_genres (
    movie_id INT,
    genre_name VARCHAR(255),
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);
