package it.polimi.ingsw.psp40.view.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class DisconnectedPopup extends PopupStage {

    private String details;

    DisconnectedPopup(Stage ownerStage, String details) {
        super(ownerStage);
        this.details = details;
        build();
    }

    private void build() {
        // Create scene
        Text text = createText(details);
        this.vBox.getChildren().add(text);
        UtilsGUI.addClassToElement(this.vBox, "disconnected-popup");

        Button resume = new Button("Something");
        resume.setOnAction(event -> {
            this.ownerStage.getScene().getRoot().setEffect(null);
            this.ownerStage.getScene().getRoot().setDisable(false);
            this.hide();
            // todo back to home or close?
        });

        this.vBox.getChildren().add(resume);
    }
}
