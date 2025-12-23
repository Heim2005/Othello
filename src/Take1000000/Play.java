/// Caleb Heim


package Take1000000;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import static javafx.geometry.Pos.TOP_CENTER;


public class Play extends Application {

    int port = 8000;
    String host = "localhost";


    private DataInputStream fromServer;
    private DataOutputStream toServer;

    private StackPane root;

    protected static boolean turn = false;

    private static Piece piece;

    StackPane waitScreen = new StackPane();

    StackPane turnWait = new StackPane();

    StackPane ruleScreen = new StackPane();

    private static final Object lock = new Object();

    volatile static int[] newData = null;

    private static Board board;

    private boolean win1 = false;
    private boolean win2 = false;
    private boolean draw = false;

    @Override
    public void start(Stage stage) throws Exception {

        root = new StackPane();

        board = new Board(root);
        //board.getGrid().setPadding(new Insets(100,100,100,100));

        Text youAre = new Text("You Are: ");
        youAre.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 30));
        youAre.setFill(new Color(0.59, 0.78, 0.64, 1));
        youAre.setTranslateY(375);
        youAre.setTranslateX(-150);
        root.getChildren().add(youAre);

        board.getGrid().setAlignment(Pos.CENTER);
        board.getGrid().setTranslateY(-50);
        //board.getGrid().setAlignment(Pos.CENTER);

        //root.getChildren().add(board.getGrid());
        Piece startPiece1 = new Piece(1);
        board.getSpace(3, 3).setOccupied(startPiece1);
        board.getSpace(3, 3).setOccupiedBy(startPiece1);
        startPiece1.placeChip(board.getSpace(3, 3));
        BoardScore.updateScore(new int[]{3, 3}, startPiece1);

        Piece startPiece2 = new Piece(1);
        board.getSpace(4, 4).setOccupied(startPiece2);
        board.getSpace(4, 4).setOccupiedBy(startPiece2);
        startPiece2.placeChip(board.getSpace(4, 4));
        BoardScore.updateScore(new int[]{4, 4}, startPiece2);

        Piece startPiece3 = new Piece(2);
        board.getSpace(3, 4).setOccupied(startPiece3);
        board.getSpace(3, 4).setOccupiedBy(startPiece3);
        startPiece3.placeChip(board.getSpace(3, 4));
        BoardScore.updateScore(new int[]{3, 4}, startPiece3);

        Piece startPiece4 = new Piece(2);
        board.getSpace(4, 3).setOccupied(startPiece4);
        board.getSpace(4, 3).setOccupiedBy(startPiece4);
        startPiece4.placeChip(board.getSpace(4, 3));
        BoardScore.updateScore(new int[]{4, 3}, startPiece4);

        Scene scene = new Scene(root, 750, 825);
        stage.setTitle("Othello");
        stage.setScene(scene);

        stage.setResizable(false);

        stage.show();


        waitingScreen(true);
        //System.out.println("huh");
        turnWaiter();
        turnWaiter(false);




        runClient(stage);



    }

    private void runClient(Stage stage) throws InterruptedException {

        try {

            // Create a socket to connect to the server
            Socket socket = null;

            socket = new Socket(host, port);


            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        } catch (Exception ex) {


            stage.close();

            //ex.printStackTrace();
        }

        new Thread(() -> {
            try {

                // Get notification from the server
                int player = fromServer.readInt();

                System.out.println("Player " + player);

                // Am I player 1 or 2?
                if (player == 1) {

                    //System.out.println("working" + player);

                    piece = new Piece(1);

                    //turn = true;


//                    Platform.runLater(() -> {
//                        lblTile.setText("Player 1 with token 'X'");
//                        lblStatus.setText("Waiting for player 2 to join");
//                    });

                    // Receive startup notification from the server
                    fromServer.readInt(); // Whatever read is ignored

                    // The other player has joined
                    Platform.runLater(() -> waitingScreen(false));


                    //Platform.runLater(() -> turnSwap());
                    //waitingScreen(false);
                } else if (player == 2) {

                    //System.out.println("working" + player);

                    piece = new Piece(2);

                    //turn = false;

                    Platform.runLater(() -> waitingScreen(false));
                    //waitingScreen(false);

                }

                Platform.runLater(this::showWhatPlayer);

                Platform.runLater(() -> waitingScreen(false));

                Platform.runLater(this::rules);

                boolean playLoop = true;

                while (playLoop) {

                    toServer.writeInt(999);
                    fromServer.readInt();

                    Platform.runLater(() -> board.checkPlayable(piece.getPlayerNum()));
                    //board.checkPlayable(piece.getPlayerNum());

                    toServer.writeInt(999);
                    fromServer.readInt();

                    endConditions(toServer, fromServer);

                    Platform.runLater(() -> {

                        if( win1 || win2 || draw){

                            StackPane stack = new StackPane();

                            Rectangle r = new Rectangle(800, 900);
                            r.setFill(new Color(0.73, 0.67, 0.93, 0.9));

                            Text t = null;

                            if(win1){
                                t = new Text("Player 1 Wins");
                                t.setFont(Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, 40));
                                t.setFill(new Color(0.25, 0.2, 0.45, 1));

                            }else if(win2){
                                t = new Text("Player 2 Wins");
                                t.setFont(Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, 40));
                                t.setFill(new Color(0.25, 0.2, 0.45, 1));

                            }else if(draw){
                                t = new Text("Draw");
                                t.setFont(Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, 40));
                                t.setFill(new Color(0.25, 0.2, 0.45, 1));

                            }


                            stack.getChildren().addAll(r, t);

                            root.getChildren().add(stack);


                        }

                    });

                    toServer.writeInt(999);
                    fromServer.readInt();

                    newData = null;

                    fromServer.readInt();         ///       ///A
                    //System.out.println("a" + player); ///1


                    //toServer.writeBoolean(turn);  ///2       ///B
                    //System.out.println("b" + player);
                    turn = fromServer.readBoolean();


                    List<int[]> list = new ArrayList<>();

                    fromServer.readInt(); //fillllerr

                    if (turn) {

                       Platform.runLater(() -> turnWaiter(false));

                        toServer.writeInt(999);
                        fromServer.readInt();


                        synchronized (lock) {
                            newData = null;   // reset BEFORE waiting
                            while (newData == null) {
                                lock.wait();
                            }
                        }


                        toServer.writeInt(100);   /// a->
                        fromServer.readInt(); /// <-b
//                        synchronized (this) {
//                            while (newData == null) {
//                                wait(); // wait until updater() notifies
//                            }
//                        }

//                        while(newData == null){
//                            System.out.print("???");
//                        }
                        //Thread.sleep(1000);


                        ///System.out.println(fromServer.readInt());
                        //sync

                        System.out.println(newData[0]);
                        System.out.println(newData[1]);

                        /// //thissss, sheckkk thsisss
                        list = new ArrayList<>(board.check(new int[]{newData[0], newData[1]}, piece));
                        //list = board.check(new int[]{newData[0], newData[1]}, piece);

                        toServer.writeInt(999);
                        fromServer.readInt();

                        toServer.writeInt(list.size()); ///3 ->c
                        System.out.println("c" + list.size());

                        System.out.print("Loop: ");
                        int integer = 0;
                        for (int[] i : list) {
                            try {
                                toServer.writeInt(i[0]); /// loop y
                                toServer.writeInt(i[1]); /// loop x

                                System.out.print(integer++ + ", ");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        System.out.println("end");

                        //fromServer.readInt();
                        System.out.println(fromServer.readInt());
                        ////sync  <-d

                        //System.out.println("wait");

                        toServer.writeInt(newData[0]); /// -> e //update row
                        //System.out.println("...1");

                        toServer.writeInt(newData[1]);/// ->f //update column
                        //System.out.println("...2");

                        toServer.writeInt(newData[2]); /// ->g //player
                        //System.out.println("...3");

                        BoardScore.score[newData[0]][newData[1]] = newData[2];

                        //newData = null;


                    } else {


                        Platform.runLater(() -> turnWaiter(true));


                        toServer.writeInt(999);
                        fromServer.readInt();

                        int oppFlip = fromServer.readInt(); /// <-h
                        System.out.println(oppFlip);


                        for (int i = 0; i < oppFlip; i++) {
                            try {
                                int one = fromServer.readInt(); ///loop y
                                int two = fromServer.readInt(); ///loop x

                                list.add(new int[]{one, two});
                            } catch (IOException e) {
                                throw new RuntimeException(e);

                            }

                        }
                        /// ??
                        System.out.println("gurlll");
                        System.out.println("here v");

                        int a = fromServer.readInt(); /// <-i
//                        System.out.println(fromServer.readInt());
                        System.out.println(a);

                        int b = fromServer.readInt(); /// <-j
                        System.out.println(b);


                        int c = fromServer.readInt(); /// <-k
                        System.out.println(c);

                        //board.getSpace(a,b).otherSidePress(c);
                        Platform.runLater(() -> board.getSpace(a, b).otherSidePress(c));

                        BoardScore.score[a][b] = c;

                        fromServer.readInt(); /// <-l

                        //piece.placeOppPiece(new int[]{a,b,c},board.getGrid());
                        //Platform.runLater(() -> piece.placeOppPiece(new int[]{a,b,c},board.getGrid()));

                    }

                    List<int[]> finList = new ArrayList<>(list);

                    //fromServer.readInt(); //sync


                    if (player == 1) {
                        if (!turn) {
//                            //turn = fromServer.readBoolean();
                            //turn = true;
                            Platform.runLater(() -> board.flipper(finList, 2));
//                            //Platform.runLater(() ->  turn = true);
                        } else {
                            //turn = false;
                            Platform.runLater(() -> board.flipper(finList, 1));
//                            //Platform.runLater(() ->  turn = false);
                        }
//
                    } else if (player == 2) {
                        if (!turn) {
                            //turn = true;
//                            //turn = fromServer.readBoolean();
                            Platform.runLater(() -> board.flipper(finList, 1));
//                            //Platform.runLater(() ->  turn = true);
                        } else {
                            //turn = false;
                            Platform.runLater(() -> board.flipper(finList, 2));
//                            Platform.runLater(() ->  turn = false);
                        }
//
                    }

                    Platform.runLater(() -> System.out.println(turn));

                    //Thread.sleep(1000);


                }

                // Continue to play
//                while (continueToPlay) {
//                    if (player == PLAYER1) {
//                        waitForPlayerAction(); // Wait for player 1 to move
//                        sendMove(); // Send the move to the server
//                        receiveInfoFromServer(); // Receive info from the server
//                    }
//                    else if (player == PLAYER2) {
//                        receiveInfoFromServer(); // Receive info from the server
//                        waitForPlayerAction(); // Wait for player 2 to move
//                        sendMove(); // Send player 2's move to the server
//                    }
//                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();

    }


    public void waitingScreen(boolean up) {

        if (up) {

            waitScreen.setDisable(false);
            waitScreen.setVisible(true);

            Rectangle r = new Rectangle(800, 900);
            r.setFill(new Color(0.73, 0.67, 0.93, 0.9));
            Text t = new Text("Waiting For Second Player");
            t.setFont(Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, 40));
            t.setFill(new Color(0.25, 0.2, 0.45, 1));

            waitScreen.getChildren().addAll(r, t);

            root.getChildren().add(waitScreen);

        } else {

            waitScreen.setDisable(true);
            waitScreen.setVisible(false);
        }

    }

    public void turnWaiter() {

        Rectangle rec = new Rectangle(800, 900);
        rec.setFill(new Color(0.73, 0.67, 0.93, 0.3));
        Text tex = new Text("Waiting For Turn");
        tex.setTranslateY(310);
        tex.setFont(Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, 40));
        tex.setFill(new Color(0.25, 0.2, 0.45, 0.5));

        turnWait.getChildren().addAll(rec, tex);

        root.getChildren().add(turnWait);

    }

    public void turnWaiter(boolean up) {

        if (up) {

            turnWait.setDisable(false);
            turnWait.setVisible(true);

        } else {

            turnWait.setDisable(true);
            turnWait.setVisible(false);

        }

        System.out.println("turnWaiter Ran");

    }

    public static Piece getPiece() {
        return piece;
    }


    public static void changeTurn(Boolean b) {
        turn = b;
    }

    public static boolean getTurn() {
        return turn;
    }


