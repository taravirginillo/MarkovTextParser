package com.example.demo;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TextResource {

    @GetMapping("/")
    public String index() {
        return "Hello World!";
    }

    /**
     * Parses text string based on prefixes to
     * @param prefixSize, the number of words in each prefix
     * @return
     */
    @PutMapping("/text")
    public ResponseEntity<String> parseText(@RequestParam String text, @RequestParam int prefixSize){
        String[] words = text.split("\\s+");
        if(prefixSize >= words.length){
            return ResponseEntity.badRequest().body("prefixSize cannot be larger than text");
        }

        String output = performMarkov(text, prefixSize);

        return ResponseEntity.ok().body(output);
    }

    private static String performMarkov(String text, int prefixSize){
        return "hi";
    }

}