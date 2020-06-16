package ViewModel;

import Model.IModel;
import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
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

    private List<AState> solutionList;

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

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MyModel) {
            if (arg instanceof Maze) {
                //generateMaze
                maze = model.getMazeMatrix();
                charRow = model.getCharacterRow();
                charCol = model.getCharacterCol();
                goalRow = model.getCharacterRow();
                goalCol = model.getCharacterCol();
                setChanged();
                notifyObservers(maze);
            }
            else if (arg instanceof Solution) {
                solutionList=model.getSolution();
                setChanged();
                notifyObservers(solutionList);
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
