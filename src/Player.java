import java.lang.reflect.Array;
import java.util.*;

public class Player{

    private boolean[][] knowledgeOfBreeze;
    private boolean[][] knowledgeOfStench;
    private boolean[][] knowledgeOfPits;
    private boolean[][] knowledgeOfWumpus;
    private boolean[][] safeSquares;
    private boolean[][] visited;
    private double [][] chanceOfPit;
    private boolean dead;
    private boolean foundGold;
    private int caveSize;
    private Node[][]cave;
    private boolean scream; // if the wumpus has been shot
    private int score;
    private Cave updateMap;
    private int direction;                      //  0 up      1 right     2 down      3 left
    private List<int[]> xyPath = new ArrayList<>();
    private int numCellsVisited = -1;
    private int finalRecurX;
    private int finalRecurY;
    private int finalRecurScore;

    Player(int inCaveSize, Node inCave[][], Cave inUpdateMap){
        caveSize = inCaveSize;
        cave = inCave;
        updateMap = inUpdateMap;
        knowledgeOfBreeze = new boolean[inCaveSize][inCaveSize];
        knowledgeOfPits   = new boolean[inCaveSize][inCaveSize];
        knowledgeOfStench = new boolean[inCaveSize][inCaveSize];
        knowledgeOfWumpus = new boolean[inCaveSize][inCaveSize];
        visited           = new boolean[inCaveSize][inCaveSize];
        safeSquares       = new boolean[inCaveSize][inCaveSize];
        safeSquares[0][0] = true;                                   // The initial must be safe
        chanceOfPit       = setInitialChance();
        direction = 2;
        score = 1;                                      // score = 1, so it is set to 0 when the recursive search begins
        updateMap.setPlayer(0,0);
        visited[0][0] = true;
    }


    public void search(int initialX, int initialY){

        while(!dead ){

            recursiveSafeSearch(initialX,initialY);
            if(foundGold){
                System.out.println("won a thing did a win");
                break;
            }
            int[] coord = getShwifty();

            dead = true;


        }



    }




    /* Recursive method to explore every safe square accessible from the initial square
     * If you find the gold, it will exit and call return to start via pathBFS
     * If you find an unsafe square, i.e. there is a breeze or stench, it will update the KB and move back
     * Returns true if gold has been found, else returns false if it has not
     */
    public boolean recursiveSafeSearch(int initialX, int initialY) {

        updateMap.setPlayer(initialX,initialY);
        updateMap.printCave();
        int x = initialX;
        int y = initialY;
        boolean hasBreeze = cave[x][y].isBreeze();
        boolean hasStench = cave[x][y].isStench();

        finalRecurX = x;
        finalRecurY = y;
        finalRecurScore = score;

        visited[x][y] = true;       // set the current square to having been visited.
        score -= 1;                 // reduce score by 1 because you made 1 move for each call to this method

        if (cave[x][y].isPit() || cave[x][y].isWumpus()) {      // if this position is a pit or a wumpus
            agentDied(x,y);         // method to update that a person died, 1st base case of recursive call
            return false;           // player is dead, return false to exit recursive stack
        }

        else if (cave[x][y].isGlitter()) {       // if you find the Glitter indicating gold
            pickUpGold(x,y);        // method to return to cave entrance if gold is found, 2nd base case
            return true;            // gold has been found, return true to exit the recursive stack
        }

        else if (hasBreeze && hasStench) {      // if there is a breeze and a stench
            knowledgeOfBreeze[x][y] = true;
            knowledgeOfStench[x][y] = true;
        }

        else if (hasBreeze) {
            knowledgeOfBreeze[x][y] = true;
        }

        else if (hasStench) {
            knowledgeOfStench[x][y] = true;
        }

        else {                      // if there is nothing of note
            setSafeNeighbors(x,y);
        }

            // if next index exists, hasn't been visited, and is safe
        if ( y < caveSize -1 && !visited[x][y+1] && safeSquares[x][y+1]) {
            boolean solved = recursiveSafeSearch(x, y + 1);
            if (solved) return true;
            updateMap.setPlayer(x,y);
            updateMap.printCave();
            score -= 1;
        }

        if ( x < caveSize -1 && !visited[x+1][y] && safeSquares[x+1][y]) {
            boolean solved = recursiveSafeSearch(x + 1, y);
            if (solved) return true;
            updateMap.setPlayer(x,y);
            updateMap.printCave();
            score -= 1;
        }

        if ( y > 0           && !visited[x][y-1] && safeSquares[x][y-1]) {
            boolean solved = recursiveSafeSearch(x, y - 1);
            if (solved) return true;
            updateMap.setPlayer(x,y);
            updateMap.printCave();
            score -= 1;
        }

        if ( x > 0           && !visited[x-1][y] && safeSquares[x-1][y]) {
            boolean solved = recursiveSafeSearch(x - 1, y);
            if (solved) return true;
            updateMap.setPlayer(x,y);
            updateMap.printCave();
            score -= 1;
        }

        // TODO: Implement logic for when it isn't safe to move, pick the best option

        return false;
    }

