package it.polimi.ingsw.psp40.view.gui;

import it.polimi.ingsw.psp40.commons.messages.Message;
import it.polimi.ingsw.psp40.commons.messages.TypeOfMessage;
import it.polimi.ingsw.psp40.model.Card;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CardScreenController2 extends ScreenController {
    private boolean waiting = false;

    @FXML
    private GridPane grid;
    @FXML
    private Button endButton;
    @FXML
    private TextArea textDescr;
    @FXML
    private TextArea textTitle;
    @FXML
    private TextArea selected;

    private int toSelect;
    List<Integer> selectedList = new ArrayList<Integer>();

    List<Card> cards;


    @FXML
    public void initialize(HashMap<Integer, Card> cardsMap, int toSelect) {
        this.toSelect = toSelect;
       cards  = new ArrayList<>(cardsMap.values());

        List<ImageView> cardimage = new ArrayList<>();

        textDescr.setWrapText(true);
        //UtilsGUI.addClassToElement(textTitle, "card-title");


        cards.forEach( card -> {
          ImageView cardView =  new ImageView( new Image(GUIProperties.class.getResource("/cardsFrame/" + card.getId() +".png").toString()) ) ;
          cardView.setFitHeight(250);
          cardView.setPreserveRatio(true);
          cardView.setSmooth(true);
          cardView.setCache(true);
          cardView.getStyleClass().add("card-image");
          cardView.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
                if (newValue) {
                    textDescr.setText(card.getDescription());
                    textTitle.setText(card.getName());
                } else {
                    textDescr.setText("");
                    textTitle.setText("");
                }
            });

            cardView.addEventHandler(MouseEvent.MOUSE_PRESSED,
                    mouseEvent -> {
                        if (selectedList.contains(card.getId())){
                            for (int i = 0; i < selectedList.size(); i++) {
                                if (card.getId() == selectedList.get(i)) {
                                    selectedList.remove(i);
                                    ColorAdjust colorAdjust = new ColorAdjust();
                                    colorAdjust.setBrightness(0);

                                    cardView.setEffect(colorAdjust);
                                }
                            }
                        }
                        else if (selectedList.size() < toSelect){
                            System.out.println("settable: " + card.getId());
                            selectedList.add(card.getId());
                            ColorAdjust colorAdjust = new ColorAdjust();
                            colorAdjust.setBrightness(0.5);
                            cardView.setEffect(colorAdjust);
                        }
            });


          cardimage.add(cardView);
           System.out.println(card.getId());

        });

        int index = 0;
        for (int row = 0; row < 3; row ++){
            for (int col = 0; col < 3; col ++){
                grid.add(cardimage.get(index), row, col);
                index ++;
            }
        }

    }


    @FXML
    void end(){

        int[] ret = new int[toSelect];
        if (selectedList.size() == toSelect){
            for (int i=0; i < selectedList.size(); i++)
            {
                ret[i] = selectedList.get(i);
                System.out.println(selectedList.size() + " " + toSelect);
        }
            getClient().sendToServer(new Message( TypeOfMessage.SET_CARDS_TO_GAME, ret));
        }

    }


}
