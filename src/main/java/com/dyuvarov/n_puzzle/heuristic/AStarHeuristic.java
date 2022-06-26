package com.dyuvarov.n_puzzle.heuristic;

import com.dyuvarov.n_puzzle.PuzzleState;

/** Calculates heuristic function h(x) for A* algorithm */
public interface AStarHeuristic {
    int calculate(PuzzleState state);
}
