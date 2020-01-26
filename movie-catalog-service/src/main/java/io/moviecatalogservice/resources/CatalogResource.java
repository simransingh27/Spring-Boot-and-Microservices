package io.moviecatalogservice.resources;

import io.moviecatalogservice.models.CatalogItem;
import io.moviecatalogservice.models.Movie;
import io.moviecatalogservice.models.Rating;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        /**
         * Get movies rated from the rated Movie API.
         * Then based on movie Id call movie-info API to get the details of the Movie.
         */
        RestTemplate restTemplate =  new RestTemplate();
      //  Movie test = restTemplate.getForObject("http://localhost:8082/movies/simu", Movie.class);
        List<Rating> ratings = Arrays.asList(
                new Rating("123",4),
                new Rating("456",4)
        );
         return ratings.stream().map(rating ->{
           // This is static way of service discovery , we will change it to dynamic later on.
           Movie movie =  restTemplate.getForObject("http://localhost:8082/movies/"+rating.getMovieId(), Movie.class);
          return  new CatalogItem(movie.getName(),"test",rating.getRating());
         })
         .collect(Collectors.toList());
      //  return Collections.singletonList(new CatalogItem("Test", "Test Desc", 4));
    }
}
