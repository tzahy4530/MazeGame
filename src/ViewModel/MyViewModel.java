package ViewModel;

import Model.IModel;
import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private int[][] maze;
    private int charRow, charCol;
    private int goalRow, goalCol;
    private IModel model;

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

    public List<Pair<Integer, Integer>> getSolutionList() {
        List<Pair<Integer, Integer>> solutionAsRowCol=new ArrayList<>();
        for (AState state:
             model.getSolution()) {
            Position position= (Position) state.getStateValue();
            solutionAsRowCol.add(new Pair<Integer, Integer>(position.getRowIndex(),position.getColumnIndex()));
        }
        return solutionAsRowCol;
    }

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
    }

    public void generateMaze(int row, int col){
        model.generateMaze(row,col);
    }

    public void moveCharacter(KeyEvent keyEvent){

        switch(keyEvent.getCode()){
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
            }
            else if (arg instanceof Solution) {
                setChanged();
                notifyObservers(getSolutionList());
            }
            else{ //move // finish
                charRow = model.getCharacterRow();
                charCol = model.getCharacterCol();
                setChanged();
                notifyObservers();
            }
        }
    }


}
