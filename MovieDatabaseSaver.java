import java.sql.*;
import java.util.List;

public class MovieDatabaseSaver {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/my_movie_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Movie2025!";

    public static void saveMovies(List<Movie> movies) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {

            // 建立資料表
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS movies (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "title VARCHAR(255), image TEXT, url TEXT, link TEXT, " +
                    "director VARCHAR(255), overview TEXT, year INT)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS movie_actors (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "movie_id INT, actor_name VARCHAR(255), " +
                    "FOREIGN KEY (movie_id) REFERENCES movies(id))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS movie_genres (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "movie_id INT, genre_name VARCHAR(255), " +
                    "FOREIGN KEY (movie_id) REFERENCES movies(id))");

            // 插入主資料表
            String insertMovieSQL = "INSERT INTO movies (title, image, url, link, director, overview, year) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement movieStmt = conn.prepareStatement(insertMovieSQL, Statement.RETURN_GENERATED_KEYS);

            for (Movie movie : movies) {
                movieStmt.setString(1, movie.getTitle());
                movieStmt.setString(2, movie.getImage());
                movieStmt.setString(3, movie.getUrl());
                movieStmt.setString(4, movie.getLink());
                movieStmt.setString(5, movie.getDirector());
                movieStmt.setString(6, movie.getOverview());
                movieStmt.setInt(7, movie.getYear());
                movieStmt.executeUpdate();

                // 取得 movie_id
                ResultSet rs = movieStmt.getGeneratedKeys();
                int movieId = 0;
                if (rs.next()) {
                    movieId = rs.getInt(1);
                }

                // 插入演員
                String insertActorSQL = "INSERT INTO movie_actors (movie_id, actor_name) VALUES (?, ?)";
                for (String actor : movie.getActors()) {
                    try (PreparedStatement actorStmt = conn.prepareStatement(insertActorSQL)) {
                        actorStmt.setInt(1, movieId);
                        actorStmt.setString(2, actor);
                        actorStmt.executeUpdate();
                    }
                }

                // 插入分類
                String insertGenreSQL = "INSERT INTO movie_genres (movie_id, genre_name) VALUES (?, ?)";
                for (String genre : movie.getGenre()) {
                    try (PreparedStatement genreStmt = conn.prepareStatement(insertGenreSQL)) {
                        genreStmt.setInt(1, movieId);
                        genreStmt.setString(2, genre);
                        genreStmt.executeUpdate();
                    }
                }
            }

            System.out.println("✅ 所有電影成功儲存到資料庫！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
