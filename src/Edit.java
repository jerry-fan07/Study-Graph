import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import java.io.File;

/**
 * Edit class inherits the Popup class, allows 
 */
public class Edit extends Popup {
    private Graph graph;

    public Edit(Card current, Graph graph) {
        super(current, graph);
        this.graph = graph;
    }

    /*
    overrides the return screen method for Popup abstract class
    to specifically remove the Add menu from the graph
    */
    @Override
    public void returnScreen() {
        this.graph.cancelAdd();
    }

    /*
    overrides the setFields method for Popup abstract class
    to implement specific menu fields for editing a card
     */
    @Override
    public void setFields() {
        super.setFields();

        Label addCard = new Label("Edit Card");
        addCard.getStyleClass().add("title-label");
        HBox topBar = new HBox(addCard);
        topBar.setAlignment(Pos.TOP_CENTER);
        topBar.setPadding(new Insets(Constants.FIELD_PADDING));

        // separate with horizontal line
        Separator topSeparator = new Separator();

        Card card = this.getCard();
        Label t = new Label("Enter Display Title:");
        Label q = new Label("Enter Question:");
        Label a = new Label("Enter Answer:");
        TextField tText = new TextField();
        TextField qText = new TextField();
        TextArea aText = new TextArea();
        tText.setText(card.getTitle());
        qText.setText(card.getQuestion());
        aText.setText(card.getAnswer());

        // warning text
        Label warning = new Label("CARD FIELDS NOT FILLED");
        warning.setTextFill(Color.RED);
        warning.setFont(new Font(Constants.FONT_SIZE));
        warning.setVisible(false);

        aText.setPrefColumnCount(Constants.ACOLUMNS);
        aText.setPrefRowCount(Constants.AROWS);
        aText.setWrapText(true);

        // needs to check if isImageCard since the card is already created
        boolean isImageCard = card instanceof ImageCard;

        Label imageLabel = new Label("Selected Image:");
        Button imageBtn = new Button("Change Image");
        Label imagePathLabel = new Label("No image");

        if (isImageCard) {
            a.setVisible(false);
            a.setManaged(false);
            aText.setVisible(false);
            aText.setManaged(false);
            ImageCard iCard = (ImageCard) card;
            if (iCard.getImagePath() != null) {
                imagePathLabel.setText(iCard.getImagePath());
            }
        } else {
            imageLabel.setVisible(false);
            imageLabel.setManaged(false);
            imageBtn.setVisible(false);
            imageBtn.setManaged(false);
            imagePathLabel.setVisible(false);
            imagePathLabel.setManaged(false);
        }

        imageBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            // filters out files to show only images
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            File selectedFile = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (selectedFile != null) {
                imagePathLabel.setText(selectedFile.getAbsolutePath());
                // absolute path later changed to specific image path
            }
        });

        VBox qaField = new VBox(t, tText, q, qText, a, aText, imageLabel, imageBtn, imagePathLabel, warning);
        qaField.setSpacing(12);

        HBox centerBar = new HBox(qaField);
        centerBar.setAlignment(Pos.CENTER);
        centerBar.setPadding(new Insets(Constants.FIELD_PADDING));

        // horizontal line for separation
        Separator bottomSeparator = new Separator();

        // bottom button bar
        Button cancel = new Button("Cancel");
        cancel.getStyleClass().add("button-secondary");
        Button confirm = new Button("Confirm");
        HBox bottomBar = new HBox(cancel, confirm);
        bottomBar.setAlignment(Pos.BOTTOM_CENTER);
        bottomBar.setPadding(new Insets(20));
        bottomBar.setSpacing(15);

        cancel.setOnAction(e -> {
            this.graph.cancelEdit();
        });

        confirm.setOnAction(e -> {
            String title = tText.getText();
            String question = qText.getText();
            String answer = aText.getText();

            // checks if fields are not filled
            if (!title.isEmpty() && !question.isEmpty()) {
                card.setTitle(title);
                card.setQuestion(question);
                if (isImageCard) {
                    ((ImageCard) card).setImagePath(imagePathLabel.getText());
                } else {
                    card.setAnswer(answer);
                }
                this.graph.confirmEdit();
            } 
            else {
                warning.setVisible(true);
            }
        });

        this.getCenterBox().getChildren().addAll(topBar, topSeparator, centerBar, bottomSeparator, bottomBar);
        VBox.setVgrow(centerBar, Priority.ALWAYS);
    }

}