    private void setSafeNeighbors(int x, int y) {
        if (x > 0)          safeSquares[x-1][y] = true;
        if (x < caveSize-1) safeSquares[x+1][y] = true;
        if (y > 0)          safeSquares[x][y-1] = true;
        if (y < caveSize-1) safeSquares[x][y+1] = true;
    }

    /* Method to print out death message
     * Provides cause of death, position of death, and score
     */
    private void agentDied(int x, int y) {
        boolean isPit = cave[x][y].isPit();
        boolean isWumpus = cave[x][y].isWumpus();

        if (isPit && isWumpus) { // if there is a wumpus and a pit where the agent died
            System.out.println("Agent died by falling in a pit then getting eaten by" +
                    "the Wumpus at position " + x + "," + y + ".");
        }
        else if (isPit) { // if there is just a pit where the agent died
            System.out.println("Agent died by falling in a pit at position " + x + "," + y + ".");
        }
        else if (isWumpus) { // if there is just a wumpus where the agent died
            System.out.println("Agent got killed by the Wumpus at position " + x + "," + y + ".");
        }
        else {  // if the agent is dead at position x,y but the position has neither a wumpus nor a pit
            System.out.println("Agent died of stupid at position " + x + "," + y + ". You shouldn't be " +
                    "able to reach this state.");
        }

        System.out.println("Your score was "+ score + ". Better luck next time!"); // give score
    }

    private void pickUpGold(int startX, int startY) {
        System.out.println("Found Gold in position " + startX + "," + startY + ". " +
                "Current score is "+score+". " +
                "Exiting the cave.");
        updateMap.printCave();

        getOut(startX,startY);
        score += 1000;
        System.out.println("You made it out with a score of: "+score);
//        ArrayList<int[]> path = BFSpath(startX, startY, 0, 0);
//        System.out.println("Have Path");
    }

    private ArrayList<int[]> BFSpath (int startX, int startY, int endX, int endY) {
        Queue<int[]> unvisitedNodes = new PriorityQueue<>();
        Map<int[], int[]> BFSpaths= new HashMap<>();
        int[] firstPoint = {endX,endY};
        int[] originPoint = new int[2];

        unvisitedNodes.add(firstPoint);

        //while there are nodes we have not checked
        while (!unvisitedNodes.isEmpty()) {
            int[] currentPosition = unvisitedNodes.poll();          // set current position based on top of queue
            int x = currentPosition[0];
            int y = currentPosition[1];

            if (x == startX && y == startY) {
                originPoint = currentPosition;
            }
            ArrayList<int[]> neighbors = getNeighbors(x,y);

            for (int[] coords : neighbors) {
                if (safeSquares[x][y] && !BFSpaths.containsKey(coords)) {
                    BFSpaths.put(coords, currentPosition);
                    unvisitedNodes.add(coords);
                }
            }

        }

        ArrayList<int[]> fastestPath = findPathFromMap(firstPoint, originPoint, BFSpaths);

        return null;
    }

