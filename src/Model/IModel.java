package Model;

import algorithms.search.AState;

import java.util.List;

public interface IModel {
    public void generateMaze(int row, int col);
    public void solveMaze();
    public void moveCharacter (int direct);
    public int[][] getMazeMatrix();
    public int getCharacterRow();
    public int getCharacterCol();
    public List<AState> getSolution();


}
