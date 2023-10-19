package com.pfs.bestmatchedrestaurantsps.domain.repository;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.pfs.bestmatchedrestaurantsps.domain.entities.Restaurant;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@AllArgsConstructor
public class RestaurantRepository {

    private final CuisineRepository cuisineRepository;
    private List<Restaurant> restaurants;

    @PostConstruct
    public void postRestaurantRepositoryConstruct() {
        this.restaurants = new ArrayList<>();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader("src/main/resources/csv/restaurants.csv"))
                .withSkipLines(1)
                .build()) {

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                Restaurant restaurant = new Restaurant();
                restaurant.setName(nextLine[0]);
                restaurant.setCustomerRating(Integer.parseInt(nextLine[1]));
                restaurant.setDistance(Integer.parseInt(nextLine[2]));
                restaurant.setPrice(Integer.parseInt(nextLine[3]));
                int cuisineId = Integer.parseInt(nextLine[4]);
                restaurant.setCuisine(
                        cuisineRepository.findAll().stream().filter(c -> c.getId() == cuisineId).findFirst().get());
                restaurants.add(restaurant);
            }

        } catch (Exception e) {
            String message = "INVALID CSV. Please, check if there are valid csv files on this path 'src/main/resources/csv''";
            log.error(message);
            throw new RuntimeException(message);
        }
    }

    public List<Restaurant> findAll() {
        return restaurants;
    }

}
