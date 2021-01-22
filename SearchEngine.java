// SearchEngine organizes professor and restaurant data

import java.util.*;
import java.io.*;
import java.nio.file.*;

public class SearchEngine {

    // classes as keys, mapped to professors who teach class
    private Map<String, Set<String>> classes;

    // professors as keys, mapped to classes they teach
    private Map<String, Set<String>> profs;

    // professor name as key, mapped to rating information
    private Map<String, String> profRatings;

    // restaurant name as key, mapped to rating information
    private Map<String, String> restaurants;

    // rating information 
    private Set<RatingSystem> rests;

    private Set<Professor> professors; 

    // constructs new SearchEngine given file of classes/professors, set of RatingSystems
    public SearchEngine(Set<Professor> profRate, Set<RatingSystem> restRate) throws FileNotFoundException {
        this.classes = new HashMap<String, Set<String>>();
        this.profs = new HashMap<String, Set<String>>();
        this.restaurants = new HashMap<String, String>();
        this.profRatings = new HashMap<String, String>(); 
        this.rests = restRate;
        this.professors = profRate; 
        //index(fileName);
        indexR(restRate);
        index(profRate);
    }

    //organizes set of Professors (ratings) into professorRatings map
   public void index(Set<Professor> ratings) {
        for (Professor p : ratings) {
            profRatings.put(p.getName(), p.overallQuality() + p.overallDifficulty() + p.overallRetake());
        }
   }
    

    // organizes set of RatingSystems into restaurants map
    public void indexR(Set<RatingSystem> ratings) {
        for (RatingSystem restaurant : ratings) {
            restaurants.put(restaurant.getName(), restaurant.overallRating());
        }
    }
    
    // adds associated prof and course to profs and classes maps
    public void add(String prof, String course) {
        add(prof, course, profs);
        add(course, prof, classes);
    }

    // helper for add
    // organizes prof and course given by user into profs or classes map
    private static void add(String key, String data, Map<String, Set<String>> map) {
        if (map.containsKey(key)) {
            Set<String> temp = map.get(key);
            temp.add(data);
        } else {
            Set<String> temp = new HashSet<String>();
            temp.add(data);
            map.put(key, temp);
        }
    }

    // organize data from external sources into prof and classes maps
    // data format should be "class firstname middlename... lastname"
    private void index(String fileName) throws FileNotFoundException {
        Scanner input = new Scanner(new File(fileName));
        while (input.hasNextLine()) {
            String line = input.nextLine();
            Scanner scan = new Scanner(line);
            String id = scan.next();
            String prof = scan.next() + scan.next();
            while (scan.hasNext()){
                add(prof, scan.next());
            }
        }
    }

    // Return set of classes if query is a professor
    // Return set of professors is query is a class
    // Return overallRating if query is a restaurant
    public Set<String> search(String query) {
        if (classes.containsKey(query)) {
            return classes.get(query);
        } else if (profs.containsKey(query)) {
            /*Set<Set<String>> result = new HashSet<Set<String>>();
            result.add(profs.get(query));
            result.add(profRatings.get(query));
            return result;*/
            return profs.get(query);
        } else {
            Set<String> temp = new HashSet<String>();
            temp.add(restaurants.get(query));
            return temp;
        }
    }

    // returns set of professor names that match or contain given query
    public Set<String> searchProf(String query) {
        Set<String> returnSet = new HashSet<>();
        String newQuery = query.toLowerCase().replace(" ", "");
        // searched for professor name
        for (Professor prof : professors) {
            if (prof.getName().toLowerCase().contains(newQuery)) {
                returnSet.add(prof.getName());
            }
            for (String className : prof.getClasses()) {
                if (className.toLowerCase().contains(newQuery) && !returnSet.contains(prof)) {
                    returnSet.add(prof.getName());
                }
            }
        }
        return returnSet;
    }

    // search for restaurant or category of restuarant
    // returns set of restaurant names that match or contain given query
    // returns set of restaurant names that are of the categories that match or contain given query
    public Set<String> searchRest(String query) {
        Set<String> returnSet = new HashSet<>();
        String newQuery = query.toLowerCase().replace(" ","");
        //searched for restaurant name
        for (RatingSystem rest : rests) {
            if (rest.getName().toLowerCase().contains(newQuery)) {
                returnSet.add(rest.getName());
            }
            //searched for a category the restaurant is in
            for (String category: rest.getCategories()) {
                if (category.toLowerCase().contains(newQuery) && !returnSet.contains(rest)) {
                    returnSet.add(rest.getName());
                }
            }
        }
        return returnSet;
    }
}