//    public static void updater(int[] i, Piece p) {
//
////        if (getTurn()) {
//
//            int[] arr = new int[3];
//
//            arr[0] = i[0];
//            arr[1] = i[1];
//            arr[2] = p.getPlayerNum();
//
//            System.out.println("is work?");
//
//            newData = arr;
//
//            //Platform.runLater(() -> Play.getBoard().checkPlayable(Play.getPiece().getPlayerNum() == 1 ? 2 : 1));
//
////        }
//    }




//    public static synchronized void updater(int[] i, Piece p) {
//        if (getTurn()) {
//            newData = new int[]{i[0], i[1], p.getPlayerNum()};
//            synchronized (Play.class) {
//                Play.class.notify(); // wake up waiting thread
//            }
//        }
//    }


    public static void updater(int[] i, Piece p) {
        synchronized (lock) {
            newData = new int[]{ i[0], i[1], p.getPlayerNum() };
            System.out.println(i[0] + "  " + i[1]);
            lock.notify();
        }
    }

    public void showWhatPlayer() {

        Text youAre = new Text("Player " + piece.getPlayerNum());
        youAre.setFont(Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, 32));
        youAre.setFill(piece.getPlayerColor());
        youAre.setTranslateY(375);
        youAre.setTranslateX(0);
        root.getChildren().add(youAre);


        Circle c = piece.placeChip(root);
        c.radiusProperty().unbind();
        c.setRadius(15);
        c.setTranslateY(750);
        c.setTranslateX(450);

    }

    public static Board getBoard() {
        return board;
    }

    public void endConditions(DataOutputStream toServer, DataInputStream fromServer) throws IOException {

        //toServer.writeInt(Score.score);

        toServer.writeBoolean(Score.stuck);


        win1 = fromServer.readBoolean();
        win2 = fromServer.readBoolean();
        draw = fromServer.readBoolean();


    }

    public static void main(String[] args) {
        launch(args);
    }


    public void rules() {

        Rectangle rRec = new Rectangle(800, 900);
        rRec.setFill(new Color(0.73, 0.67, 0.93, 0.93));
        Text rtex = new Text("Rules");
        rtex.setTranslateY(-350);
        rtex.setFont(Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, 40));
        rtex.setFill(new Color(0.25, 0.2, 0.45, 0.8));
        Text tRule = new Text("The goal of the game is to have more of your \ncolored chips on the board than your opponent.\n\n" +
                "Players take turns placing their chips \non empty spaces on the board.\n" +
                "The placed chip must have the opponents \nchips between their newly placed chip and one \nalready on the board (in a straight line).\n " +
                "All of the opponents chips between yours \nwill be flipped to your color chip.\n\n" +
                "The game ends when one of the players \ncannot make another move.\n" +
                "The total amount of each players chips \nare then counted up, and the one \nwith the most is the winner.\n\n" +
                "Click anywhere to begin."
        );
        tRule.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 25));
        tRule.setFill(new Color(0.25, 0.2, 0.45, 0.8));
        tRule.setTextAlignment(TextAlignment.CENTER);

        ruleScreen.getChildren().addAll(rRec, rtex, tRule);

        root.getChildren().add(ruleScreen);

        ruleScreen.setOnMouseClicked(e -> root.getChildren().remove(ruleScreen));
    }

    public void rules(boolean up) {



    }

}
