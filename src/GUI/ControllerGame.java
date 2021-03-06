package GUI;

import Cmd.Land.LandProperty;
import Cmd.Others.BankruptException;
import Cmd.Others.Dice;
import Cmd.Others.Output;
import Cmd.Others.Property;
import Cmd.Player.Player;
import Cmd.Player.PlayerAI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ListIterator;
import java.util.Objects;


/**
 * Controller for the JavaFX Form: FormGame.
 */
public class ControllerGame {

    private final static int MAXPLAYERNUMBER = 6, MAXLANDNUMBER = 20;

    @FXML
    private Label CurrentLandName, CurrentLandPrice, CurrentLandRent, WinnerLbl;

    @FXML
    private Button ButtonAction, ButtonEndTurn;

    @FXML
    private GridPane GridPanePlayer1, GridPanePlayer2, GridPanePlayer3,
            GridPanePlayer4, GridPanePlayer5, GridPanePlayer6;
    private GridPane GridPanePlayer[];

    @FXML
    private GridPane Land1, Land2, Land3, Land4, Land5, Land6, Land7, Land8, Land9, Land10,
            Land11, Land12, Land13, Land14, Land15, Land16, Land17, Land18, Land19, Land20;
    private GridPane[] Land;

    @FXML
    private Label TypePlayer1, TypePlayer2, TypePlayer3,
            TypePlayer4, TypePlayer5, TypePlayer6;
    private Label TypePlayer[];

    @FXML
    private Label MoneyPlayer1, MoneyPlayer2, MoneyPlayer3,
            MoneyPlayer4, MoneyPlayer5, MoneyPlayer6;
    private Label MoneyPlayer[];

    @FXML
    private Label StatuPlayer1, StatuPlayer2, StatuPlayer3,
            StatuPlayer4, StatuPlayer5, StatuPlayer6;
    private Label StatuPlayer[];

    @FXML
    private Rectangle RectanglePlayer1, RectanglePlayer2, RectanglePlayer3,
            RectanglePlayer4, RectanglePlayer5, RectanglePlayer6;
    private Rectangle RectanglePlayer[];

    @FXML
    private TextArea ActionLog;

    @FXML
    private GridPane ActionMenu, FinishPane;

    @FXML
    private ImageView Dice1, Dice2;
    private final String Color[] = {"#F8BBD0" , "#FFE0B2", "#C8E6C9" , "#B2EBF2" , "#C5CAE9" , "#E1BEE7"};
    private final Image DiceImg[] = {
            null,
            new Image("GUI/resources/d1.jpg"),
            new Image("GUI/resources/d2.jpg"),
            new Image("GUI/resources/d3.jpg"),
            new Image("GUI/resources/d4.jpg"),
            new Image("GUI/resources/d5.jpg"),
            new Image("GUI/resources/d6.jpg"),
    };


    /**
     * @return the label landname
     */
    private Label getCurrentLandName() {
        return CurrentLandName;
    }

    /**
     * @return the pane action menu
     */
    GridPane getActionMenu() {
        return ActionMenu;
    }

    /**
     * Change the current menu interface
     */
    void changeMenu(){
        if (!getActionMenu().isVisible()) {
            getActionMenu().setVisible(true);
        } else {
            getActionMenu().setVisible(false);
        }
    }

