package it.polimi.ingsw.psp40.view.gui;

import javafx.scene.image.Image;

/**
 * @author sup3rgiu
 */

public class Ground extends Block {

    Ground(int row, int col) {
        this(row, col, GUIProperties.CameraType.RIGHT);
    }

    Ground(int row, int col, GUIProperties.CameraType cameraType) {
        super(row, col, 0);
        this.setOpacity(0);
        this.setFitWidth(GUIProperties.tileWidth);
        this.setFitHeight(GUIProperties.tileHeight);
        setCamera(cameraType);
        this.setOnMouseClicked(event -> handleClick());
    }

    @Override
    void handleClick() {
        GameScreenController.blockClicked(row, col, z);
    }

    @Override
    void loadImage(GUIProperties.CameraType cameraType) {
        switch (cameraType) {
            case RIGHT:
                this.setImage(new Image(getClass().getResource("/images/tile_dx.png").toString()));
                break;

            case LEFT:
                this.setImage(new Image(getClass().getResource("/images/tile_sx.png").toString()));
                break;

            case TOP:
                break;
        }
    }

    @Override
    void display(int row, int col) {
        this.setXPosition((col - row) * (GUIProperties.tileWidthHalf + GUIProperties.tileXSpacing));
        this.setYPosition((col + row) * (GUIProperties.tileHeightHalf + GUIProperties.tileYSpacing));
    }

    @Override
    Ground copy() {
        return new Ground(this.row, this.col);
    }

    @Override
    Ground copyAndSetCamera(GUIProperties.CameraType cameraType) {
        return new Ground(this.row, this.col, cameraType);
    }
}