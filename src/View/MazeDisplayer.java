package View;

import Model.Options;
import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.util.List;

public class MazeDisplayer extends Canvas {

    public StringProperty imageFileNameWall = new SimpleStringProperty(Options.getOptions().getWallImagPath());
    public StringProperty imageFileNamePlayer = new SimpleStringProperty(Options.getOptions().getCharacterImagPath());
    public StringProperty imageFileNameGoal = new SimpleStringProperty(Options.getOptions().getGoalImagPath());

    private int[][] maze;
    private int row_player;
    private int col_player;
    private int row_goal;
    private int col_goal;
    private List<Pair<Integer, Integer>> solution=null;


    public MazeDisplayer(){};

    public void setSolution(List<Pair<Integer, Integer>> solution) {
        this.solution = solution;
    }

    public void setMaze(int[][] maze, int rowStartChar, int colStartChar, int rowGoal, int colGoal) {
        this.maze = maze;
        this.row_player = rowStartChar;
        this.col_player = colStartChar;
        this.row_goal = rowGoal;
        this.col_goal = colGoal;
        solution = null;

    }

    public int getRow_player(){return row_player;}
    public int getCol_player(){return col_player;}

    public int getMazeMaxRow() {return maze.length;}
    public int getMazeMaxCol() {return maze[0].length;}
    public boolean isAWall(int row, int col){
        if(maze[row][col]==1) return true;
        return false;
    }

    public void set_player_position(int row, int col) {
        this.row_player = row;
        this.col_player = col;
        draw();
    }


    public void draw(){
        int row = maze.length;
        int col = maze[0].length;
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();

        double cellHeight = canvasHeight/row;
        double cellWidth = canvasWidth/col;
        GraphicsContext graphicsContext = getGraphicsContext2D();
        graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);
        graphicsContext.setFill(Color.WHITE);
        double w,h;
        Image wallImage = null;
        try {
            wallImage = new Image(new FileInputStream(imageFileNameWall.get()));
        }
        catch(Exception e){
            System.out.println(imageFileNameWall.get());
            System.out.println("cannot find the wall Image");
        }
        for(int i=0;i<row;i++)
        {
            for(int j=0;j<col;j++)
            {
                h = i * cellHeight;
                w = j * cellWidth;
                if(maze[i][j] == 1) // Wall
                {
                    if (wallImage == null){
                        graphicsContext.fillRect(w,h,cellWidth,cellHeight);
                    }else{
                        graphicsContext.drawImage(wallImage,w,h,cellWidth,cellHeight);
                    }
                }
                else{
                    graphicsContext.fillRect(w,h,cellWidth,cellHeight);
                }
            }
        }
        if(solution != null) {
            graphicsContext.setFill(Color.RED);
            for (Pair<Integer, Integer> pair : solution) {
                double r = pair.getKey() * cellHeight;
                double c = pair.getValue() * cellWidth;
                graphicsContext.fillRect(c, r, cellWidth, cellHeight);
            }
        }
        double h_player = row_player * cellHeight;
        double w_player = col_player * cellWidth;
        Image goalImage = null;
        try {
            goalImage = new Image(new FileInputStream(imageFileNameGoal.get()));
        } catch (Exception e) {
            System.out.println("There is no Image player....");
        }
        graphicsContext.drawImage(goalImage,(double) col_goal*cellWidth,(double)row_goal*cellHeight,cellWidth,cellHeight);
        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(imageFileNamePlayer.get()));
        } catch (Exception e) {
            System.out.println("There is no Image player....");
        }
        graphicsContext.drawImage(playerImage,w_player,h_player,cellWidth,cellHeight);

    }
}



