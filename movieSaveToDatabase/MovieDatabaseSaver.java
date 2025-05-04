package movieSaveToDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MovieDatabaseSaver {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/my_movie_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Movie2025!";

    public static void saveMovies(List<Movie> movies) {
        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/my_movie_db", "root",
                        "Movie2025!")) {

            // 建立資料表
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("DELETE FROM movie_actors");
            stmt.executeUpdate("DELETE FROM movie_genres");
            stmt.executeUpdate("DELETE FROM movies");

            stmt.executeUpdate("ALTER TABLE movies AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE movie_actors AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE movie_genres AUTO_INCREMENT = 1");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS movies (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "title VARCHAR(255), " +
                    "year VARCHAR(255), " +
                    "overview TEXT, " +
                    "link VARCHAR(255), " +
                    "image TEXT)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS movie_actors (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "movie_id INT," +
                    "actor_name VARCHAR(255), " +
                    "FOREIGN KEY (movie_id) REFERENCES movies(id))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS movie_genres (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "movie_id INT," +
                    "genre_name VARCHAR(255), " +
                    "FOREIGN KEY (movie_id) REFERENCES movies(id))");

            // 插入主資料表
            String insertMovieSQL = "INSERT INTO movies (title, year, overview, link, Image) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement movieStmt = conn.prepareStatement(insertMovieSQL, Statement.RETURN_GENERATED_KEYS);

            for (Movie movie : movies) {
                movieStmt.setString(1, movie.getTitle());
                movieStmt.setString(2, movie.getYear());
                movieStmt.setString(3, movie.getOverview());
                movieStmt.setString(4, movie.getLink());
                movieStmt.setString(5, movie.getPoster());

                movieStmt.executeUpdate(); // 執行前面準備好的 SQL 語句，並把資料寫入資料庫。

                // 取得 movie_id
                ResultSet rs = movieStmt.getGeneratedKeys();
                int movieId = 0;
                if (rs.next()) {
                    movieId = rs.getInt(1);
                }

                // 插入演員
                List<String> actors = movie.getActors();
                if (actors != null && !actors.isEmpty()) { // 避免空指標
                    String insertActorSQL = "INSERT INTO movie_actors (movie_id, actor_name) VALUES (?, ?)";
                    for (String actor : actors) {
                        try (PreparedStatement actorStmt = conn.prepareStatement(insertActorSQL)) {
                            actorStmt.setInt(1, movieId);
                            actorStmt.setString(2, actor);
                            actorStmt.executeUpdate();
                        }
                    }
                }

                // 插入分類
                List<String> genres = movie.getGenres();
                if (genres != null && !genres.isEmpty()) {
                    String insertGenreSQL = "INSERT INTO movie_genres (movie_id, genre_name) VALUES (?, ?)";
                    for (String genreName : genres) {
                        if (genreName != null && !genreName.trim().isEmpty()) { // 確保 genreName 不為 null
                            try (PreparedStatement genreStmt = conn.prepareStatement(insertGenreSQL)) {
                                genreStmt.setInt(1, movieId);
                                genreStmt.setString(2, genreName);
                                genreStmt.executeUpdate();
                            }
                        } else {
                            System.out.println("Skipped null or empty genre for movie: " + movie.getTitle());
                        }
                    }
                } else {
                    System.out.println("No genres to insert for movie: " + movie.getTitle());
                }
            }

            System.out.println("所有電影成功儲存到資料庫！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 根據電影的 ID 查詢電影資訊
    public static Movie getMovieById(int movieId) {
        Movie movie = null;

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            // 1. 查主表：movies
            String movieSQL = "SELECT * FROM movies WHERE id = ?"; // 選取 movies 表中所有欄位，*代表所有欄位
            try (PreparedStatement movieStmt = conn.prepareStatement(movieSQL)) {
                movieStmt.setInt(1, movieId);
                ResultSet rs = movieStmt.executeQuery();

                if (rs.next()) {
                    movie = new Movie();
                    movie.setTitle(rs.getString("title"));
                    movie.setYear(rs.getString("year"));
                    movie.setOverview(rs.getString("overview"));
                    movie.setLink(rs.getString("link"));
                    movie.setPoster(rs.getString("image"));

                    // 2. 查演員表：movie_actors
                    String actorSQL = "SELECT actor_name FROM movie_actors WHERE movie_id = ?";
                    try (PreparedStatement actorStmt = conn.prepareStatement(actorSQL)) {
                        actorStmt.setInt(1, movieId);
                        ResultSet actorRs = actorStmt.executeQuery();

                        List<String> actors = new ArrayList<>();
                        while (actorRs.next()) {
                            actors.add(actorRs.getString("actor_name"));
                        }
                        movie.setActors(actors);
                    }

                    // 3. 查類別表：movie_genres
                    String genreSQL = "SELECT genre_name FROM movie_genres WHERE movie_id = ?";
                    try (PreparedStatement genreStmt = conn.prepareStatement(genreSQL)) {
                        genreStmt.setInt(1, movieId);
                        ResultSet genreRs = genreStmt.executeQuery();

                        List<String> genres = new ArrayList<>();
                        while (genreRs.next()) {
                            genres.add(genreRs.getString("genre_name"));
                        }
                        movie.setGenres(genres);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movie;
    }

}
