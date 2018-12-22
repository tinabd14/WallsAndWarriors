package com.oops.wallsandwarriors.screens.challenges;

import com.oops.wallsandwarriors.util.CopyUtils;
import com.oops.wallsandwarriors.util.DebugUtils;
import com.oops.wallsandwarriors.Game;
import com.oops.wallsandwarriors.model.ChallengeData;
import com.oops.wallsandwarriors.screens.Screen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.oops.wallsandwarriors.util.EncodeUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * This class implements the distinguishable features for Custom Challenges Screen.
 * Extending the BaseChallengesScreen , it implements an additional methods for
 * the process of importing challenges.
 * @author OOPs
 * @version 21.12.19
 */
public class CustomChallengesScreen extends BaseChallengesScreen {

    private static final int LAY_X = 50;
    private static final int LAY_Y = 150;
    private static final int LIST_PREF_HEIGHT = 350;
    private static final int LIST_PREF_WIDTH = 350;
    private static final int FONT_SIZE = 14;
    private static final int ZERO = 0;
    private static final double PREF_WIDTH = 220.00;
    private static final int SPACING = 20;
    private static final int COL_IND = 0;
    private static final int ROW_IND = 1;
    private static final int H_GAP = 10;
    private static final int V_GAP = 10;
    private static final int GRID_LAY_X = 450;
    private static final int GRID_LAY_Y = 150;

    CustomChallengesData customChallengesData;

    ObservableList<String> challengeNames;
    List<ChallengeData> customChallenges;

    GridPane grid = super.getGrid();

