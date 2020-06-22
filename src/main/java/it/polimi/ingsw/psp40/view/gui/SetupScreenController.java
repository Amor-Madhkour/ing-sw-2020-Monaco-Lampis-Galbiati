package it.polimi.ingsw.psp40.view.gui;

import animatefx.animation.ZoomIn;
import it.polimi.ingsw.psp40.commons.Configuration;
import it.polimi.ingsw.psp40.commons.messages.LoginMessage;
import it.polimi.ingsw.psp40.commons.messages.TypeOfMessage;
import it.polimi.ingsw.psp40.view.cli.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.function.UnaryOperator;

public class SetupScreenController extends ScreenController {

    /* Attributes */

    private HashMap<Control, Boolean> validationMap = new HashMap<>();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button connectButton;

    @FXML
    private Button sendInfoButton;

    @FXML
    private VBox vBoxForServerProps;

    @FXML
    private TextField ipAddressTextField;

    @FXML
    private TextField portTextField;

    @FXML
    private VBox vBoxForUserProps;

    @FXML
    private TextField usernameTextField;

    @FXML
    private DatePicker birthdayDatePicker;

    @FXML
    private ComboBox<Integer> numOfPlayerComboBox;

    @FXML
    private ImageView santoriniLogo;


    /* Methods */

    @FXML
    public void initialize() {
        TextFormatter<Integer> textFormatter = new TextFormatter<>(integerFilter);
        portTextField.setTextFormatter(textFormatter); // portTextField will now accept only integers (or blank string)
        validationMap.put(ipAddressTextField, false);
        validationMap.put(portTextField, false);

        LocalDate minDate = LocalDate.parse(Configuration.minDate, DateTimeFormatter.ofPattern(Configuration.formatDate));
        LocalDate maxDate = LocalDate.now();
        birthdayDatePicker.setDayCellFactory(d ->
            new DateCell() {
            @Override public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
        }});
        birthdayDatePicker.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isValidDate = Utils.isValidDateBool(newValue);
            validationMap.put(birthdayDatePicker, isValidDate);
            validateSendFields();
        });

        numOfPlayerComboBox.getItems().addAll(2,3);

        UtilsGUI.buttonHoverEffect(connectButton);
        UtilsGUI.buttonHoverEffect(sendInfoButton);

        new ZoomIn(santoriniLogo).play();
        new ZoomIn(vBoxForServerProps).play();

        vBoxForServerProps.requestFocus(); // remove initial focus from first TextField
    }

    // just for testing
    protected void mockSendConnect() {
        getClient().setServerIP("localhost");
        getClient().setServerPort(Integer.parseInt("1234"));
        getClient().connectToServer();
    }

    // just for testing
    protected void mockSendLogin(int numOfPlayers) {
        String username = (new Date()).toString();
        Date birthday = new Date();
        getClient().setUsername(username);
        LoginMessage loginMessage = new LoginMessage(username, birthday, numOfPlayers, TypeOfMessage.LOGIN);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getClient().sendToServer(loginMessage);
    }


    @FXML
    public void handleConnectButton(ActionEvent actionEvent) {
        validationMap.clear();
        getClient().setServerIP(ipAddressTextField.getText());
        getClient().setServerPort(Integer.parseInt("0" + portTextField.getText().trim()));
        getClient().connectToServer();
    }

    @FXML
    public void onEnterIpAddress(ActionEvent actionEvent) {
        portTextField.requestFocus();
    }

    @FXML
    public void onEnterPortText(ActionEvent actionEvent) {
        connectButton.fire();
    }

    @FXML
    public void ipAddressChanged(KeyEvent keyEvent) {
        boolean hasInsertedValidIp = Utils.isValidIp(ipAddressTextField.getText());
        validationMap.put(ipAddressTextField, hasInsertedValidIp);
        if (hasInsertedValidIp) {
            UtilsGUI.removeClassToElement(ipAddressTextField, "error-text");
        } else {
            UtilsGUI.addClassToElement(ipAddressTextField, "error-text");
        }
        validateConnectFields();
    }

    @FXML
     public void portChanged(KeyEvent keyEvent) {
        boolean hasInsertedValidPort = Utils.isValidPort(Integer.parseInt("0" + portTextField.getText().trim()));
        if (hasInsertedValidPort) {
            UtilsGUI.removeClassToElement(portTextField, "error-text");
        } else {
            UtilsGUI.addClassToElement(portTextField, "error-text");
        }
        validationMap.put(portTextField, hasInsertedValidPort);
        validateConnectFields();
    }

    public void displayUserForm() {
        validationMap.clear();
        vBoxForServerProps.setVisible(false);
        vBoxForUserProps.setVisible(true);
        vBoxForUserProps.requestFocus(); // remove initial focus from first TextField
        validationMap.put(usernameTextField, false);
        validationMap.put(birthdayDatePicker, false);
        validationMap.put(numOfPlayerComboBox, false);
    }

    @FXML
    public void usernameChanged(KeyEvent keyEvent) {
        boolean hasInsertedValidUsername = Utils.isValidUsername(usernameTextField.getText());
        if(hasInsertedValidUsername) {
            UtilsGUI.removeClassToElement(usernameTextField, "error-text");
        } else {
            UtilsGUI.addClassToElement(usernameTextField, "error-text");
        }
        validationMap.put(usernameTextField, hasInsertedValidUsername);
        validateSendFields();
    }

    @FXML
    public void numOfPlayerChanged(ActionEvent actionEvent) {
        boolean validNumOfPlayer = numOfPlayerComboBox.getItems().contains(numOfPlayerComboBox.getValue());
        validationMap.put(numOfPlayerComboBox, validNumOfPlayer);
        validateSendFields();
    }


    @FXML
    public void handleSendInfoButton(ActionEvent actionEvent) {
        String username = usernameTextField.getText();

        ZoneId defaultZoneId = ZoneId.systemDefault();
        birthdayDatePicker.setValue(birthdayDatePicker.getConverter().fromString(birthdayDatePicker.getEditor().getText()));
        LocalDate birthdayFromDataPicker = birthdayDatePicker.getValue();
        Date birthday = Date.from(birthdayFromDataPicker.atStartOfDay(defaultZoneId).toInstant());

        Integer numOfPlayers = numOfPlayerComboBox.getValue();

        getClient().setUsername(username);
        LoginMessage loginMessage = new LoginMessage(username, birthday, numOfPlayers, TypeOfMessage.LOGIN);
        getClient().sendToServer(loginMessage);
    }

    private void errorAlert(String text) {
        TilePane r = new TilePane();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(text);
        alert.show();
        anchorPane.getChildren().add(r);
    }

    public void errorAlertSetup(String text) {
        connectButton.setDisable(true);
        errorAlert(text);
    }

    public void errorAlertLogin(String text) {
        UtilsGUI.addClassToElement(usernameTextField, "error-text");
        errorAlert(text);
    }

    private void validateConnectFields() {
        if (validationMap.values().stream().filter(valid -> valid.equals(Boolean.FALSE)).findFirst().orElse(true)) {
            connectButton.setDisable(false);
        } else {
            connectButton.setDisable(true);
        }
    }

    private void validateSendFields() {
        if (validationMap.values().stream().filter(valid -> valid.equals(Boolean.FALSE)).findFirst().orElse(true)) {
            sendInfoButton.setDisable(false);
        } else {
            sendInfoButton.setDisable(true);
        }
    }

    UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.equals("") || newText.matches("([1-9][0-9]{0,4})")) {
            return change;
        }
        return null;
    };

}
