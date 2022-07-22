package com.dyuvarov.n_puzzle.util;

import com.dyuvarov.n_puzzle.PuzzleState;
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
     * If N is odd, then puzzle instance is solvable if number of inversions is even in the input state.
     * If N is even, puzzle instance is solvable if:
     *      the blank is on an even row counting from the bottom and number of inversions is odd.
     *      the blank is on an odd row counting from the bottom  and number of inversions is even.
     * For all other cases, the puzzle instance is not solvable.
     *
     * @return true when puzzle solvable, else false
     */
    public static boolean puzzleSolvable(PuzzleState state) {
        int inversions = 0;
        int size = state.getPuzzle().length;
        int[] nums = Arrays.stream(state.getPuzzle()).flatMapToInt(Arrays::stream).toArray();
        for (int i = 0; i < nums.length; ++i) {
            if (nums[i] == PuzzleState.EMPTY) {
                continue;
            }
            for (int j = i+1; j < nums.length; ++j) {
                if (nums[j] != PuzzleState.EMPTY && nums[i] > nums[j]) {
                    ++inversions;
                }
            }
        }

        if (size % 2 == 0) {
            if (inversions%2 == 0) {
                return (size - state.getEmptyRow()) % 2 != 0;
            } else {
                return (size - state.getEmptyRow()) % 2 == 0;
            }
        } else {
            return inversions % 2 == 0;
        }
    }
}
