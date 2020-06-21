package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import javafx.util.Pair;

import java.io.File;
import java.util.List;
import java.util.Observer;

public interface IModel {
    public void generateMaze(int row, int col);
    public void solveMaze();
    public void moveCharacter (int direct);
    public int[][] getMazeMatrix();
    public int getCharacterRow();
    public int getCharacterCol();
    public List<AState> getSolution();
    public void assignObserver(Observer o);
    public int getGoalRow();
    public int getGoalCol();
    public void setMaze(Maze maze);
    public void setCharacterPosition(Position charPosition);
    public void loadMazeFromFile (File mazeFile) throws Exception;
    public boolean saveMaze(String whereToSave,String resurcePath);

}
