import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;

import com.sun.net.httpserver.*;

public class Server {
    private static final String QUERY_TEMPLATE = "{\"items\":[%s]}";

    private static Object LOCK = new Object();

    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        // Create an HttpServer instance on port 8000 accepting up to 100 concurrent connections
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 100);
        // Step 0: Initialize data for the algorithm
        Set<RatingSystem> restRate = new HashSet<>();
        Set<Professor> profRate = new HashSet<>();
        Map<String, String> restRateMap = new HashMap<>(); 
        Map<String, String> profRateMap = new HashMap<>(); 
        Scanner inputR = new Scanner(new File("RestaurantIds.csv"));
        Scanner input = new Scanner(new File("ProfessorIds.csv"));
      
        while (inputR.hasNextLine()) {
            String[] info = inputR.nextLine().split(",");
            RatingSystem ratingSystem = new RatingSystem(info[0], info[1]);
            Thread.sleep(1000);
            for (int i = 2; i < info.length; i ++) {
                ratingSystem.addCategory(info[i]);
            }
            String result = ratingSystem.overallRating() + " Related: ";
            for (String s : ratingSystem.getCategories()) {
                result += (s + ", ");
            }
            result += " ";
            Rating s2 = ratingSystem.getSingleRating();
            result += (s2.commentRating + " " + s2.starRating + "/5 ");
            restRate.add(ratingSystem);
            restRateMap.put(ratingSystem.getName(), result);
            Thread.sleep(1000);
        }
        
        while (input.hasNextLine()) {
            String[] info = input.nextLine().split(",");
            Professor professor = new Professor(info[0], info[1]);
            Thread.sleep(1000);
            for (int i = 2; i < info.length; i ++) {
                professor.addClass(info[i]);
            }
            RatingProfessor s2 = professor.getSingleRating(); 
            profRate.add(professor);
            String statistics = " Quality = " + professor.overallQuality() +
                                " Difficulty = " + professor.overallDifficulty() +
                                " Retake = " + professor.overallRetake() +
                                " Comment = " + s2.getComment() +
                                " Difficulty = " + s2.getDifficulty() +
                                " Quality = " + s2.getQuality();
            profRateMap.put(professor.getName(), statistics);
            Thread.sleep(1000);
        }
       
        //System.out.println(restRate);
        //System.out.println(profRate);
        SearchEngine engine = new SearchEngine(profRate, restRate);
        // Return the index.html file when the browser asks for the web app
        server.createContext("/", (HttpExchange t) -> {
            System.out.println("handled");
            String html = Files.readString(Paths.get("index.html"));
            send(t, "text/html; charset=utf-8", html);
        });
        // Return a list of suggestions for the given query string, s
        server.createContext("/query", (HttpExchange t) -> {
            System.out.println("handled2");
            String sR = parse("s", t.getRequestURI().getQuery().split("&"));
            if (sR.equals("")) {
                send(t, "application/json", String.format(QUERY_TEMPLATE, ""));
                return;
            }

            Map<String, String> resultRest = new LinkedHashMap<>(); 
            Set<String> searchRestaurant = engine.searchRest(sR);
            // System.out.println("search is " + searchRestaurant);
            for (String searchResult: searchRestaurant) {
                resultRest.put(searchResult, " | Overall Rating: " + restRateMap.get(searchResult) + "|");
            }
            String test = json(resultRest.entrySet());
            System.out.println(test);
            send(t, "application/json", String.format(QUERY_TEMPLATE, test));
        });

        server.createContext("/", (HttpExchange t) -> {
            System.out.println("handled3");
            String html = Files.readString(Paths.get("index.html"));
            send(t, "text/html; charset=utf-8", html);
        });
        // Return a list of suggestions for the given query string, s
        server.createContext("/query2", (HttpExchange t) -> {
            System.out.println("handled4");
            String sP = parse("b", t.getRequestURI().getQuery().split("&"));
            System.out.println("parse is " + sP);
            if (sP.equals("")) {
                send(t, "application/json", String.format(QUERY_TEMPLATE, ""));
                return;
            }
            Map<String, String> resultProf = new LinkedHashMap<>(); 
            Set<String> searchProfessor = engine.searchProf(sP);
            System.out.println("search is " + searchProfessor);
            for (String s2: searchProfessor) {
                resultProf.put(s2, profRateMap.get(s2));
            }
            System.out.println("profs: " + resultProf);
            String test2 = json(resultProf.entrySet());
            System.out.println(test2);
            send(t, "application/json", String.format(QUERY_TEMPLATE, test2));
        });
        /*
        server.createContext("/random", (HttpExchange t) -> {
            // Step 2: Remove this API endpoint
            send(t, "application/json", "{\"s\":\"" + result + "\"}");
        });
        */
        server.setExecutor(null);
        server.start();
    }

    private static String parse(String key, String... params) {
        for (String param : params) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals(key)) {
                return pair[1];
            }
        }
        return "";
    }

    private static void send(HttpExchange t, String contentType, String data)
            throws IOException, UnsupportedEncodingException {
        t.getResponseHeaders().set("Content-Type", contentType);
        byte[] response = data.getBytes("UTF-8");
        t.sendResponseHeaders(200, response.length);
        try (OutputStream os = t.getResponseBody()) {
            os.write(response);
        }
    }

    private static String json(Iterable<Map.Entry<String, String>> matches) {
        StringBuilder results = new StringBuilder();
        for (Map.Entry<String, String> entry : matches) {
            if (results.length() > 0) {
                results.append(',');
            }
            // Step 1: Return the top 5 matches as a JSON object (dict)
            results.append('{');
            results.append("\"query\":\"")
            .append(entry.getKey()).append("\",");
            results.append("\"weight\":\"").append(entry.getValue()).append("\"");
            results.append('}');
        }
        return results.toString();
    }
}
