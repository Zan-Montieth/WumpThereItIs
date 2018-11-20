public class Cave {

    private int caveSize;
    private Node[][] cave;

    public Cave(int inSize){
        caveSize = inSize;
        cave = new Node[caveSize][caveSize];
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
        for(int y = 0; y < caveSize; y++){
            for(int x = 0; x < caveSize; x++){
                if(cave[x][y].isPit()){
                    setBreezes(x,y);
                }
            }
        }

        cave[0][0].setStart();

        int gold = (int)(Math.random() * (caveSize * caveSize) - 1) + 1;
        int goldY = (gold / caveSize);
        int goldX = gold % caveSize;
        cave[goldX][goldY].setGold();
        cave[goldX][goldY].setGlitter();
        System.out.println(gold + " gold x = " + goldX + " gold y = " + goldY);

        int wumpus = (int)(Math.random() * (caveSize * caveSize) - 1) + 1;

        int wumpY = (wumpus / caveSize);
        int wumpX = wumpus % caveSize;
        cave[wumpX][wumpY].setWumpus();
        setStenchs(wumpX,wumpY);
        System.out.println(wumpus + " wumpus x = " + wumpX + " wumpus y = " + wumpY);
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

    public void setPlayer(int inX, int inY, boolean inThere){
        cave[inX][inY].setPlayer(inThere);
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
                if(cave[x][y].isBreeze()) {
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
                }else {
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
    }
}