    /**
     * Initialize this pane
     */
    @FXML
    public void initialize() {
        GridPanePlayer = new GridPane[] {GridPanePlayer1, GridPanePlayer2, GridPanePlayer3,
                GridPanePlayer4, GridPanePlayer5, GridPanePlayer6};
        TypePlayer = new Label[] {TypePlayer1, TypePlayer2, TypePlayer3,
                TypePlayer4, TypePlayer5, TypePlayer6};
        MoneyPlayer = new Label[] {MoneyPlayer1, MoneyPlayer2, MoneyPlayer3,
                MoneyPlayer4, MoneyPlayer5, MoneyPlayer6};
        StatuPlayer = new Label[] {StatuPlayer1, StatuPlayer2, StatuPlayer3,
                StatuPlayer4, StatuPlayer5, StatuPlayer6};
        RectanglePlayer = new Rectangle[] {RectanglePlayer1, RectanglePlayer2, RectanglePlayer3,
                RectanglePlayer4, RectanglePlayer5, RectanglePlayer6};
        Land = new GridPane[] {null, Land1, Land2, Land3, Land4, Land5, Land6, Land7, Land8, Land9, Land10,
                Land11, Land12, Land13, Land14, Land15, Land16, Land17, Land18, Land19, Land20};

        Main.getGame().setGuiOutput(new GUIOutput(ActionLog));
        Main.getGame().setEndTurn(ButtonEndTurn);
        Main.getGame().setAction(ButtonAction);
        Main.getGame().setDice1(Dice1);
        Main.getGame().setDice2(Dice2);
        Main.getGame().setControllerGame(this);
        changeMenu();
        updateGraph();
    }

    /**
     * handle continue event
     */
    @FXML
    public void HandleContinue(){
        changeMenu();
        Main.getGame().getGUIhelper()[Main.getGame().getCurPlayer()].run();
        updateGraph();
    }

    /**
     * handle retire event
     */
    @FXML
    public void HandleRetire(){
        Player player = Main.getGame().getPlayerList()[Main.getGame().getCurPlayer()];
        player.retired();
        updateGraph();
        Main.getGame().nextTurn();
    }

    /**
     * handle auto event, change to robot
     */
    @FXML
    public void HandleAuto(){
        Player player = Main.getGame().getPlayerList()[Main.getGame().getCurPlayer()];
        Main.getGame().getPlayerList()[Main.getGame().getCurPlayer()] = player.toRobot();
        Main.getGame().getGUIhelper()[Main.getGame().getCurPlayer()] = new GUIPlayer(Main.getGame().getPlayerList()[Main.getGame().getCurPlayer()]);
        for (Property x: Main.getGame().getPlayerList()[Main.getGame().getCurPlayer()].getPropertyList()){
            x.setBelongs(Main.getGame().getPlayerList()[Main.getGame().getCurPlayer()]);
        }
        updateGraph();
        HandleContinue();
    }

