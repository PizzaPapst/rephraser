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
    public ResponseEntity<?> rephrase(@RequestParam String text){
        // Count words of Text
        int wordCount = text.trim().split("\\s+").length;
        // Create Hashmap
        Map<String, String> response = new HashMap<>();

        //Check if marked Text ist to short
        if (wordCount < 10){
            response.put("antwort", "Text ist zu kurz! Bitte markiere mindestens 10 Wörter.");
        } else {
            String prompt = String.format("""
            %s
            
            Kannst du mir das umschreiben, sodass es besser klingt? Bitte Verzichte auch Erklärungen oder einleitende Sätze, sodass ich den Text direkt kopieren kann.
            """, text);

            String geminiAntwort = googleAPI.frageGemini(prompt);

            response.put("antwort", geminiAntwort);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/correct")
    public ResponseEntity<?> correct(@RequestParam String text){
        // Create Hashmap
        Map<String, String> response = new HashMap<>();

        String prompt = String.format("""
            %s
            Ich brauche deine Hilfe bei der Rechtschreibkorrektur.
            Kannst du mir den Text so zum kopieren geben, dass er nach der deutschen Rechtschreibung, Grammatik und Zeichensetzung korrekt ist?
            
            Bitte Verzichte auch Erklärungen oder einleitende Sätze, sodass ich den Text direkt kopieren kann. 
            Hier ein Beispiel ich welchem Format ich die Antwort gerne hätte. Input: "Das ist ein ganz einfach satz mit einigen fehlern". Dein Output: "Das ist ein ganz einfacher Satz mit einigen Fehlern."
            
            Input: 
            """, text);

        String geminiAntwort = googleAPI.frageGemini(prompt);

        response.put("antwort", geminiAntwort);

        return ResponseEntity.ok(response);
    }

}
