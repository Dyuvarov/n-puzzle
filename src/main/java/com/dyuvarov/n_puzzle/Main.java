package com.dyuvarov.n_puzzle;

import com.dyuvarov.n_puzzle.heuristic.MisplacedHeuristic;
import com.dyuvarov.n_puzzle.util.SolutionBuilder;

public class Main {

    public static void main(String[] args) {
        int[][] puzzle_3x3 = {  {1, 2, 0},
                                {4, 5, 3},
                                {7, 8, 6}
                            };

        AStar aStar = new AStar(new MisplacedHeuristic());

        PuzzleState initial = new PuzzleState(0, puzzle_3x3, 2, 0, null, null);
        PuzzleState solved = aStar.execute(initial);
        System.out.println("Visited states number: " + aStar.getVisitedStates());
        System.out.println("States represented at memory: " + aStar.getOpenedSet().size());
        var sb = new SolutionBuilder();
        sb.buildSolution(solved);
        System.out.println("Total number of moves: " + sb.getMovesCount());
        System.out.println("Moves sequence:");
        System.out.println(sb.getSolutionSequence());
    }



}
