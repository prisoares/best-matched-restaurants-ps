package com.pfs.bestmatchedrestaurantsps.application.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pfs.bestmatchedrestaurantsps.application.exception.ValidatorException;
import com.pfs.bestmatchedrestaurantsps.domain.entities.Restaurant;
import com.pfs.bestmatchedrestaurantsps.domain.repository.CuisineRepository;
import com.pfs.bestmatchedrestaurantsps.domain.repository.RestaurantRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final CuisineRepository cuisineRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public List<Restaurant> getBestMatchedRestaurants(Integer distance,
            Integer customerRating,
            Integer price,
            String restaurantName,
            String cuisine) {

        if (distance == null && customerRating == null && price == null && restaurantName == null && cuisine == null) {
            return new ArrayList<>();
        }

        validateOptionalFields(distance, customerRating, price, restaurantName, cuisine);

        var restaurantFilteredStream = restaurantRepository.findAll().stream();
        if (distance != null) {
            restaurantFilteredStream = restaurantFilteredStream
                    .filter(restaurant -> restaurant.getDistance() <= distance);
        }

        if (customerRating != null) {
            restaurantFilteredStream = restaurantFilteredStream
                    .filter(restaurant -> restaurant.getCustomerRating() >= customerRating);
        }

        if (price != null) {
            restaurantFilteredStream = restaurantFilteredStream.filter(restaurant -> restaurant.getPrice() <= price);
        }

        if (restaurantName != null) {
            restaurantFilteredStream = restaurantFilteredStream
                    .filter(restaurant -> restaurant.getName().toLowerCase().contains(restaurantName.toLowerCase()));
        }

        if (cuisine != null) {
            restaurantFilteredStream = restaurantFilteredStream.filter(
                    restaurant -> restaurant.getCuisine().getName().toLowerCase().contains(cuisine.toLowerCase()));
        }

        restaurantFilteredStream = restaurantFilteredStream
                .sorted(Comparator.comparing(Restaurant::getDistance)
                        .thenComparing(Restaurant::getCustomerRating)
                        .thenComparing(Restaurant::getPrice));

        return restaurantFilteredStream.limit(5).collect(Collectors.toList());

    }

    private void validateOptionalFields(Integer distance, Integer customerRating, Integer price,
            String restaurantName, String cuisine) {

        if (distance != null && !(distance >= 1 && distance <= 10)) {
            String message = "Restaurant Distance is not valid. The Distance should be range from 1 to 10 miles.";
            log.info(message);
            throw new ValidatorException(message);
        }

        if (customerRating != null && !(customerRating >= 1 && customerRating <= 5)) {
            String message = "Customer Rating is not valid. The Customer Rating should be a range from 1 to 5 stars.";
            log.info(message);
            throw new ValidatorException(message);
        }

        if (price != null && !(price >= 10 && price <= 50)) {
            String message = "Restaurant Price is not valid. The Restaurant Prices should be an average of how much one person usually spend. For this application, it can be a range from $10 to $50.";
            log.info(message);
            throw new ValidatorException(message);
        }

        if (restaurantName != null) {
            List<Restaurant> matchedRestaurantsName = restaurantRepository.findAll().stream()
                    .filter(restName -> restName.getName().toLowerCase().contains(restaurantName.toLowerCase()))
                    .collect(Collectors.toList());

            if (matchedRestaurantsName.isEmpty()) {
                String message = "Restaurant Name is not valid. The Restaurant Name should be an exact or partial name of local restaurants located near your company.";
                log.warn(message);
                throw new ValidatorException(message);
            }
        }

        if (cuisine != null
                && !cuisineRepository.findAll().stream()
                        .anyMatch(c -> c.getName().toLowerCase().contains(cuisine.toLowerCase()))) {
            String message = "Cuisine is not valid. The Cuisine should be an exact or partial name of the most familiar cuisines, like Chinese, American, Thai, etc).";
            log.info(message);
            throw new ValidatorException(message);
        }
    }
}
