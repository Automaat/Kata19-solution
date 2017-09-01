package sabre.word_chains

import sabre.word_chains.errors.ErrorMessages
import spock.lang.Specification
import spock.lang.Unroll

class ChainFinderSpec extends Specification {

    def wordDictionaryMock = new WordDictionary("src/test/resources/dictionary_full.txt")

    def chainFinder = new ChainFinder(wordDictionaryMock)

    @Unroll
    def 'should throw exception with message: #expectedMessage'() {
        when:
        chainFinder.find(start, end)

        then:
        def exception = thrown(IllegalArgumentException)
        exception.getMessage() == expectedMessage

        where:
        start           | end               | expectedMessage
        "nonExisting"   | "nonExisting"     | ErrorMessages.UNKNOWN_WORDS.getMessage()
        null            | "nonExisting"     | ErrorMessages.UNKNOWN_WORDS.getMessage()
        "nonExisting"   | null              | ErrorMessages.UNKNOWN_WORDS.getMessage()
        "zoetic"        | "zoetropes"       | ErrorMessages.DIFFERENT_SIZE_WORDS.getMessage()

    }

    @Unroll
    def 'should find chain for start: #start, end: #end'() {
        expect:
        def find = chainFinder.find(start, end)
        find == expectedList

        where:
        start       |   end         |   expectedList
        "cat"       |   "dog"       |   ["cat", "cot", "dot", "dog"]
        "gold"      |   "lead"      |   ["gold", "goad", "load", "lead"]
        "ruby"      |   "code"      |   ["ruby", "rube", "rude", "rode", "code"]
        "brushing"  |   "cheating"  |   ["brushing", "crushing", "crusting", "cresting", "creating", "cheating"]
        "yumpies"   |   "yobboes"   |   []
    }
}
