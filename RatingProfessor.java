//represents a single rating for a professor
public class RatingProfessor {

    //comment about the professor
    private String comment;

    //user rated quality from 0-5
    private double quality;
    
    ////user rated difficulty from 0-5
    private double difficulty;

    //creates a new RatingProfessor object
    public RatingProfessor (String comment, double quality, double difficulty) {
        this.comment = comment;
        this.quality = quality;
        this.difficulty = difficulty;
    }

    //returns the comment
    public String getComment() {
        return comment;
    }

    //returns the quality value
    public double getQuality() {
        return quality;
    }

    //returns the difficulty value
    public double getDifficulty() {
        return difficulty;
    }
}

