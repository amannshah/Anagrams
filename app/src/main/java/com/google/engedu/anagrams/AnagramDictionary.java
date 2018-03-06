/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    ArrayList<String> wordList = new ArrayList<>();
    HashSet<String> wordSet;
    HashMap<String, ArrayList<String>> lettersToWord;
    HashMap<Integer, ArrayList<String>> sizeToWords;
    private int wordLength;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        wordSet = new HashSet<>();
        lettersToWord = new HashMap<>();
        sizeToWords = new HashMap<>();
        wordLength = DEFAULT_WORD_LENGTH;
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);

            int length = word.length();

            if (sizeToWords.containsKey(length)) {
                sizeToWords.get(length).add(word);
            }
            else {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(word);
                sizeToWords.put(length, arrayList);
            }
                if (lettersToWord.containsKey(sortLetter(word))) {
                    ArrayList<String> anagrams = lettersToWord.get(sortLetter(word));
                    anagrams.add(word);
                    lettersToWord.put(sortLetter(word), anagrams);
                } else {
                    ArrayList<String> anagrams = new ArrayList<>();
                    anagrams.add(word);
                    lettersToWord.put(sortLetter(word), anagrams);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if (!wordSet.contains(word) || word.toLowerCase().contains(base.toLowerCase()))
            return false;
        else return true;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedTargetWord = sortLetter(targetWord);
        for (String s: wordList) {
            if (sortLetter(s).equals(sortedTargetWord)) {
                result.add(s);
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for (char alphabet ='a';alphabet <= 'z';alphabet++) {
            String s = word + alphabet;
            if (lettersToWord.containsKey(sortLetter(s))) {
                result.addAll(lettersToWord.get(sortLetter(s)));
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> wordListSize = sizeToWords.get(wordLength);
        Random random = new Random();
        int size = wordListSize.size();
        int n = random.nextInt(size);
        for (int i=n;i<n + size;i++) {
            String word = wordListSize.get(i % size);
            ArrayList<String> arrayList = lettersToWord.get(sortLetter(word));
            if (arrayList.size() >= MIN_NUM_ANAGRAMS) {
                wordLength++;
                return word;
            }
        }
        wordLength++;
        return "stop";
    }

    public String sortLetter (String s) {
        s=s.toLowerCase();
        char[] c = s.toCharArray();
        Arrays.sort(c);
        String sorted = new String(c);
        return sorted;
    }
}
