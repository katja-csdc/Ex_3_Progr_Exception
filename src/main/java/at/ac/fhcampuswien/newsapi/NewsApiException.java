package at.ac.fhcampuswien.newsapi;

public class NewsApiException extends RuntimeException {
    public NewsApiException(String errMsg) {
        super(errMsg);
    }
}
