import java.util.*;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

//represents information for a single professor
public class Professor{

    //the name of the professor
    private String name;
    
    //quality of the professor from 0-5
    private double overallQuality;

    //difficulty of the professor from 0-5
    private double overallDifficulty;

    //percentage of students willing to retake the professor's class from 0-100%
    private double overallRetake;

    //all the classes the professor teaches
    private Set<String> allClasses;

    //all individual ratings for the professor
    private Set<RatingProfessor> allRatings;

    //creates a Professor object
    public Professor(String iD, String name) throws IOException {
        this.name = name;
        ProfessorData profData = new ProfessorData(iD);
        allRatings = profData.getProfData();
        allClasses = new HashSet<>();
        overallQuality = profData.getProfStat("quality");
        overallDifficulty = profData.getProfStat("difficulty");
        overallRetake = profData.getProfStat("retake");
    }

    //returns the overall quality
    public String overallQuality() {
        return overallQuality +  "";
    }

    //returns the overall difficulty
    public String overallDifficulty() {
        return overallDifficulty + "";
    }

    //returns the retake percentage
    public String overallRetake() {
        return overallRetake + "%";
    }

    //adds a class to the set of all classes the professor teaches
    public void addClass(String className) {
        allClasses.add(className);
    }

    //returns the name of the professor
    public String getName() {
        return name;
    }

    //returns a single RatingProfessr. Each RatingProfessor has its own comment, quality, and difficulty values for 
    //displat purposes
    public RatingProfessor getSingleRating() {
        return allRatings.iterator().next();
    }

    //returns the set of all classes the professor teaches
    public Set<String> getClasses() {
        return allClasses;
    }

    //returns the set of all ratings for the professor
    public Set<RatingProfessor> getAllRatings() {
        return allRatings;
    }
}
