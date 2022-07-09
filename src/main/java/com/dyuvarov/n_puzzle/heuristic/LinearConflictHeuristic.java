package com.dyuvarov.n_puzzle.heuristic;

import com.dyuvarov.n_puzzle.PuzzleState;
import com.dyuvarov.n_puzzle.util.PuzzleValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LinearConflictHeuristic extends ManhattanDistanceHeuristic{

    @Override
    public int calculate(PuzzleState state) {
        int lineConflicts = rowsConflicts(state.getPuzzle()) + columnsConflicts(state.getPuzzle());
        return super.calculate(state) + lineConflicts*2;
    }

    private int rowsConflicts(int[][] puzzle) {
        int conflicts = 0;
        for (int i = 0; i < puzzle.length; ++i) {
            int currentRowMax = 0;
            for (int j = 0; j < puzzle.length; ++j) {
                int value = puzzle[i][j];
                if (value != PuzzleState.EMPTY && PuzzleValidator.goalRow(value, puzzle.length) == i) {
                    if (value <= currentRowMax) {
                        ++conflicts;
                        break;
                    }
                    currentRowMax = value;
                }
            }
        }
        return conflicts;
    }

    private int columnsConflicts(int[][] puzzle) {
        int conflicts = 0;
        for (int i = 0; i < puzzle.length; ++i) {
            int currentColMax = 0;
            for (int j = 0; j < puzzle.length; ++j) {
                int value = puzzle[j][i];
                if (value != PuzzleState.EMPTY && PuzzleValidator.goalCol(value, puzzle.length) == i) {
                    if (value <= currentColMax) {
                        ++conflicts;
                        break;
                    }
                    currentColMax = value;
                }
            }
        }
        return conflicts;
    }
}
