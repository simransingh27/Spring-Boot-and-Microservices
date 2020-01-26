package io.moviecatalogservice.resources;

import io.moviecatalogservice.models.CatalogItem;
import io.moviecatalogservice.models.Movie;
import io.moviecatalogservice.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        /**
         * Get movies rated from the rated Movie API.
         * Then based on movie Id call movie-info API to get the details of the Movie.
         */

        //  Movie test = restTemplate.getForObject("http://localhost:8082/movies/simu", Movie.class);
        List<Rating> ratings = Arrays.asList(
                new Rating("123", 4),
                new Rating("456", 4)
        );
        return ratings.stream().map(rating -> {
            /**
             * This is Rest template,first way of calling and fetching data from an API , unmarshal it to object.
             * Also ,we are using hardcoded url which is a static way of service discovery.
             * we will change it to dynamic discovery later on.
             * */
            Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
            return new CatalogItem(movie.getName(), "test", rating.getRating());
        })
                /**
                 * Second way is to use webclient , below the code . It will do the same thing as rest template but it uses
                 * Movie movie = webClientBuilder.build()
                 *                     .get()
                 *                     .uri("http://localhost:8082/movies/" + rating.getMovieId())
                 *                     .retrieve()
                 *                     .bodyToMono(Movie.class)
                 *                     .block();
                 * return new CatalogItem(movie.getName(), "test", rating.getRating());
                 * */

                .collect(Collectors.toList());
        //  return Collections.singletonList(new CatalogItem("Test", "Test Desc", 4));
    }
}
