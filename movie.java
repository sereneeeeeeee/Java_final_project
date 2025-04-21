import java.util.List;

public class Movie {
    private String title;
    private String image;
    private String url;
    private String link;
    private List<String> actors;
    private String overview;
    private int year;
    private List<String> genre;

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public String getLink() {
        return link;
    }

    public List<String> getActors() { return actors; }
    public String getOverview() { return overview; }
    public int getYear() { return year; }
    public List<String> getGenre() { return genre; }
}
