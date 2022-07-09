package com.dyuvarov.n_puzzle.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class PuzzleValidator {
    /**
     * Calculate goal row for cell
     *
     * @param value cell value
     * @param cols number of columns in puzzle
     * @return goal row index (count from 0)
     */
    public static int goalRow(int value, int cols) {
        if (value == 0) {
            return cols-1;
        }
        return --value / cols;
    }

    /**
     * Calculate goal column for cell
     *
     * @param value cell value
     * @param cols number of columns in puzzle
     * @return goal column index (count from 0)
     */
    public static int goalCol (int value, int cols) {
        if (value == 0) {
            return cols-1;
        }
        --value;
        return value < cols ? value : value % cols;
    }

    /**
     * Check cells values in puzzle
     * @return true when all required values are present, else false
     */
    public static boolean validateNumbers(int[][] puzzle) {
        Set<Integer> required = IntStream.range(0, puzzle.length*puzzle.length)
                .boxed()
                .collect(Collectors.toSet());
        Set<Integer> actual = Arrays.stream(puzzle)
                .map(Arrays::stream)
                .flatMap(IntStream::boxed)
                .collect(Collectors.toSet());
        required.removeAll(actual);
        return required.isEmpty();
    }

    /**
     * Check puzzle solvable
     * If it needs even number of cell substitutions to get solved puzzle - puzzle unsolvable,
     * else solvable
     *
     * @return true when puzzle has solution, else false
     */
    public static boolean puzzleSolvable(int[][] puzzle) {
        int substitutions = 0;
        for (int i = 0; i < puzzle.length; ++i) {
            for (int j = 0; j < puzzle.length; ++j) {
                int goalRow = goalRow(puzzle[i][j], puzzle.length);
                int goalColumn = goalCol(puzzle[i][j], puzzle.length);
                while (i != goalRow || j != goalColumn) {
                    //swap elements
                    int tmp = puzzle[i][j];
                    puzzle[i][j] = puzzle[goalRow][goalColumn];
                    puzzle[goalRow][goalColumn] = tmp;
                    ++substitutions;
                    goalRow = goalRow(puzzle[i][j], puzzle.length);
                    goalColumn = goalCol(puzzle[i][j], puzzle.length);
                }
            }
        }
        return substitutions % 2 != 0;
    }
}
