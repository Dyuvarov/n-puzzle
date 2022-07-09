package com.dyuvarov.n_puzzle.heuristic;

import com.dyuvarov.n_puzzle.PuzzleState;

/** Calculate total number of cells with wrong position */
public class MisplacedHeuristic implements AStarHeuristic{
    @Override
    public int calculate(PuzzleState state) {
        int wrong = 0; //misplaced cells counter
        int fv = 1; //cell valid value
        int[][] puzzle = state.getPuzzle();
        for (var i = 0; i < puzzle.length; ++i) {
            for (var j = 0; j < puzzle[i].length; ++j) {
                if (puzzle[i][j] != fv++ && puzzle[i][j] != PuzzleState.EMPTY) {
                    ++wrong;
                }
            }
        }
        return wrong;
    }
}
