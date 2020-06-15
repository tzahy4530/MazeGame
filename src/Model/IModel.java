package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;

import java.util.List;

public interface IModel {
    public Maze generateMaze(int row, int col);
    public List<AState> solveMaze(Maze toSolve);

}
