import java.util.ArrayList;
import java.util.List;

public class Player{

    private boolean[][] knowledgeOfBreeze;
    private boolean[][] knowledgeOfStench;
    private boolean[][] knowledgeOfPits;
    private boolean[][] knowledgeOfWumpus;
    private boolean[][] fullyExploredCells;
    private boolean[][] visited;
    private int[][]     chanceOfPit;
    private boolean dead;
    private boolean foundGold = false;
    private int caveSize;
    private Node[][]cave;
    private boolean scream = false; // if the wumpus has been shot
    private int score = 1000;
    private Cave updateMap;
    private int direction;                      //  0 up      1 right     2 down      3 left
    private List<int[]> xyPath = new ArrayList<int[]>();
    private int numCellsVisited = -1;


    public Player(int inCaveSize, Node inCave[][], Cave inUpdateMap){
        caveSize = inCaveSize;
        cave = inCave;
        updateMap = inUpdateMap;
        knowledgeOfBreeze = new boolean[inCaveSize][inCaveSize];
        knowledgeOfPits   = new boolean[inCaveSize][inCaveSize];
        knowledgeOfStench = new boolean[inCaveSize][inCaveSize];
        knowledgeOfWumpus = new boolean[inCaveSize][inCaveSize];
        visited           = new boolean[inCaveSize][inCaveSize];
        chanceOfPit       = new     int[inCaveSize][inCaveSize];
        direction = 2;
        updateMap.setPlayer(0,0);
        visited[0][0] = true;
    }

    public void findGold(int x, int y){
        int[] coords = {x,y};
        xyPath.add(coords);
        numCellsVisited++;
        if(cave[x][y].isPit()){
            System.out.println("you died");
        }
        if(cave[x][y].isGlitter()){
            foundGold = true;
            score += 1000;
        }
        if(foundGold == true){
            getOut(x,y);
            return;
        }
        updateMap.setPlayer(x,y);
        visited[x][y] = true;
        updateMap.printCave();

        if(!cave[x][y].isBreeze()) { setNoPit(x,y); } // if there isn't a breeze, we know that adjacent squares are not pits

        if(!cave[x][y].isBreeze() && !cave[x][y].isStench() && !haveVisited(x,y) && canMove(x, y) && !foundGold){
            moveIn(x,y);
        }else if(!cave[x][y].isBreeze() && !cave[x][y].isStench() && !haveVisited(x,y) && !canMove(x, y) && !foundGold){
            turnLeft();
            if(canMove(x,y)) {
                moveIn(x, y);
            }
        }
        if(!cave[x][y].isBreeze() && !cave[x][y].isStench() && haveVisited(x,y) && !foundGold){
            turnLeft();
            if(canMove(x,y)) {
                findGold(x, y);
            }else{
                turnLeft();
            }
            if(canMove(x,y)) {
                findGold(x, y);
            }else{
                turnLeft();
            }
            if(canMove(x,y)) {
                findGold(x, y);
            }else{
                System.out.println("help im entirely stuck");
            }
        }
        else if(!cave[x][y].isBreeze() && cave[x][y].isStench() && !foundGold){  // if stench, shoot arrow to the left (?)
            turnLeft();
            scream = shootArrow(x, y);
            updateMap.printCave();
            if(canMove(x,y)) {
                moveIn(x, y);
            }
        }
        else if(cave[x][y].isBreeze() && !cave[x][y].isStench() && !foundGold){
            xyPath.remove(numCellsVisited);
            numCellsVisited--;
            int[] prevXY = xyPath.get(numCellsVisited);
            findGold(prevXY[0],prevXY[1]);
            knowledgeOfBreeze[x][y]=true;
        }

        //TODO handle breeze
        //TODO hanle breeze and stench
        //TODO handle all situations where have visited is true and check if its the only way to go
        //fully explored function will return true if all 4 adjacent cells are either walls or have been visited
        //TODO implent list of places ive been
        //xyPath holds the xy coordinates of all visited locations on the way to the gold as a list of 2 dimensional arrays, x index 0 and y index 1
        //haveVisited checks if we have visited the next cell
    }

    /* Sets all squared adjacent to square x,y to have no chance of being pits
     * called when there is no breeze in a square
     */
    private void setNoPit(int x, int y) {
        if (x < caveSize-1) chanceOfPit[x+1][y] = 0;
        if (x > 0 ) chanceOfPit[x-1][y] = 0;
        if (y < caveSize-1) chanceOfPit[x][y+1] = 0;
        if (y > 0) chanceOfPit[x][y-1] = 0;
    }

