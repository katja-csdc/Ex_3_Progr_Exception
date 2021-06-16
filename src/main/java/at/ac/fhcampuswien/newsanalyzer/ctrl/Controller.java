package at.ac.fhcampuswien.newsanalyzer.ctrl;


import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.NewsApiException;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.SortBy;
import java.util.*;
import static at.ac.fhcampuswien.newsapi.enums.Endpoint.TOP_HEADLINES;

public class Controller {

	public static final String APIKEY = "52c142235f584b9590a9663822397532";  //TODO add your api key

	public void process(String searchWord) {
		System.out.println("Start process");

		//TODO implement Error handling
		try {
			if (searchWord.isEmpty())
				throw new NewsApiException("Search word is empty!");

			//TODO load the news based on the parameters
			NewsApi newsApi = new NewsApiBuilder()
					.setApiKey(APIKEY)
					.setQ(searchWord)
					.setEndPoint(TOP_HEADLINES)
					.setSortBy(SortBy.RELEVANCY)
					.setSourceCountry(Country.at)
					.createNewsApi();

			NewsResponse newsResponse = newsApi.getNews();

			if (newsResponse != null) {
				List<Article> articles = newsResponse.getArticles();
				articles.stream().forEach(article -> System.out.println(article.toString()));
			} else {
				throw new NewsApiException("JSON Responce is empty!");
			}

			//TODO implement methods for analysis
			//Number of articles found
			List<Article> articles = newsResponse.getArticles();
			System.out.println("\nNumber of articles for your search is " + articles.size());

//Count of most providers
			Map<String, Integer> map = new HashMap<>();

			for (Article a : articles) {
				Integer i = map.get(a.getSource().getName());
				map.put(a.getSource().getName(), i == null ? 1 : i + 1);
			}

			Map.Entry<String, Integer> max = null;

			for (Map.Entry<String, Integer> e : map.entrySet()) {
				if (max == null || e.getValue() > max.getValue())
					max = e;
			}

			System.out.println("Provider \"" + max.getKey() + "\" has the most articles!");

			//Shortest author's  name
			int min = Integer.MIN_VALUE;
			String shortestName = "";

			for (Article a : articles) {
				if (a.getAuthor() != null && a.getAuthor().length() > 1) {
					if (a.getAuthor().length() < min) {
						min = a.getAuthor().length();
						shortestName = a.getAuthor();
					}
				}
			}
			System.out.println("The shortest author's name is " + shortestName);

			//Sort articles by longest title by alphabet
for (int i = 0; i < articles.size(); i++) {
	for (int j = 0; j < articles.size() -i - 1; j++) {
		if (articles.get(i).getTitle().length() < articles.get(j + 1).getTitle().length())
			Collections.swap(articles, i, j + 1);
	}
}
for (int i = 0; i < articles.size() -1; i++) {
	if (articles.get(i).getTitle().compareToIgnoreCase(articles.get(i + 1).getTitle()) > 0
			&& articles.get(i).getTitle().length() == articles.get(i + 1).getTitle().length())
		Collections.swap(articles, i, i + 1);
}

System.out.println();


		} catch (NewsApiException e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("End process");
		}
	}


	public Object getData() {

		return null;
	}
}