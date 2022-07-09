package com.dyuvarov.n_puzzle.util;

import com.dyuvarov.n_puzzle.PuzzleState;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class PuzzleValidator {
    public static int goalRow(int value, int cols) {
        if (value == 0) {
            return cols-1;
        }
        return --value / cols;
    }

    public static int goalCol (int value, int cols) {
        if (value == 0) {
            return cols-1;
        }
        --value;
        return value < cols ? value : value % cols;
    }

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
