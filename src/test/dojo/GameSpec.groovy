package dojo

import dojo.exception.GameAlreadyCompleteException
import spock.lang.Specification
import spock.lang.Unroll
import sun.plugin.dom.exception.InvalidStateException


class GameSpec extends Specification {

    @Unroll
    void "test #title game"() {
        given:
        Game g = new Game();
        for (int roll : rolls) {
            g.roll(roll)
        }

        when:
        int score = g.score()

        then:
        score == expectedScore

        where:
        title                              | expectedScore | rolls
        "no score"                         | 0             | [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        "Rubbish Score"                    | 1             | [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1]
        "Single Strike no bonus"           | 10            | [10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        "Single Strike One pin"            | 12            | [10, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        "Single Strike One pin twice "     | 14            | [10, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        "Single Strike One Pin thrice"     | 15            | [10, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        "Single Spare no bonus"            | 10            | [5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        "Single Spare One bonus"           | 12            | [5, 5, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        "Single Spare One bonus one pin"   | 13            | [5, 5, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        "Single Spare One bonus one pin"   | 21            | [5, 5, 5, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        "Waste of a strike with some pins" | 13            | [0, 10, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        "Turkey"                           | 60            | [10, 10, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        "Perfect Game"                     | 300           | [10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10]
        "Spare then Strike Final Frame"    | 32            | [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 10, 1, 1]
        "Strike 9th frame"                 | 19            | [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 5, 4]
        "Strike 8th, 9th frame"            | 44            | [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 10, 5, 4]
        "Not a spare"                      | 11            | [0, 5, 5, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        "Part of game"                     | 45            | [0, 5, 5, 1, 9, 1, 10, 2]
    }

    @Unroll
    void "test completed #title game"() {
        when:
        Game g = new Game();
        for (int roll : rolls) {
            g.roll(roll)
        }
        then:
        thrown(GameAlreadyCompleteException)

        where:
        title                              | rolls
        "no score"                         | [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        "spare in last frame"              | [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 0, 0]
        "strike in last frame"             | [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 10, 10, 0]
        "Perfect Game"                     | [10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10]
    }

    @Unroll
    void "Expect frame to raise exception because #title"() {
        when:
        Game g = new Game();
        for (int roll : rolls) {
            g.roll(roll)
        }

        then:
        thrown(InvalidStateException)

        where:
        title                              | rolls
        "pins less than zero"              | [0, -1]
        "pins grater than 1"               | [0, 11]
        "total is frame greater than 10"   | [5, 6]
    }
}
