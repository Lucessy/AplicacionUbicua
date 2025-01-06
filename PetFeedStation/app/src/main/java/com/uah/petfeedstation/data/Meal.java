package com.uah.petfeedstation.data;

import java.util.Objects;

public class Meal {
    private String time;
    private String portions;

    public Meal(String time, String portions) {
        this.time = time;
        this.portions = portions;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPortions() {
        return portions;
    }

    public void setPortions(String portions) {
        this.portions = portions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return Objects.equals(time, meal.time) &&
                Objects.equals(portions, meal.portions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, portions);
    }
}
