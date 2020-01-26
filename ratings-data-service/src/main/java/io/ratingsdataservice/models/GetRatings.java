package io.ratingsdataservice.models;

import java.util.List;

public class GetRatings {
    private List<Rating> ratings;
    public GetRatings() {

    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
}
