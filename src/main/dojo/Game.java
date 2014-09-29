package dojo;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private static final int FRAMES_IN_GAME = 10;
    private static final int EXTRA_BALLS_FOR_STRIKE = 2;
    private static final int EXTRA_BALLS_FOR_SPARE = 1;

    private List<Frame> frames = new ArrayList<Frame>();

    public Game() {
        frames.add(new Frame());
    }

    public void roll(int pins) {
        if (!currentFrame().isOpenFrame()) {
            newFrame();
        }
        currentFrame().roll(pins);
    }

    public Integer score() {
        Integer score = 0;

        for (Integer i = 0; i < frames.size(); i++) {
            Frame frame = frames.get(i);
            score += frame.totalScore();

            Integer bonusBalls = 0;
            if (frame.isStrike()) {
                bonusBalls = EXTRA_BALLS_FOR_STRIKE;
            } else if (frame.isSpare()) {
                bonusBalls = EXTRA_BALLS_FOR_SPARE;
            }

            Integer bonusFrame = i + 1;

            if (bonusBalls > 0 && bonusFrame < frames.size()) {
                score += frames.get(bonusFrame).bonusBallsScore(bonusBalls);
                bonusBalls -= frames.get(bonusFrame).bonusBallsUsed();
                bonusFrame++;
            }


            if (bonusBalls > 0 && bonusFrame < frames.size()) {
                score += frames.get(bonusFrame).bonusBallsScore(bonusBalls);
            }
        }
        return score;
    }

    private Frame currentFrame() {
        return frames.get(frames.size()-1);
    }

    private void newFrame() {
        frames.add(new Frame());
        if (frames.size() == FRAMES_IN_GAME) {
            currentFrame().setFinalFrame(true);
        }
    }
}