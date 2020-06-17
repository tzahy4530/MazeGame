package Model;

import algorithms.mazeGenerators.EmptyMazeGenerator;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.BestFirstSearch;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.DepthFirstSearch;
import algorithms.search.ISearchingAlgorithm;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.Properties;

public final class Options {

    static private Options me;
    String filePath = "./resources/options.properties";
    static private Properties prop;

    private Options() {
        try {
            File file = new File(filePath);
            file.createNewFile();
            InputStream fileInput = new FileInputStream(file);
            prop = new Properties();
            prop.load(fileInput);
            fileInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Options getOptions() {
        if (me == null) {
            me = new Options();
        }
        return me;
    }

    public void setMazeSize(int row, int col){
        OutputStream fileOutput = null;
        try {
            fileOutput = new FileOutputStream(this.filePath);
            this.prop.setProperty("MazeRow", ((Integer) row).toString());
            this.prop.setProperty("MazeCol",((Integer) col).toString());
            this.prop.store(fileOutput, null);
            fileOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSoundsMode(Boolean soundsMode){
        OutputStream fileOutput = null;
        try {
            fileOutput = new FileOutputStream(this.filePath);
            this.prop.setProperty("Sounds", soundsMode.toString());
            this.prop.store(fileOutput, null);
            fileOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getSoundsMode(){
        boolean soundMode = true;
        String soundsModeStr;
        try {
            soundsModeStr = this.prop.getProperty("Sounds");
        }
        catch (Exception e){
            this.prop.setProperty("Sounds", "true");
            soundsModeStr = "true";
        }
        switch (soundsModeStr){
            case "false":
                soundMode = false;
                break;
        }
        return soundMode;
    }

    public int[] getMazeSize(){
        // index 0 --> row
        // index 1 --> col
        String mazeRowStr;
        String mazeColStr;
        try {
            mazeRowStr = this.prop.getProperty("MazeRow");
            mazeColStr = this.prop.getProperty("MazeCol");
        } catch (Exception e){
            mazeRowStr = "10";
            mazeColStr = "10";
            this.prop.setProperty("MazeRow", "10");
            this.prop.setProperty("MazeCol", "10");
        }
        int[] mazeSize = new int[]{Integer.parseInt(mazeRowStr),Integer.parseInt(mazeColStr)};
        return mazeSize;
    }
}