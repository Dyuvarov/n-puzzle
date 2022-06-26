package com.dyuvarov.n_puzzle;

import com.dyuvarov.n_puzzle.heuristic.AStarHeuristic;
import com.dyuvarov.n_puzzle.type.Move;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

@Getter
@Setter
public class AStar {

    /** All possible states of puzzle */
    private final Set<PuzzleState>    openedSet = new HashSet<>();

    /** Opened set as priority queue to choose best one fast. */
    private final PriorityQueue<PuzzleState>    openedSetQ;

    /** Heuristic function */
    private final AStarHeuristic heuristic;

    /** Visited states counter. For statistics */
    private int visitedStates = 0;


    public AStar(AStarHeuristic heuristic) {
        this.openedSetQ = new PriorityQueue<>((o1, o2) -> o2.getValue() - o1.getValue()); //min element should be on top
        this.heuristic = heuristic;
    }

    /**
     * Search puzzle solution using A* algorithm
     *
     * @param initial puzzle initial state
     * @return goal state
     * @throws RuntimeException when no solution found
     */
    public PuzzleState execute(PuzzleState initial) {
        openedSet.add(initial);

        openedSetQ.add(initial);
        while (!openedSetQ.isEmpty()) {
            var state = openedSetQ.poll();
            ++visitedStates;

            if (stateIsGoal(state)) {
                return state;
            }
            findStates(state);
        }

        throw new RuntimeException("Puzzle can't be resolved");
    }

    /** Check state is solved puzzle */
    private boolean stateIsGoal(PuzzleState state) {
        return false; //todo
    }

    /** Find possible next states for given state by moving empty field */
    private void findStates(PuzzleState state) {
        var row = state.getEmptyRow();
        var col = state.getEmptyCol();

        if (row > 0) {
            //Move empty field UP and create new state
            var puzzle = state.getPuzzleCopy();
            var tmp = puzzle[row-1][col];
            puzzle[row-1][col] = PuzzleState.EMPTY;
            puzzle[row][col] = tmp;
            PuzzleState newState = new PuzzleState(state.getDepth() + 1, puzzle,col, row-1, state, Move.UP);
            addStateToOpenedSet(newState);
        }
    }


    /** Calculate value for given state and add it in opened set */
    private void addStateToOpenedSet(PuzzleState state) {
        if (!openedSet.contains(state)) {
            state.setValue(state.getDepth() + heuristic.calculate(state));
            openedSet.add(state);
            openedSetQ.add(state);
        }
    }
}
