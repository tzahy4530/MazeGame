package Model;

import Client.IClientStrategy;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.AState;
import algorithms.search.Solution;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ClientStrategySolveMaze implements IClientStrategy {
    private Maze maze;
    private Solution solution;

    public ClientStrategySolveMaze(Maze maze) {
        this.maze = maze;
        this.solution = null;
    }

    @Override
    public void clientStrategy(InputStream inputStream, OutputStream outputStream) {

        try {
            ObjectOutputStream toServer = new ObjectOutputStream(outputStream);
            ObjectInputStream fromServer = new ObjectInputStream(inputStream);
            toServer.flush();
            toServer.writeObject(maze);
            toServer.flush();
            Solution mazeSolution = (Solution) fromServer.readObject();
            solution = mazeSolution;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Solution getSolution() {
        return solution;
    }
}
