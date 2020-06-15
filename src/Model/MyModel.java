package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;

import java.net.Socket;
import java.util.List;
import java.util.Observable;

public class MyModel extends Observable implements IModel  {

    @Override
    public Maze generateMaze(int row, int col) {
        return null;
    }

    @Override
    public List<AState> solveMaze(Maze toSolve) {
        return null;
    }
}
