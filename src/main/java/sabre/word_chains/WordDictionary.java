package sabre.word_chains;

import com.google.common.base.Preconditions;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

class WordDictionary {

    private final Map<Integer, List<String>> words;
    private final LevenshteinDistance distance = new LevenshteinDistance();

    WordDictionary(String filePath) {
        Preconditions.checkNotNull(filePath);
        this.words = readFile(filePath);
    }

    List<String> getWordsThatDifferWithOneLetterFrom(String givenWord) {
        return words.getOrDefault(givenWord.length(), emptyList())
                .stream()
                .filter(word -> distance.apply(givenWord, word) == 1)
                .collect(toList());
    }

    boolean doesNotContain(String word) {
        return !words
                .getOrDefault(word.length(), emptyList())
                .contains(word);
    }

    private static Map<Integer, List<String>> readFile(String filePath) {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {

            return lines.collect(groupingBy(String::length));

        } catch (IOException e) {
            e.printStackTrace();
            return emptyMap();
        }
    }
}
