import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import org.json.*;

public class MovieApi {
    public static void main(String[] args) throws Exception {
        String apiKey = "86d39449bb17fd1414156ecdb7e24d8b";
        int totalPages = 3;
        
        JSONArray jsonArray = new JSONArray();
        
        for (int page = 1; page <= totalPages; page++) {
            String urlStr = "https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey + "&language=zh-TW&page=" + page;
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
                    movieDetails.put("genres", movie.getJSONArray("genre_ids"));
                    movieDetails.put("link", "https://www.themoviedb.org/movie/" + movie.getInt("id"));
                    movieDetails.put("poster", "https://image.tmdb.org/t/p/w500/" + movie.optString("poster_path", ""));
                    
                    jsonArray.put(movieDetails);
                }
            }
        }
        
        
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("movies.json"), StandardCharsets.UTF_8)) {
            writer.write(jsonArray.toString(2));
        }
        
        
    }
}