package com.example.demo;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class TextResource {

    private final TextService textService;

    public TextResource(TextService textService){
        this.textService = textService;

    }

    @GetMapping("/hello")
    public String index() {
        System.out.println("returning hello world");
        return "Hello World!";
    }

    /**
     * Parses text string based on prefixes to
     * @param prefixSize, the number of words required in each prefix
     * @param text, the text to parse
     * @return (String) the parsed text
     */
    @PutMapping("/text")
    public ResponseEntity<String> parseText(@RequestBody String text, @RequestParam int prefixSize, @RequestParam(defaultValue = "10") int maxOutputSize){
        System.out.println("hi");
        String[] words = text.split("\\s+");
        if(prefixSize > words.length || prefixSize < 1){
            return ResponseEntity.badRequest().body("prefixSize must be greater than 0 and less than text length");
        } else if(prefixSize == words.length){
            return ResponseEntity.ok().body(text);
        }
        if(maxOutputSize <= prefixSize || maxOutputSize < 1){
            return ResponseEntity.badRequest().body("maxOutputSize must be larger than prefixSize");
        }

        return ResponseEntity.ok().body(textService.performMarkov(words, prefixSize, maxOutputSize));
    }

}