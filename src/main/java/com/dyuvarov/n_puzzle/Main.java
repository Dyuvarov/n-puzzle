package com.dyuvarov.n_puzzle;

import com.dyuvarov.n_puzzle.heuristic.AStarHeuristic;
import com.dyuvarov.n_puzzle.heuristic.LinearConflictHeuristic;
import com.dyuvarov.n_puzzle.heuristic.ManhattanDistanceHeuristic;
import com.dyuvarov.n_puzzle.heuristic.MisplacedHeuristic;
import com.dyuvarov.n_puzzle.util.PuzzleParser;
import com.dyuvarov.n_puzzle.util.PuzzleValidator;
import com.dyuvarov.n_puzzle.util.SolutionBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    /** Map to choose heuristic by passed argument */
    private static final Map<Integer, AStarHeuristic> heuristicMap;
    static {
        heuristicMap = new HashMap<>();
        heuristicMap.put(1, new MisplacedHeuristic());
        heuristicMap.put(2, new ManhattanDistanceHeuristic());
        heuristicMap.put(3, new LinearConflictHeuristic());
    }


    public static void main(String[] args) {
        if (!validateArgs(args)) {
            return;
        }

        int[][] puzzle = readPuzzle(args[0]);
        if (puzzle == null) {
            return;
        }

        if(!PuzzleValidator.validateNumbers(puzzle)) {
            System.out.println("ERROR: wrong numbers in puzzle");
            return;
        }

        short[] emptyCell = findEmptyCell(puzzle);
        if (emptyCell == null) {
            System.out.println("ERROR: no empty field");
            return;
        }
        AStarHeuristic heuristic = heuristicMap.get(Integer.parseInt(args[1]));
        var aStar = new AStar(heuristic);
        PuzzleState initial = new PuzzleState(0, puzzle, emptyCell[1], emptyCell[0], null, null);

        if (!PuzzleValidator.puzzleSolvable(initial)) {
            System.out.println("Puzzle unsolvable!");
            return;
        }

        PuzzleState solved = aStar.execute(initial);
        System.out.println("Total number of states ever selected in the \"opened\" set: " + aStar.getSelectedInOS());
        System.out.println("Maximum number of states ever represented in memory at the same time " +
                "during the search: " + aStar.getMaxStatesInMemory());
        System.out.println("Time to solve: " + aStar.getSolvingTime() + " ms");
        var sb = new SolutionBuilder();
        sb.buildSolution(solved);
        System.out.println("Total number of moves: " + sb.getMovesCount());
        System.out.println("Moves sequence:");
        System.out.println(sb.getSolutionSequence());
    }

    /**
     * read puzzle from file
     *
     * @param filePath file with puzzle
     * @return puzzle array
     */
    private static int[][] readPuzzle(String filePath) {
        int[][] puzzle = null;
        try {
            puzzle = PuzzleParser.parse(filePath);
        } catch (RuntimeException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println(String.format("ERROR: File %s not found", filePath));
        } catch (IOException e) {
            System.out.println("Error while parsing");
        }
        return puzzle;
    }

    /**
     * Check passed args
     *
     * @param args program args
     * @return true when args valid, else false
     */
    private static boolean validateArgs(String[] args) {
        if (args.length != 2) {
            System.out.println("Wrong number of arguments!\nRequired 2 arguments:\n fileName (file with puzzle)\n heuristic [1 - misplaced, 2 - Manhattan distance, 3 - linear conflict]");
            return false;
        }

        Integer heuristic;
        try {
            heuristic = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            heuristic = null;
        }
        if (heuristic == null || heuristic < 1 || heuristic > 3) {
            System.out.println("Wrong heuristic value. Allowed values: 1, 2, 3");
            return false;
        }
        return true;
    }

    /**
     * Find empty cell in puzzle
     *
     * @param puzzle array
     * @return array with 2 items array[0] - row with empty cell, array[1] - column with empty cell
     */
    private static short[] findEmptyCell(int[][] puzzle) {
        for (short i = 0; i < puzzle.length; ++i) {
            for (short j = 0; j < puzzle.length; ++j) {
                if (puzzle[i][j] == PuzzleState.EMPTY) {
                    return new short[] {i, j};
                }
            }
        }
        return null;
    }
}
