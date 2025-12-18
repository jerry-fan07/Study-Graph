import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 * Node menu generates a menu that includes three buttons:
 * add
 * remove
 * review
 */
public class NodeMenu {
    private Graph graph;
    private Card card;
    private StackPane remove;
    private StackPane add;
    private StackPane review;

    public NodeMenu(Card card, Graph graph) {
        this.card = card;
        this.graph = graph;

        double cardX = this.card.getLayout()[0];
        double cardY = this.card.getLayout()[1];

        Ellipse removeButton = new Ellipse(0, 0, 20, 20);
        removeButton.getStyleClass().addAll("node-menu-button", "node-menu-remove");

        Ellipse addButton = new Ellipse(cardX + 100, cardY, 20, 20);
        addButton.getStyleClass().addAll("node-menu-button", "node-menu-add");

        Rectangle verticalBar = new Rectangle(6, 20);
        verticalBar.setFill(Color.WHITE);
        verticalBar.setMouseTransparent(true);

        Rectangle horizontalBar = new Rectangle(20, 6);
        horizontalBar.setFill(Color.WHITE);
        horizontalBar.setMouseTransparent(true);

        // review button
        Ellipse reviewButton = new Ellipse(cardX, cardY + 100, 20, 20);
        reviewButton.getStyleClass().addAll("node-menu-button", "node-menu-review");

        Polygon triangle = new Polygon(12, 10, 12, 30, 28, 20);
        triangle.setFill(Color.WHITE);
        triangle.setMouseTransparent(true);

        // remove button
        this.remove = new StackPane(removeButton);
        Rectangle r1 = new Rectangle(20, 6);
        r1.setFill(Color.WHITE);
        r1.setRotate(45);
        Rectangle r2 = new Rectangle(20, 6);
        r2.setFill(Color.WHITE);
        r2.setRotate(-45);
        this.remove.getChildren().addAll(r1, r2);
        r1.setMouseTransparent(true);
        r2.setMouseTransparent(true);

        this.remove.setLayoutX(cardX + 125);
        this.remove.setLayoutY(cardY + 25);

        this.add = new StackPane(addButton, verticalBar, horizontalBar);
        this.add.setLayoutX(cardX + 120);
        this.add.setLayoutY(cardY + 80);

        this.review = new StackPane(reviewButton, triangle);
        this.review.setLayoutX(cardX + 80);
        this.review.setLayoutY(cardY + 120);

        this.mouseInteraction();
    }

    /*
    mouse interaction occurs by generating each 
    respective class for the button
     */
    private void mouseInteraction() {
        this.remove.setOnMousePressed(e -> e.consume());
        this.remove.setOnMouseClicked(e -> {
            this.graph.removeCard(this.card);
            this.graph.removeActions();
            e.consume();
        });

        this.add.setOnMousePressed(e -> e.consume());
        this.add.setOnMouseClicked(e -> {
            this.graph.addCardMenu();
            this.graph.removeActions();
            e.consume();
        });

        this.review.setOnMousePressed(e -> e.consume());
        this.review.setOnMouseClicked(e -> {
            this.graph.reviewCard();
            this.graph.removeActions();
            e.consume();
        });
    }

    public StackPane getRemovePane() {
        return this.remove;
    }

    public StackPane getAddPane() {
        return this.add;
    }

    public StackPane getReviewPane() {
        return this.review;
    }
}