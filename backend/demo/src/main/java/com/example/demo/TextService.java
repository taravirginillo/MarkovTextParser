package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TextService {
    public static String performMarkov(String[] words, int prefixSize, int maxOutputSize){
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
