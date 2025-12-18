import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;

public class Review extends Popup {
    private Graph graph;
    private Card card;

    public Review(Card current, Graph graph) {
        super(current, graph);
        this.graph = graph;
        this.card = current;
        this.setReviewFields();
    }

    @Override
    public void returnScreen() {
        this.graph.cancelReview();
    }

    public void setReviewFields() {
        HBox topBar = new HBox(this.card.prompt());
        topBar.setAlignment(Pos.TOP_CENTER);
        topBar.setPadding(new Insets(60));

        Separator topSeparator = new Separator();

        HBox centerBar = new HBox(this.card.response());
        centerBar.setAlignment(Pos.CENTER);
        centerBar.setPadding(new Insets(20));

        Separator bottomSeparator = new Separator();

        Button edit = new Button("Edit");
        edit.getStyleClass().add("button-secondary");
        Button cancel = new Button("Cancel");
        cancel.getStyleClass().add("button-secondary");
        Button show = new Button("Show Answer");

        Button correct = new Button("Correct");
        correct.getStyleClass().add("button-success");
        Button incorrect = new Button("Again");
        incorrect.getStyleClass().add("button-danger");

        HBox bottomBar = new HBox(edit, show, cancel);
        bottomBar.setAlignment(Pos.BOTTOM_CENTER);
        bottomBar.setPadding(new Insets(20));
        bottomBar.setSpacing(10);

        edit.setOnAction(e -> {
            this.graph.editCard();
        });

        show.setOnAction(e -> {
            this.card.updateResponse();
            bottomBar.getChildren().remove(show);
            bottomBar.getChildren().remove(cancel);
            bottomBar.getChildren().addAll(correct, incorrect, cancel);
        });

        cancel.setOnAction(e -> this.graph.cancelReview());

        correct.setOnAction(e -> this.graph.endReview(true));
        incorrect.setOnAction(e -> this.graph.endReview(false));

        super.setFields();
        this.getCenterBox().getChildren().addAll(topBar, topSeparator, centerBar, bottomSeparator, bottomBar);
        VBox.setVgrow(centerBar, Priority.ALWAYS);
    }

    public void updateFields() {
        this.card.updatePrompt();
        this.card.updateResponse();
    }

}
