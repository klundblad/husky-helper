import java.util.*;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

//A RatingSystem represents infromation about a single restaurant
public class RatingSystem{

    //the name of the restaurant
    private String name;

    //list of all ratings for a place
    private Set<Rating> allRatings;

    //all categories that the restaurant falls into (ie. seafood, Japanese, etc.)
    private Set<String> allCategories;

    //creates a new RatingSystem object
    public RatingSystem(String iD, String name) throws IOException {
        this.name = name;
        YelpData yelp = new YelpData(iD);
        allRatings = yelp.getData();
        allCategories = new HashSet<>();
    }

    //returns the name of the restaurant
    public String getName() {
        return name;
    }

    //returns the average of all the star ratings
    public String overallRating() {
        double total = 0;
        for (Rating r : allRatings) {
            total += r.starRating;
        }
        return roundData(total / allRatings.size());
    }

    //adds a new category into the set of all categories
    public void addCategory(String category) {
        allCategories.add(category);
    }

    //returns a set of all the restaurant's categories
    public Set<String> getCategories() {
        return allCategories;
    }

    //returns the set of all ratings
    public Set<Rating> getAllRatings() {
        return allRatings;
    }

    //returns a single Rating. Each Rating has its own comment and star rating value for display purposes
    public Rating getSingleRating() {
        return allRatings.iterator().next();
    }

    //rounds double to two decimal places for display purposes
    private String roundData(double data) {
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(data);
    }
}


