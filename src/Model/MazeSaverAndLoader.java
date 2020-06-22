package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.util.Pair;

import java.io.*;

public class MazeSaverAndLoader implements IMazeSaveAndLoader {
    private Position position;
    private Maze maze;

    public Position getPosition() {
        return position;
    }

    public Maze getMaze() {
        return maze;
    }


    @Override
    public void loadMaze(File path) {
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Pair<Maze, algorithms.mazeGenerators.Position> mazePlusCharacterPosition = (Pair<Maze, algorithms.mazeGenerators.Position>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            maze = (mazePlusCharacterPosition.getKey());
            position = mazePlusCharacterPosition.getValue();
        } catch (IOException| ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveMaze(String path, Maze maze, Position characterPosition, String resurcePath) throws IOException {

            File f = new File(resurcePath + path + ".maze");
            f.delete();
            f.createNewFile();
            Pair<Maze, Position> toWrite = new Pair<Maze, Position>(maze, characterPosition);
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(toWrite);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
            System.out.println(resurcePath+"/" + path + ".maze Created");

    }
}