    private void calculatePitOdds() {

        for (int x = 0; x < caveSize; x++) {
            for (int y = 0; y < caveSize; y++) {
                int countPossiblePits = 0;
                if (knowledgeOfBreeze[x][y]) {
                    int potentialPits = numPotentialPits(x,y); // number of squares that could be pits




                }

            }
        }

    }

    private int numPotentialPits(int x, int y) {
        int count = 0;
        if (x < caveSize-1 && !knowledgeOfPits[x+1][y]) count++;
        if (x > 0 && !knowledgeOfPits[x-1][y]) count++;
        if (y < caveSize-1 && !knowledgeOfPits[x][y+1]) count++;
        if (y > 0 && !knowledgeOfPits[x][y-1]) count++;
        return count;
    }

    public boolean shootArrow(int x, int y){
        score = score - 10;
        if(direction == 0){
            for(int v = y; v >=0; v--){
                if(cave[x][v].isWumpus()){
                    updateMap.killWumpus();
                    return true;
                }
            }
        }else if(direction == 1){
            for(int h = x; h < caveSize; h++){
                if(cave[h][y].isWumpus()){
                    updateMap.killWumpus();
                    return true;
                }
            }
        }else if(direction == 2){
            for(int v = y; v < caveSize; v++){
                if(cave[x][v].isWumpus()){
                    updateMap.killWumpus();
                    return true;
                }
            }
        }else if(direction == 3){
            for(int h = x; h >= 0; h--){
                if(cave[h][y].isWumpus()){
                    updateMap.killWumpus();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean haveVisited(int x, int y){
        if(y-1 >= 0 && direction == 0){
            return visited[x][y-1];
        }else if(x+1 < caveSize && direction == 1){
            return visited[x+1][y];
        }else if(y+1 < caveSize && direction == 2){
            return visited[x][y+1];
        }else if((x-1) >= 0  && direction == 3){
            return visited[x-1][y];
        }
        return false;
    }

    private boolean fullyExplored(int x, int y){
        int blockedVisited = 0;
        if((x-1) >= 0 || visited[x-1][y]){
            blockedVisited++;
        }else if(y-1 >= 0 || visited[x][y-1]){
            blockedVisited++;
        }else if(y+1 < caveSize || visited[x][y+1]){
            blockedVisited++;
        }else if(x+1 < caveSize || visited[x+1][y]){
            blockedVisited++;
        }
        if(blockedVisited == 4){
            return true;
        }
        return false;
    }

    public void moveIn(int x, int y){
        score = score - 1;
        if(direction == 0){
            findGold(x, (y - 1));
        }else if(direction == 1){
            findGold((x + 1), y);
        }else if(direction == 2){
            findGold(x, (y + 1));
        }else if(direction == 3){
            findGold((x - 1), y);
        }
    }

    private void moveOut(int x, int y){
        score = score - 1;
        if(direction == 0){
            getOut(x, (y - 1));
        }else if(direction == 1){
            getOut((x + 1), y);
        }else if(direction == 2){
            getOut(x, (y + 1));
        }else if(direction == 3){
            getOut((x - 1), y);
        }
    }

    private void turnLeft(){
        if(direction == 0){
           direction = 3;
        }else{
            direction -= 1;
        }
    }

    public boolean canMove(int x, int y){
        if(direction == 0){
            if(y - 1 < 0){
                return false;
            }
        }else if(direction == 1){
            if(x + 1 >= caveSize){
                return false;
            }
        }else if(direction == 2){
            if(y + 1 >= caveSize){
                return false;
            }
        }else if(direction == 3){
            if(x - 1 < 0){
                return false;
            }
        }
        return true;
    }

    public void getOut(int x, int y){
        updateMap.setPlayer(x,y);
        updateMap.setGold(x,y);
        updateMap.printCave();
        if(x == 0 && y == 0){
            System.out.println(score);
        }else{
            updateMap.setPlayer(x,y);
            checkForVisited(x, y);
        }
    }

    private void checkForVisited(int x, int y){
        if((x-1) >= 0 && visited[x-1][y]){
            direction = 3;
        }else if(y-1 >= 0 && visited[x][y-1]){
            direction = 0;
        }else if(y+1 < caveSize && visited[x][y+1]){
            direction = 2;
        }else if(x+1 < caveSize && visited[x+1][y]){
            direction = 1;
        }
        moveOut(x,y);
    }


    public void printKnowledgeOfBreezes(){
        for(int y = 0; y < caveSize; y++){
            for(int x = 0; x < caveSize; x++){
                System.out.print(knowledgeOfBreeze[x][y] + " ");
            }
            System.out.println();
        }
    }
}
