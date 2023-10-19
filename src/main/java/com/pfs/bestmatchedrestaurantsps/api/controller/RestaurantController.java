package com.pfs.bestmatchedrestaurantsps.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pfs.bestmatchedrestaurantsps.application.service.RestaurantService;
import com.pfs.bestmatchedrestaurantsps.domain.entities.Restaurant;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @RequestMapping(value = "/api/restaurants/search", method = RequestMethod.GET)
    public List<Restaurant> getBestMatchedRestaurants(
            @RequestParam(name = "distance", required = false) Integer distance,
            @RequestParam(name = "customer_rating", required = false) Integer customerRating,
            @RequestParam(name = "price", required = false) Integer price,
            @RequestParam(name = "restaurant_name", required = false) String restaurantName,
            @RequestParam(name = "cuisine", required = false) String cuisine) {
        return restaurantService.getBestMatchedRestaurants(distance, customerRating, price, restaurantName, cuisine);
    }
}
