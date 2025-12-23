/// Caleb Heim

package Take1000000;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

//class for the players 'Go' pieces
public class Piece {

    private Color color;//black or white depending on player
    //private Color outline;
    private int playerNum; //make testing whos turn easier

    //protected Circle chip;
    private boolean turn;


    // constructor - used to set colors of each player
    public Piece (int player){

        if(player == 1){
//            color = new Color(0.1,0.1,0.1,1);
            color = new Color(0.63, 0.68, 0.97,1);
            //outline = new Color(0.25,0.1,0.86,1);
            turn = true;
        }
        else if (player == 2){
            color = new Color (0.25,0.1,0.86,1);
            //outline = new Color(0.87, 0.92, 0.97,1);
            turn = false;
        }



        playerNum = player;
    }

//    public static void pieceSetter(int player){
//
//        if(player == 1){
//            color = new Color(0.1,0.1,0.1,1);
//            playerNum = "Player 1";
//
//        }
//        else if (player == 2){
//            color = new Color (0.8,0.8,0.8,1);
//            playerNum = "Player 2";
//        }
//
//    }



    public int getPlayerNum(){
        return playerNum;
    }

    public Color getPlayerColor(){
        return color;
    }

    public void getPlayerTurn(int pN){
        playerNum = pN;
    }

    public void setPlayerColor(Color c){
        color = c;
    }


    public Circle placeChip(StackPane p){

        Circle c = new Circle();
        c.setFill(color);
        //c.setStroke(outline);
        c.setManaged(false);

        c.setCenterX(40);
        c.setCenterY(40);

        c.radiusProperty().bind(p.widthProperty().multiply(0.4));

        p.getChildren().add(c);
        c.toFront();

        return c;

    }
    //places other players piece
    public void placeOppPiece(int c, StackPane p){

        //System.out.println(c);

        Piece pi = new Piece(c);

        Circle chip = new Circle();
        chip.setFill(pi.color);
        chip.setManaged(false);

        chip.setCenterX(40);
        chip.setCenterY(40);

        chip.radiusProperty().bind(p.widthProperty().multiply(0.4));

        p.getChildren().add(chip);

//        c.setCenterX(40);
//        c.setCenterY(40);
        //c.toFront();
    }


    public boolean myTurn(){
        return turn;
    }

    public void setMyTurn(boolean b){
        turn = b;
    }

}



