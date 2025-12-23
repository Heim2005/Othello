/// Caleb Heim

package Take1000000;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Application implements BoardScore {

    int port = 8000;

    public static boolean p1Stuck = false;
    public static boolean p2Stuck = false;

    volatile static boolean win1 = false;
    volatile static boolean win2 = false;

    volatile static boolean draw = false;

   // volatile static int p1Score = 2;
   // volatile static int p2Score = 2;

    private int players = 0;
    private boolean lastAction = false;

    private final StackPane sPane = new StackPane();
    private final HBox hbox = new HBox();
    private final HBox hbox2 = new HBox();

    private final Rectangle b1 = new Rectangle(175,175);
    private final Rectangle b2 = new Rectangle(175,175);

    private Color idle = new Color(1,1,1,1);
    private Color inPlay = new Color(0.612,1,0.447,1);

    private Circle cOne;
    private Circle cTwo;

    private boolean player1Turn = true;
    private boolean player2Turn = false;

    private int checkNet = 0;


    @Override
    public void start(Stage stage) throws Exception {

        design();

        Scene scene = new Scene(sPane,500,300);

        stage.setTitle("Othello Server");
        stage.setScene(scene);
        scene.setFill(new Color(0.90,0.86,0.98,1));
        stage.setResizable(false);
        stage.show();

        BoardScore.updateScore(new int[]{3,3},1);
        BoardScore.updateScore(new int[]{4,4},1);
        BoardScore.updateScore(new int[]{3,4},2);
        BoardScore.updateScore(new int[]{4,3},2);

        new Thread( () -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
//                Platform.runLater(() -> taLog.appendText(new Date() +
//                        ": Server started at socket 8000\n"));

                // Ready to create a session for every two players
                while (true) {
//                    Platform.runLater(() -> taLog.appendText(new Date() +
//                            ": Wait for players to join session " + sessionNo + '\n'));

                    Socket player1 = serverSocket.accept();//player 1 joins

                    new DataOutputStream(player1.getOutputStream()).writeInt(1);


//                    BufferedReader p1in = new BufferedReader(new InputStreamReader(player1.getInputStream()));
//                    PrintWriter p1out = new PrintWriter(player1.getOutputStream(), true);


                    //new DataOutputStream(player1.getOutputStream()).writeInt(1);


                    Platform.runLater(() -> {

                        //System.out.println("1");
                    });

                    //new DataOutputStream(player1.getOutputStream()).writeInt(0);

                    // Notify that the player is Player 1
                    //new DataOutputStream(player1.getOutputStream()).writeInt(1);


                    Socket player2 = serverSocket.accept(); //player 2 joins


                    Platform.runLater(() -> {
                        //System.out.println("2");
                    });


                    new DataOutputStream(player2.getOutputStream()).writeInt(2);


                    Platform.runLater(() -> {

                        //lastAction = true;
                        b1.setStrokeWidth(15);


                    });

                    new DataOutputStream(player1.getOutputStream()).writeInt(0);

                    new Thread(new WhenRan(player1, player2)).start();

                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }).start();


    }

    public static void main(String[] args) {
        launch(args);
    }

    public void moveFix(){

        cOne.setTranslateX(0);
        cTwo.setTranslateX(0);

    }

    public void turn(Rectangle r){

//        b1.setFill(idle);
//        b2.setFill(idle);

        b1.setStrokeWidth(0);
        b2.setStrokeWidth(0);


        r.setStrokeWidth(15);

        //moveFix();
    }

    class WhenRan implements Runnable{
        private Socket player1;
        private Socket player2;

        private DataInputStream fromPlayer1;
        private DataOutputStream toPlayer1;
        private DataInputStream fromPlayer2;
        private DataOutputStream toPlayer2;

//        private boolean p1Turn;
//        private boolean p2Turn;

        public WhenRan(Socket p1, Socket p2){

            player1 = p1;
            player2 = p2;


        }


        @Override
        public void run() {

            try {
                // Create data input and output streams
                DataInputStream fromPlayer1 = new DataInputStream(
                        player1.getInputStream());
                DataOutputStream toPlayer1 = new DataOutputStream(
                        player1.getOutputStream());
                DataInputStream fromPlayer2 = new DataInputStream(
                        player2.getInputStream());
                DataOutputStream toPlayer2 = new DataOutputStream(
                        player2.getOutputStream());

                // Write anything to notify player 1 to start
                // This is just to let player 1 know to start
                //toPlayer1.writeInt(1);

                // Continuously serve the players and determine and report
                // the game status to the players
                while (true) {
                    // Receive a move from player 1

                    BoardScore.test();

                    fromPlayer1.readInt();
                    fromPlayer2.readInt();

                    toPlayer1.writeInt(999);
                    toPlayer2.writeInt(999);

                    endConditions(fromPlayer1,fromPlayer2,toPlayer1,toPlayer2);

                    fromPlayer1.readInt();
                    fromPlayer2.readInt();

                    toPlayer1.writeInt(999);
                    toPlayer2.writeInt(999);

                    fromPlayer1.readInt();
                    fromPlayer2.readInt();

                    toPlayer1.writeInt(999);
                    toPlayer2.writeInt(999);

                    /// A
                    toPlayer1.writeInt(1); ///1
                    //System.out.println("a1");

                    toPlayer2.writeInt(2); ///1
                    //System.out.println("a2");

                    /// B
                    //boolean p1Turn = fromPlayer1.readBoolean(); ///2
                    System.out.println("1 turn: " + player1Turn);
                    toPlayer1.writeBoolean(player1Turn);

                    //boolean p2Turn = fromPlayer2.readBoolean();  ///2
                    System.out.println("2 turn: " + player2Turn);
                    toPlayer2.writeBoolean(player2Turn);

//                    Score.win1 = fromPlayer1.readBoolean();
//                    Score.win2 = fromPlayer1.readBoolean();

//                    if(win1){
//                        System.out.println("win1");
//                        break;
//                    }
//                    if(win2){
//
//                        break;
//                    }
//                    if(draw){
//
//                        break;
//                    }

                    List<int[]> list = new ArrayList<>();

                    toPlayer1.writeInt(100);  //empty
                    toPlayer2.writeInt(100);   //filler

                    if(player1Turn){

                        fromPlayer1.readInt();
                        toPlayer1.writeInt(999);


                        //System.out.println("p1Turn");

                        //Thread.sleep(100);
                        //System.out.println(fromPlayer1.readInt());

                        fromPlayer1.readInt();/// <-a
                        toPlayer1.writeInt(100); /// ->b

                        fromPlayer1.readInt(); /// <-a
                        toPlayer1.writeInt(999); /// ->b

                        int size = fromPlayer1.readInt();///3 <-c
                        System.out.println("c" + size);

                        System.out.print("Loop: ");
                        for (int i = 0; i < size; i++){

                            //System.out.print(i + ":  ");

                            int y = fromPlayer1.readInt(); ///loop y
                            //System.out.print(y);

                            int x = fromPlayer1.readInt(); ///loop x
                            //System.out.println(x);

                            BoardScore.updateScore(new int[]{y,x},1); //check what does

                            list.add(new int[]{y,x});
                            //System.out.println(list.toString());

                            System.out.print(i + ", ");

                        }
                        System.out.println("end");

                        toPlayer1.writeInt(200); /// ->d

                        int[] yx = new int[2];///waiting for THISSS

                        //maybe write a sync ^

                        yx[0] = fromPlayer1.readInt(); /// <-e
                        yx[1] = fromPlayer1.readInt(); /// <-f


                        //System.out.println(yx[0] + "   " + yx[1]);

                        int who = fromPlayer1.readInt(); ///<-g
                        //System.out.println(who);

                        BoardScore.updateScore(yx,who);

                        //System.out.println("p1TurnOver");

                        ///

                        fromPlayer2.readInt();
                        toPlayer2.writeInt(999);

                        toPlayer2.writeInt(size); /// ->h

                        //System.out.println(list.toString());

                        for (int[] i : list){

                            toPlayer2.writeInt(i[0]); ///loop y
                            toPlayer2.writeInt(i[1]); ///loop x
                        }


                        toPlayer2.writeInt(yx[0]); /// ->i
//                        toPlayer2.writeInt(999);

                        toPlayer2.writeInt(yx[1]); /// ->j
                        toPlayer2.writeInt(who); /// ->k

                        toPlayer2.writeInt(100); /// ->l

                        ///

                        //toPlayer2.writeBoolean(true);//swap turn
                        turn(b2);

                        //Thread.sleep(1000);

//                        toPlayer1.writeBoolean(false);
//                        toPlayer2.writeBoolean(true);
                        //toPlayer1.writeInt(100);

                    }else if(player2Turn){

                        fromPlayer2.readInt();
                        toPlayer2.writeInt(999);

                        //System.out.println("p2Turn");

                        //Thread.sleep(1000);

                        fromPlayer2.readInt(); /// <-a
                        toPlayer2.writeInt(100); /// ->b

                        fromPlayer2.readInt(); /// <-a
                        toPlayer2.writeInt(999); /// ->b

                        int size = fromPlayer2.readInt(); /// <-c
                        System.out.println("c" + size);

                        for (int i = 0; i < size; i++){

                            int y = fromPlayer2.readInt(); ///loop y
                            int x = fromPlayer2.readInt(); ///loop x

                            BoardScore.updateScore(new int[]{y,x},2);

                            list.add(new int[]{y,x});
                        }


                        toPlayer2.writeInt(100); /// ->d


                        int[] yx = new int[2];

                        yx[0] = fromPlayer2.readInt(); /// <-e
                        yx[1] = fromPlayer2.readInt(); /// <-f

                        int who = fromPlayer2.readInt(); /// <-g

                        BoardScore.updateScore(yx,who);

                        //System.out.println("p2TurnOver");

                        ///

                        fromPlayer1.readInt();
                        toPlayer1.writeInt(999);

                        toPlayer1.writeInt(size); /// -> h
                        System.out.println("ARE YOU GETTING THIS " + size);

                        for (int[] i : list){

                            toPlayer1.writeInt(i[0]); ///loop y
                            toPlayer1.writeInt(i[1]); ///loop x
                        }

                        toPlayer1.writeInt(yx[0]); /// ->i
                        toPlayer1.writeInt(yx[1]); /// ->j
                        toPlayer1.writeInt(who); /// ->k

                        toPlayer1.writeInt(100); /// ->l

                        ///

                        //toPlayer1.writeBoolean(true);

                        turn(b1);

                        //Thread.sleep(1000);

                        //toPlayer2.writeInt(100);
//                        toPlayer1.writeBoolean(true);
//                        toPlayer2.writeBoolean(false);

                    }

                    Thread.sleep(1000);

                    if(player1Turn){
                        player1Turn = false;
                        player2Turn = true;
                    }else if(player2Turn){
                        player2Turn = false;
                        player1Turn = true;
                    }

                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        }
    }

    public void design(){

        StackPane s1 = new StackPane();
        b1.setFill(idle);
        Piece p1 = new Piece(1);
        Text t1 = new Text("Player 1");
        t1.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 30));
        t1.setFill(p1.getPlayerColor());
        t1.setTranslateY(-55);
        s1.getChildren().addAll(b1,t1);
        s1.setTranslateX(-105);
        s1.setTranslateY(30);
        cOne = p1.placeChip(s1);
        cOne.radiusProperty().unbind();
        cOne.setRadius(35);
        cOne.setCenterX(148);
        cOne.setCenterY(205);
//        cOne.setCenterX(b1.getLayoutBounds().getCenterX());
//        cOne.setCenterY(b1.getLayoutBounds().getCenterY());
        //cOne.setTranslateY(135);
//        c1.translateXProperty().bind(b1.widthProperty().multiply(0.5).subtract(c1.getRadius()));

        StackPane s2 = new StackPane();
        b2.setFill(idle);
        Piece p2 = new Piece(2);
        Text t2 = new Text("Player 2");
        t2.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 30));
        t2.setFill(p2.getPlayerColor());
        t2.setTranslateY(-55);
        s2.getChildren().addAll(b2,t2);
        s2.setTranslateX(105);
        s2.setTranslateY(30);
        cTwo = p2.placeChip(s2);
        cTwo.radiusProperty().unbind();
        cTwo.setRadius(35);
        cTwo.setCenterX(358);
        cTwo.setCenterY(205);
