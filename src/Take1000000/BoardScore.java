/// Caleb Heim

package Take1000000;


//Stores the score -- kind of a mess
public interface BoardScore {

    public static int[][] score = new int[8][8];

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



    public static void test(){

        for(int i = 0; i < 8; i ++){
            for(int j =0; j < 8; j ++){
                System.out.print(score[i][j] + " ");
            }
            System.out.println();
        }
    }



}

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
