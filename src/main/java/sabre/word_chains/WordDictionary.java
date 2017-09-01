package sabre.word_chains;

import org.apache.commons.text.similarity.LevenshteinDistance;
import sabre.word_chains.errors.ErrorMessages;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

class WordDictionary {

    private static final int LEVENSHTEIN_SCORE = 1;
    private final Map<Integer, Set<String>> words;
    private final LevenshteinDistance distance = new LevenshteinDistance();

    WordDictionary(String filePath) {
        checkNotNull(filePath);
        this.words = readFile(filePath);
    }

    Set<String> getWordsThatDifferWithOneLetterFrom(String givenWord) {
        return words.getOrDefault(givenWord.length(), emptySet())
                .stream()
                .filter(word -> distance.apply(givenWord, word) == LEVENSHTEIN_SCORE)
                .collect(toSet());
    }

    boolean doesNotContain(String word) {
        return !words
                .getOrDefault(word.length(), emptySet())
                .contains(word);
    }

    private void checkNotNull(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException(ErrorMessages.FILE_DOES_NOT_EXISTS.getMessage());
        }
    }

    private static Map<Integer, Set<String>> readFile(String filePath) {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {

            return lines.collect(groupingBy(String::length, toSet()));

        } catch (Exception e) {
            throw new IllegalArgumentException(ErrorMessages.COULD_NOT_READ_FILE.getMessage());
        }
    }
}
