package com.dyuvarov.n_puzzle;

import com.dyuvarov.n_puzzle.heuristic.AStarHeuristic;
import com.dyuvarov.n_puzzle.type.Move;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

@Getter
@Setter
public class AStar {

    /** States to examined and candidates to expansion */
    private final Set<PuzzleState>    openedSet = new HashSet<>();

    /** Opened set as priority queue to choose best one fast */
    private final PriorityQueue<PuzzleState>    openedSetQ;

    /** States already selected by algorithm , compared to solution, and expanded */
    private final Set<PuzzleState>  closedSet = new HashSet<>();

    /** Heuristic function */
    private final AStarHeuristic heuristic;

    /** How many milliseconds search has taken */
    private long solvingTime = 0;

    /** Total number of states ever selected in the "opened" set   */
    private long selectedInOS = 0;

    /**
     * Maximum number of states ever represented in memory at the same time
     * during the search
     */
    private long maxStatesInMemory = 0;


    public AStar(AStarHeuristic heuristic) {
        this.openedSetQ = new PriorityQueue<>(Comparator.comparingInt(PuzzleState::getValue)); //min element should be on top
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
        long start = System.currentTimeMillis();
        openedSet.add(initial);
        openedSetQ.add(initial);

        while (!openedSetQ.isEmpty()) {
            var state = openedSetQ.poll();

            if (stateIsGoal(state.getPuzzle())) {
                this.solvingTime = System.currentTimeMillis() - start;
                return state;
            }
            openedSet.remove(state);
            closedSet.add(state);
            findStates(state);
        }
        this.solvingTime = System.currentTimeMillis() - start;
        throw new RuntimeException("Puzzle can't be resolved");
    }

    /** Check state is solved puzzle */
    private boolean stateIsGoal(int[][] puzzle) {
        var fv = 1; //valid field value

        for (var i = 0; i < puzzle.length; ++i) {//rows cycle
            for (var j = 0; j < puzzle[i].length; ++j) {//col cycle
                if (i == puzzle.length-1 && j == puzzle[i].length-1) {
                    return puzzle[i][j] == PuzzleState.EMPTY; //check last field is empty
                }
                if (puzzle[i][j] != fv++) {
                    return false; //check current field, update valid value
                }
            }
        }
        return true;
    }

    /** Find possible next states for given state by moving empty field */
    private void findStates(PuzzleState state) {
        var row = state.getEmptyRow();
        var col = state.getEmptyCol();

        if (row > 0) {
            //Move empty field UP and create new state
            var puzzle = swapFields(state.getPuzzleCopy(), row, col, row-1, col);
            addStateToOpenedSet(new PuzzleState(state.getDepth() + 1, puzzle, col, row-1, state, Move.UP));
            maxStatesInMemory = Math.max(maxStatesInMemory, openedSet.size() + closedSet.size() + 1); //statistics
        }
        if (row < state.getPuzzle().length-1) {
            //Move empty field down and create new state
            var puzzle = swapFields(state.getPuzzleCopy(), row, col, row+1, col);
            addStateToOpenedSet(new PuzzleState(state.getDepth() + 1, puzzle, col, row+1, state, Move.DOWN));
            maxStatesInMemory = Math.max(maxStatesInMemory, openedSet.size() + closedSet.size() + 1); //statistics
        }
        if (col > 0) {
            //Move empty field left and create new state
            var puzzle = swapFields(state.getPuzzleCopy(), row, col, row, col-1);
            addStateToOpenedSet(new PuzzleState(state.getDepth() + 1, puzzle, col-1, row, state, Move.LEFT));
            maxStatesInMemory = Math.max(maxStatesInMemory, openedSet.size() + closedSet.size() + 1); //statistics
        }
        if (col < state.getPuzzle()[row].length - 1) {
            //Move empty field right and create new state
            var puzzle = swapFields(state.getPuzzleCopy(), row, col, row, col+1);
            addStateToOpenedSet(new PuzzleState(state.getDepth() + 1, puzzle, col+1, row, state, Move.RIGHT));
            maxStatesInMemory = Math.max(maxStatesInMemory, openedSet.size() + closedSet.size() + 1); //statistics
        }
    }

    /**
     * Swap two fields values in given array
     *
     * @param puzzle puzzle to swap fields
     * @param r1 field 1 row
     * @param c1 field 1 column
     * @param r2 field 2 row
     * @param c2 field 2 column
     * @return given array with swapped fields
     */
    private int[][] swapFields(int[][] puzzle, int r1, int c1, int r2, int c2) {
        var tmp = puzzle[r2][c2];
        puzzle[r2][c2] = puzzle[r1][c1];
        puzzle[r1][c1] = tmp;
        return puzzle;
    }

    /** Calculate value for given state and add it in opened set */
    private void addStateToOpenedSet(PuzzleState state) {
        if (!openedSet.contains(state) && !closedSet.contains(state)) {
            state.setValue(state.getDepth() + heuristic.calculate(state));
            openedSet.add(state);
            openedSetQ.add(state);
            ++selectedInOS; //statistics
        }
    }
}
