package ViewModel;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    int [][] maze;
    int charRow,charCol;


    public MyViewModel(int[][] maze) {
        this.maze = null;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
