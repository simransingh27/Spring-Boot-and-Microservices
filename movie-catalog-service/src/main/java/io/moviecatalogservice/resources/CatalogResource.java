package io.moviecatalogservice.resources;

import io.moviecatalogservice.models.CatalogItem;
import io.moviecatalogservice.models.GetRatings;
import io.moviecatalogservice.models.Movie;
import io.moviecatalogservice.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
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

        //GetRatings ratings = restTemplate.getForObject("http://localhost:8083//ratingsdata/users/" + userId, GetRatings.class);
        //Using service discovery
        GetRatings ratings = restTemplate.getForObject("http://ratings-data-service//ratingsdata/users/" + userId, GetRatings.class);
        return ratings.getRatings().stream().map(rating -> {
            /**
             * This is Rest template,first way of calling and fetching data from an API
             * for each movie id , call movie info service and get details
             * */
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
            return new CatalogItem(movie.getName(), "test", rating.getRating());
        })
                //putting them all together
                .collect(Collectors.toList());
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
    }
}
