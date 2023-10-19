package com.pfs.bestmatchedrestaurantsps.api.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.pfs.bestmatchedrestaurantsps.domain.entities.Cuisine;
import com.pfs.bestmatchedrestaurantsps.domain.entities.Restaurant;
import com.pfs.bestmatchedrestaurantsps.domain.repository.CuisineRepository;
import com.pfs.bestmatchedrestaurantsps.domain.repository.RestaurantRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class RestaurantControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private RestaurantRepository restaurantRepository;

        @MockBean
        private CuisineRepository cuisineRepository;

        @Test
        void testEmptySearch() throws Exception {
                mockMvc.perform(get("/api/restaurants/search"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        void testAllFiltersTogetherToFindBestMatchRestaurants() throws Exception {
                // Given
                String path = "/api/restaurants/search?distance=8&customer_rating=1&price=20&restaurant_name=a&cuisine=a";

                List<Restaurant> sampleRestaurantsAndCuisines = Arrays.asList(
                        new Restaurant("Perfection Table", 1, 1, 15, new Cuisine(14, "Malaysian")),
                        new Restaurant("Baby Tasty", 3, 1, 20, new Cuisine(8, "Korean")),
                        new Restaurant("Perfection Palace", 3, 1, 20, new Cuisine(3, "Thai")),
                        new Restaurant("Fodder Table", 4, 1, 20, new Cuisine(8, "Korean")),
                        new Restaurant("Place Chow", 2, 2, 15, new Cuisine(11, "Spanish")));

                List<Cuisine> sampleCuisines = Arrays.asList(
                        new Cuisine(14, "Malaysian"),
                        new Cuisine(8, "Korean"),
                        new Cuisine(3, "Thai"),
                        new Cuisine(11, "Spanish"));

                Mockito.when(restaurantRepository.findAll()).thenReturn(sampleRestaurantsAndCuisines);
                Mockito.when(cuisineRepository.findAll()).thenReturn(sampleCuisines);

                // when
                mockMvc.perform(get(path))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].name").value("Perfection Table"))
                                .andExpect(jsonPath("$[1].name").value("Baby Tasty"))
                                .andExpect(jsonPath("$[2].name").value("Perfection Palace"))
                                .andExpect(jsonPath("$[3].name").value("Fodder Table"))
                                .andExpect(jsonPath("$[4].name").value("Place Chow"));
        }


        @Test
        void testAllFiltersTogetherToFindBestMatchRestaurantsAndDoesNotMatchRestaurant() throws Exception {
                // Given
                String path = "/api/restaurants/search?distance=8&customer_rating=1&price=20&restaurant_name=me&cuisine=a";

                List<Restaurant> sampleRestaurantsAndCuisines = Arrays.asList(
                        new Restaurant("Perfection Table", 1, 1, 15, new Cuisine(14, "Malaysian")),
                        new Restaurant("Baby Tasty", 3, 1, 20, new Cuisine(8, "Korean")),
                        new Restaurant("Perfection Palace", 3, 1, 20, new Cuisine(3, "Thai")),
                        new Restaurant("Fodder Table", 4, 1, 20, new Cuisine(8, "Korean")),
                        new Restaurant("Place Chow", 2, 2, 15, new Cuisine(11, "Spanish")));

                List<Cuisine> sampleCuisines = Arrays.asList(
                        new Cuisine(14, "Malaysian"),
                        new Cuisine(8, "Korean"),
                        new Cuisine(3, "Thai"),
                        new Cuisine(11, "Spanish"));

                Mockito.when(restaurantRepository.findAll()).thenReturn(sampleRestaurantsAndCuisines);
                Mockito.when(cuisineRepository.findAll()).thenReturn(sampleCuisines);

                // when
                mockMvc.perform(get(path))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").exists());
        }
        
        @Test
        void testDistanceFilterToFindBestMatchRestaurants() throws Exception {
                String path = "/api/restaurants/search?distance=1";

                List<Restaurant> sampleRestaurants = Arrays.asList(
                        new Restaurant("Chow Table", 1, 1, 10, new Cuisine(2, "Chinese")),
                        new Restaurant("Herbed Table", 1, 1, 15, new Cuisine(12, "Greek")),
                        new Restaurant("Palate Table", 1, 1, 15, new Cuisine(14, "Malaysian")),
                        new Restaurant("Whole Tasty", 1, 1, 30, new Cuisine(5, "French")),
                        new Restaurant("Bit Kitchen", 1, 1, 30, new Cuisine(18, "Russian")));

                Mockito.when(restaurantRepository.findAll()).thenReturn(sampleRestaurants);
                Mockito.when(cuisineRepository.findAll()).thenReturn(Arrays.asList(new Cuisine(2, "Chinese")));

                mockMvc.perform(get(path))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].name").value("Chow Table"))
                                .andExpect(jsonPath("$[1].name").value("Herbed Table"))
                                .andExpect(jsonPath("$[2].name").value("Palate Table"))
                                .andExpect(jsonPath("$[3].name").value("Whole Tasty"))
                                .andExpect(jsonPath("$[4].name").value("Bit Kitchen"));
        }

        @Test
        void testDistanceFilterToFindBestMatchRestaurantsGreaterThanMAX() throws Exception {
                String path = "/api/restaurants/search?distance=11";

                List<Restaurant> sampleRestaurants = Arrays.asList(
                        new Restaurant("Chow Table", 1, 1, 10, new Cuisine(2, "Chinese")),
                        new Restaurant("Herbed Table", 1, 1, 15, new Cuisine(12, "Greek")),
                        new Restaurant("Palate Table", 1, 1, 15, new Cuisine(14, "Malaysian")),
                        new Restaurant("Whole Tasty", 1, 1, 30, new Cuisine(5, "French")),
                        new Restaurant("Bit Kitchen", 1, 1, 30, new Cuisine(18, "Russian")));

                Mockito.when(restaurantRepository.findAll()).thenReturn(sampleRestaurants);
                Mockito.when(cuisineRepository.findAll()).thenReturn(Arrays.asList(new Cuisine(2, "Chinese")));

                mockMvc.perform(get(path))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").exists());
        }

        @Test
        void testDistanceFilterToFindBestMatchRestaurantsLessThanMIN() throws Exception {
                String path = "/api/restaurants/search?distance=-1";

                List<Restaurant> sampleRestaurants = Arrays.asList(
                        new Restaurant("Chow Table", 1, 1, 10, new Cuisine(2, "Chinese")),
                        new Restaurant("Herbed Table", 1, 1, 15, new Cuisine(12, "Greek")),
                        new Restaurant("Palate Table", 1, 1, 15, new Cuisine(14, "Malaysian")),
                        new Restaurant("Whole Tasty", 1, 1, 30, new Cuisine(5, "French")),
                        new Restaurant("Bit Kitchen", 1, 1, 30, new Cuisine(18, "Russian")));

                Mockito.when(restaurantRepository.findAll()).thenReturn(sampleRestaurants);
                Mockito.when(cuisineRepository.findAll()).thenReturn(Arrays.asList(new Cuisine(2, "Chinese")));

                mockMvc.perform(get(path))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").exists());
        }

        @Test
        void testCustomerRatingFilterToFindTheBestMatchRestaurants() throws Exception {
                // Given
                String path = "/api/restaurants/search?customer_rating=1";

                List<Restaurant> sampleRestaurants = Arrays.asList(
                        new Restaurant("Chow Table", 1, 1, 10, new Cuisine(2, "Chinese")),
                        new Restaurant("Herbed Table", 1, 1, 15, new Cuisine(12, "Greek")),
                        new Restaurant("Palate Table", 1, 1, 15, new Cuisine(14, "Malaysian")),
                        new Restaurant("Whole Tasty", 1, 1, 30, new Cuisine(5, "French")),
                        new Restaurant("Bit Kitchen", 1, 1, 30, new Cuisine(18, "Russian")));

                Mockito.when(restaurantRepository.findAll()).thenReturn(sampleRestaurants);
                Mockito.when(cuisineRepository.findAll()).thenReturn(Arrays.asList(new Cuisine(2, "Chinese")));

                // when
                mockMvc.perform(get(path))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].name").value("Chow Table"))
                                .andExpect(jsonPath("$[1].name").value("Herbed Table"))
                                .andExpect(jsonPath("$[2].name").value("Palate Table"))
                                .andExpect(jsonPath("$[3].name").value("Whole Tasty"))
                                .andExpect(jsonPath("$[4].name").value("Bit Kitchen"));
        }

        @Test
        void testCustomerRatingFilterToFindBestMatchRestaurantsGreaterThanMAX() throws Exception {
                // Given
                String path = "/api/restaurants/search?customer_rating=6";

                List<Restaurant> sampleRestaurants = Arrays.asList(
                        new Restaurant("Chow Table", 1, 1, 10, new Cuisine(2, "Chinese")),
                        new Restaurant("Herbed Table", 1, 1, 15, new Cuisine(12, "Greek")),
                        new Restaurant("Palate Table", 1, 1, 15, new Cuisine(14, "Malaysian")),
                        new Restaurant("Whole Tasty", 1, 1, 30, new Cuisine(5, "French")),
                        new Restaurant("Bit Kitchen", 1, 1, 30, new Cuisine(18, "Russian")));

                Mockito.when(restaurantRepository.findAll()).thenReturn(sampleRestaurants);
                Mockito.when(cuisineRepository.findAll()).thenReturn(Arrays.asList(new Cuisine(2, "Chinese")));

                // when
                mockMvc.perform(get(path))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").exists());
        }

        @Test
        void testCustomerRatingFilterToFindBestMatchRestaurantsLessThanMIN() throws Exception {
                // Given
                String path = "/api/restaurants/search?customer_rating=0";

                List<Restaurant> sampleRestaurants = Arrays.asList(
                        new Restaurant("Chow Table", 1, 1, 10, new Cuisine(2, "Chinese")),
                        new Restaurant("Herbed Table", 1, 1, 15, new Cuisine(12, "Greek")),
                        new Restaurant("Palate Table", 1, 1, 15, new Cuisine(14, "Malaysian")),
                        new Restaurant("Whole Tasty", 1, 1, 30, new Cuisine(5, "French")),
                        new Restaurant("Bit Kitchen", 1, 1, 30, new Cuisine(18, "Russian")));

                Mockito.when(restaurantRepository.findAll()).thenReturn(sampleRestaurants);
                Mockito.when(cuisineRepository.findAll()).thenReturn(Arrays.asList(new Cuisine(2, "Chinese")));

                // when
                mockMvc.perform(get(path))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").exists());
        }

        @Test
        void testPriceFilterToFindBestMatchRestaurants() throws Exception {
                // Given
                String path = "/api/restaurants/search?price=14";

                List<Restaurant> sampleRestaurants = Arrays.asList(
                        new Restaurant("Chow Table", 1, 1, 10, new Cuisine(2, "Chinese")),
                        new Restaurant("Kitchenster", 2, 1, 10, new Cuisine(1, "American")),
                        new Restaurant("Dished Grill", 3, 1, 10, new Cuisine(8, "Korean")),
                        new Restaurant("Deliciousgenix", 4, 1, 10, new Cuisine(11, "Spanish")),
                        new Restaurant("Deliciousquipo", 2, 2, 10, new Cuisine(19, "Malaysian")));

                Mockito.when(restaurantRepository.findAll()).thenReturn(sampleRestaurants);
                Mockito.when(cuisineRepository.findAll()).thenReturn(Arrays.asList(new Cuisine(2, "Chinese")));

                // when
                mockMvc.perform(get(path))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].name").value("Chow Table"))
                                .andExpect(jsonPath("$[1].name").value("Kitchenster"))
                                .andExpect(jsonPath("$[2].name").value("Dished Grill"))
                                .andExpect(jsonPath("$[3].name").value("Deliciousgenix"))
                                .andExpect(jsonPath("$[4].name").value("Deliciousquipo"));
        }

        @Test
        void testPriceFilterToFindTheBestMatchRestaurantsGreaterThanMAX() throws Exception {
                // Given
                String path = "/api/restaurants/search?price=51";

                List<Restaurant> sampleRestaurants = Arrays.asList(
                        new Restaurant("Chow Table", 1, 1, 10, new Cuisine(2, "Chinese")),
                        new Restaurant("Kitchenster", 2, 1, 10, new Cuisine(1, "American")),
                        new Restaurant("Dished Grill", 3, 1, 10, new Cuisine(8, "Korean")),
                        new Restaurant("Deliciousgenix", 4, 1, 10, new Cuisine(11, "Spanish")),
                        new Restaurant("Deliciousquipo", 2, 2, 10, new Cuisine(19, "Malaysian")));

                Mockito.when(restaurantRepository.findAll()).thenReturn(sampleRestaurants);
                Mockito.when(cuisineRepository.findAll()).thenReturn(Arrays.asList(new Cuisine(2, "Chinese")));

                // when
                mockMvc.perform(get(path))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").exists());
        }

        @Test
        void testPriceFilterToFindBestMatchRestaurantsGreaterThanMIN() throws Exception {
                // Given
                String path = "/api/restaurants/search?price=9";

                List<Restaurant> sampleRestaurants = Arrays.asList(
                        new Restaurant("Chow Table", 1, 1, 10, new Cuisine(2, "Chinese")),
                        new Restaurant("Kitchenster", 2, 1, 10, new Cuisine(1, "American")),
                        new Restaurant("Dished Grill", 3, 1, 10, new Cuisine(8, "Korean")),
                        new Restaurant("Deliciousgenix", 4, 1, 10, new Cuisine(11, "Spanish")),
                        new Restaurant("Deliciousquipo", 2, 2, 10, new Cuisine(19, "Malaysian")));

                Mockito.when(restaurantRepository.findAll()).thenReturn(sampleRestaurants);
                Mockito.when(cuisineRepository.findAll()).thenReturn(Arrays.asList(new Cuisine(2, "Chinese")));

                // when
                mockMvc.perform(get(path))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").exists());
        }

        @Test
        void testRestaurantNameFilterToFindBestMatchRestaurants() throws Exception {
                // Given
                String path = "/api/restaurants/search?restaurant_name=me";

                List<Restaurant> sampleRestaurants = Arrays.asList(
                        new Restaurant("Me Grill", 5, 5, 25, new Cuisine(9, "Vietnamese")),
                        new Restaurant("Acclaimed Yummy", 5, 8, 50, new Cuisine(8, "Korean")));

                Mockito.when(restaurantRepository.findAll()).thenReturn(sampleRestaurants);
                Mockito.when(cuisineRepository.findAll()).thenReturn(Arrays.asList(new Cuisine(2, "Chinese")));

                // when
                mockMvc.perform(get(path))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].name").value("Me Grill"))
                                .andExpect(jsonPath("$[1].name").value("Acclaimed Yummy"));
        }

        @Test
        void testInvalidRestaurantNameFilterToFindBestMatchRestaurants() throws Exception {
                // Given
                String path = "/api/restaurants/search?restaurant_name=xx";

                List<Restaurant> sampleRestaurants = Arrays.asList(
                        new Restaurant("Me Grill", 5, 5, 25, new Cuisine(9, "Vietnamese")),
                        new Restaurant("Acclaimed Yummy", 5, 8, 50, new Cuisine(8, "Korean")),
                        new Restaurant("Chow Table", 1, 1, 10, new Cuisine(2, "Chinese")),
                        new Restaurant("Kitchenster", 2, 1, 10, new Cuisine(1, "American")),
                        new Restaurant("Dished Grill", 3, 1, 10, new Cuisine(8, "Korean")),
                        new Restaurant("Deliciousgenix", 4, 1, 10, new Cuisine(11, "Spanish")),
                        new Restaurant("Deliciousquipo", 2, 2, 10, new Cuisine(19, "Malaysian")));

                Mockito.when(restaurantRepository.findAll()).thenReturn(sampleRestaurants);
                Mockito.when(cuisineRepository.findAll()).thenReturn(Arrays.asList(new Cuisine(2, "Chinese")));

                // when
                mockMvc.perform(get(path))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").exists());
        }

        @Test
        void testCuisineFilterToFindBestMatchRestaurants() throws Exception {
                // Given
                String path = "/api/restaurants/search?cuisine=t";

                List<Restaurant> sampleRestaurantsAndCuisines = Arrays.asList(
                        new Restaurant("Perfection Palace", 3, 1, 20, new Cuisine(3, "Thai")),
                        new Restaurant("Smash Kitchen", 1, 2, 50, new Cuisine(7, "Turkish")),
                        new Restaurant("Deliciousquipo", 2, 2, 10, new Cuisine(19, "Other")),
                        new Restaurant("Hut Chow", 2, 2, 10, new Cuisine(3, "Thai")),
                        new Restaurant("Deliciouszoid", 3, 2, 30, new Cuisine(4, "Italian")));

                List<Cuisine> sampleCuisines = Arrays.asList(
                        new Cuisine(3, "Thai"),
                        new Cuisine(4, "Italian"),
                        new Cuisine(7, "Turkish"),
                        new Cuisine(19, "Other"));

                Mockito.when(restaurantRepository.findAll()).thenReturn(sampleRestaurantsAndCuisines);
                Mockito.when(cuisineRepository.findAll()).thenReturn(sampleCuisines);

                // when
                mockMvc.perform(get(path))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].name").value("Perfection Palace"))
                                .andExpect(jsonPath("$[1].name").value("Smash Kitchen"))
                                .andExpect(jsonPath("$[2].name").value("Deliciousquipo"))
                                .andExpect(jsonPath("$[3].name").value("Hut Chow"))
                                .andExpect(jsonPath("$[4].name").value("Deliciouszoid"));
        }

        @Test
        void testInvalidCuisineFilterToFindBestMatchRestaurants() throws Exception {
                // Given
                String path = "/api/restaurants/search?cuisine=xx";

                List<Restaurant> sampleRestaurantsAndCuisines = Arrays.asList(
                        new Restaurant("Perfection Palace", 3, 1, 20, new Cuisine(3, "Thai")),
                        new Restaurant("Smash Kitchen", 1, 2, 50, new Cuisine(7, "Turkish")),
                        new Restaurant("Deliciousquipo", 2, 2, 10, new Cuisine(19, "Other")),
                        new Restaurant("Hut Chow", 2, 2, 10, new Cuisine(3, "Thai")),
                        new Restaurant("Deliciouszoid", 3, 2, 30, new Cuisine(4, "Italian")));

                List<Cuisine> sampleCuisines = Arrays.asList(
                        new Cuisine(3, "Thai"),
                        new Cuisine(4, "Italian"),
                        new Cuisine(7, "Turkish"),
                        new Cuisine(19, "Other"));

                Mockito.when(restaurantRepository.findAll()).thenReturn(sampleRestaurantsAndCuisines);
                Mockito.when(cuisineRepository.findAll()).thenReturn(sampleCuisines);

                // when
                mockMvc.perform(get(path))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").exists());
        }

}
