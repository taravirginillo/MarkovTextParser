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
    public ResponseEntity<String> parseText(@RequestParam String text, @RequestParam int prefixSize, @RequestParam(defaultValue = "10") int maxOutputSize){
        String[] words = text.split("\\s+");
        if(prefixSize > words.length || prefixSize < 1){
            return ResponseEntity.badRequest().body("prefixSize must be greater than 0 and less than text length");
        }
        if(maxOutputSize < prefixSize || maxOutputSize < 1){
            return ResponseEntity.badRequest().body("maxOutputSize must be larger than prefixSize");
        }

        return ResponseEntity.ok().body(performMarkov(words, prefixSize, maxOutputSize));
    }

    private static String performMarkov(String[] words, int prefixSize, int maxOutputSize){
        Map<String, List<String>> dictOfSuffixes = new HashMap<>();

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
                dictOfSuffixes.put(prefix, new ArrayList<>() {{add(suffix);}});
            }
        }

        // We now empty the hashmap at random to produce our parsed text
        StringBuilder outputText = new StringBuilder("");
        int outputTextLength = 0;
        List keys = new ArrayList(dictOfSuffixes.keySet());
        Collections.shuffle(keys);
        for(Object prefix : keys){
            List<String> nextSuffixes = dictOfSuffixes.get(prefix);

            // Do not include prefix with no suffix (end of words array)
            if(nextSuffixes.size() > 0 && !nextSuffixes.get(0).equals("")){
                if(outputTextLength + prefixSize + 1 > maxOutputSize){
                    return outputText.toString();
                }
                outputText.append(prefix).append(" ").append(nextSuffixes.get(0)).append(" ");
                outputTextLength = outputTextLength + prefixSize + 1; // suffix size is always 1
            }
        }
        return outputText.toString();
    }

}