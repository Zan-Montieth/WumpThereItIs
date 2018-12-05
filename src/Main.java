import java.io.*;

public class Main {

    public static void main(String[] args) {

//        wumpusPrompt();

        Cave fourBfour   = new Cave(4);
        Player fourBfourPlayer = new Player(4, fourBfour.getNodeCave(), fourBfour);
        fourBfourPlayer.recursiveSafeSearch(0,0);

//        fourBfourPlayer.findGold(0,0);
//        fourBfourPlayer.printKnowledgeOfBreezes();

//        Cave fiveBfive   = new Cave(5);
//
//        Cave eightBeight = new Cave(8);
//        Player eightBeightPlayer = new Player(8, eightBeight.getNodeCave(), eightBeight);
//        eightBeightPlayer.recursiveSafeSearch(0,0);
//
//        Cave tenBten     = new Cave(10);
//        tenBten.printCave();
    }

    private static void wumpusPrompt() {

        try {
            String dimension = null;
            int dim = 0;
            System.out.print("Enter the desired size of Wumpus world, or -1 to exit: ");
            System.out.println();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            dimension = bufferedReader.readLine();
            try {
                dim = Integer.parseInt(dimension);
            } catch (java.lang.NumberFormatException e) {
                System.out.println("Please enter a number.");
            }

            while ( !(dim == -1) && (dim < 2 || dim > 50)) {
                System.out.println("Please choose a size between 2 and 50, or -1 to exit:");
                dimension = bufferedReader.readLine();
                try {
                    dim = Integer.parseInt(dimension);
                } catch (java.lang.NumberFormatException e) {
                    System.out.println("Please enter a number.");
                }
            }

            while (dim != -1) {

                Cave myCave = new Cave(dim);
                Player myPlayer = new Player(dim, myCave.getNodeCave(), myCave);
                myPlayer.search(0, 0);

                System.out.print("Enter the desired size of Wumpus world, or -1 to exit: ");
                System.out.println();

                dimension = bufferedReader.readLine();
                try {
                    dim = Integer.parseInt(dimension);
                } catch (java.lang.NumberFormatException e) {
                    System.out.println("Please enter a number.");
                }            }
            System.out.println("Goodbye");
        }
        catch (IOException e) {
            System.out.println("Error:"+e);
        }


    }
}
