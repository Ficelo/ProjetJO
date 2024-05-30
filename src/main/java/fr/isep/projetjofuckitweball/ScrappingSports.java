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

            Element table = doc.selectFirst("table.wikitable:nth-of-type(9)");

            Elements rows = table.select("tbody > tr");

            for (Element row : rows) {
                Elements lis = row.select("li");

                for (Element li : lis) {
                    Element link = li.selectFirst("a");
                    if (link != null) {
                        String url = "https://en.wikipedia.org" + link.attr("href");
                        sportsList.add(link.text());
                        getEvents(url, eventList, link.text());
                    } else {
                        sportsList.add(li.text());
                    }
                }

                System.out.println(eventList);
            }

            for (String sport : sportsList) {
                sport = removePattern(sport);

                String requete = "INSERT INTO discipline (nom) VALUES ('" + sport + "');";
                System.out.println(requete);

                try {
                    FileWriter writer = new FileWriter("insert_queries_sports.sql", true);
                    writer.write(requete + "\n");
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

        Elements tables = doc.select("table.wikitable.plainrowheaders");

        for (Element table : tables) {
            Elements rows = table.select("tr");

            for (Element row : rows) {
                Element firstCell = row.selectFirst("td");

                if (firstCell != null && !firstCell.text().equals("0")) {
                    String eventName = firstCell.text().replace("details", "").trim();

                    if (eventsList.contains(eventName)) {
                        if (!eventsList.contains(eventName + " (men s)")) {
                            eventsList.add(eventName + " (men s)");
                        } else if (!eventsList.contains(eventName + " (women s)")) {
                            eventsList.add(eventName + " (women s)");
                        } else {
                            // on fait rien
                        }
                    } else {
                        String event =  sport + " " + eventName;
                        event = event.replace("Men's", "Men s");
                        event = event.replace("Women's", "Women s");
                        eventsList.add(event);

                        String requete = "INSERT INTO evenement (nom, discipline_id) VALUES ('" + sport + " " + eventName + "', NULL);";
                        System.out.println(requete);

                        try {
                            FileWriter writer = new FileWriter("insert_queries_events.sql", true);
                            writer.write(requete + "\n");
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
        String pattern = "\\s+\\(\\d+\\)";

        Pattern p = Pattern.compile(pattern);

        Matcher m = p.matcher(input);

        String result = m.replaceAll("");

        return result;
    }

}
