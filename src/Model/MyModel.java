package Model;

import Client.*;
import View.Main;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel {
    private Maze maze;
    private int charCol, charRow;
    private Solution solution;

    public void setMaze(Maze maze) {
        this.maze = maze;
        setChanged();
        notifyObservers(maze);
    }

    @Override
    public void setCharacterPosition(Position charPosition) {
        charRow = charPosition.getRowIndex();
        charCol = charPosition.getColumnIndex();
        setChanged();
        notifyObservers(maze);
    }

    public MyModel() {
    }

    @Override
    public void generateMaze(int row, int col) {

        ClientStrategyGenerateMaze clientStrategyGenerateMaze = new ClientStrategyGenerateMaze(row, col);
        try {
            Client mazeGenerate = new Client(InetAddress.getLocalHost(), 5400, clientStrategyGenerateMaze);
            mazeGenerate.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        maze = clientStrategyGenerateMaze.getMaze();
        charRow = maze.getStartPosition().getRowIndex();
        charCol = maze.getStartPosition().getColumnIndex();
        setChanged();
        notifyObservers(maze);
    }

    @Override
    public void solveMaze() {
        if (maze == null)
            return;
        maze.getStartPosition().setRow(charRow);
        maze.getStartPosition().setColumn(charCol);
        ClientStrategySolveMaze clientStrategySolveMaze = new ClientStrategySolveMaze(maze);
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, clientStrategySolveMaze);
            client.communicateWithServer();
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
                if (charRow != 0 && maze.getMazeMatrix()[charRow - 1][charCol] != 1)
                    charRow--;
                break;

            case 2: //Down
                if (charRow != maze.getMazeMatrix().length - 1 && maze.getMazeMatrix()[charRow + 1][charCol] != 1)
                    charRow++;
                break;
            case 3: //Left
                if (charCol != 0 && maze.getMazeMatrix()[charRow][charCol - 1] != 1)
                    charCol--;
                break;
            case 4: //Right
                if (charCol != maze.getMazeMatrix()[0].length - 1 && maze.getMazeMatrix()[charRow][charCol + 1] != 1)
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
    public int getGoalRow() {
        return maze.getGoalPosition().getRowIndex();
    }

    @Override
    public int getGoalCol() {
        return maze.getGoalPosition().getColumnIndex();
    }

    @Override
    public List<AState> getSolution() {
        return solution.getSolutionPath();
    }

    @Override
    public void assignObserver(Observer o) {
        addObserver(o);
    }

    public void loadMazeFromFile(File mazeFile) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(mazeFile);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Pair<Maze, Position> mazePlusCharacterPosition = (Pair<Maze, Position>) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        this.setMaze(mazePlusCharacterPosition.getKey());
        this.setCharacterPosition(mazePlusCharacterPosition.getValue());
    }

    @Override
    public Pair<Maze, Position> getObjectToSaveMaze() {
        Pair<Maze, Position> obj = new Pair<Maze, Position>(maze, new Position(charRow, charCol));
        return obj;
    }
}
