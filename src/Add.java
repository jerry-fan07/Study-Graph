import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import java.io.File;


/**
 * Add class inherits the Popup class, used to generate a 
 * graphical menu for adding a new flashcard to the tree.
 */
public class Add extends Popup {
    private Graph graph;
    private Card card;
    private String selection;

    public Add(Card current, Graph graph) {
        super(current, graph);
        this.card = current;
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
    to implement specific menu fields for adding a new card
     */
    @Override
    public void setFields() {
        super.setFields();

        Label addCard = new Label("New Card");
        addCard.getStyleClass().add("title-label");
        HBox topBar = new HBox(addCard);
        topBar.setAlignment(Pos.TOP_CENTER);
        topBar.setPadding(new Insets(Constants.FIELD_PADDING));

        // separates with horizontal line
        Separator topSeparator = new Separator();

        Label t = new Label("Enter Display Title:");
        TextField tText = new TextField();
        Label q = new Label("Enter Question:");
        TextField qText = new TextField();
        Label a = new Label("Enter Answer:");
        TextArea aText = new TextArea();

        // slider
        Label c = new Label("Adjust closeness to parent card:");
        Slider closeness = new Slider(0.1, 1.0, 0.55);

        // image addition
        Label imageLabel = new Label("Select Image:");
        Button imageButton = new Button("Choose Image");
        Label imagePathLabel = new Label("No image selected");
        imageLabel.setVisible(false);
        imageButton.setVisible(false);
        imagePathLabel.setVisible(false);
        imageLabel.setManaged(false); // does not take up space
        imageButton.setManaged(false);
        imagePathLabel.setManaged(false);
        imageButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            // filters through files to only allow images
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            // gets file from file chooser
            File selectedFile = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (selectedFile != null) {
                imagePathLabel.setText(selectedFile.getAbsolutePath());
            }
        });

        // drop down menu
        Label d = new Label("Select card type:");
        ComboBox<String> drop = new ComboBox<>();
        drop.getItems().addAll("Normal", "Code", "Image");
        drop.setPromptText("Choose type");
        drop.setEditable(false);
        drop.setOnAction(e -> {
            this.selection = drop.getValue();
            boolean isImage = this.selection.equals("Image");

            a.setVisible(!isImage);
            a.setManaged(!isImage);
            aText.setVisible(!isImage);
            aText.setManaged(!isImage);

            imageLabel.setVisible(isImage);
            imageLabel.setManaged(isImage);
            imageButton.setVisible(isImage);
            imageButton.setManaged(isImage);
            imagePathLabel.setVisible(isImage);
            imagePathLabel.setManaged(isImage);
        });

        // warning label for unfilled fields
        Label warning = new Label("CARD FIELDS NOT FILLED");
        warning.getStyleClass().add("warning-text");
        warning.setVisible(false);

        qText.setPrefColumnCount(60);
        aText.setPrefColumnCount(60);
        aText.setPrefRowCount(5);

        VBox qaField = new VBox(t, tText, q, qText, a, aText, 
            imageLabel, imageButton, imagePathLabel,
             c, closeness, d, drop, warning);
        qaField.setSpacing(12);

        HBox centerBar = new HBox(qaField);
        centerBar.setAlignment(Pos.CENTER);
        centerBar.setPadding(new Insets(20));


        // separate with horizontal line
        Separator bottomSeparator = new Separator();

        // menu bar buttons
        Button cancel = new Button("Cancel");
        cancel.getStyleClass().add("button-secondary");
        Button confirm = new Button("Confirm");
        HBox bottomBar = new HBox(cancel, confirm);
        bottomBar.setAlignment(Pos.BOTTOM_CENTER);
        bottomBar.setPadding(new Insets(Constants.FIELD_PADDING));
        bottomBar.setSpacing(15);

        cancel.setOnAction(e -> {
            this.graph.cancelAdd();
        });

        confirm.setOnAction(e -> {
            String title = tText.getText();
            String question = qText.getText();
            String answer = aText.getText();
            String imagePath = imagePathLabel.getText();

            // if entry fields are empty, yields warning
            if (!title.isEmpty() && !question.isEmpty()) {
                Card newCard;
                if (this.selection.equals("Normal")) {
                    newCard = new Card(title, this.graph);
                    newCard.setAnswer(answer);
                } else if (this.selection.equals("Code")) {
                    newCard = new CodeCard(title, this.graph);
                    newCard.setAnswer(answer);
                } else {
                    newCard = new ImageCard(title, this.graph);
                    ((ImageCard) newCard).setImagePath(imagePath);
                    newCard.setAnswer("IMAGE CARD");
                }
                this.graph.addCard(this.card, newCard);
                newCard.setQuestion(question);
                newCard.setCloseness(closeness.getValue());
                this.graph.cancelAdd();
            } 
            else {
                warning.setVisible(true);
            }
        });

        this.getCenterBox().getChildren().addAll(topBar, topSeparator, centerBar, bottomSeparator, bottomBar);
        VBox.setVgrow(centerBar, Priority.ALWAYS);
    }

}
