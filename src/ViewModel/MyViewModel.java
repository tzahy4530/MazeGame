package ViewModel;

import Model.IModel;
import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private int[][] maze;
    private int charRow, charCol;
    private IModel model;
    private List<AState> solutionList;

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
