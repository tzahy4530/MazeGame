package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface IMazeSaveAndLoader {
    public void loadMaze(File path);
    public void saveMaze(String path, Maze maze, Position characterPosition, String resurcePath) throws IOException;
    public Maze getMaze();
    public Position getPosition();

}
