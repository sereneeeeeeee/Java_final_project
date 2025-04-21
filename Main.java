import java.util.List;

//主程式執行入口
public class Main {
    public static void main(String[] args) {
        try {
            List<Movie> movies = MovieParser.parseJson("movies.json");
            MovieDatabaseSaver.saveMovies(movies);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
