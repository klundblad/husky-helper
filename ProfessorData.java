import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

//retrieves data from a specific professor's ratemyprofessor website
public class ProfessorData {

    //the professor's ID is needed to get the correct website for the professor
    private String profID;

    //creates a ProfessorData object
    public ProfessorData(String profID) {
      this.profID = profID;
    }

    //returns a set of all ratings for the professor
    public Set<RatingProfessor> getProfData() throws IOException {
        // Makes a URL to the web page
        URL url = new URL("https://www.ratemyprofessors.com/ShowRatings.jsp?tid=" + profID + "&showMyProfs=true");
        // Get the input stream through URL Connection
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        List<String> c2 = new ArrayList<>();
        Set<RatingProfessor> allRatings = new HashSet<>();
        // reads each line
        while ((line = br.readLine()) != null) {
            //this line will contain the data we need
            if (line.contains(" </style></div><div")) {
                //splits the line to allow easier access to each rating's comments
                String[] comments = line.split("Comments__StyledComments");
                String c;
                for (int i = 2; i < comments.length; i ++) {
                    String[] comments2 = comments[i].split("</div><div");
                    for (int j = 0; j < comments2.length; j ++) {
                        if (comments2[j].charAt(0) == '-') {
                            //this is a single comment
                            c2.add(comments2[j].replace("&#x27;", "'").substring(18));
                        }
                    }
                }
                int count = 0;
                //this split allows easier access to each rating's difficulty and quality ratings
                String[] ratings = line.split("RatingValues__RatingValue");
                for (int i = 1; i < ratings.length - 1; i += 2) {
                    //creates a new RatingProfessor object using the data we retrieved
                    allRatings.add(new RatingProfessor(c2.get(count), Double.parseDouble(ratings[i].substring(21,24)), Double.parseDouble(ratings[i + 1].substring(21,24 ))));
                    count += 1;
                }
            }
        }
        return allRatings;
    }

    //returns either a quality, difficulty, or retake value based on the value of determine
    public double getProfStat(String determine) throws IOException {
        // Make a URL to the web page
        URL url = new URL("https://www.ratemyprofessors.com/ShowRatings.jsp?tid=" +profID + "&showMyProfs=true");
        // Get the input stream through URL Connection
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            if (line.contains(" </style></div><div")) {
                if (determine.equals("quality")) {
                    String[] values = line.split("RatingValue__Numerator-");
                    //this is the overall quality value
                    return Double.parseDouble(values[1].substring(17,20));
                } else {
                    String[] values = line.split("FeedbackItem__FeedbackNumber-");
                    if (determine.equals("retake")) {
                        //this is the overall retake percentage
                        return Double.parseDouble(values[1].substring(17,19));
                    }
                    else {
                        //this is the overall difficulty value
                        return Double.parseDouble(values[2].substring(17,20));
                    }
                }
            }
        }
        return 0;
    }
}
