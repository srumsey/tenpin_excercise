package dojo;

import com.google.common.base.Optional;

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
        if (!presentFrame().isOpenFrame()) {
            newFrame();
        }
        presentFrame().roll(pins);
    }

    public Integer score() {
        Integer score = 0;

        for (Frame frame : frames) {
            score += frame.totalScore();
            if (frame.isStrike() || frame.isSpare()) {
                score += bonusScoreForFrame(frame);
            }
        }
        return score;
    }

    private Frame presentFrame() {
        return frames.get(frames.size()-1);
    }

    private void newFrame() {
        frames.add(new Frame());
        if (frames.size() == FRAMES_IN_GAME) {
            presentFrame().setFinalFrame(true);
        }
    }

    private Optional<Frame> nextFrame(Frame frame) {
        Integer nextFrameIndex = frames.indexOf(frame) + 1;
        return nextFrameIndex >= frames.size() ? Optional.fromNullable((Frame) null) : Optional.of(frames.get(nextFrameIndex));
    }

    private Integer bonusScoreForFrame(Frame frame) {

        Integer bonusScore = 0;
        Integer bonusBalls = 0;

        if (frame.isStrike()) {
            bonusBalls = EXTRA_BALLS_FOR_STRIKE;
        } else if (frame.isSpare()) {
            bonusBalls = EXTRA_BALLS_FOR_SPARE;
        }

        Optional<Frame> bonusFrame = nextFrame(frame);
        if (bonusFrame.isPresent()) {
            bonusScore += bonusFrame.get().bonusBallsScore(bonusBalls);
            bonusBalls -= bonusFrame.get().bonusBallsUsed();

            if (bonusBalls > 0) {
                bonusFrame = nextFrame(bonusFrame.get());
                if (bonusFrame.isPresent()) {
                    bonusScore += bonusFrame.get().bonusBallsScore(bonusBalls);
                }
            }
        }

        return bonusScore;
    }
}