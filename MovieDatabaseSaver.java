import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class MovieDatabaseSaver {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/my_movie_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Movie2025!";

    public static void saveMovies(List<Movie> movies) {
        String insertMovieSQL = "INSERT INTO movies (title, image, url, link, overview, year) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {

            for (Movie movie : movies) {
                // 插入主表
                reparedStatement pstmt = conn.prepareStatement(insertMovieSQL, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, movie.getTitle());
                pstmt.setString(2, movie.getImage());
                pstmt.setString(3, movie.getUrl());
                pstmt.setString(4, movie.getLink());
                pstmt.setString(5, movie.getDirector());
                pstmt.setString(6, movie.getOverview());
                pstmt.setInt(7, movie.getYear());
                pstmt.executeUpdate();

                // 取得自動產生的 movie_id
                ResultSet rs = pstmt.getGeneratedKeys();
                int movieId = 0;
                if (rs.next()) {
                    movieId = rs.getInt(1);
                }

                // 插入演員
                String insertActorSQL = "INSERT INTO movie_actors (movie_id, actor_name) VALUES (?, ?)";
                for (String actor : movie.getActors()) {
                    PreparedStatement actorStmt = conn.prepareStatement(insertActorSQL);
                    actorStmt.setInt(1, movieId);
                    actorStmt.setString(2, actor);
                    actorStmt.executeUpdate();
                }

                // 插入分類
                String insertGenreSQL = "INSERT INTO movie_genres (movie_id, genre_name) VALUES (?, ?)";
                for (String genre : movie.getGenre()) {
                    PreparedStatement genreStmt = conn.prepareStatement(insertGenreSQL);
                    genreStmt.setInt(1, movieId);
                    genreStmt.setString(2, genre);
                    genreStmt.executeUpdate();
                }
            }

            System.out.println("✅ 所有電影成功儲存到資料庫！");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
