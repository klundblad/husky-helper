import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

//retrieves data from Yelp's API for a specific restaurant
public class YelpData {

    //the restaurant's ID is needed to access the correct information from Yelp's API
    private String restaurantID;

    //creates a new YelpData object
    public YelpData(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    //code from https://www.mkyong.com/webservices/jax-rs/restfull-java-client-with-java-net-url/
    public Set<Rating> getData() throws IOException { 
        //url for Yelp API
        URL url = new URL("https://api.yelp.com/v3/businesses/" + restaurantID + "/reviews");
        //URL url = new URL("https://api.yelp.com/v3/businesses/din-tai-fung-seattle/reviews");
        //allows a connection for access
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //only gets data from the API
        conn.setRequestMethod("GET");
        //applies JSON data
        conn.setRequestProperty("Accept", "application/json");
        //Yelp API key is needed for authorization to use the API
        conn.setRequestProperty("Authorization", 
            "Bearer ACTrUl2_raUS1WLL46ywg1BNurXBK8L0-Ubmt6_8gkXo25fqn3uxQb6fP7b8RQnmUmY6Yum135_wqexlmeWIgl33Et8fUvk9W14UIAVBNSiazqzKl8DGeIoKWqvGX3Yx");
        //prevents runtime errors
        if (conn.getResponseCode() != 200)
        {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
        //reads the lines of the API
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        //string that will contain all data from the API
        StringBuilder apiOutput = new StringBuilder();
        String input;
        //while there is a line to be read, the line will be appended to a string
        while ((input = (br.readLine())) != null) {
            apiOutput.append(input);
        }
        //stops the reading
        conn.disconnect();
        return getRatingArray(apiOutput.toString());
    }

    //returns a set of all Ratings gathered from the API
    private Set<Rating> getRatingArray(String apiOutput) throws IOException{
        Set<Rating> ratingArray = new HashSet<>();
        //splits the text to allow easier access to data we need
        String[] yelpArray = apiOutput.split(":");
        for (int i = 5; i < yelpArray.length; i += 15) {
            //creates a new Rating with the comment and star rating value
            ratingArray.add(new Rating(Double.parseDouble(yelpArray[i + 1].substring(1, 2)), 
            yelpArray[i].substring(2, yelpArray[i].length() - 11)));
        }
        return ratingArray;
    }
}
