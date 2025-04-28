package movieSaveToDatabase;

import java.util.List;

public class Movie {
    private String title;
    private String poster;
    private String url;
    private String link;
    private List<String> actors;
    private String overview;
    private String year;
    private List<Integer> genre;

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getUrl() {
        return url;
    }

    public String getLink() {
        return link;
    }

    public List<String> getActors() {
        return actors;
    }

    public String getOverview() {
        return overview;
    }

    public String getYear() {
        return year;
    }

    public List<Integer> getGenre() {
        return genre;
    }
}
