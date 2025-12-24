/// Caleb Heim
/// I regret making this interface....
/// completely not needed, but oh well

package Take1000000;


/// Stores the score -- kind of a mess
public interface BoardScore {

    public static int[][] score = new int[8][8]; ///board keeping track of who is where

    public static int[] nextYx = {-1,-1};


//    public static boolean win1 = false;
//    public static boolean win2 = false;
//
//    public static boolean draw = false;


    /*
    *    [] [] [] [] [] [] [] []
    *    [] [] [] [] [] [] [] []
    *    [] [] [] [] [] [] [] []
    *    [] [] [] [] [] [] [] []
    *    [] [] [] [] [] [] [] []
    *    [] [] [] [] [] [] [] []
    *    [] [] [] [] [] [] [] []
    *    [] [] [] [] [] [] [] []
    */

    /// updates the score variable
    public static void updateScore(int[] position, Piece piece){

        score[position[0]][position[1]] = piece.getPlayerNum();

        if(piece.getPlayerNum() == 1){
           Score.score++;

        }else if(piece.getPlayerNum() == 2){
            Score.score++;
        }

        System.out.println("Score: " + Score.score);
        //check(position,piece);

        //test();
    }

    /// updates the score variable as well
    public static void updateScore(int[] position, int num){

        score[position[0]][position[1]] = num;

        if(num == 1){
            Score.score++;

        }else if(num == 2){
            Score.score++;
        }

        System.out.println("Score: " + Score.score);
        //check();

        //test();
    }


    /// debug
    public static void test(){

        for(int i = 0; i < 8; i ++){
            for(int j =0; j < 8; j ++){
                System.out.print(score[i][j] + " ");
            }
            System.out.println();
        }
    }



}

/// literally ended up only being used to keep track of if a player cannot move anymore
/// pretty much not needed tbh
class Score{

//    volatile static boolean p1Stuck = false;
//    volatile static boolean p2Stuck = false;

    protected static boolean stuck = false;

//    volatile static boolean win1 = true;
//    volatile static boolean win2 = false;

    protected static boolean draw = false;

    protected static int score = 2;
//    volatile static int p2Score = 2;

}
