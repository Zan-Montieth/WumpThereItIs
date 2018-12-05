import java.util.concurrent.*; // for timer

public class Cave {

    private int caveSize;
    private Node[][] cave;
    private int playerPrevX = 0;
    private int playerPrevY = 0;
    private int goldPrevX;
    private int goldPrevY;
    private int wumpX;
    private int wumpY;
    private boolean[][] visited;

    public Cave(int inSize){
        caveSize = inSize;
        cave = new Node[caveSize][caveSize];
        visited = new boolean[caveSize][caveSize];
        for(int y = 0; y < caveSize; y++){
            for(int x = 0; x < caveSize; x++){
                Node temp = new Node(x,y);
                cave[x][y] = temp;
                if(x != 0 && y != 0) {
                    double pit = Math.random();
                    if (pit < 0.2) {
                        temp.setPit();
                    }
                }
            }
        }

        int gold = (int)(Math.random() * (caveSize * caveSize) - 1) + 1;
        goldPrevY = (gold / caveSize);
        goldPrevX = gold % caveSize;
        cave[goldPrevX][goldPrevY].setGold();
        cave[goldPrevX][goldPrevY].setGlitter();
        if(cave[goldPrevX][goldPrevY].isPit()){
            cave[goldPrevX][goldPrevY].removePit();
        }

        int wumpus = (int)(Math.random() * (caveSize * caveSize) - 1) + 1;
        wumpY = (wumpus / caveSize);
        wumpX = wumpus % caveSize;
        cave[wumpX][wumpY].setWumpus();
        setStenchs(wumpX,wumpY);

        for(int y = 0; y < caveSize; y++){
            for(int x = 0; x < caveSize; x++){
                if(cave[x][y].isPit()){
                    setBreezes(x,y);
                }
            }
        }

        cave[0][0].setStart();

    }

    private void setBreezes(int inX, int inY){
        if(inX + 1 < caveSize){
            cave[inX + 1][inY].setBreeze();
        }
        if(!((inX - 1) < 0)){
            cave[inX - 1][inY].setBreeze();
        }
        if(inY + 1 < caveSize){
            cave[inX][inY + 1].setBreeze();
        }
        if(!((inY - 1) < 0)){
            cave[inX][inY - 1].setBreeze();
        }
    }

    private void setStenchs(int inX, int inY){
        if(inX + 1 < caveSize){
            cave[inX + 1][inY].setStench();
        }
        if(!((inX - 1) < 0)){
            cave[inX - 1][inY].setStench();
        }
        if(inY + 1 < caveSize){
            cave[inX][inY + 1].setStench();
        }
        if(!((inY - 1) < 0)){
            cave[inX][inY - 1].setStench();
        }
    }

    public void setPlayer(int inX, int inY){
        cave[playerPrevX][playerPrevY].setPlayer(false);
        cave[inX][inY].setPlayer(true);
        playerPrevX = inX;
        playerPrevY = inY;
    }



    public void setGold(int x, int y){
        cave[goldPrevX][goldPrevY].removeGold();
        cave[goldPrevX][goldPrevY].removeGlitter();
        cave[x][y].setGold();
        goldPrevX = x;
        goldPrevY = y;
    }

    public void killWumpus(){
        cave[wumpX][wumpY].removeWumpus();
        if((wumpX-1) >= 0){
            cave[wumpX-1][wumpY].removeStench();
        }
        if(wumpY-1 >= 0){
            cave[wumpX][wumpY-1].removeStench();
        }
        if(wumpY+1 < caveSize){
            cave[wumpX][wumpY+1].removeStench();
        }
        if(wumpX+1 < caveSize){
            cave[wumpX+1][wumpY].removeStench();
        }
    }

    public Node[][] getNodeCave(){
        return cave;
    }

    public void printCave(){

        System.out.print("-");
        for(int x = 0; x < caveSize; x++){
            System.out.print(" - - - - - - - -");
        }
        System.out.println();


        for(int y = 0; y < caveSize; y++){
        //Prints out Stench and x coord
            System.out.print("|");
            for(int x = 0; x < caveSize; x++){
                if(cave[x][y].isStench()) {
                    System.out.print(" Stench   ");
                }else{
                    System.out.print("          ");
                }
                System.out.print("X = " + cave[x][y].getxCoord() + "|");
            }
            System.out.println();
        //prints out Wumpus and y coord
            System.out.print("|");
            for(int x = 0; x < caveSize; x++){
                if(cave[x][y].isWumpus()) {
                    System.out.print(" Wumpus   ");
                }else{
                    System.out.print("          ");
                }
                System.out.print("Y = " + cave[x][y].getyCoord() + "|");
            }
            System.out.println();
        //prints out glitter
            System.out.print("|");
            for(int x = 0; x < caveSize; x++){
                if(cave[x][y].isGlitter()) {
                    System.out.print(" Glitter       |");
                }else {
                    System.out.print("               |");
                }
            }
            System.out.println();
        //prints out Gold
            System.out.print("|");
            for(int x = 0; x < caveSize; x++){
                if(cave[x][y].isGold()) {
                    System.out.print(" Gold     ");
                }else {
                    System.out.print("          ");
                }
                if(x == 0 && y == 0){
                    System.out.print("start|");
                }else{
                    System.out.print("     |");
                }
            }
            System.out.println();
        //prints out Breeze
            System.out.print("|");
            for(int x = 0; x < caveSize; x++){
                if(cave[x][y].isBreeze() && !cave[x][y].isPit()) {
                    System.out.print(" Breeze        |");
                }else {
                    System.out.print("               |");
                }
            }
            System.out.println();
        //prints out pit
            System.out.print("|");
            for(int x = 0; x < caveSize; x++){
                if(cave[x][y].isPit()) {
                    System.out.print(" Pit           |");
                }else {
                    System.out.print("               |");
                }
            }
            System.out.println();
        //prints out player
            System.out.print("|");
            for(int x = 0; x < caveSize; x++){
                if(cave[x][y].isPlayer()) {
                    System.out.print(" Player        |");
                    visited[x][y] = true;
                }else if(visited[x][y]){
                    System.out.print(" Visited       |");
                }
                else {
                    System.out.print("               |");
                }
            }
            System.out.println();

        //prints divider for each line
            System.out.print("-");
            for(int x = 0; x < caveSize; x++){
                System.out.print(" - - - - - - - -");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();

//        try { TimeUnit.SECONDS.sleep((long)1.5); }  // Sleep for 1 second after printing
//        catch (java.lang.InterruptedException e) {
//            System.out.println("Error:"+e);
//        }

    }
}
