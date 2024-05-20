package fr.isep.projetjofuckitweball;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Scrapping {



    public static <IOException> void main(String[] args) throws java.io.IOException {

        Document doc;
        doc = Jsoup.connect("https://en.wikipedia.org/wiki/2024_Summer_Olympics#Participating_National_Olympic_Committees").get();

        ArrayList<PaysScrapping> pays = getLiensPays(doc, false);

        for (PaysScrapping pay : pays) {
            doc = Jsoup.connect(pay.getUrl()).get();

            System.out.println("\n\n********** " + pay.getNom() + " **********");

            // Select all <h2> elements
            Elements h2Elements = doc.select("h2");

            ArrayList<String> sports = new ArrayList<>();

            for (Element h2 : h2Elements) {
                // Select all <span> elements with class "mw-headline" within the current <h2> element
                Elements spans = h2.select("span.mw-headline");

                // Print the text of each selected <span> element
                for (Element span : spans) {
                    if (!span.text().equals("Competitors") && !span.text().equals("See also") && !span.text().equals("References")) {
                        sports.add(span.text());
                    }
                }
            }

            // Select all tables that have a <th> with the text "Athlete"
            Elements tables = doc.select("table:has(th:contains(Athlete))");

            for (Element table : tables) {
                // Print the sport associated with the current table
                if (!sports.isEmpty()) {
                    System.out.println("-> " + sports.get(0));
                    sports.remove(0);
                }

                // Select all header <th> elements within the table
                Elements headers = table.select("th");
                int athleteColumnIndex = -1;
                int eventColumnIndex = -1;

                // Find the index of the "Athlete" column and the "Event" column
                for (int i = 0; i < headers.size(); i++) {
                    String headerText = headers.get(i).text().trim();
                    if (headerText.equalsIgnoreCase("Athlete")) {
                        athleteColumnIndex = i;
                    } else if (headerText.equalsIgnoreCase("Event")) {
                        eventColumnIndex = i;
                    }

                    // If both columns are found, exit the loop
                    if (athleteColumnIndex != -1 && eventColumnIndex != -1) {
                        break;
                    }
                }

                // If both "Athlete" and "Event" columns are found
                if (athleteColumnIndex != -1 && eventColumnIndex != -1) {
                    // Select all rows in the table
                    Elements rows = table.select("tr");

                    // Iterate through each row
                    for (Element row : rows) {
                        // Select all <td> elements within the row
                        Elements tds = row.select("td");

                        // Check if the row has enough columns
                        if (tds.size() > athleteColumnIndex && tds.size() > eventColumnIndex) {
                            // Get the text of the cell in the "Athlete" column
                            String athlete = tds.get(athleteColumnIndex).text().trim();
                            String event = tds.get(eventColumnIndex).text().trim();

                            // Print the athlete's name and the event they are competing in
                            if (!athlete.isEmpty() && !athlete.equals("*") && !athlete.equals("0") && !athlete.equals("—") && !athlete.equals("--")) {
                                System.out.println(athlete + " - " + event);
                            }
                        }
                    }
                }
            }
        }




    }

    public static ArrayList<PaysScrapping> getLiensPays (Document doc, boolean verbose){

        ArrayList<PaysScrapping> pays = new ArrayList<>();
        Elements paysDansTableau = doc.selectXpath("//*[@id=\"mw-content-text\"]/div[1]/table[10]/tbody/tr[2]/td/div/ul/li");

        for( Element p : paysDansTableau){

            String lien = "https://en.wikipedia.org" + Objects.requireNonNull(p.select("a").first()).attr("href");
            String nom = Objects.requireNonNull(p.text()).trim().replaceAll(" \\(\\d+\\)", "");
            pays.add(new PaysScrapping(nom,lien));
        }

        if(verbose) {
            for( PaysScrapping p : pays){
                System.out.println(p);
            }
        }
        return pays;
    }



}
