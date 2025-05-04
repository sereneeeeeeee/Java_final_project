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
    private List<String> genres;

    // Getter methods
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

    public List<String> getGenres() {
        return genres;
    }

    // Setter methods
    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}
