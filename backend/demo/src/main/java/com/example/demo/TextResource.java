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
     * @param incomingTextDTO,  body of type IncomingTextDTO
     * @return A markov-chain parsed text of maximum size maxOutputSize.
     */
    @PutMapping(value="/text", produces = "application/json")
    public ResponseEntity<String> parseText(@RequestBody IncomingTextDTO incomingTextDTO){

        if(incomingTextDTO.getText() == null){
            return ResponseEntity.badRequest().body("text must not be null");

        }
        // Assumption that each word is separated by a space.
        String[] words = incomingTextDTO.getText().split("\\s+");
        int prefixSize = incomingTextDTO.getPrefixSize();
        int maxOutputSize = incomingTextDTO.getMaxOutputSize();

        if(prefixSize > words.length){
            return ResponseEntity.badRequest().body("prefixSize must be greater than 0 and less than text length");
        }
        if(prefixSize == words.length){
            return ResponseEntity.ok().body(incomingTextDTO.getText());
        }
        if(maxOutputSize <= prefixSize){
            return ResponseEntity.badRequest().body("maxOutputSize must be larger than prefixSize");
        }

        return ResponseEntity.ok().body(textService.parseText(words, prefixSize, maxOutputSize));
    }

}