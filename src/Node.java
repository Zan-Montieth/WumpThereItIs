public class Node {
    private int xCoord;
    private int yCoord;
    private boolean start = false;
    private boolean pit = false;
    private boolean breeze = false;
    private boolean gold = false;
    private boolean glitter = false;
    private boolean wumpus = false;
    private boolean stench = false;
    private boolean player = false;
    private boolean visited = false;


    public Node(int inX, int inY){
        xCoord = inX;
        yCoord = inY;
    }

    public void setPlayer(boolean in){
        player = in;
    };

    public void setStart(){
        start = true;
    }

    public void setPit(){
        pit = true;
    }

    public void removePit(){
        pit = false;
    }

    public void setBreeze(){
        breeze = true;
    }

    public void setGold(){
        gold = true;
    }

    public void removeGold(){
        gold = false;
    }

    public void setWumpus(){
        wumpus = true;
    }

    public void removeWumpus(){
        wumpus = false;
    }

    public void setStench(){
        stench = true;
    }

    public void removeStench(){
        stench = false;
    }

    public void setGlitter(){
        glitter = true;
    }

    public void setVisited(){
        visited = true;
    }

    public boolean isVisited(){
        return visited;
    }

    public void removeGlitter(){
        glitter = false;
    }

    public boolean isBreeze() {
        return breeze;
    }

    public boolean isGold() {
        return gold;
    }

    public boolean isPit() {
        return pit;
    }

    public boolean isStart() {
        return start;
    }

    public boolean isStench() {
        return stench;
    }

    public boolean isWumpus() {
        return wumpus;
    }

    public boolean isGlitter() {
        return glitter;
    }

    public boolean isPlayer(){
        return player;
    }

    public int getxCoord(){
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }




}
