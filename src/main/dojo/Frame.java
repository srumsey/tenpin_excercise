package dojo;

import sun.plugin.dom.exception.InvalidStateException;

import java.util.ArrayList;
import java.util.List;

public class Frame {
    private static final int FIRST_BALL = 1;
    private static final int BALLS_IN_STANDARD_FRAME = 2;
    private static final int MAX_BALLS_IN_FINAL_FRAME = 3;
    private static final int TOTAL_PINS_IN_FRAME = 10;
    private static final int MIN_ALLOWED_PINS_PER_ROLL = 0;
    private static final int MAX_ALLOWED_PINS_PER_ROLL = 10;
    private final List<Integer> rolls = new ArrayList<Integer>();
    private Boolean openFrame = true;
    private Boolean finalFrame = false;

    public void roll(Integer pins) {
        if (!isOpenFrame()) {
            throw new InvalidStateException("Cannot add pins to an frame which is not open");
        } else if (pins < MIN_ALLOWED_PINS_PER_ROLL) {
            throw new InvalidStateException("Pins is less than minimun allowed [" + MIN_ALLOWED_PINS_PER_ROLL + "]");
        } else if (pins > MAX_ALLOWED_PINS_PER_ROLL) {
            throw new InvalidStateException("Pins is greater than maximum allowed [" + MAX_ALLOWED_PINS_PER_ROLL + "]");
        } else if (!finalFrame && pins + totalScore() > TOTAL_PINS_IN_FRAME) {
            throw new InvalidStateException("Total pins in frame cannot exceed [" + TOTAL_PINS_IN_FRAME + "]");
        }
        rolls.add(pins);
        setOpenFrame();
    }

    public boolean isStrike() {
        return sumRolls(firstRolls(FIRST_BALL)) == TOTAL_PINS_IN_FRAME;
    }

    public boolean isSpare() {
        return !isStrike() && sumRolls(firstRolls(BALLS_IN_STANDARD_FRAME)) == TOTAL_PINS_IN_FRAME;
    }

    public boolean isOpenFrame() {
        return openFrame;
    }

    public Integer totalScore() {
        return sumRolls(rolls);
    }

    private Integer sumRolls (List<Integer> rollsToSum) {
        Integer sum = 0;
        for (Integer roll : rollsToSum) sum += roll;
        return sum;
    }

    private List<Integer> firstRolls (Integer numberOfRolls) {
        return rolls.size() >= numberOfRolls ? rolls.subList(0, numberOfRolls) : rolls;
    }

    public Integer bonusBallsScore(Integer bonusBalls) {
        return sumRolls(firstRolls(bonusBalls));
    }

    public Integer bonusBallsUsed() {
        return rolls.size() <= BALLS_IN_STANDARD_FRAME ? rolls.size() : BALLS_IN_STANDARD_FRAME;
    }

    public Integer firstRollScore() {
        return sumRolls(firstRolls(FIRST_BALL));
    }

    public Integer totalRolls() {
        return rolls.size();
    }

    private void setOpenFrame() {
        if (finalFrame) {
            if (((isStrike() || isSpare()) && rolls.size() == MAX_BALLS_IN_FINAL_FRAME) || ((!isStrike() && !isSpare()) && rolls.size() == BALLS_IN_STANDARD_FRAME)) {
                openFrame = false;
            }
        } else {
            if (isStrike() || (rolls.size() == BALLS_IN_STANDARD_FRAME)) {
                openFrame = false;
            }
        }
    }

    public void setFinalFrame(Boolean finalFrame) {
        this.finalFrame = finalFrame;
    }

    public Boolean getFinalFrame() {
        return this.finalFrame;
    }
}
