package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TextService {
    /**
     * Performs a Markov Chain algorithm on a list of words (String[]) by identifying prefixes
     * and suffixes and returning a randomized combination of said prefixes/suffixes.
     *
     * @param words, an array of Strings to parse
     * @param prefixSize, the number of words in each prefix
     * @param maxOutputSize, the maximum number of words in the output String
     * @return String, a sentence parsed by the markov algorithm
     */
    public static String parseText(String[] words, int prefixSize, int maxOutputSize){

        Map<String, List<String>> dictOfSuffixes = buildMarkovMap(words, prefixSize);

        StringBuilder outputText = new StringBuilder("");
        int outputTextLength = 0;

        // Shuffle all prefixes in hashmap
        List keys = new ArrayList(dictOfSuffixes.keySet());
        Collections.shuffle(keys);

        // Output prefixes & suffixes to our outputText until we reach maxOutputLength
        for(Object prefix : keys){
            List<String> nextSuffixes = dictOfSuffixes.get(prefix);

            // Do not include prefix with no suffix (end of words array)
            if(nextSuffixes.size() > 0 && !nextSuffixes.get(0).equals("")){
                // suffixSize is always 1
                if(outputTextLength + prefixSize + 1 > maxOutputSize){
                    return outputText.toString();
                }

                outputText.append(prefix).append(" ").append(nextSuffixes.get(0)).append(" ");
                outputTextLength = outputTextLength + prefixSize + 1; // suffix size is always 1
            }
        }
        return outputText.toString();
    }

    /**
     * Used to build the markov chain algorithm map.
     * @param words a String array
     * @param prefixSize the number of words in the prefix
     * @return Map such that each key is a prefix and each value is a list of suffixes for said prefix.
     */
    private static Map<String, List<String>> buildMarkovMap(String[] words, int prefixSize){
        Map<String, List<String>> dictOfSuffixes = new HashMap<>();

        // Creating a hashmap with key = prefix and value = suffix
        for(int i=0; i < words.length - prefixSize; i++){

            // Building prefix based on prefixSize
            String prefix = words[i];
            for(int j=i+1; j < (prefixSize + i); j++){
                prefix = prefix + " " + words[j];
            }

            // Suffix is "" if we have hit the end of the array.
            String suffix = (i + prefixSize) < words.length ? words[i + prefixSize] : "";

            if(dictOfSuffixes.containsKey(prefix)){
                dictOfSuffixes.get(prefix).add(suffix);
            } else {
                dictOfSuffixes.put(prefix, new ArrayList<>() {{add(suffix);}});
            }
        }
        return dictOfSuffixes;
    }
}
