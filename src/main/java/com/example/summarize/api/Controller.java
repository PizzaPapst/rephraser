package com.example.summarize.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class Controller {
    private final GoogleAPI googleAPI;

    public Controller(GoogleAPI googleAPI) {
        this.googleAPI = googleAPI;
    }

    /*@GetMapping("/summarize")
    public ResponseEntity<?> summarize(@RequestParam String text, Integer laenge){
        String responseLaenge = null;
        switch (laenge){
            case 1:
                responseLaenge = "extra kurz zusammen. Versuche den Text auf in 2 Sätzen zusammenzufassen";
                break;
            case 2:
                responseLaenge = "kurz zusammen. Versuche den Text auf 30% der Originallänge zu kürzen";
                break;
            case 3:
                responseLaenge = "zusammen. Versuche den Text auf 50% der Originallänge zu kürzen";
                break;
            case 4:
                responseLaenge = "etwas zusammen. Versuche den Text auf 70% der Originallänge zu kürzen";
                break;
            case 5:
                responseLaenge = "Stichpunktartig zusammen";
                break;
            default:
                responseLaenge= "zusammen";
                break;
        }

        String prompt = "Fasse mir den folgenden Text " + responseLaenge +
                "Achte darauf die wichtigsten Inhaltspunkte beizubehalten. Hier der Text: " + text;

        // Hier rufst du deine API auf, aber du musst die Antwort in JSON verpacken
        String geminiAntwort = googleAPI.frageGemini(prompt);

        // Antwort als JSON zurückgeben
        Map<String, String> response = new HashMap<>();
        response.put("antwort", geminiAntwort);
        return ResponseEntity.ok(response); // Antwort als JSON
    }*/

    @GetMapping("/rephrase")
    public ResponseEntity<?> rephrase(@RequestParam String text, Integer stil){
        String stilBeschreibung = switch (stil) {
            case 1 -> "in einem formellen und professionellen Ton";
            case 2 -> "kurz und prägnant. Kürze den Text auf etwa 50% der Originallänge.";
            case 3 -> "freundlich und höflich";
            case 4 -> "locker und umgangssprachlich";
            case 5 -> "verkaufsorientiert, überzeugend und aktivierend";
            case 6 -> "erklärend und leicht verständlich, so als würdest du es einem Laien erklären";
            default -> "neutral umformuliert";
        };

        String prompt = String.format("""
        Formuliere den folgenden Text um %s.
        Ändere dabei nur den Stil, aber behalte die Bedeutung bei. Füge keine neuen Informationen hinzu.
        Vermeide einleitende Floskeln wie "Hier ist dein Text" – gib nur die überarbeitete Version zurück.
    
        Text:
        %s
        """, stilBeschreibung, text);


        String geminiAntwort = googleAPI.frageGemini(prompt);

        Map<String, String> response = new HashMap<>();
        response.put("antwort", geminiAntwort);
        return ResponseEntity.ok(response);
    }

}
