package Model;

import Client.*;
import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel {
    private Maze maze;
    private int charCol, charRow;
    private Solution solution;

    public MyModel() {
        solution = null;
        charCol = 0;
        charRow = 0;
    }

    @Override
    public void generateMaze(int row, int col) {
        ClientStrategyGenerateMaze clientStrategyGenerateMaze = new ClientStrategyGenerateMaze(row, col);
        try {
            Client mazeGenerate = new Client(InetAddress.getLocalHost(), 5400, clientStrategyGenerateMaze);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        maze = clientStrategyGenerateMaze.getMaze();
        setChanged();
        notifyObservers(maze);

    }

    @Override
    public void solveMaze() {
        if (maze == null)
            return;
        ClientStrategySolveMaze clientStrategySolveMaze = new ClientStrategySolveMaze(maze);
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, clientStrategySolveMaze);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        solution = clientStrategySolveMaze.getSolution();
        setChanged();
        notifyObservers(solution);
    }

    @Override
    public void moveCharacter(int direction) {
        /*
            direction = 1 -> Up
            direction = 2 -> Down
            direction = 3 -> Left
            direction = 4 -> Right
         */

        switch (direction) {
            case 1: //Up
                if (charRow != 0)
                    charRow--;
                break;

            case 2: //Down
                if (charRow != maze.getMazeMatrix().length - 1)
                    charRow++;
                break;
            case 3: //Left
                if (charCol != 0)
                    charCol--;
                break;
            case 4: //Right
                if (charCol != maze.getMazeMatrix()[0].length - 1)
                    charCol++;
                break;
        }


        setChanged();
        notifyObservers();

    }

    @Override
    public int[][] getMazeMatrix() {
        return maze.getMazeMatrix();
    }

    @Override
    public int getCharacterRow() {
        return charRow;
    }

    @Override
    public int getCharacterCol() {
        return charCol;
    }

    @Override
    public List<AState> getSolution() {
        return solution.getSolutionPath();
    }

    @Override
    public void assignObserver(Observer o) {
        addObserver(o);
    }
}
