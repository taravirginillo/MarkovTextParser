package com.example.demo;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@Validated
public class TextResource {

    private final TextService textService;

    public TextResource(TextService textService){
        this.textService = textService;
    }

    /**
     * Parses an input text via a markov chain algorithm.
     * @param prefixSize,  size of each prefix for markov chain algorithm
     * @param maxOutputSize, max number of words allowed in output
     * @param file, of type plain/text
     * @return A markov-chain parsed text of maximum size maxOutputSize.
     */
    @PostMapping(value="/text", consumes = {"multipart/form-data"})
    public ResponseEntity<String> parseText(@RequestParam(defaultValue="1") @Min(1) @Max(1000) int prefixSize,
                                            @RequestParam(defaultValue="10") @Min(1) @Max(1000) int maxOutputSize,
                                            @RequestPart("file") MultipartFile file){

        IncomingTextParametersDTO incomingTextDTO = new IncomingTextParametersDTO(prefixSize, maxOutputSize);
        if(file.isEmpty()){
            return ResponseEntity.badRequest().body("file cannot be empty");
        }

        boolean isFileTypeTextFile = file.getContentType().equals("text/plain");
        if(!isFileTypeTextFile){
            return ResponseEntity.badRequest().body("only text files are accepted");
        }

        boolean isMaxOutputSizeLessThanOrEqualToPrefixSize = maxOutputSize <= prefixSize ? true : false;
        if(isMaxOutputSizeLessThanOrEqualToPrefixSize){
            return ResponseEntity.badRequest().body("maxOutputSize must be larger than prefixSize");
        }

        try{
            String parsedText = textService.parseTextFileUsingMarkov(file, incomingTextDTO);
            return ResponseEntity.ok().body(parsedText);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("IO Exception. Please ensure file type is text");
        }
    }

}