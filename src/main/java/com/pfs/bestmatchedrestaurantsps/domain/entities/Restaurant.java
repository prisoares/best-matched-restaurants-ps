package com.pfs.bestmatchedrestaurantsps.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {

    private String name;
    private int customerRating;
    private int distance;
    private int price;
    private Cuisine cuisine;

}
