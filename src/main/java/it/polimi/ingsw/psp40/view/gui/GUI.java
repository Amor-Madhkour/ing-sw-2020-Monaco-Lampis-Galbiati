package it.polimi.ingsw.psp40.view.gui;

import it.polimi.ingsw.psp40.commons.messages.Message;
import it.polimi.ingsw.psp40.commons.messages.TypeOfMessage;
import it.polimi.ingsw.psp40.controller.Phase;
import it.polimi.ingsw.psp40.model.Card;
import it.polimi.ingsw.psp40.model.CardManager;
import it.polimi.ingsw.psp40.model.Player;
import it.polimi.ingsw.psp40.network.client.Client;
import it.polimi.ingsw.psp40.view.ViewInterface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GUI extends Application implements ViewInterface {

    /* Attributes */

    private Stage primaryStage;

    private Client client;

    private final String errorString = "ERROR";

    private static final Logger LOGGER = Logger.getLogger("GUI");

    private SetupScreenController setupScreenController;

    private GameScreenController gameScreenController;

    private CardScreenController cardScreenController;

    private FXMLLoader fxmlLoader;

    /* Methods */

    @Override
    public void start(Stage primaryStage) throws Exception {
        fxmlLoader = new FXMLLoader();
        this.primaryStage = primaryStage;

        primaryStage.setOnCloseRequest((WindowEvent t) -> {
            Platform.exit();
            System.exit(0);
        });

        // List<String> args = getParameters().getRaw();

        client = new Client();
        client.setView(this);

        //displaySetup();
        //testDisplayGame();
        testCardManager();
    }

    @Override
    public void displaySetup() {

        fxmlLoader.setLocation(getClass().getResource("/FXML/SetupScreen.fxml"));

        Parent root;
        Scene scene;

        try {
            root = fxmlLoader.load();
            scene = new Scene(root);

        }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE, "SetupScreen.fxml not found");
            scene = new Scene(new Label(errorString));
        }

        primaryStage.setScene(scene);

        primaryStage.setTitle("Santorini");
        primaryStage.setResizable(false);

        primaryStage.show();

        setupScreenController = fxmlLoader.getController();
        setupScreenController.setClient(client);
    }

    private void testDisplayGame() {
        fxmlLoader.setLocation(getClass().getResource("/FXML/GameScreen.fxml"));

        Parent root;
        Scene scene;

        try {
            root = fxmlLoader.load();
            scene = new Scene(root);
        }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE, "GameScreen.fxml not found");
            scene = new Scene(new Label(errorString));
        }

        primaryStage.setScene(scene);

        primaryStage.setTitle("Santorini");
        primaryStage.setResizable(true);

        primaryStage.show();

        gameScreenController = fxmlLoader.getController();
        gameScreenController.setClient(client);
    }

    @Override
    public void displaySetupFailure() {

    }

    @Override
    public void displayLogin() {
        setupScreenController.displayUserForm();
    }

    @Override
    public void displayLoginSuccessful() {
        System.out.println("You have been logged in successfully");
    }

    @Override
    public void displayLoginFailure(String details) {

    }

    @Override
    public void displayUserJoined(String details) {

    }

    @Override
    public void displayAddedToQueue(String details) {

    }

    @Override
    public void displayStartingMatch() {

    }

    @Override
    public void displayDisconnected(String details) {

    }

    @Override
    public void displayGenericMessage(String message) {

    }

    @Override
    public void displayCardSelection(HashMap<Integer, Card> cards, int numPlayers) {

        fxmlLoader.setLocation(getClass().getResource("/FXML/CardScreen.fxml"));

        Parent root;
        Scene scene;

        try {
            root = fxmlLoader.load();
            scene = new Scene(root);

        }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE, "SetupScreen.fxml not found");
            scene = new Scene(new Label(errorString));
        }

        primaryStage.setScene(scene);

        primaryStage.setTitle("Santorini");
        primaryStage.setResizable(false);

        primaryStage.show();

        cardScreenController = fxmlLoader.getController();
        cardScreenController.initialize(cards, numPlayers);


        int[] selection = cardScreenController.selection();

        /* sending to server */
        client.sendToServer(new Message( TypeOfMessage.SET_CARDS_TO_GAME, selection));

    }

    @Override
    public void displayChoicePersonalCard(List<Card> availableCards) {

    }

    @Override
    public void displayCardInGame(List<Card> cardInGame) {

    }

    @Override
    public void displayForcedCard(Card card) {

    }

    @Override
    public void displaySetInitialPosition(List<Player> playerList) {

    }

    @Override
    public void displayAskFirstPlayer(List<Player> allPlayers) {

    }

    @Override
    public void displayChoiceOfAvailablePhases() {

    }

    @Override
    public void displayChoiceOfAvailableCellForMove() {

    }

    @Override
    public void displayChoiceSelectionOfWorker() {

    }

    @Override
    public void displayChoiceOfAvailableCellForBuild() {

    }

    @Override
    public void displayMoveWorker() {

    }

    @Override
    public void displayBuildBlock() {

    }

    @Override
    public void displayLobbyCreated(String playersWaiting) {

    }

    @Override
    public void displayWinnerMessage() {

    }
    @Override
    public void displayLoserMessage() {

    }


    @Override
    public void displayLoserPlayer(Player player) {

    }




    public void testCardManager(){

        fxmlLoader.setLocation(getClass().getResource("/FXML/CardScreen.fxml"));

        Parent root;
        Scene scene;

        try {
            root = fxmlLoader.load();
            scene = new Scene(root);

        }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE, "SetupScreen.fxml not found");
            scene = new Scene(new Label(errorString));
        }

        primaryStage.setScene(scene);

        primaryStage.setTitle("Santorini");
        primaryStage.setResizable(false);

        primaryStage.show();

        cardScreenController = fxmlLoader.getController();
        cardScreenController.initialize(CardManager.initCardManager().getCardMap(), 2);


    }
}
