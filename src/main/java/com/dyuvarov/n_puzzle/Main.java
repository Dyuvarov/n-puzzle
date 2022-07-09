package com.dyuvarov.n_puzzle;

import com.dyuvarov.n_puzzle.heuristic.LinearConflictHeuristic;
import com.dyuvarov.n_puzzle.heuristic.ManhattanDistanceHeuristic;
import com.dyuvarov.n_puzzle.heuristic.MisplacedHeuristic;
import com.dyuvarov.n_puzzle.util.SolutionBuilder;

public class Main {

    public static void main(String[] args) {
        var puzzle_3x3 = new int[][]    {   {3, 2, 1},
                                            {4, 5, 0},
                                            {7, 8, 6}
                                        };

        var aStar = new AStar(new MisplacedHeuristic());

        PuzzleState initial = new PuzzleState(0, puzzle_3x3, 2, 0, null, null);
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

/*
LinearConflict:
    Total number of states ever selected in the "opened" set: 2826
    Maximum number of states ever represented in memory at the same time during the search: 2827
    Time to solve: 16 ms
    Total number of moves: 21

ManhattanDistance:
    Total number of states ever selected in the "opened" set: 3043
    Maximum number of states ever represented in memory at the same time during the search: 3044
    Time to solve: 14 ms
    Total number of moves: 21

Misplaced:
    Total number of states ever selected in the "opened" set: 8168
    Maximum number of states ever represented in memory at the same time during the search: 8169
    Time to solve: 28 ms
    Total number of moves: 21
 */

}
