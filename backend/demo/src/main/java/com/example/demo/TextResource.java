package com.example.demo;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class TextResource {

    @GetMapping("/")
    public String index() {
        return "Hello World!";
    }

    /**
     * Parses text string based on prefixes to
     * @param prefixSize, the number of words required in each prefix
     * @param text, the text to parse
     * @return (String) the parsed text
     */
    @PutMapping("/text")
    public ResponseEntity<String> parseText(@RequestParam String text, @RequestParam int prefixSize){
        String[] words = text.split("\\s+");
        if(prefixSize >= words.length){
            return ResponseEntity.badRequest().body("prefixSize cannot be larger than text");
        }

        return ResponseEntity.ok().body(performMarkov(words, prefixSize));
    }

    private static String performMarkov(String[] words, int prefixSize){
        Map<String, List<String>> dictOfSuffixes = new HashMap<String, List<String>>();

        // Creating a hashmap with key = prefix and value = suffix
        for(int i=0; i < words.length - prefixSize; i++){
            String prefix = words[i];
            for(int j=i+1; j < (prefixSize + i); j++){
                prefix = prefix + " " + words[j];
            }

            String suffix = (i + prefixSize) < words.length ? words[i + prefixSize] : "";
            if(dictOfSuffixes.containsKey(prefix)){
                dictOfSuffixes.get(prefix).add(suffix);
            } else {
                dictOfSuffixes.put(prefix, new ArrayList<String>() {{add(suffix);}});
            }
        }

        // We now empty the hashmap at random to produce our parsed text
        StringBuilder outputText = new StringBuilder("");
        List keys = new ArrayList(dictOfSuffixes.keySet());
        Collections.shuffle(keys);
        for(Object prefix : keys){
            List<String> nextSuffixes = dictOfSuffixes.get(prefix);

            // Do not include prefix with no suffix (end of words array)
            if(nextSuffixes.size() > 0 && !nextSuffixes.get(0).equals("")){
                outputText.append(prefix).append(" ").append(nextSuffixes.get(0)).append(" ");
            }
        }
        return outputText.toString();
    }

}