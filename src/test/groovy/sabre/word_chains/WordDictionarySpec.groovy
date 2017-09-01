package sabre.word_chains

import sabre.word_chains.errors.ErrorMessages
import spock.lang.Specification
import spock.lang.Unroll

class WordDictionarySpec extends Specification {

    def dictionary = new WordDictionary("src/test/resources/dictionary.txt")

    @Unroll
    def 'dictionary should contain #word'() {
        when:
        def notContains = dictionary.doesNotContain(word)

        then:
        notContains == expectedResult

        where:
        word        | expectedResult
        "pies"      | false
        "kot"       | true
    }

    @Unroll
    def 'dictionary should throw exception #expectedMessage when opening broken file'() {
        when:
        def dictionary = new WordDictionary(filePath)

        then:
        def exception = thrown(IllegalArgumentException)
        exception.getMessage() == expectedMessage

        where:
        filePath                                        | expectedMessage
        null                                            | ErrorMessages.FILE_DOES_NOT_EXISTS.getMessage()
        "src/test/resources/corrupted_dictionary.txt"   | ErrorMessages.COULD_NOT_READ_FILE.getMessage()
    }

    @Unroll
    def 'dictionary should contain #count word differing with one letter from #word'() {
        expect:
        dictionary.getWordsThatDifferWithOneLetterFrom(word).size() == count

        where:
        word    |   count
        "pies"  |       4
        "cat"   |       2
    }
}
