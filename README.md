# MarkovTextParser

## Description

This project takes an input text file and transforms the contents using a Markov chain algorithm. A user can use the simple React web UI to input a text file and a prefix size.

The algorithm used to parse text works by splitting the text into prefixes and suffixes. A table is created with pairs of prefixes and suffixes from the text, and a randomized output is generated from this table. The user has the ability to change the prefix size (the number of words in the prefix) and the maximum output size (the number of words output).

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

## Run the Project

There are two components to this project: a backend server and a web UI.

### Backend Requirements
- Gradle 4+
- JDK 17.0.4

#### To run the backend server:
Open a command line terminal and navigate to the backend/demo folder. Run the commands to clean & run the backend server: 

> cd ./demo

> ./gradlew clean

> ./gradlew bootRun

Your server will run on local port 9000. You will see an "Application is Running" if your server started successfully.
You will run into an error if this port is already being used.

### Web UI Requirements
- Node v14.15.4
- npm >= 5.6

#### To run the web ui:

Open a command line terminal and navigate to the frontend/markov-web-ui folder. Run the command:

> cd ./markov-web-ui

> npm run full-install

> npm start

This should open up a web browser at port 3000. 

## Assumptions of File:
- The file is within 0 and 2147483647 characters (the String limit in Java). With more time, an Iterable<String> or a String[] could be used to parse a longer text file through chunks.
- We are going to assume all words are separated by spaces.

## To Dos:
- Add an Iterable<String> or a String[] to parse the text file into chunks.
- Add caching to store prefixes/suffixes.
