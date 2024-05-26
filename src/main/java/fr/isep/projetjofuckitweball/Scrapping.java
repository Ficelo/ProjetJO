package fr.isep.projetjofuckitweball;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Scrapping {


    public static <IOException> void main(String[] args) throws java.io.IOException {

        Document doc;
        doc = Jsoup.connect("https://en.wikipedia.org/wiki/2024_Summer_Olympics#Participating_National_Olympic_Committees").get();

        ArrayList<PaysScrapping> pays = getLiensPays(doc, false);
        ArrayList<AthleteScrapping> athletesList = new ArrayList<>();

        // Pour les athlètes ajouter un truc pour détecter quand y'a plusieurs athlètes dans la même case

        for (PaysScrapping pay : pays) {
            doc = Jsoup.connect(pay.getUrl()).get();

            System.out.println("\n\n********** " + pay.getNom() + " **********");
            
            Elements h2Elements = doc.select("h2");

            ArrayList<String> sports = new ArrayList<>();

            for (Element h2 : h2Elements) {
                Elements spans = h2.select("span.mw-headline");

                for (Element span : spans) {
                    if (!span.text().equals("Competitors") && !span.text().equals("See also") && !span.text().equals("References")) {
                        sports.add(span.text());
                    }
                }
            }

            Elements tables = doc.select("table:has(th:contains(Athlete))");

            for (Element table : tables) {

                if (!sports.isEmpty()) {
                    System.out.println("-> " + sports.get(0));
                    sports.remove(0);
                }

                Elements headers = table.select("th");
                int athleteColumnIndex = -1;
                int eventColumnIndex = -1;

                for (int i = 0; i < headers.size(); i++) {
                    String headerText = headers.get(i).text().trim();
                    if (headerText.equalsIgnoreCase("Athlete")) {
                        athleteColumnIndex = i;
                    } else if (headerText.equalsIgnoreCase("Event")) {
                        eventColumnIndex = i;
                    }


                    if (athleteColumnIndex != -1 && eventColumnIndex != -1) {
                        break;
                    }
                }

                if (athleteColumnIndex != -1 && eventColumnIndex != -1) {

                    Elements rows = table.select("tr");

                    for (Element row : rows) {

                        Elements tds = row.select("td");


                        if (tds.size() > athleteColumnIndex && tds.size() > eventColumnIndex) {
                            String athleteCell = tds.get(athleteColumnIndex).text().trim();
                            String event = tds.get(eventColumnIndex).text().trim();

                            if (!athleteCell.isEmpty() && !athleteCell.equals("*") && !athleteCell.equals("0") && !athleteCell.equals("—") && !athleteCell.equals("--")) {
                                String[] athletes = athleteCell.split(" ");
                                String[] athletesComplete = new String[athletes.length / 2];

                                int a = 0;
                                for (int i = 0; i < athletes.length; i++) {
                                    if(a <= athletesComplete.length - 1) {
                                        if (i % 2 != 0) {
                                            athletesComplete[a] = athletesComplete[a] + " " + athletes[i]; // Ensure space between first and last name
                                            a += 1;
                                        } else {
                                            athletesComplete[a] = athletes[i];
                                        }
                                    }
                                }

                                for (String athlete : athletesComplete) {
                                    if (athlete != null && !athlete.isEmpty()) {
                                        System.out.println(athlete.trim() + " - " + event); // Trim to remove any leading or trailing spaces
                                        String[] athleteSepare = athlete.split(" ");
                                        athletesList.add(new AthleteScrapping(athleteSepare[0], athleteSepare[1]));
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

        getInfosAthletes(doc, true, athletesList);


    }

    public static ArrayList<PaysScrapping> getLiensPays (Document doc, boolean verbose){

        ArrayList<PaysScrapping> pays = new ArrayList<>();
        Elements paysDansTableau = doc.selectXpath("//*[@id=\"mw-content-text\"]/div[1]/table[10]/tbody/tr[2]/td/div/ul/li");

        for( Element p : paysDansTableau){

            String lien = "https://en.wikipedia.org" + Objects.requireNonNull(p.select("a").first()).attr("href");
            String nom = Objects.requireNonNull(p.text()).trim().replaceAll(" \\(\\d+\\)", "");
            pays.add(new PaysScrapping(nom,lien));

            String requete = "INSERT INTO pays (nom) VALUES ('" +
                    nom + "');";

            try {
                FileWriter writer = new FileWriter("insert_queries_pays.sql", true);
                writer.write(requete + "\n");
                writer.close();
                System.out.println("SQL statement added to file.");
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file.");
                e.printStackTrace();
            }

        }



        if(verbose) {
            for( PaysScrapping p : pays){
                System.out.println(p);
            }
        }
        return pays;
    }

    public static void getInfosAthletes(Document doc, boolean verbose, ArrayList<AthleteScrapping> athletesOld) throws IOException {
        ArrayList<AthleteScrapping> athletes = new ArrayList<>();

        for (AthleteScrapping athlete : athletesOld) {
            String lien = "https://en.wikipedia.org/wiki/" + athlete.getNom() + "_" + athlete.getPrenom();
            try {
                doc = Jsoup.connect(lien).get();
                //String dateNaissance = doc.select(".bday").text();
                String age = "";
                age = doc.select(".ForceAgeToShow").text();
                String texteIntro = doc.selectXpath("/html/body/div[2]/div/div[3]/main/div[3]/div[3]/div[1]/p[2]").text();
                String[] split = texteIntro.split(" ");
                boolean sexe = false;
                for (String s : split) {
                    if (s.equals("She") || s.equals("she")) {
                        sexe = true;
                    }
                }

                String sport = "";
                String event = "";
                String nationalite = "";

                Elements thElements = doc.select("th:contains(Sport), th:contains(Event), th:contains(Nationality)");
                for (org.jsoup.nodes.Element thElement : thElements) {
                    org.jsoup.nodes.Element nextTd = thElement.nextElementSibling();
                    if (nextTd != null) {
                        if (thElement.text().equalsIgnoreCase("Sport")) {
                            sport = nextTd.text();
                        } else if (thElement.text().equalsIgnoreCase("Event")) {
                            event = nextTd.text();
                        } else if (thElement.text().equalsIgnoreCase("Nationality")) {
                            nationalite = nextTd.text();
                        }
                    }
                }

                System.out.println(athlete.getNom() + " " + athlete.getPrenom() + " : " + (age.equals("") ? "???" : age) + " sexe : " + (sexe ? "Femme" : "Homme") + " Sport: " + (sport.equals("") ? "???" : sport)  + " Event: " + (event.equals("") ? "???" : event) + " Nationalite : " + (nationalite.equals("") ? "???" : nationalite));

                if (!age.equals("") && !sport.equals("") && !event.equals("") && !nationalite.equals("")) {
                    String nom = athlete.getNom().replace("'", "");
                    String prenom = athlete.getPrenom().replace("'", "");
                    String sportCleaned = sport.replace("'", "");
                    String eventCleaned = event.replace("'", "");
                    String nationaliteCleaned = nationalite.replace("'", "");

                    String requete = "INSERT INTO athletes (nom, prenom, age, sport, event, nationalite, sexe) VALUES ('" +
                            nom + "', '" + prenom + "', '" + age + "', '" +
                            sportCleaned + "', '" + eventCleaned + "', '" + nationaliteCleaned + "', '" +
                            (sexe ? "Femme" : "Homme") + "');";
                    System.out.println(requete);

                    try {
                        FileWriter writer = new FileWriter("insert_queries.sql", true); // Set 'true' to append to file
                        writer.write(requete + "\n"); // Add newline for readability
                        writer.close();
                        System.out.println("SQL statement added to file.");
                    } catch (IOException e) {
                        System.out.println("An error occurred while writing to the file.");
                        e.printStackTrace();
                    }
                }


            } catch (HttpStatusException e) {
                System.err.println("Page not found for athlete: " + athlete.getNom() + " " + athlete.getPrenom() + " - URL: " + lien);
            }
        }

        if (verbose) {
            // Additional verbose logic if needed
        }
    }


}
