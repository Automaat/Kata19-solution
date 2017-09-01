package sabre.word_chains.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessages {

    UNKNOWN_WORDS("Unknown words"),
    DIFFERENT_SIZE_WORDS("Words must have the same size"),
    FILE_DOES_NOT_EXISTS("You have to specify file path!"),
    COULD_NOT_READ_FILE("File could not be read");

    private final String message;
}
