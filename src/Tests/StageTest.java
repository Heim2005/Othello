package Tests;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javafx.geometry.Insets;



public class StageTest extends Application {

    @Override
    public void start(Stage stage) {

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        //grid.setPadding(new Insets(20, 20, 20, 20));
        grid.add(new Rectangle(100,100),1,1);
        grid.setGridLinesVisible(true);



        Scene scene = new Scene(grid, 500, 500);
        stage.setTitle("Othello");
        stage.setScene(scene);
        stage.show();

    }
}
