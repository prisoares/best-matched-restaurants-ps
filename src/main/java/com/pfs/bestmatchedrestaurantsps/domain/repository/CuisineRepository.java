package com.pfs.bestmatchedrestaurantsps.domain.repository;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.pfs.bestmatchedrestaurantsps.domain.entities.Cuisine;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class CuisineRepository {

    private List<Cuisine> cuisines;

    public CuisineRepository() {
        this.cuisines = new ArrayList<>();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader("src/main/resources/csv/cuisines.csv"))
                .withSkipLines(1) // to skip the header
                .build()) {

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                Cuisine cuisine = new Cuisine();
                cuisine.setId(Integer.parseInt(nextLine[0]));
                cuisine.setName(nextLine[1]);
                cuisines.add(cuisine);
            }
        } catch (Exception e) {
            String message = "INVALID CSV. Please, check if there are valid csv files on this path 'src/main/resources/csv''. "
                    + e.getMessage();
            log.error(message);
            throw new RuntimeException(message);
        }
    }

    public List<Cuisine> findAll() {
        return this.cuisines;
    }

}