    /**
     * handle save event
     */
    @FXML
    public void HandleSave(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        //System.out.println(pic.getId());

        File file = fileChooser.showSaveDialog(Main.getMainStage());
        if (file != null) {
            try {
                Main.getGame().setCurrentPlayer(Main.getGame().getCurPlayer());
                Main.getGame().setSavePath(file.getAbsolutePath());
                Main.getGame().saveGame();

            } catch (Exception e) {
                //Logger.getLogger(JavaFX_Text.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    /**
     * handle load event, choose file and load
     */
    @FXML
    public void HandleLoad() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        //System.out.println(pic.getId());

        File file = fileChooser.showOpenDialog(Main.getMainStage());
        if (file != null) {
            Main.getGame().initGame(6, 0);

            try {
                Main.getGame().setLoadPath(file.getAbsolutePath());
                Main.getGame().loadGame();
            } catch (Exception e) {
                //Logger.getLogger(JavaFX_Text.class.getName()).log(Level.SEVERE, null, e);
            }
            for (int i = 0; i < Main.getGame().getPlayerNumber(); i++)
                Main.getGame().getGUIhelper()[i].setPlayer(Main.getGame().getPlayerList()[i]);

            for (int i = 1; i <= Main.getGame().getMAXLANDNUMBER(); i++){
                if (Main.getGame().getLandList()[i] instanceof LandProperty){
                    ((LandProperty)Main.getGame().getLandList()[i]).getProperty().setBelongs(null);
                }
            }
            for (int i = 0; i < Main.getGame().getPlayerNumber(); i++) {
                for (Property x : Main.getGame().getPlayerList()[i].getPropertyList()) {
                    x.setBelongs(Main.getGame().getPlayerList()[i]);
                }
            }

            //System.out.println(Main.getGame().getPlayerList()[0].getPosition());
            Main.getGame().clearWinner();
            updateGraph();
            Main.getGame().setCurPlayer(Main.getGame().getCurrentPlayer());
            Main.getGame().setCurPlayer(Main.getGame().getCurPlayer() - 1);
            Main.getGame().nextTurn();
        }
    }

    /**
     * Handle end Turn event
     * if end turn is end turn than end current turn
     * else handle dice event
     */
    @FXML
    public void HandleEndTurn(){
        if (Objects.equals(ButtonEndTurn.getText(), "End turn")) {
            Player player = Main.getGame().getPlayerList()[Main.getGame().getCurPlayer()];
            Main.getGame().getGuiOutput().Print(player + " decides to end turn.");
            updateGraph();
            Main.getGame().nextTurn();
        } else { // Dice
            Player player = Main.getGame().getPlayerList()[Main.getGame().getCurPlayer()];
            GUIPlayer guiPlayer = Main.getGame().getGUIhelper()[Main.getGame().getCurPlayer()];

            Dice dice = new Dice();
            dice.dice();
            Main.getGame().getDice1().setImage(DiceImg[dice.getX()]);
            Main.getGame().getDice2().setImage(DiceImg[dice.getY()]);
            Main.getGame().getEndTurn().setText("End turn");
            Main.getGame().getAction().setDisable(true);

            if (dice.isEqual()) {
                player.release();
                int step = dice.getStep();

                player.move(step);

                Main.getGame().getControllerGame().updateGraph();
                if (player.getPosition() instanceof Cmd.Land.LandProperty){
                    GUILandProperty Guimodule = new GUILandProperty();
                    Guimodule.run(player.getPosition(), player);
                } else {
                    try {
                        player.getPosition().run(player);
                        if (player instanceof PlayerAI) {
                            Main.getGame().getControllerGame().HandleEndTurn();
                        }
                    } catch (Exception e) {
                        updateGraph();
                        Main.getGame().nextTurn();
                    }
                }
            } else if (player.getJailDay() == 3) { // The third day
                try {
                    Output.println(player + " must pay.");
                    final int JAILMONEY = 90;
                    player.decMoney(JAILMONEY);
                    player.release();
                    guiPlayer.run();
                } catch (BankruptException e) {
                    updateGraph();
                    Main.getGame().nextTurn();
                }
            } else {
                if (player instanceof PlayerAI) {
                    Main.getGame().getControllerGame().HandleEndTurn();
                }
            }
        }
        updateGraph();
    }

    /**
     * handle action event
     */
    @FXML
    public void HandleAction(){
        Player player = Main.getGame().getPlayerList()[Main.getGame().getCurPlayer()];
        GUIPlayer guiPlayer = Main.getGame().getGUIhelper()[Main.getGame().getCurPlayer()];
        Property property = GUILandProperty.getCurProperty();
        // Buy
        try {
            if (player.getPosition() instanceof LandProperty) {
                Output.println(player + " decides to rent " + property.toString() + ".");
                player.addProperty(property);
                player.decMoney(property.getPrice());
                property.setBelongs(player);
                Main.getGame().getAction().setDisable(true);

            } else { // In Jail
                Output.println(player + " decides to pay.");
                Main.getGame().getEndTurn().setText("End turn");
                final int JAILMONEY = 90;
                player.decMoney(JAILMONEY);
                player.release();
                guiPlayer.run();
            }
        } catch (BankruptException e) {
          updateGraph();
          Main.getGame().nextTurn();
        }
        updateGraph();
    }

    /**
     * handle restart event
     */
    public void HandleRestart() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FormNewGame.fxml"));
            final int NEWGAMEHEIGHT = 204, NEWGAMEWIDTH = 600;
            Main.setStage(root, NEWGAMEWIDTH, NEWGAMEHEIGHT);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * update the graph
     */
    void updateGraph(){

        // reset
        for (int i = 1; i <= Main.getGame().getMAXLANDNUMBER(); i++){
            Land[i].setStyle("-fx-border-color: #000; -fx-background-color: #ffffff");
            for (int j = 0; j < Main.getGame().getPlayerNumber(); j++)
                Land[i].getChildren().remove(RectanglePlayer[j]);
        }

        for (int i = 0; i < Main.getGame().getPlayerNumber(); i++){
            GridPanePlayer[i].setVisible(true);
            GridPanePlayer[i].setStyle("");

        // set
            if (Main.getGame().getPlayerList()[i] instanceof PlayerAI)
                TypePlayer[i].setText("AI");
            else
                TypePlayer[i].setText("Human");

            MoneyPlayer[i].setText(Main.getGame().getGUIhelper()[i].getMoney() + "");
            StatuPlayer[i].setText(Main.getGame().getGUIhelper()[i].getStatus());

            if (!Main.getGame().getPlayerList()[i].isDead()) {
                RectanglePlayer[i].setVisible(true);
                Land[Main.getGame().getGUIhelper()[i].getPosition()].add(RectanglePlayer[i], i % 3, i / 3);
            } else
                RectanglePlayer[i].setVisible(false);

        }

        for (int i = Main.getGame().getPlayerNumber(); i < MAXPLAYERNUMBER; i++) {
            TypePlayer[i].setText("");
            MoneyPlayer[i].setText("");
            StatuPlayer[i].setText("");
            RectanglePlayer[i].setVisible(false);
            GridPanePlayer[i].setVisible(false);
        }

        for (int i = 1; i <= MAXLANDNUMBER; i++) {
            for (int j = 0; j < Main.getGame().getPlayerNumber(); j++) {
                if (Main.getGame().getLandList()[i] instanceof LandProperty) {
                    if (((LandProperty) Main.getGame().getLandList()[i]).getProperty().getBelongs() == Main.getGame().getPlayerList()[j]) {
                        String color = Color[j];
                        Land[i].setStyle("-fx-border-color: #000; -fx-background-color: " + color);
                    }
                }
            }
        }
        if (Main.getGame().getPlayerAlive() > 1 && Main.getGame().getRounds() <= 100) {
            if (Main.getGame().getCurPlayer() < Main.getGame().getPlayerNumber()) {
                String color = Color[Main.getGame().getCurPlayer()];
                GridPanePlayer[Main.getGame().getCurPlayer()].setStyle("-fx-background-color:" + color);

                FinishPane.setVisible(false);
                Player player = Main.getGame().getPlayerList()[Main.getGame().getCurPlayer()];
                getCurrentLandName().setText(player.getPosition().getName());
                if (player.getPosition() instanceof LandProperty) {
                    if (((LandProperty) player.getPosition()).getProperty().getBelongs() != null)
                        CurrentLandPrice.setText("--");
                    else
                        CurrentLandPrice.setText("" + ((LandProperty) player.getPosition()).getProperty().getPrice());
                    CurrentLandRent.setText("" + ((LandProperty) player.getPosition()).getProperty().getRent());
                } else {
                    CurrentLandPrice.setText("--");
                    CurrentLandRent.setText("--");
                }
            }
        } else {
            if (Main.getGame().getWinner().isEmpty())
                Main.getGame().EndGame();
        }
        if (!Main.getGame().getWinner().isEmpty()) {
            StringBuilder str = new StringBuilder();
            ListIterator it = Main.getGame().getWinner().listIterator();
            str.append(((Player) it.next()).getName());
            String nextString;
            while (it.hasNext()) {
                str.append(" & ");
                nextString = ((Player) it.next()).getName();
                str.append(nextString.charAt(nextString.length() - 1));
            }

            WinnerLbl.setText(str.toString() + "\nis the winner!");
            String color;
            if (Main.getGame().getWinner().size() == 1) {
                String winnerString = Main.getGame().getWinner().get(0).toString();
                color = Color[Character.getNumericValue(winnerString.charAt(winnerString.length() - 1)) - 1];
            } else
                color = "#fff";
            FinishPane.setStyle("-fx-border-color: #000; -fx-background-color: " + color);

            FinishPane.setVisible(true);
        }
    }
}