//        cTwo.setCenterX(b2.getLayoutBounds().getCenterX());
//        cTwo.setCenterY(b2.getLayoutBounds().getCenterY());
//        c2.translateXProperty().bind(b2.widthProperty().multiply(0.5).subtract(c1.getRadius()));


        b1.setStroke(inPlay);
        b2.setStroke(inPlay);

        b1.setStrokeWidth(0);
        b2.setStrokeWidth(0);

        b1.setStrokeType(StrokeType.OUTSIDE);
        b1.setStrokeType(StrokeType.OUTSIDE);

        hbox.setAlignment(Pos.CENTER);
        hbox.setTranslateY(30);
        hbox.setSpacing(40);

        hbox2.setAlignment(Pos.CENTER);
        hbox2.setTranslateY(30);
        hbox2.setSpacing(215);



        Text t = new Text("Othello Server");
        t.setFont(Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, 30));
        t.setFill(new Color(0.25,0.2,0.45,1));
        t.setTranslateY(-100);

        StackPane sPane1 = new StackPane();

        sPane1.getChildren().addAll(s1,s2);
//        sPane1.getChildren().addAll(hbox);
        sPane1.getChildren().add(t);

        StackPane sPane2 = new StackPane();

        sPane2.getChildren().addAll(cOne,cTwo);

        sPane.getChildren().addAll(sPane1,sPane2);

    }

    public void endConditions(DataInputStream fromPlayer1, DataInputStream fromPlayer2, DataOutputStream toPlayer1, DataOutputStream toPlayer2) throws IOException {


//        p1Score = fromPlayer1.readInt();
//        p2Score = fromPlayer2.readInt();

        p1Stuck = fromPlayer1.readBoolean();
        p2Stuck = fromPlayer2.readBoolean();


        int player1Score = 0;
        int player2Score = 0;

        if(p1Stuck || p2Stuck){


            for(int i = 0; i < 8; i ++){
                for(int j =0; j < 8; j ++){

                    if(BoardScore.score[i][j] == 1){
                        player1Score++;
                    }
                    if(BoardScore.score[i][j] == 2){
                        player2Score++;
                    }

                    System.out.print(BoardScore.score[i][j] + " ");
                }
                System.out.println();
            }



            if(player1Score > player2Score){
                win1 = true;
            }else if(player2Score > player1Score){
                win2 = true;

            }else{
                draw = true;
            }

        }

        toPlayer1.writeBoolean(win1);
        toPlayer2.writeBoolean(win1);

        toPlayer1.writeBoolean(win2);
        toPlayer2.writeBoolean(win2);

        toPlayer1.writeBoolean(draw);
        toPlayer2.writeBoolean(draw);


    }

}