    /**
     * An overriden getScene method to return the current Screen.
     * @return the current screen as a Screen object.
     */
    @Override
    public Scene getScene(){
        Group root = new Group();
        Scene scene = new Scene(root);

        challengeNames = FXCollections.observableArrayList ();
        customChallengesData = new CustomChallengesData();
        customChallenges = customChallengesData.getCustomChallenges();

        DebugUtils.initClickDebugger(scene);
        addBackgroundCanvas(root, "/com/oops/wallsandwarriors/resources/images/background2.png",
                "Custom Challenges");
        super.renderButtons(root);

        Text title = new Text(50, 100, "Choose a challenge.");
        Font theFont = Font.font("Arial", FontWeight.BOLD, 20);
        title.setFont(theFont);
        root.getChildren().add(title);

        showChallenges(root);

        Button importButton = new Button("Import");
        importButton.setLayoutX(50);
        importButton.setLayoutY(520);

        importButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                importChallenge();
            }
        });
        root.getChildren().add(importButton);
        constructGrid(root,grid);

        return scene;
    }

    /**
     * A method to display CustomChallenges on the Screen.
     * @param root root as a Group object.
     */
    private void showChallenges(Group root)
    {
        for (int i = 0; i < customChallenges.size(); i++) {
            challengeNames.add(customChallenges.get(i).getName());
        }

        ListView<String> list = new ListView<>();
        list.setLayoutX(LAY_X);
        list.setLayoutY(LAY_Y);
        list.setOrientation(Orientation.VERTICAL);
        list.setPrefWidth(LIST_PREF_WIDTH);
        list.setPrefHeight(LIST_PREF_HEIGHT);
        list.setItems(challengeNames);
        root.getChildren().add(list);

        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                grid.getChildren().clear();
                int challengeIndex = list.getSelectionModel().getSelectedIndex();
                try {
                    if (challengeIndex >= ZERO) {
                        showChallengeInfo(customChallenges.get(challengeIndex), root);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * A method to display a CustomChallenge information on the Screen with the buttons.
     * @param challenge ChallengeData of the challenge to be displayed.
     * @param root root as a Group object.
     * @throws FileNotFoundException in case of a file error.
     */
    public void showChallengeInfo(ChallengeData challenge, Group root) throws FileNotFoundException
    {
        Game.getInstance().challengeManager.setChallengeData(challenge);
        
        super.displayChallengePreview(challenge);
        Label nameLabel = new Label("Name: " + challenge.getName());
        Label descLabel = new Label("Description: " + challenge.getDescription());

        Label creatorLabel = new Label("Creator: " + challenge.getCreator());
        Label typeLabel = new Label("Type: " + challenge.getType());
        Label warriorLabel = new Label("Info: " + challenge.knights.size() + " Knights");

        Button playButton = new Button("Play");
        playButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startChallenge(challenge.createCopy(true),challenge.createCopy(false));
            }
        });

        Button shareButton = new Button("Share");
        shareButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){

                try {
                    shareChallenge(challenge);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        
        Button removeButton = new Button( "Remove");
        removeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                removeChallenge(challenge);
                playButton.setDisable(true);
                shareButton.setDisable(true);
                removeButton.setDisable(true);
            }
        });
        
        Font theFont = Font.font("Arial", FontWeight.MEDIUM, FONT_SIZE);
        nameLabel.setTextFill(Color.BEIGE);
        descLabel.setTextFill(Color.BEIGE);
        creatorLabel.setTextFill(Color.BEIGE);
        typeLabel.setTextFill(Color.BEIGE);
        warriorLabel.setTextFill(Color.BEIGE);
        nameLabel.setFont(theFont);
        descLabel.setFont(theFont);
        creatorLabel.setFont(theFont);
        typeLabel.setFont(theFont);
        warriorLabel.setFont(theFont);
        double prefferedWidth = PREF_WIDTH;
        nameLabel.setPrefWidth(prefferedWidth);
        nameLabel.setWrapText(true);
        descLabel.setPrefWidth(prefferedWidth);
        descLabel.setWrapText(true);
        creatorLabel.setPrefWidth(prefferedWidth);
        creatorLabel.setWrapText(true);
        typeLabel.setPrefWidth(prefferedWidth);
        typeLabel.setWrapText(true);
        warriorLabel.setPrefWidth(prefferedWidth);
        warriorLabel.setWrapText(true);
        
        HBox buttons = new HBox();
        buttons.setSpacing(SPACING);
        buttons.getChildren().addAll(playButton, shareButton, removeButton);

        grid.add(nameLabel,COL_IND,ROW_IND);
        grid.add(descLabel,COL_IND,ROW_IND+1);
        grid.add(creatorLabel,COL_IND,ROW_IND+2);
        grid.add(typeLabel,COL_IND,ROW_IND+3);
        grid.add(warriorLabel,COL_IND,ROW_IND+4);
        grid.add(buttons, COL_IND, ROW_IND+5);
    }

    /**
     * A method to share a CustomChallenge from the list.
     * @param challenge ChallengeData of the challenge to be shared.
     * @throws FileNotFoundException in case of a file error.
     * @throws IOException in case of an input/output error.
     */
    private void shareChallenge(ChallengeData challenge ) throws FileNotFoundException,IOException{

        TextArea textArea = new TextArea(EncodeUtils.encode(challenge));
        textArea.setEditable(false);
        textArea.setWrapText(true);
        GridPane gridPane = new GridPane();
        gridPane.add(textArea, COL_IND, ROW_IND-1);
        ButtonType clipboard = new ButtonType("Copy To Clipboard!");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Share the challenge code.");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(gridPane);
        alert.getButtonTypes().add(clipboard);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == clipboard ) {
            CopyUtils.copyToClipboard(textArea.getText());
        }
        
    }

    /**
     * A method to import a CustomChallenge with the Base64 encoding.
     */
    public void importChallenge()
    {
        TextInputDialog textInputDialog = new TextInputDialog(null);
        textInputDialog.setTitle("Add Challenge");
        textInputDialog.setHeaderText("Enter the code of the challenge");
        textInputDialog.setContentText("Code: ");
        textInputDialog.showAndWait();

        String code = textInputDialog.getEditor().getText();
        try {
            if (code != null) {
                ChallengeData toImp = EncodeUtils.decode(code);
                customChallenges.add(toImp);
                customChallengesData.update(toImp);
            }
        } catch (IOException | ClassNotFoundException e ) {
            e.printStackTrace();
        }

        Screen refresh = Game.getInstance().screenManager.customChallenges;
        Game.getInstance().setScreen(refresh);
    }

    /**
     * A method to remove a CustomChallenge from the list.
     * @param challengeToRemove as challenge to be removed.
     */
    public void removeChallenge(ChallengeData challengeToRemove) {
            ChallengeData toRemove = challengeToRemove;
            customChallenges.remove(toRemove);
            customChallengesData.remove(toRemove);
            Screen refresh = Game.getInstance().screenManager.customChallenges;
            Game.getInstance().setScreen(refresh);
    }

    /**
     * A method to construct the grid of list.
     * @param root as a Group object.
     * @param grid as a GridPane object.
     */
    public void constructGrid(Group root, GridPane grid)
    {
        grid.setHgap(H_GAP);
        grid.setVgap(V_GAP);
        grid.setLayoutX(GRID_LAY_X);
        grid.setLayoutY(GRID_LAY_Y);
        root.getChildren().add(grid);
    }
}
