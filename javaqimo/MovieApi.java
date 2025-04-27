import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.json.*;

public class MovieApi {
    private static final Map<Integer, String> genreMap = createGenreMap();

    public static void main(String[] args) throws Exception {
        String apiKey = "86d39449bb17fd1414156ecdb7e24d8b"; 
        int totalPages = 3; 

        JSONArray jsonArray = new JSONArray();

        for (int page = 1; page <= totalPages; page++) {
            String urlStr = "https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey + "&language=en-US&page=" + page;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                JSONObject obj = new JSONObject(sb.toString());
                JSONArray results = obj.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject movie = results.getJSONObject(i);
                    JSONObject movieDetails = new JSONObject();

                    // 基本資料
                    movieDetails.put("title", movie.getString("title"));
                    movieDetails.put("overview", movie.getString("overview"));

                    String releaseDate = movie.optString("release_date", "");
                    String year = "";
                    if (!releaseDate.isEmpty()) {
                        String[] parts = releaseDate.split("-");
                        if (parts.length > 0) {
                            year = parts[0];
                        }
                    }
                    movieDetails.put("year", year);

                    //genres_id to genres 
                    JSONArray genreNames = new JSONArray();
                    JSONArray genreIds = movie.getJSONArray("genre_ids");
                    for (int j = 0; j < genreIds.length(); j++) {
                        int genreId = genreIds.getInt(j);
                        String genreName = genreMap.getOrDefault(genreId, "Unknown");
                        genreNames.put(genreName);
                    }
                    movieDetails.put("genres", genreNames);

                   
                    movieDetails.put("link", "https://www.themoviedb.org/movie/" + movie.getInt("id"));
                    movieDetails.put("image", "https://image.tmdb.org/t/p/w500/" + movie.optString("poster_path", ""));

                    // actor
                    int movieId = movie.getInt("id");
                    JSONArray actorArray = fetchActors(apiKey, movieId);
                    movieDetails.put("actors", actorArray);

                    jsonArray.put(movieDetails); // add to array
                }
            }
        }

       
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("movies.json"), StandardCharsets.UTF_8)) {
            writer.write(jsonArray.toString(2));
        }

        System.out.println("movies.json done!");
    }

    private static JSONArray fetchActors(String apiKey, int movieId) throws Exception {
        String creditsUrlStr = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=" + apiKey + "&language=en-US";
        URL creditsUrl = new URL(creditsUrlStr);
        HttpURLConnection conn = (HttpURLConnection) creditsUrl.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept-Charset", "UTF-8");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            JSONObject creditsObj = new JSONObject(sb.toString());
            JSONArray castArray = creditsObj.getJSONArray("cast");

            JSONArray actors = new JSONArray();
            int maxActors = Math.min(5, castArray.length()); // 5 main actors
            for (int i = 0; i < maxActors; i++) {
                JSONObject actorObj = castArray.getJSONObject(i);
                actors.put(actorObj.getString("name"));
            }

            return actors;
        }
    }

    private static Map<Integer, String> createGenreMap() {
        Map<Integer, String> genreMap = new HashMap<>();
        genreMap.put(28, "Action");
        genreMap.put(12, "Adventure");
        genreMap.put(16, "Animation");
        genreMap.put(35, "Comedy");
        genreMap.put(80, "Crime");
        genreMap.put(99, "Documentary");
        genreMap.put(18, "Drama");
        genreMap.put(10751, "Family");
        genreMap.put(14, "Fantasy");
        genreMap.put(36, "History");
        genreMap.put(27, "Horror");
        genreMap.put(10402, "Music");
        genreMap.put(9648, "Mystery");
        genreMap.put(10749, "Romance");
        genreMap.put(878, "Science Fiction");
        genreMap.put(10770, "TV Movie");
        genreMap.put(53, "Thriller");
        genreMap.put(10752, "War");
        genreMap.put(37, "Western");

        return genreMap;
    }
}
