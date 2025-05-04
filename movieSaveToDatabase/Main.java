package movieSaveToDatabase;

import java.util.List;

//主程式執行入口
public class Main {
    public static void main(String[] args) {
        try {
            List<Movie> movies = MovieParser.parseJson("D:\\\\Java_final_project\\\\javaqimo\\\\movies.json");
            MovieDatabaseSaver.saveMovies(movies);

            Movie movie = MovieDatabaseSaver.getMovieById(10); // 你可以換成其他 ID 試試

            if (movie != null) {
                System.out.println("電影標題: " + movie.getTitle());
                System.out.println("上映年份: " + movie.getYear());
                System.out.println("簡介: " + movie.getOverview());
                System.out.println("電影連結: " + movie.getLink());
                System.out.println("圖片連結: " + movie.getPoster());

                System.out.println("演員:");
                for (String actor : movie.getActors()) {
                    System.out.println(" - " + actor);
                }

                System.out.println("類型:");
                for (String genre : movie.getGenres()) {
                    System.out.println(" - " + genre);
                }
            } else {
                System.out.println("找不到該 ID 的電影。");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
