package fr.isep.projetjofuckitweball;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScrappingSports {


    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/2024_Summer_Olympics#Participating_National_Olympic_Committees").get();
            ArrayList<String> sportsList = new ArrayList<>();
            ArrayList<String> eventList = new ArrayList<>();

            // Selecting the table
            Element table = doc.selectFirst("table.wikitable:nth-of-type(9)");

            // Selecting the table rows within the table
            Elements rows = table.select("tbody > tr");

            for (Element row : rows) {
                // Selecting <li> elements within the current row
                Elements lis = row.select("li");

                // Iterating through <li> elements and extracting text and URLs
                for (Element li : lis) {
                    Element link = li.selectFirst("a");
                    if (link != null) {
                        // If there's a link within the <li>, get the URL and text of the link
                        String url = "https://en.wikipedia.org" + link.attr("href");
                        sportsList.add(link.text());
                        getEvents(url, eventList, link.text()); // Call getEvents method with the URL
                    } else {
                        // Otherwise, get the text of the <li>
                        sportsList.add(li.text());
                    }
                }

                System.out.println(eventList);
            }

            // Printing the extracted text
            for (String sport : sportsList) {
                sport = removePattern(sport);

                String requete = "INSERT INTO discipline (nom) VALUES ('" + sport + "');";
                System.out.println(requete);

                try {
                    FileWriter writer = new FileWriter("insert_queries_sports.sql", true); // Set 'true' to append to file
                    writer.write(requete + "\n"); // Add newline for readability
                    writer.close();
                    System.out.println("SQL statement added to file.");
                } catch (IOException e) {
                    System.out.println("An error occurred while writing to the file.");
                    e.printStackTrace();
                }

                System.out.println(sport);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getEvents(String url, ArrayList<String> eventsList, String sport) throws IOException {
        Document doc = Jsoup.connect(url).get();
        System.out.println(url);

        // Select all tables with the class "wikitable plainrowheaders"
        Elements tables = doc.select("table.wikitable.plainrowheaders");

        // Iterate through each table
        for (Element table : tables) {
            // Find all rows in the table
            Elements rows = table.select("tr");

            // Iterate through each row
            for (Element row : rows) {
                // Find the first cell (td) in the row
                Element firstCell = row.selectFirst("td");

                // If the first cell exists, add its text to the events list
                if (firstCell != null && !firstCell.text().equals("0")) {
                    // Get the text of the cell and remove "(details)"
                    String eventName = firstCell.text().replace("details", "").trim();

                    // Handle event name duplicates
                    if (eventsList.contains(eventName)) {
                        // If the event already exists, add "(men's)" or "(women's)" to distinguish
                        // between events for men and women
                        if (!eventsList.contains(eventName + " (men s)")) {
                            eventsList.add(eventName + " (men s)");
                        } else if (!eventsList.contains(eventName + " (women s)")) {
                            eventsList.add(eventName + " (women s)");
                        } else {
                            // If both "(men's)" and "(women's)" versions already exist, ignore this duplicate
                            continue;
                        }
                    } else {
                        String event =  sport + " " + eventName;
                        event = event.replace("Men's", "Men s");
                        event = event.replace("Women's", "Women s");
                        eventsList.add(event);

                        String requete = "INSERT INTO evenement (nom, discipline_id) VALUES ('" + sport + " " + eventName + "', NULL);";
                        System.out.println(requete);

                        try {
                            FileWriter writer = new FileWriter("insert_queries_events.sql", true); // Set 'true' to append to file
                            writer.write(requete + "\n"); // Add newline for readability
                            writer.close();
                            System.out.println("SQL statement added to file.");
                        } catch (IOException e) {
                            System.out.println("An error occurred while writing to the file.");
                            e.printStackTrace();
                        }


                    }
                }
            }
        }
    }

    public static String removePattern(String input) {
        // Define the regular expression pattern
        String pattern = "\\s+\\(\\d+\\)";

        // Create a Pattern object
        Pattern p = Pattern.compile(pattern);

        // Create a Matcher object
        Matcher m = p.matcher(input);

        // Replace matching patterns with an empty string
        String result = m.replaceAll("");

        return result;
    }

}
