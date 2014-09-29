package dojo

import spock.lang.Specification
import spock.lang.Unroll


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
