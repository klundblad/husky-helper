import java.io.IOException;
import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;

//Rating object to represent an individual rating
public class Rating{

    //represents stars given from 0 to 5
    public final double starRating;

    //represents a comment
    public final String commentRating;

    //creates a new Rating object
    public Rating(double starRating, String commentRating) {
        this.starRating = starRating;
        this.commentRating = commentRating;
    }
}
