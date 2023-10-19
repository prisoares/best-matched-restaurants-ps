package com.pfs.bestmatchedrestaurantsps.application.service;

import java.util.List;

import com.pfs.bestmatchedrestaurantsps.domain.entities.Restaurant;

public interface RestaurantService {
    
    List<Restaurant> getBestMatchedRestaurants(Integer distance, Integer customerRating, Integer price, String restaurantName, String cuisine);
}
