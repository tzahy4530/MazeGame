package Model;

import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;

import java.io.*;

public class ClientStrategyGenerateMaze implements IClientStrategy {
    private Maze maze;

    public void setRowDimension(int rowDimension) {
        this.rowDimension = rowDimension;
    }

    public void setColDimension(int colDimension) {
        this.colDimension = colDimension;
    }

    private int rowDimension, colDimension;

    public ClientStrategyGenerateMaze(int rowDimension, int colDimension) {
        this.rowDimension = rowDimension;
        this.colDimension = colDimension;
    }

    public Maze getMaze() {
        return maze;
    }

    @Override
    public void clientStrategy(InputStream inputStream, OutputStream outputStream) {
        try {
            ObjectOutputStream toServer = new ObjectOutputStream(outputStream);
            ObjectInputStream fromServer = new ObjectInputStream(inputStream);
            toServer.flush();
            int[] mazeDimensions = new int[]{rowDimension, colDimension};
            toServer.writeObject(mazeDimensions);
            toServer.flush();
            byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
            InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
            byte[] decompressedMaze = new byte[251000 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
            is.read(decompressedMaze); //Fill decompressedMaze with bytes
            Maze maze = new Maze(decompressedMaze);
            this.maze=maze;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

