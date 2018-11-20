public class Player {

    private boolean[][] knowledgeOfBreeze;
    private boolean[][] knowledgeOfStench;
    private boolean[][] knowledgeOfPits;
    private boolean[][] knowledgeOfWumpus;
    private boolean dead;
    private int caveSize;
    private Cave cave;
    private int score;

    public Player(int inCaveSize, Cave inCave){
        caveSize = inCaveSize;
        cave = inCave;
        knowledgeOfBreeze = new boolean[inCaveSize][inCaveSize];
        knowledgeOfPits   = new boolean[inCaveSize][inCaveSize];
        knowledgeOfStench = new boolean[inCaveSize][inCaveSize];
        knowledgeOfWumpus = new boolean[inCaveSize][inCaveSize];
        cave.setPlayer(0,0, true);
        cave.printCave();
    }

    public void sense(int inX, int inY){

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
