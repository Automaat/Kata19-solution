package sabre.word_chains

import spock.lang.Specification
import spock.lang.Unroll

class WordDictionarySpec extends Specification {

    def dictionary = new WordDictionary("src/test/resources/dictionary.txt")

    def "dictionary should contain word"() {
        when:
        def contains = dictionary.doesNotContain("pies")

        then:
        contains
    }

    @Unroll
    def "dictionary should contain #count word differing with one letter from #word"() {
        expect:
        dictionary.getWordsThatDifferWithOneLetterFrom(word).size() == count

        where:
        word    |   count
        "pies"  |       4
        "cat"   |       2
    }
}
