/// Caleb Heim
/// the board that will be played on

package Take1000000;

import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


/// Game board
public class Board {

    protected Spaces[][] spacers = new Spaces[8][8]; //allows access to spaces in board

    protected boolean[][] playable= new boolean[8][8]; //checks if a piece can be put down

    protected GridPane grid = new GridPane(); //layout

    public Board (StackPane root){

        //grid.setAlignment(Pos.CENTER);

        grid.setMaxSize(640,640);



        /// sets up the spaces to be in an 8x8 grid
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){

                Spaces space = new Spaces(new int[]{i,j});
                spacers[i][j] = space;
                //space.setId(j + "" + i);
                grid.add(spacers[i][j],j,i);


//                Text num = new Text(i + ", " +j);
//                grid.add(num,j,i);


            }
        }

//        for (int i = 0; i < 8; i++) {
//            ColumnConstraints col = new ColumnConstraints(80);
//            grid.getColumnConstraints().add(col);
//
//            RowConstraints row = new RowConstraints(80);
//            grid.getRowConstraints().add(row);
//        }

        root.getChildren().add(grid);
        //grid.setGridLinesVisible(true); //for testing
    }


    public GridPane getGrid(){
        return grid;
    }

    public Spaces getSpace(int a, int b){
        return spacers[a][b];
    }

    /// is it a playable option??


    /// checks if move is possible
    public boolean[][] checkPlayable(int player) {

        playable = new boolean[8][8];
        boolean can = false;

        int opp = 0;

        if (player == 1){
            opp = 2;

        }else if(player ==2) {
            opp = 1;

        }

        //reset
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                spacers[i][j].makeCanPlay(false);
            }
        }


        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {


                //continues if empty
                if (BoardScore.score[i][j] == 0){

                    //different directions of a space to check
                    int[][] possible = {{-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}};


                    //goes through those directions
                    for (int[] p : possible) {

                        int row = i + p[0];
                        int col = j + p[1];

                        boolean oppInMid = false;

                        // if in bounds

                        while (row >= 0 && row < 8 && col >= 0 && col < 8) {

                            if (BoardScore.score[row][col] == opp) {

                                oppInMid = true;

                                row += p[0];
                                col += p[1];

                            } else if (BoardScore.score[row][col] == player && oppInMid) {

                                playable[i][j] = true;

                                spacers[i][j].makeCanPlay(true);

                                can = true;
                                break;

                            } else {

                                break;
                            }
                        }
                    }
                }
            }
        }
        if(!can){
            Score.stuck = true;
        }else{
            Score.stuck = false;
        }


        return playable;//returns outcome

    }






//    public List<int[]> check(int[] position, Piece piece){
//
//        List<int[]> allFlips = new ArrayList<>();
//
//        int opp = 0;
//
//        if (piece.getPlayerNum() == 1){
//            opp = 2;
//        }
//        else if(piece.getPlayerNum() ==2){
//            opp = 1;
//        }
//
//        int[][] possible = {{-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}};
//
//        for (int[] p : possible) {
//            int row = position[0] + p[0];
//            int col = position[1] + p[1];
//
//            List<int[]> temp = new ArrayList<>();
//
//            while (row >= 0 && row < 8 && col >= 0 && col < 8) {
//
//                if (!spacers[row][col].getOccupied()) {
//                    temp.clear();
//                    break;
//                }
//
//                if (spacers[row][col].getOccupiedBy() == opp) {
//                    temp.add(new int[]{row, col});
//                }
//                else if (spacers[row][col].getOccupiedBy() == piece.getPlayerNum()) {
//                    allFlips.addAll(temp);
//                    break;
//                }
//                else {
//                    break;
//                }
//
//                row += p[0];
//                col += p[1];
//            }
//        }
//
//        return allFlips;
//    }

    /// check who flips
    public List<int[]> check(int[] position, Piece piece){

        List<int[]> allFlips = new ArrayList<>();

        int opp = 0;

        if (piece.getPlayerNum() == 1){
            opp = 2;
        }
        else if(piece.getPlayerNum() ==2){
            opp = 1;
        }

        //possible directions
        int[][] possible = {{-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}};

        for (int[] p : possible) {
            int row = position[0] + p[0];
            int col = position[1] + p[1];

            if (row < 0 || row >= 8 || col < 0 || col >= 8 || BoardScore.score[row][col] != opp) {
                continue;
            }

            List<int[]> temp = new ArrayList<>();

            while (row >= 0 && row < 8 && col >= 0 && col < 8) {


                if (BoardScore.score[row][col] == opp) {
                    temp.add(new int[]{row, col});

                }
                else if (BoardScore.score[row][col] == piece.getPlayerNum()) {
                    allFlips.addAll(temp);
                    break;

                }
                else {
                    break;

                }



//                if (BoardScore.score[row][col] == 0) {
//                    temp.clear();
//                    break;
//                }
//
//                if (BoardScore.score[row][col] == opp) {
//                    temp.add(new int[]{row, col});
//                }
//                else if (BoardScore.score[row][col] == piece.getPlayerNum()) {
//                    allFlips.addAll(temp);
//                    break;
//                }
//                else {
//                    break;
//                }

                row += p[0];
                col += p[1];
            }
        }

        return allFlips;
    }

    /// flips chips
    public void flipper(List<int[]> list, int player){

        for(int[] i : list){

            Piece newP = new Piece(player);
            newP.placeChip(spacers[i[0]][i[1]]);
            spacers[i[0]][i[1]].setOccupied(new Piece(player));

            BoardScore.score[i[0]][i[1]] = player;


            System.out.print(i[0] + " " + i[1] + " " +player);

        }

    }

}

