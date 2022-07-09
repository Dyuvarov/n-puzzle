package com.dyuvarov.n_puzzle;

import com.dyuvarov.n_puzzle.heuristic.LinearConflictHeuristic;
import com.dyuvarov.n_puzzle.heuristic.ManhattanDistanceHeuristic;
import com.dyuvarov.n_puzzle.heuristic.MisplacedHeuristic;
import com.dyuvarov.n_puzzle.util.SolutionBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        validateArgs(args);

        int[][] puzzle;
        try {
            puzzle = PuzzleParser.parse(args[0]);
        } catch (RuntimeException e) {
            System.out.println("ERROR: " + e.getMessage());
            return;
        } catch (FileNotFoundException e) {
            System.out.println(String.format("ERROR: File %s not found", args[0]));
            return;
        } catch (IOException e) {
            System.out.println("Error while parsing");
            return;
        }

        //todo validate numbers in puzzle

        var aStar = new AStar(new MisplacedHeuristic());

        PuzzleState initial = new PuzzleState(0, puzzle, 2, 0, null, null);
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

    private static void validateArgs(String[] args) {
        if (args.length != 2) {
            System.out.println("Wrong number of arguments!\nRequired 2 arguments:\n fileName (file with puzzle)\n heuristic [0 - misplaced, 1 - Manhattan distance, 2 - linear conflict]");
            System.exit(1);
        }

        Integer heuristic;
        try {
            heuristic = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            heuristic = null;
        }
        if (heuristic == null || heuristic < 1 || heuristic > 3) {
            System.out.println("Wrong heuristic value. Allowed values: 1, 2, 3");
            System.exit(1);
        }
    }
}
