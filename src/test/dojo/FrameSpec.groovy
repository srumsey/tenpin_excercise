package dojo

import dojo.exception.GameAlreadyCompleteException
import spock.lang.Specification
import spock.lang.Unroll
import sun.plugin.dom.exception.InvalidStateException


class FrameSpec extends Specification {

    @Unroll
    void "Score for #title frame"() {
        given:
        Frame frame = new Frame();

        when:
        for (int roll : rolls) {
            frame.roll(roll)
        }

        then:
        frame.totalScore() == expectedScore
        frame.totalRolls() == expectedBalls
        frame.isStrike() == expectedStrike
        frame.isSpare() == expectedSpare
        frame.isOpenFrame() == expectedFrameOpen
        frame.firstRollScore() == expectedFirstBallScore

        where:
        title                              | expectedScore | expectedStrike | expectedSpare | expectedBalls | expectedFrameOpen | expectedFirstBallScore | rolls
        "no score one ball"                | 0             | false          | false         | 1             | true              | 0                      | [0]
        "5 scored one ball"                | 5             | false          | false         | 1             | true              | 5                      | [5]
        "no score"                         | 0             | false          | false         | 2             | false             | 0                      | [0, 0]
        "score first ball"                 | 1             | false          | false         | 2             | false             | 1                      | [1, 0]
        "score second ball"                | 1             | false          | false         | 2             | false             | 0                      | [0, 1]
        "strike"                           | 10            | true           | false         | 1             | false             | 10                     | [10]
        "spare"                            | 10            | false          | true          | 2             | false             | 5                      | [5,5]
        "spare 10 seconds ball"            | 10            | false          | true          | 2             | false             | 0                      | [0,10]
    }

    @Unroll
    void "Expect exception because #title"() {
        given:
        Frame frame = new Frame();
        frame.setFinalFrame(finalFrame)

        when:
        for (int roll : rolls) {
            frame.roll(roll)
        }

        then:
        thrown(InvalidStateException)

        where:
        title                              | finalFrame | rolls
        "score ball 1 less than 0"         | false      | [-1, 0]
        "score ball 2 less than 0"         | false      | [0, -1]
        "score ball 1 greater than 10"     | false      | [11, 0]
        "score ball 2 greater than 10"     | false      | [0, 11]
        "score grater than 10"             | false      | [5, 6]
        "score grater than 10 again"       | false      | [6, 5]
        "3rd roll normal frame"            | false      | [0, 0, 0]

        "final frame not strike or spare"  | true       | [0, 0, 0]
        "final strike 4 balls rolled"      | true       | [10, 10, 10, 10]
        "final spare 4 balls rolled"       | true       | [5, 5, 10, 10]
    }

    @Unroll
    void "Bonus score and balls used #title frame"() {
        given:
        Frame frame = new Frame();

        when:
        for (int roll : rolls) {
            frame.roll(roll)
        }

        then:
        frame.bonusBallsScore(2) == expectedBonus2Balls
        frame.bonusBallsScore(1) == expectedBonus1Ball
        frame.bonusBallsUsed() == expectedBallsUsed

        where:
        title                              | expectedBonus2Balls | expectedBonus1Ball | expectedBallsUsed  | rolls
        "no score"                         | 0                   | 0                  | 2                  | [0, 0]
        "strike"                           | 10                  | 10                 | 1                  | [10]
        "spare"                            | 10                  | 5                  | 2                  | [5,5]
    }

    @Unroll
    void "Final frame #title"() {
        given:
        Frame frame = new Frame();
        frame.setFinalFrame(true);

        when:
        for (int roll : rolls) {
            frame.roll(roll)
        }

        then:
        frame.bonusBallsScore(2) == expectedBonus2Balls
        frame.bonusBallsScore(1) == expectedBonus1Ball
        frame.totalScore() == expectedScore
        frame.isOpenFrame() == expectedOpenFrame

        where:
        title                              | expectedScore | expectedBonus2Balls | expectedBonus1Ball   | expectedOpenFrame  | rolls
        "no score"                         | 0             | 0                   | 0                    | false              | [0, 0]
        "one roll no score"                | 0             | 0                   | 0                    | true               | [0]
        "strike"                           | 10            | 10                  | 10                   | true               | [10]
        "strike and 1 bonus roll"          | 15            | 15                  | 10                   | true               | [10,5]
        "strike and 2 bonus roll"          | 20            | 15                  | 10                   | false              | [10,5,5]
        "spare"                            | 10            | 10                  | 5                    | true               | [5,5]
        "spare and 1 bonus"                | 15            | 10                  | 5                    | false              | [5,5,5]
        "strike bonus strike"              | 20            | 20                  | 10                   | true               | [10,10]
        "strike bonus strike + 5"          | 25            | 20                  | 10                   | false              | [10,10,5]
    }
}
