import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public abstract class Popup extends BorderPane {
    private Card card;
    private Graph graph;
    private VBox centerBox;
    private boolean centerClicked;

    public Popup(Card card, Graph graph) {
        this.card = card;
        this.graph = graph;

        this.setMinSize(Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

        this.setBorders();
        this.setFields();
        this.mouseInteraction();
    }

    public abstract void returnScreen();

    private void mouseInteraction() {
        this.centerBox.setOnMouseClicked(e -> {
            this.centerClicked = true;
        });

        this.setOnMouseClicked(e -> {
            if (!this.centerClicked) {
                System.out.println("outside clicked");
                this.returnScreen();
            }
            this.centerClicked = false;
        });
    }

    private void setBorders() {
        VBox leftBox = new VBox(100);
        leftBox.setPadding(new Insets(100));
        leftBox.setMinWidth(100);
        leftBox.getStyleClass().add("modal-overlay");
        this.setLeft(leftBox);

        VBox rightBox = new VBox(100);
        rightBox.setPadding(new Insets(100));
        rightBox.setMinWidth(100);
        rightBox.getStyleClass().add("modal-overlay");
        this.setRight(rightBox);

        VBox bottomBox = new VBox(100);
        bottomBox.setPadding(new Insets(10));
        bottomBox.setMinHeight(100);
        bottomBox.getStyleClass().add("modal-overlay");
        this.setBottom(bottomBox);

        VBox topBox = new VBox(100);
        topBox.setPadding(new Insets(100));
        topBox.setMinHeight(100);
        topBox.getStyleClass().add("modal-overlay");
        this.setTop(topBox);
    }

    public void setFields() {
        this.centerBox = new VBox();
        this.centerBox.getStyleClass().add("modal-content");
        this.setCenter(centerBox);
    }

    public Card getCard() {
        return this.card;
    }

    public VBox getCenterBox() {
        return this.centerBox;
    }

}
