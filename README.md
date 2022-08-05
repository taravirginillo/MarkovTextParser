# MarkovTextParser

## Description

The purpose of this project is to design an easy-to-use text parser that takes an input text file and parses based on the Markov Chain Algorithm. The project involves a Java Spring backend and a React Web UI. The server takes an input text file and transforms the contents using a Markov chain algorithm. A user can use the simple React web UI to input a text file. 

The algorithm used to parse text works by splitting the text into prefixes and suffixes. A table is created with pairs of prefixes and suffixes from the text, and a randomized output is generated from this table. The user has the ability to change the prefix size (the number of words in the prefix) and the maximum output size (the number of words output). This gives the user the ability to tailor the output to his liking. I chose to pick a prefixSize and a maximumOutputSize rather than giving the user the ability to input a dictionary of prefixes because I believe the prefixSize enables the user to create proper sentences based on how many verbs, adverbs, nouns etc. are in the sentence. To train the model further, see the Todos section below. 

For example, we can take the sentence 

> "she sells sea shells and she sells tea"

For a prefix size of 2, we can loop through the sentence to identify all prefixes and suffixes. From the beginning to the end, we get:

| Prefix        | Suffix         
| ------------- |:-------------:| 
| she sells     | sea, tea      |
| sells sea     | shells        |
| sea shells    | and           |
| shells and    | she           |
| and she       |  sells        |
| sells tea     |               |

We can see that one prefix may have more than one suffix. In this case, we choose to output the first suffix. We also notice our last prefix does not have a suffix. We ignore this case.
So, at random, our output of size 10 might be 

> "sells sea shells sea shells and and she sells"

## Constraints and Assumptions
- The prefix size must be between 1 and 1000. The maximum output size must be between 1 and 1000.
- The maximum file size is MGB.
- We are going to assume all words are separated by spaces.
- If we get to the end of the array and there is no suffix for the prefix, we ignore the prefix.

## Run the Project

There are two components to this project: a backend server and a web UI.

### Backend Requirements
- Gradle 4+
- JDK 17.0.4

#### To run the backend server:
Open a command line terminal and navigate to the root project directory. Run the commands to clean & run the backend server: 

  `cd backend/demo`

  `./gradlew clean`

  `./gradlew bootRun`

Your server will run on local port 9000. You will see an "Application is Running" in your terminal f your server started successfully.
You will run into an error if this port is already being used.

#### To run the test suite:
 `cd backend/demo`
 
 `./gradlew test`

### Web UI Requirements
- Node v14.15.4
- npm >= 5.6

#### To run the web ui:

Open a command line terminal and navigate to the root project directory. Run the command:

  `cd frontend/markov-web-ui`

  `npm install`

  `npm start`

This should open up a web browser at port 3000. 

## To Dos
- Right now we are parsing the file and adding each prefix and suffix to the dictionary before performing the algorithm. This limits our file size because we are storing the entire text of the file in memory. For a file with n words and worst case scenario of prefixSize of 1 & no words being the same, we would be storing a dictionary at O(n*n) space since we would be storing each prefix once and each suffix once. We could hypothetically perform the algorithm on each chunk and append to a StringBuilder. However, the text parser might then be skewed with outputing only the first few paragraphs since we have a maxOutputSize, and the final paragraphs would not be utilized. It is more ideal for the text parser to randomize the entire text file. This may work, however, if we give the user a little bit more control over the output, or filter out prefixes & suffixes that do not make sense, such that less of the dictionary would be valid. 
- Add caching to store prefixes/suffixes. 
- Give the user the ability to limit or identify proper prefixes or suffixes. Right now, we are only using the first suffix found for each prefix. An option would be to create a parameter `String[] ignoreSuffixes` or `String[] ignorePrefixes` that we use to ignore certain words when identifying suffixes or prefixes, respectively. Similarly, you could do the reverse and include a `String[] suffixDictionary` or `String[] prefixDictionary` such that only prefixes and/or suffixes in this dictionary can be output. This would give the user even more control over the output.
- Furthering from the previous point, we could train the model by identifying incorrect suffixes or prefixes. For example, we don't like having a verb as the first word in a prefix (since the prefix always comes at the beginning of the sentence). To do so, I might create a table called IgnorePrefix that stores each word that is input by the user in the `String[] ignorePrefixes` parameter. The more the model is used, the more optimized it would be. Another option for this example would be to use a third-party API such as WordNet to identify prefixes that start with verbs and exclude them from our table.
-Our test coverage is quite minimal. We have only implemented a few integration tests for our text parsing endpoint. We would likely want to create a test suite with unit tests and more integration tests.
