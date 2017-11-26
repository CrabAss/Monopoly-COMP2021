package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

/**
 * Controller for the JavaFX Form: FormNewGame.
 */
public class ControllerNewGame {

    private final int WIDTH = 854;
    private final int HEIGHT = 480;

    @FXML
    private Slider NumberOfPlayer;
    @FXML
    private Slider NumberOfAI;
    @FXML
    private Button StartGame;

    @FXML
    private void HandleNumberOfPlayer(){
        NumberOfAI.setMax(NumberOfPlayer.getValue());
        // System.out.println(NumberOfPlayer.getValue());
    }

    @FXML
    private void HandleStartGame(ActionEvent event){

        Main.newGame();
        Main.getGame().initGame((int)(NumberOfPlayer.getValue()), (int)(NumberOfAI.getValue()));

        try {
            Parent root = FXMLLoader.load(getClass().getResource("FormGame.fxml"));
            Main.setStage(root, WIDTH, HEIGHT);
            Main.getGame().nextTurn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void HandleBackBtn(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FormWelcome.fxml"));
            Main.setStage(root, WIDTH, HEIGHT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
