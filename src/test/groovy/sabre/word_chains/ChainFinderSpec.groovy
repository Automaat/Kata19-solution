package sabre.word_chains

import spock.lang.Specification
import spock.lang.Unroll

import static org.assertj.core.api.Assertions.assertThat

class ChainFinderSpec extends Specification {

    def wordDictionaryMock = new WordDictionary("src/test/resources/dictionary_full.txt")

    def chainFinder = new ChainFinder(wordDictionaryMock)

    def "should validate if words exists"() {
        given:
        def start = "nonExisting"
        def end = "nonExisting"

        when:
        chainFinder.find(start, end)

        then:
        def exception = thrown(RuntimeException)
        exception.getMessage() == "Unknown words"
    }

    def "should validate if words have the same length"() {
        given:
        def start = "zoetic"
        def end = "zoetropes"

        when:
        chainFinder.find(start, end)

        then:
        def exception = thrown(RuntimeException)
        exception.getMessage() == "Words must have the same size"
    }

    @Unroll
    def "should find chain for start: #start, end: #end"() {
        expect:
        def find = chainFinder.find(start, end)
        assertThat(find).hasSameElementsAs(expectedList)

        where:
        start       |   end         |   expectedList
        "cat"       |   "dog"       |   ["cat", "cot", "cog", "dog"]
        "gold"      |   "lead"      |   ["lead", "load", "goad", "gold"]
        "ruby"      |   "code"      |   ["ruby", "rube", "robe", "rode", "code"]
        "brushing"  |   "cheating"  |   ["brushing", "crushing", "crusting", "cresting", "creating", "cheating"]
        "yumpies"   |   "yobboes"   |   []
    }
}