    private ArrayList<int[]> findPathFromMap(int[] to, int[] from, Map<int[], int[]> BFSpaths) {
        ArrayList<int[]> fastestPath = new ArrayList<>();
        int[] current = BFSpaths.get(from);
        fastestPath.add(current);
        while ( !(current[0] == to[0] && current[1] == to[1]) ) {
            current = BFSpaths.get(current);
            fastestPath.add(current);
        }
        return fastestPath;
    }

    /* Method to get all neighbors around a point
     * returns an arraylist of 2 integers, (x,y) for each neighbor
     */
    private ArrayList<int[]> getNeighbors(int x, int y) {
        ArrayList<int[]> neighbors = new ArrayList<int[]>();
        if (x < caveSize-1) {
            int[] xyArray = {x+1,y};
            neighbors.add( xyArray );
        }
        if (x > 0) {
            int[] xyArray = {x-1,y};
            neighbors.add( xyArray );
        }
        if (y < caveSize-1) {
            int[] xyArray = {x,y+1};
            neighbors.add( xyArray );
        }
        if (y > 0) {
            int[] xyArray = {x,y-1};
            neighbors.add( xyArray );
        }
        return neighbors;
    }

    public void findGold(int x, int y){
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
        int[] coords = {x,y};
        xyPath.add(coords);
        numCellsVisited++;
        updateMap.setPlayer(x,y);
        visited[x][y] = true;
        updateMap.printCave();
        if(!cave[x][y].isBreeze()) { setNoPit(x,y); } // if there isn't a breeze, we know that adjacent squares are not pits
        if(!cave[x][y].isBreeze() && !cave[x][y].isStench() && !haveVisited(x,y) && canMove(x, y) && !foundGold){
            moveIn(x,y);
        }else if(!cave[x][y].isBreeze() && !cave[x][y].isStench() && !haveVisited(x,y) && !canMove(x, y) && !foundGold && !fullyExplored(x,y)){
            turnLeft();
            if(canMove(x,y)) {
                moveIn(x, y);
            }else{
                findGold(x,y);
            }
        }else if(!cave[x][y].isBreeze() && !cave[x][y].isStench() && !haveVisited(x,y) && !canMove(x, y) && !foundGold && fullyExplored(x,y)){
            if(direction == 2 && canMove(x,y)){
                moveIn(x,y);
            }else if(direction == 1 && canMove(x,y)){
                moveIn(x,y);
            }else if(direction == 3 && canMove(x,y)){
                moveIn(x,y);
            }else if(direction == 0 && canMove(x,y)){
                moveIn(x,y);
            }
        }
        else if(!cave[x][y].isBreeze() && !cave[x][y].isStench() && haveVisited(x,y) && !foundGold){
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

    /* Method for checking all squares on the map to find potential of pits on any square adjacent to a known breeze
     * TODO: logic for places where pit chance is weighted by other breezes i.e. there are multiple configurations for breeze pit setups in an area
     */


    // calculates the least risky place to explore given current knowledge
    // takes in current position and then moves player to the least Shwifty spot, if player does not die and
    // current position is not a breeze or stench returns to that other method thing

    private int[] getShwifty(){

        // avoid stench

        chanceOfPit =setInitialChance(); // zero out

        for (int x = 0; x < caveSize; x++) {   // anything that could be a pit is a pit!!!! unless its not
            for (int y = 0; y < caveSize; y++) {
                if (knowledgeOfBreeze[x][y]) {   //  squares that could be pits
                    if(x+1<caveSize && safeSquares[x+1][y]) {
                        chanceOfPit[x + 1][y] +=1;
                    }
                    if(x-1>0 && safeSquares[x-1][y]) {
                        chanceOfPit[x - 1][y] +=1;
                    }
                    if(y+1<caveSize&& safeSquares[x][y+1]) {
                        chanceOfPit[x][y+1] +=1;
                    }
                    if(y-1>0&& safeSquares[x][y-1]) {
                        chanceOfPit[x][y-1] +=1;
                    }
                }
            }
        }


        int tempX = -1; // going to be our least deathy spot
        int tempY = -1;

        for (int i = 0; i < caveSize; i++) {   // find the min point that isnt 0
            for (int j = 0; j < caveSize; j++) {
                if(chanceOfPit[i][j]>0){
                    if(tempX==-1){ // first case set to
                        tempX=i;
                        tempY=j;
                    }
                    else if(chanceOfPit[i][j] < chanceOfPit[tempX][tempY]){
                        tempX=i;
                        tempY=j;
                    }
                }
            }
        }
        int [] coord = new int[2];

        System.out.println("Gettin shwifty, going to "+ tempX+", "+ tempY);

        coord[0]= tempX;
        coord[1]= tempY;
        return coord;
    }





    private void calculatePitOdds() {

        for (int x = 0; x < caveSize; x++) {
            for (int y = 0; y < caveSize; y++) {
                if (knowledgeOfBreeze[x][y]) {
                    ArrayList<int[]> potentialPits = potentialPits(x,y); //  squares that could be pits

                    for (int[] position: potentialPits) { // for position in potential pits
                        chanceOfPit[position[0]][position[1]] = 1/(potentialPits.size());
                    }
                }
            }
        }
    }

    private ArrayList<int[]> potentialPits(int x, int y) {
        ArrayList<int[]> potentialPits = new ArrayList<int[]>();
        if (x < caveSize-1 && !knowledgeOfPits[x+1][y]) {
            int[] xyArray = {x+1,y};
            potentialPits.add( xyArray );
        }
        if (x > 0 && !knowledgeOfPits[x-1][y]) {
            int[] xyArray = {x-1,y};
            potentialPits.add( xyArray );
        }
        if (y < caveSize-1 && !knowledgeOfPits[x][y+1]) {
            int[] xyArray = {x,y+1};
            potentialPits.add( xyArray );
        }
        if (y > 0 && !knowledgeOfPits[x][y-1]) {
            int[] xyArray = {x,y-1};
            potentialPits.add( xyArray );
        }

        return potentialPits;
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

    private boolean haveVisited(int x, int y){
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
        if((x-1) >= 0 || ((x-1) >= 0 && visited[x-1][y])){
            blockedVisited++;
        }else if(y-1 >= 0 || (y-1 >= 0 && visited[x][y-1])){
            blockedVisited++;
        }else if(y+1 < caveSize || (y+1 < caveSize && visited[x][y+1])){
            blockedVisited++;
        }else if(x+1 < caveSize || (x+1 < caveSize && visited[x+1][y])){
            blockedVisited++;
        }
        return blockedVisited == 4;
    }

    private void moveIn(int x, int y){
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
        if(direction == 0) getOut(x, (y - 1));
        else if(direction == 1) getOut((x + 1), y);
        else if(direction == 2) getOut(x, (y + 1));
        else if(direction == 3) getOut((x - 1), y);
    }

    private void turnLeft(){
        if(direction == 0){
           direction = 3;
        }else{
            direction -= 1;
        }
    }

    private boolean canMove(int x, int y){
        if(direction == 0){
            return y - 1 >= 0;
        }else if(direction == 1){
            return x + 1 < caveSize;
        }else if(direction == 2){
            return y + 1 < caveSize;
        }else if(direction == 3){
            return x - 1 >= 0;
        }
        return true;
    }

    private void getOut(int x, int y){
        updateMap.setPlayer(x,y);
        updateMap.setGold(x,y);
        updateMap.printCave();
        if(x == 0 && y == 0){
//            System.out.println(score);
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

    private double[][] setInitialChance () {
        double[][] chance = new double[caveSize][caveSize];
        for (int x = 0; x < caveSize; x++) {
            for (int y = 0; y < caveSize; y++) {
                chance[x][y] = 0;
            }
        }
        return chance;
    }
}
