package com.dyuvarov.n_puzzle.heuristic;

import com.dyuvarov.n_puzzle.PuzzleState;
import com.dyuvarov.n_puzzle.util.PuzzleValidator;
import lombok.RequiredArgsConstructor;

/**
 * Improved manhattan distance heuristic.
 * If to cells in one row or column and their goal positions in same row or column
 * add 2 to manhattan distance. Because it requires 2 extra moves (up,down or left,right) to resolve this situation.
 */
@RequiredArgsConstructor
public class LinearConflictHeuristic extends ManhattanDistanceHeuristic{

    @Override
    public int calculate(PuzzleState state) {
        int lineConflicts = rowsConflicts(state.getPuzzle()) + columnsConflicts(state.getPuzzle());
        return super.calculate(state) + lineConflicts*2;
    }

    /** Calculate count of conflicts int rows */
    private int rowsConflicts(int[][] puzzle) {
        int conflicts = 0;
        for (int i = 0; i < puzzle.length; ++i) {
            int currentRowMax = 0;
            for (int j = 0; j < puzzle.length; ++j) {
                int value = puzzle[i][j];
                if (value != PuzzleState.EMPTY && PuzzleValidator.goalRow(value, puzzle.length) == i) {
                    if (value <= currentRowMax) {
                        ++conflicts;
                        break; //only one conflict per line
                    }
                    currentRowMax = value;
                }
            }
        }
        return conflicts;
    }

    /** Calculate count of conflicts int columns */
    private int columnsConflicts(int[][] puzzle) {
        int conflicts = 0;
        for (int i = 0; i < puzzle.length; ++i) {
            int currentColMax = 0;
            for (int j = 0; j < puzzle.length; ++j) {
                int value = puzzle[j][i];
                if (value != PuzzleState.EMPTY && PuzzleValidator.goalCol(value, puzzle.length) == i) {
                    if (value <= currentColMax) {
                        ++conflicts;
                        break; //only one conflict per line
                    }
                    currentColMax = value;
                }
            }
        }
        return conflicts;
    }
}
