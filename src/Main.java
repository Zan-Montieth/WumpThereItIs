public class Main {

    public static void main(String[] args) {

        Cave fourBfour   = new Cave(4);
        Player fourBfourPlayer = new Player(4, fourBfour.getNodeCave(), fourBfour);
        fourBfourPlayer.findGold(0,0);
        //fourBfourPlayer.printKnowledgeOfBreezes();

//        Cave fiveBfive   = new Cave(5);
//
//        Cave eightBeight = new Cave(8);
//
//        Cave tenBten     = new Cave(10);
    }
}
