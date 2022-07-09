package com.dyuvarov.n_puzzle.util;

import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@UtilityClass
public class PuzzleParser {

    private static final char commentSymbol = '#';

    /**
     * Read puzzle from file
     *
     * @param filePath path to file
     * @return puzzle array int[][]
     */
    public static int[][] parse(String filePath) throws IOException {
        var reader = new BufferedReader(new FileReader(filePath));
        int[][] puzzle = null;
        int size = 0;
        var line = reader.readLine();
        int rowInd = 0;
        while (line != null) {
            line = clearFromComment(line);
            if (!line.isEmpty()) {
                if (size == 0) {
                    size = parseNumber(line);
                    puzzle = new int[size][];
                } else {
                    if (rowInd >= size) {
                        throw new RuntimeException("Wrong number of rows!");
                    }
                    puzzle[rowInd++] = stringToArrayRow(line, size);
                }
            }
            line = reader.readLine();
        }
        reader.close();

        if (puzzle == null) {
            throw new RuntimeException("Puzzle cant be empty");
        }

        return puzzle;
    }

    private static String clearFromComment(String line) {
        int commentStart = line.indexOf(commentSymbol);
        if (commentStart >= 0) {
            line = line.substring(0, commentStart);
        }
        return line;
    }

    private static int parseNumber(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Parsing error: wrong number " + s);
        }
    }

    private static int[] stringToArrayRow(String s, int size) {
        String[] splitted = s.trim().split("\\s+");
        if (splitted.length != size) {
            throw new RuntimeException(String.format("line '%s' has wrong number of columns", s));
        }
        int[] row = new int[size];
        for (int i = 0; i < size; ++i) {
            row[i] = parseNumber(splitted[i]);
        }
        return row;
    }
}
