package Model;

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
        soundsModeStr = this.prop.getProperty("Sounds");
        if (soundsModeStr==null){
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
        mazeRowStr = this.prop.getProperty("MazeRow");
        mazeColStr = this.prop.getProperty("MazeCol");
        if(mazeRowStr==null || mazeColStr == null){
        mazeRowStr = "10";
        mazeColStr = "10";
        this.prop.setProperty("MazeRow", "10");
        this.prop.setProperty("MazeCol", "10");
    }
        int[] mazeSize = new int[]{Integer.parseInt(mazeRowStr),Integer.parseInt(mazeColStr)};
        return mazeSize;
    }
    public String getWallImagPath(){
        String res=this.prop.getProperty("wallImage");
        if (res==null) {
            setWall("wall");
            return "./resources/Walls/wall.png";
        }
        return res;
    }
    public String getCharacterImagPath(){
        String res=this.prop.getProperty("CharacterImage");
        if (res==null) {
            setCharacter("cartman");
            setGoalImagePath("cartman");
            return "./resources/Pictures/cartman.png";
        }
        return res;
    }
    public String getGoalImagPath(){
        String res=this.prop.getProperty("GoalImage");
        if (res==null) {
            setGoalImagePath("cartman");
            return "./resources/Goals/cartman.png";
        }
        return res;
    }

    private void setGoalImagePath(String name){
        FileOutputStream fileOutputStream=null;
        try {
            fileOutputStream=new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        getOptions();
        prop.setProperty("GoalImage","./resources/Goals/"+name+".png");
        try {
            prop.store(fileOutputStream,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCharacter(String name){
        FileOutputStream fileOutputStream=null;
        try {
            fileOutputStream=new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        getOptions();
        prop.setProperty("CharacterImage","./resources/Pictures/"+name+".png");
        setGoalImagePath(name);
        try {
            prop.store(fileOutputStream,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setWall(String name){
        FileOutputStream fileOutputStream=null;
        try {
            fileOutputStream=new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        getOptions();
        prop.setProperty("wallImage","./resources/Walls/"+name+".png");
        try {
            prop.store(fileOutputStream,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}