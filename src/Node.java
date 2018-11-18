public class Node {
    private int xCoord;
    private int yCoord;
    private boolean start = false;
    private boolean pit = false;
    private boolean breeze = false;
    private boolean gold = false;
    private boolean wumpus = false;


    public Node(int inX, int inY){
        xCoord = inX;
        yCoord = inY;
    }

    public void setStart(){
        start = true;
    }

    public void setPit(){
        pit = true;
    }

    public void setBreeze(){
        breeze = true;
    }

    public void setGold(){
        gold = true;
    }

    public void setWumpus(){
        wumpus = true;
    }

    public void setStench(){
        stench = true;
    }
}
