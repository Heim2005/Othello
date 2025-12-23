/// Caleb Heim


package Take1000000;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//class to make each space to put pieces on interactable
public class Spaces extends StackPane {

    protected Rectangle box = new Rectangle();

    private Color idle = new Color(0.85,0.92,0.87,1);
    private Color hover = new Color(0.56,0.74,0.6,1);

    private int[] yx;

    private boolean occupied = false;

    private int occupiedBy = 0;

    private boolean canPlay;

    public Spaces(int[] yx) {

        canPlay = false;

        this.yx = yx;

        box.setFill(idle);
//        box.setStroke(new Color(0.667,0.6,0.929,1));
        box.setStroke(new Color(1,1,1,1));
        box.setStrokeWidth(5);

        setPrefSize(80,80);

        setMaxSize(80,80);

        setAlignment(Pos.CENTER);

        box.widthProperty().bind(widthProperty());
        box.heightProperty().bind(heightProperty());

        getChildren().add(box);


        setOnMouseEntered(e -> onHover());
        setOnMouseExited(e -> onNotHover());
        setOnMousePressed(e -> onPress());


//        setWidth(10);
//        setHeight(10);

//       autosize();

//        box = new Rectangle(100,100);
//        box.setFill(new Color(0.3,1,0.7,1));

    }

//    public Rectangle getBox(){
//        return box;
//    }

    public void onNotHover() {
        box.setFill(idle);

    }

    //if(Play.getBoard().checkPlayable(Play.getPiece().getPlayerNum())[yx[0]][yx[1]])

    public void onHover() {

//        boolean q = Play.getTurn();
//
//        System.out.println(q);

        if(Play.getPiece() != null && Play.getTurn()){
            if(!occupied) {
                if (canPlay) {
                    box.setFill(hover);
                }
            }
        }

        //System.out.println("Hovered");

    }

    public void onPress(){

        //System.out.println("presss");

        if(Play.getPiece() != null && Play.getTurn()){
            if(!occupied) {
                if(canPlay){
                    //BoardScore.updateScore(yx, Play.getPiece());
                    Play.getPiece().placeChip(this);
                    System.out.println(yx[0]);
                    System.out.println(yx[1]);

//                BoardScore.nextYx[0] = yx[0];
//                BoardScore.nextYx[1] = yx[1];
                    Play.updater(yx,Play.getPiece());
                    occupied = true;
                    occupiedBy = Play.getPiece().getPlayerNum();

//                    if(Play.getPiece().getPlayerNum() == 1){
//                        Play.turn = false;
//                    }else if(Play.getPiece().getPlayerNum() == 2) {
//                        Play.turn = false;
//                    }
                }

            }

        }

        //BoardScore.score[yx[0]][yx[1]] = Play.

    }

    public int[] getYx(){
        return yx;
    }

    public void otherSidePress(int c){

        Play.getPiece().placeOppPiece(c, this);
        occupied = true;

    }

    public void setOccupied(Piece piece){
        occupied = true;
        Play.updater(yx,piece);
    }
    public boolean getOccupied(){
        return occupied;
    }

    public void setOccupiedBy(Piece piece){
        occupiedBy = piece.getPlayerNum();
    }
    public int getOccupiedBy(){
        return occupiedBy;
    }

    public void makeCanPlay(boolean play){
        canPlay = play;
    }

    public boolean getCanPlay(){
        return canPlay;
    }

}
