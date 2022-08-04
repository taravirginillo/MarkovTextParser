package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Service
public class TextService {

    /**
     * Parses text file and performs Markov chain algorithm.
     * @param incomingTextDTO
     * @return String parsed output sentence
     */
    public static String parseTextFileUsingMarkov(MultipartFile file, IncomingTextParametersDTO incomingTextDTO) throws IOException {
        Map<String, List<String>> dictOfSuffixes = new HashMap<>();

        String line;
        InputStream is = file.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            buildMarkovMap(dictOfSuffixes, line, incomingTextDTO.getPrefixSize());
        }
        return randomizePrefixesAndReturnOutputString(dictOfSuffixes, incomingTextDTO.getPrefixSize(), incomingTextDTO.getMaxOutputSize());
    }


    /**
     * Used to build the markov chain algorithm map.
     * @param dictOfSuffixes, a map to append prefix/suffix to
     * @param text, String
     * @param prefixSize the number of words in the prefix
     * @return Map such that each key is a prefix and each value is a list of suffixes for said prefix.
     */
    private static Map<String, List<String>> buildMarkovMap(Map<String, List<String>> dictOfSuffixes, String text, int prefixSize){
        String[] words = text.split("\\s+");

        for(int indexOfArray=0; indexOfArray < words.length - prefixSize; indexOfArray++){

            StringBuilder buildPrefix = new StringBuilder(words[indexOfArray]);
            for(int nextPrefixIndex=indexOfArray+1; nextPrefixIndex < (prefixSize + indexOfArray); nextPrefixIndex++){
                buildPrefix.append(" ").append(words[nextPrefixIndex]);
            }

            boolean isPrefixAtEndOfWordsArray = (indexOfArray + prefixSize) > words.length - 1;
            if(!isPrefixAtEndOfWordsArray) {
                String suffix = words[indexOfArray + prefixSize];
                if (dictOfSuffixes.containsKey(buildPrefix.toString())) {
                    dictOfSuffixes.get(buildPrefix.toString()).add(suffix);
                } else {
                    dictOfSuffixes.put(buildPrefix.toString(), new ArrayList<>() {{
                        add(suffix);
                    }});
                }
            }
        }
        return dictOfSuffixes;
    }

    /**
     * Randomizes a collection of prefixes & their suffixes and outputs a string
     * with a randomized order of prefixes & their suffixes.
     * @param dictOfSuffixes
     * @param prefixSize
     * @param maxOutputSize
     * @return String of randomized prefixes & suffixes
     */
    private static String randomizePrefixesAndReturnOutputString(Map<String, List<String>> dictOfSuffixes, int prefixSize, int maxOutputSize){
        StringBuilder outputText = new StringBuilder("");
        int outputTextLength = 0;
        int suffixSize = 1;

        List prefixKeys = new ArrayList(dictOfSuffixes.keySet());
        Collections.shuffle(prefixKeys);

        for(Object prefix : prefixKeys){
            List<String> nextSuffixes = dictOfSuffixes.get(prefix);
            int outputLengthWithAdditionalPrefixSuffix = outputTextLength + prefixSize + suffixSize;
            if(nextSuffixes.size() > 0){
                if(outputLengthWithAdditionalPrefixSuffix > maxOutputSize){
                    return outputText.toString();
                }

                outputText.append(prefix).append(" ").append(nextSuffixes.get(0)).append(" ");
                outputTextLength = outputLengthWithAdditionalPrefixSuffix;
            }
        }
        return outputText.toString();
    }
}
