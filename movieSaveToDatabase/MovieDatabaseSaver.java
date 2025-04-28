package movieSaveToDatabase;

import java.sql.*;
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

                // genre ID 與名稱的對應表
                Map<Integer, String> genreMap = Map.ofEntries(
                        Map.entry(28, "Action"),
                        Map.entry(12, "Adventure"),
                        Map.entry(16, "Animation"),
                        Map.entry(35, "Comedy"),
                        Map.entry(80, "Crime"),
                        Map.entry(99, "Documentary"),
                        Map.entry(18, "Drama"),
                        Map.entry(10751, "Family"),
                        Map.entry(14, "Fantasy"),
                        Map.entry(36, "History"),
                        Map.entry(27, "Horror"),
                        Map.entry(10402, "Music"),
                        Map.entry(9648, "Mystery"),
                        Map.entry(10749, "Romance"),
                        Map.entry(878, "Science Fiction"),
                        Map.entry(10770, "TV Movie"),
                        Map.entry(53, "Thriller"),
                        Map.entry(10752, "War"),
                        Map.entry(37, "Western"));

                // 插入分類
                List<Integer> genres = movie.getGenre();
                if (genres != null && !genres.isEmpty()) {
                    String insertGenreSQL = "INSERT INTO movie_genres (movie_id, genre_name) VALUES (?, ?)";
                    for (Integer genreId : genres) {
                        String genreName = genreMap.get(genreId); // 轉換為對應的名稱
                        if (genreName != null) { // 確保 genreName 不為 null
                            try (PreparedStatement genreStmt = conn.prepareStatement(insertGenreSQL)) {
                                genreStmt.setInt(1, movieId);
                                genreStmt.setString(2, genreName);
                                genreStmt.executeUpdate();
                            }
                        }
                    }
                }
            }

            System.out.println("✅ 所有電影成功儲存到資料庫！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
