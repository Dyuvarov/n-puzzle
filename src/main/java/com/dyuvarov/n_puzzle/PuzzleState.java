package com.dyuvarov.n_puzzle;

import com.dyuvarov.n_puzzle.type.Move;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

/** Representation of puzzle state. */
@Getter
@NoArgsConstructor
public class PuzzleState {

    /** Empty field value */
    public static final int EMPTY = 0;

    /** State assess. f(x) value for A* algorithm */
    @Setter
    private int value;

    /** Depth in all states graph, i.e. step number in the solution */
    private int depth;

    /** Current state of puzzle with numbers at actual positions */
    private int[][] puzzle;

    /** Empty field column index (counting from 0) */

    private int emptyCol;

    /** Empty field row index (counting from 0) */
    private int emptyRow;

    /** State from which current state received */
    private PuzzleState previousState;

    /** Move to receive current state from previous state */
    private Move    move;

    public PuzzleState(int depth, int[][] puzzle, int emptyCol, int emptyRow, PuzzleState previousState, Move move) {
        this.depth = depth;
        this.puzzle = puzzle;
        this.emptyCol = emptyCol;
        this.emptyRow = emptyRow;
        this.previousState = previousState;
        this.move = move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PuzzleState that = (PuzzleState) o;
        return Arrays.deepEquals(puzzle, that.puzzle);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(puzzle);
    }

    /** Get copy of puzzle */
    public int[][] getPuzzleCopy() {
        if (puzzle == null) {
            return null;
        }
        return puzzle.clone();
    }
}
