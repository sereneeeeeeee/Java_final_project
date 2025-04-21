import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.util.List;

//讀取JSON檔案解析成Java的List<Movie>物件清單
public class MovieParser {
    public static List<Movie> parseJson(String filePath) throws Exception {
        Gson gson = new Gson();// 把 JSON 自動轉成 Java 物件
        FileReader reader = new FileReader(filePath);// 打開指定路徑的 JSON 檔案來讀取內容。

        return gson.fromJson(reader, new TypeToken<List<Movie>>() {
        }.getType());// 把 JSON 檔案內容 ➜ 轉成 Java 的 List<Movie>，也就是一個包含很多 Movie 物件的陣列。
    }
}
