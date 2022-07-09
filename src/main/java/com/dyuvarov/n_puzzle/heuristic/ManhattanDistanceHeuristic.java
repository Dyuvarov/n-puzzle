package com.dyuvarov.n_puzzle.heuristic;

import com.dyuvarov.n_puzzle.PuzzleState;
import com.dyuvarov.n_puzzle.util.PuzzleValidator;

/**
 * Manhattan-distance heuristic implementation.
 * For each cell count distance between its actual and goal position as |x1 - x2| + |y1 - y2|
 * where x1, x2 - row index, y1, y2 - column index
 * Sum of all distances is result of heuristic function
 */
public class ManhattanDistanceHeuristic implements AStarHeuristic{
    @Override
    public int calculate(PuzzleState state) {
        int result = 0;
        var puzzle = state.getPuzzle();
        for (int x = 0; x < puzzle.length; ++x) {
            for (int y = 0; y < puzzle[0].length; ++y) {
                var value = puzzle[x][y];
                if (value == PuzzleState.EMPTY) {
                    continue;
                }

                var goalRow = PuzzleValidator.goalRow(value, puzzle.length); //puzzle is square, same number of rows and cols.
                var goalCol = PuzzleValidator.goalCol(value, puzzle.length);

                result += Math.abs(x - goalRow) + Math.abs(y - goalCol);
            }
        }
        return result;
    }
}
