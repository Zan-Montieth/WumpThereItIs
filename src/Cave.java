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
                double pit = Math.random();
                if(pit < 0.2){
                    temp.setPit();
                    setBreezes(x,y);
                }
            }
        }

        cave[0][0].setStart();

        int gold = (int)(Math.random() * (caveSize ^ 2) - 1) + 1;
        int goldY = (int)(gold / caveSize);
        int goldX = gold % caveSize;
        cave[goldX][goldY].setGold();

        int wumpus = (int)(Math.random() * (caveSize ^ 2) - 1) + 1;
        int wumpY = (int)(gold / caveSize);
        int wumpX = gold % caveSize;
        cave[wumpX][wumpY].setWumpus();
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
}
