package com.dyuvarov.n_puzzle.util;

import com.dyuvarov.n_puzzle.PuzzleState;
import lombok.Getter;

import java.util.StringJoiner;

/** Print puzzle solution */
public class SolutionBuilder {
    private StringJoiner sj = new StringJoiner("\n↓\n");

    @Getter
    private int movesCount = -1;

    /**
     * Recursively build string representation for each state of solution
     */
    public void buildSolution(PuzzleState state) {
        if (state == null) {
            return;
        }
        buildSolution(state.getPreviousState());
        sj.add(state.puzzleToString());
        ++movesCount;
    }

    /** Whole solution as string */
    public String getSolutionSequence() {
        return sj.toString();
    }
}
