import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.util.List;

public class MovieParser {
    public static List<Movie> parseJson(String filePath) throws Exception {
        Gson gson = new Gson();
        FileReader reader = new FileReader(filePath);

        return gson.fromJson(reader, new TypeToken<List<Movie>>() {
        }.getType());
    }
}
