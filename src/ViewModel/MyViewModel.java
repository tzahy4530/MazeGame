package ViewModel;

import Model.IModel;
import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private int[][] maze;
    private int charRow, charCol;
    private int goalRow, goalCol;
    private IModel model;
    private List<Pair<Integer, Integer>> solution;
    private Button[] buttons;
    private File dataFile;
    private boolean hasSavedMaze;
    private Alert aboutInformtion;
    String resursecPath;
    private double sceneWidth, sceneHigh;

    public double getSceneWidth() {
        return sceneWidth;
    }

    public double getSceneHigh() {
        return sceneHigh;
    }

    public void setSceneWidth(double sceneWidth) {
        this.sceneWidth = sceneWidth;
    }

    public void setSceneHigh(double sceneHigh) {
        this.sceneHigh = sceneHigh;
    }

    public Button[] getButtons() {
        return buttons;
    }

    public boolean hasSavedMaze() {
        return hasSavedMaze;
    }


    public int getGoalRow() {
        return goalRow;
    }

    public int getGoalCol() {
        return goalCol;
    }

    public int[][] getMaze() {
        return maze;
    }

    public int getCharRow() {
        return charRow;
    }

    public int getCharCol() {
        return charCol;
    }

    public void makeSolutionList(List<AState> solList) {
        List<Pair<Integer, Integer>> solutionAsRowCol = new ArrayList<>();
        for (AState state :
                solList) {
            Position position = (Position) state.getStateValue();
            solutionAsRowCol.add(new Pair<Integer, Integer>(position.getRowIndex(), position.getColumnIndex()));
        }
        solution = solutionAsRowCol;
    }

    public Alert getAboutInformtion() {
        return aboutInformtion;
    }

    public void solve() {
        model.solveMaze();
    }

    private void setaboutInforamtionAlert() {
        aboutInformtion = new Alert(Alert.AlertType.INFORMATION);
        aboutInformtion.setHeaderText("Wellcome to SoutPark Maze.");
        aboutInformtion.setContentText("This game adopt songs,characters, and images of the cartoon TV-Show \"SoutPark\".\n"
        +"The purpose is to challenge the players with an infinity of hard/easy mazes.\n"
        +"your goal? try to solve them.");
        aboutInformtion.setTitle("About");
    }

    public MyViewModel(IModel model) throws IOException, ClassNotFoundException {
        resursecPath = "./resources/";

        setaboutInforamtionAlert();

        this.model = model;
        this.model.assignObserver(this);

        this.solution = null;

        maze = null;
        charRow = 0;
        charCol = 0;
        goalRow = 0;
        goalCol = 0;

        buttons = new Button[10];

        dataFile = new File(resursecPath + "MazeDataFile.mdf");
        if (dataFile.exists()) {
            FileInputStream fileInputStream = new FileInputStream(dataFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            List<String> saveMazeList = (List<String>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            int index = 0;
            for (String fileName :
                    saveMazeList) {
                buttons[index++] = new Button(fileName);
            }
            hasSavedMaze = true;
        } else {
            for (int i = 0; i < buttons.length; i++) {
                buttons[i] = new Button("save " + (i + 1));
            }
            hasSavedMaze = false;
        }
    }

    public void generateMaze(int row, int col) {
        model.generateMaze(row, col);
    }

    public void moveCharacter(KeyEvent keyEvent) {

        switch (keyEvent.getCode()) {
            case NUMPAD8:
            case UP:
                model.moveCharacter(1);
                break;
            case NUMPAD2:
            case DOWN:
                model.moveCharacter(2);
                break;
            case NUMPAD4:
            case LEFT:
                model.moveCharacter(3);
                break;
            case NUMPAD6:
            case RIGHT:
                model.moveCharacter(4);
                break;
            case E:
            case NUMPAD9:
                model.moveCharacter(5);
                break;
            case Q:
            case NUMPAD7:
                model.moveCharacter(6);
                break;
            case C:
            case NUMPAD3:
                model.moveCharacter(7);
                break;
            case Z:
            case NUMPAD1:
                model.moveCharacter(8);
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MyModel) {
            if (arg instanceof Maze) {
                //generateMaze
                maze = model.getMazeMatrix();
                charRow = model.getCharacterRow();
                charCol = model.getCharacterCol();
                goalRow = model.getGoalRow();
                goalCol = model.getGoalCol();
                setChanged();
                notifyObservers(maze);
            } else if (arg instanceof Solution) {
                makeSolutionList(model.getSolution());
                setChanged();
                notifyObservers(solution);
            } else { //move // finish
                charRow = model.getCharacterRow();
                charCol = model.getCharacterCol();
                setChanged();
                notifyObservers();
            }
        }
    }


    public void saveMaze(String whereSave) throws IOException {
        hasSavedMaze = model.saveMaze(whereSave, resursecPath);

    }


    public void loadMaze(String whereLoad) throws Exception {
        model.loadMazeFromFile(new File(resursecPath + whereLoad + ".maze"));
        setChanged();
        notifyObservers(new Boolean(true));
    }

    public void preClosing() {
        List<String> toSave = new ArrayList<>();
        for (Button b :
                buttons) {
            toSave.add(b.getText());
        }
        dataFile.delete();
        try {
            dataFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(dataFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(toSave);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            System.out.println("File Saving Fail");
        }
    }
}
