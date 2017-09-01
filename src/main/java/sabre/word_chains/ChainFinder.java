package sabre.word_chains;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import sabre.word_chains.errors.ErrorMessages;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.MapUtils.isEmpty;

@RequiredArgsConstructor
public class ChainFinder {

    private static final int MAX_SEARCH_LIMIT = 1000;
    private final WordDictionary wordDictionary;

    public List<String> find(String start, String end) {
        validateUserInput(start, end);
        return recreateChainFrom(
                end,
                rawSolution(start, end)
        );
    }

    private void validateUserInput(String start, String end) {
        checkNotNull(start, end);
        checkIfWordsExists(start, end);
        checkIfWordsHaveSameLength(start, end);
    }

    private void checkNotNull(String start, String end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException(ErrorMessages.UNKNOWN_WORDS.getMessage());
        }
    }

    private void checkIfWordsExists(String start, String end) {
        if (wordDictionary.doesNotContain(start) || wordDictionary.doesNotContain(end)) {
            throw new IllegalArgumentException(ErrorMessages.UNKNOWN_WORDS.getMessage());
        }
    }

    private static void checkIfWordsHaveSameLength(String start, String end) {
        if (start.length() != end.length()) {
            throw new IllegalArgumentException(ErrorMessages.DIFFERENT_SIZE_WORDS.getMessage());
        }
    }

    private Map<String, String> rawSolution(String start, String end) {
        Map<String, String> visited = new HashMap<>();
        List<String> candidates = new LinkedList<>();
        int searchLimit = 0;

        visited.put(start, null);
        candidates.add(start);

        while (candidates.size() != 0) {
            if (++searchLimit > MAX_SEARCH_LIMIT) return emptyMap();

            String candidate = candidates.remove(0);
            List<String> potentialCandidates = findPotentialCandidates(visited, candidate);
            markPotentialCandidatesAsVisited(potentialCandidates, visited, candidate);
            Optional<String> solution = getCurrentSolution(end, potentialCandidates);

            if (solution.isPresent()) {
                return visited;
            } else {
                candidates.addAll(potentialCandidates);
            }
        }

        return emptyMap();
    }

    private static List<String> recreateChainFrom(String end, Map<String, String> rawSolution) {
        if (isEmpty(rawSolution)) {
            return emptyList();
        }

        List<String> chain = Lists.newArrayList(end);

        String nextWord = rawSolution.get(end);

        while (nextWord != null) {
            chain.add(nextWord);
            nextWord = rawSolution.get(nextWord);
        }

        Collections.reverse(chain);
        return chain;
    }

    private List<String> findPotentialCandidates(Map<String, String> visited, String candidate) {
        return wordDictionary.getWordsThatDifferWithOneLetterFrom(candidate).stream()
                .filter(word -> !visited.containsKey(word))
                .collect(toList());
    }

    private static void markPotentialCandidatesAsVisited(List<String> potentialCandidates, Map<String, String> visited, String candidate) {
        potentialCandidates.forEach(word -> visited.put(word, candidate));
    }

    private static Optional<String> getCurrentSolution(String end, List<String> potentialCandidates) {
        return potentialCandidates.stream()
                .filter(end::equals)
                .findFirst();
    }
}
