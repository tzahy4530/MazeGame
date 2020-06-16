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
    private IModel model;
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

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MyModel) {
            if (arg instanceof Maze) {
                maze = model.getMazeMatrix();
            }
            if (arg instanceof Solution) {
                solutionList=model.getSolution();
            }
        }

    }
}
