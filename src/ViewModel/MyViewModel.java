package ViewModel;

import Model.IModel;
import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.geometry.Pos;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private int[][] maze;
    private int charRow, charCol;
    private int goalRow, goalCol;
    private IModel model;
    private List<Pair<Integer, Integer>> solution;

    public int getGoalRow() {
        return goalRow;
    }

    public int getGoalCol() {
        return goalCol;
    }

    public int[][] getMaze() {
        return maze;
    }

    public int getCharRow() {
        return charRow;
    }

    public int getCharCol() {
        return charCol;
    }

    public void makeSolutionList(List<AState> solList) {
        List<Pair<Integer, Integer>> solutionAsRowCol = new ArrayList<>();
        for (AState state :
                solList) {
            Position position = (Position) state.getStateValue();
            solutionAsRowCol.add(new Pair<Integer, Integer>(position.getRowIndex(), position.getColumnIndex()));
        }
        solution = solutionAsRowCol;
    }

    public void solve() {
        model.solveMaze();
    }

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
        this.solution = null;
        maze = null;
        charRow = 0;
        charCol = 0;
        goalRow = 0;
        goalCol = 0;
    }

    public void generateMaze(int row, int col) {
        model.generateMaze(row, col);
    }

    public void moveCharacter(KeyEvent keyEvent) {

        switch (keyEvent.getCode()) {
            case UP:
                model.moveCharacter(1);
                break;
            case DOWN:
                model.moveCharacter(2);
                break;
            case LEFT:
                model.moveCharacter(3);
                break;
            case RIGHT:
                model.moveCharacter(4);
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MyModel) {
            if (arg instanceof Maze) {
                //generateMaze
                maze = model.getMazeMatrix();
                charRow = model.getCharacterRow();
                charCol = model.getCharacterCol();
                goalRow = model.getGoalRow();
                goalCol = model.getGoalCol();
                setChanged();
                notifyObservers(maze);
            } else if (arg instanceof Solution) {
                makeSolutionList(model.getSolution());
                setChanged();
                notifyObservers(solution);
            } else { //move // finish
                charRow = model.getCharacterRow();
                charCol = model.getCharacterCol();
                setChanged();
                notifyObservers();
            }
        }
    }


    public void saveMaze(File f) throws IOException {
        Pair<Maze,Position> toWrite = model.getObjectToSaveMaze();
        FileOutputStream fileOutputStream=new FileOutputStream(f);
        f.createNewFile();
        ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(toWrite);
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public void loadMazeFromFile(File mazeFile) throws Exception {
        model.loadMazeFromFile(mazeFile);
    }
}
