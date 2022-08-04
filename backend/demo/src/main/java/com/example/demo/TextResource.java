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

    /**
     * Get Mapping used to test the server and say hello.
     * @return "Hello World!"
     */
    @GetMapping("/hello")
    public String index() {
        System.out.println("returning hello world");
        return "Hello World!";
    }

    /**
     * Parses an input text via a markov chain algorithm.
     * @param prefixSize, the number of words required in each sentence prefix
     * @param maxOutputSize, the maximum number of words to return
     * @requestBody text
     * @return A markov-chain parsed text of maximum size maxOutputSize.
     */
    @PutMapping(value="/text", produces = "text/plain")
    public ResponseEntity<String> parseText(@RequestBody String text, @RequestParam int prefixSize, @RequestParam(defaultValue = "10") int maxOutputSize){
        // Assumption that each word is separated by a space.
        String[] words = text.split("\\s+");

        if(prefixSize > words.length || prefixSize < 1){
            return ResponseEntity.badRequest().body("prefixSize must be greater than 0 and less than text length");
        }
        if(prefixSize == words.length){
            return ResponseEntity.ok().body(text);
        }
        if(maxOutputSize <= prefixSize || maxOutputSize < 1){
            return ResponseEntity.badRequest().body("maxOutputSize must be larger than prefixSize");
        }

        return ResponseEntity.ok().body(textService.parseText(words, prefixSize, maxOutputSize));
    }

}