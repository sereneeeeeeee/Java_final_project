import java.sql.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import org.json.*;

public class InsertMovie {

    // 資料庫連線訊息
    private static final String DB_URL = "jdbc:mysql://localhost:3306/my_movie_db";
    private static final String USER = "root";
    private static final String PASS = "Movie2025!";

    public static void main(String[] args) {
        try {
            // 讀取 JSON 檔案並轉成字串
            String content = new String(Files.readAllBytes(Paths.get("movies.json")), "UTF-8");

            // 解析成 JSONArray
            JSONArray movies = new JSONArray(content);
            System.out.println("共讀入 " + movies.length() + " 筆電影資料。");

            // 設定資料庫連線
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);

            // 創建資料表（如果尚未創建）
            String createTableSQL = "CREATE TABLE IF NOT EXISTS movies ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "title VARCHAR(255), "
                    + "year INT, "
                    + "director VARCHAR(255), "
                    + "link VARCHAR(255), "
                    + "poster VARCHAR(255))";
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(createTableSQL);

            // 準備批次插入語句
            String insertSQL = "INSERT INTO movies (title, year, director, link, poster) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

            // 開始批次插入
            for (int i = 0; i < movies.length(); i++) {
                JSONObject movie = movies.getJSONObject(i);

                preparedStatement.setString(1, movie.getString("title"));
                preparedStatement.setInt(2, movie.getInt("year"));
                preparedStatement.setString(3, movie.getString("director"));
                preparedStatement.setString(4, movie.getString("link"));
                preparedStatement.setString(5, movie.getString("poster"));

                preparedStatement.addBatch(); // 添加到批次
            }

            // 執行批次插入
            preparedStatement.executeBatch();

            System.out.println("Movies inserted successfully!");

            // 關閉連線
            preparedStatement.close();
            stmt.close();
            connection.close();

        } catch (IOException | JSONException | SQLException e) {
            e.printStackTrace();
        }
    }
}
