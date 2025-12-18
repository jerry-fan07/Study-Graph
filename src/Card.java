import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 * Card class acts as a node on a directed acyclic graph, 
 * holding information for parents and children 
 * (in the future, I will add multiple parents for
 * a single card - multiple connections).
 */
public class Card {
    private Graph graph;
    private List<Card> parents;
    private List<Card> children;
    private double nodeDistance = Constants.DEFAULT_ND;
    private String title;
    private String question;
    private String answer;
    private double closeness;
    private Label qLabel;
    private Label aLabel;

    private double masteryRating;
    private LocalDateTime lastReviewed;
    private CardBubble graphBubble;
    private CardBubble sideBubble;

    public Card(String title, Graph graph) {
        this.graph = graph;
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
        this.title = title;

        this.masteryRating = 0.0;
        this.lastReviewed = LocalDateTime.now();

        this.graphBubble = new CardBubble(title, this);
        this.mouseInteraction();
    }

    /* 
    sets ups mouse interaction to occur
    so that the bubbles will expand when hovering
    and generate a menu on click
    */
    private void mouseInteraction() {
        this.hoverInteraction(this.graphBubble);
        this.graphBubble.getStackPane().setOnMouseClicked(e -> {
            this.graph.setCurrent(this, true);
            this.graphBubble.expand();
            this.graph.clickActions();
        });
    }

    /*
    sets up hover interactions (depending on if bubble is graph/side)
     */
    private void hoverInteraction(CardBubble bubble) {
        StackPane s = bubble.getStackPane();
        s.setOnMouseEntered(e -> {
            if (!this.graph.isAnySelected()) {
                bubble.expand();
            }
        });
        s.setOnMouseExited(e -> {
            bubble.pauseExpand();
            if (!this.graph.isAnySelected()) {
                bubble.deflate();
            }
        });
    }

    /*
    calculates priority of the card depending on parameters:
    time passed,
    closeness,
    mastery,
    total children (parent nodes have more importance)
    */
    public double calculatePriority() {
        double priority = this.timePassed() / (100 + 10 * Math.exp(this.masteryRating))
                + this.closeness * 10
                + this.getTotalChildren() * 10;

        this.graphBubble.setColor(priority);
        return priority;
    }

    /*
    updates values after review,
    if closeness threshold is reached then parent is also
    updated.
    */
    public void reviewUpdate(boolean isCorrect) {
        if (isCorrect){
            this.masteryRating = Math.min(this.masteryRating + 1, 10);
            this.lastReviewed = LocalDateTime.now();
            for (Card parent : this.parents) {
                if (closeness > 0.5) {
                    parent.reviewUpdate(isCorrect);
                }
            }
        }
        else {
            this.masteryRating = Math.max(this.masteryRating-0.5, 0 );
        }
    }

    /*
    calculates time passed
     */
    public double timePassed() {
        return java.time.Duration.between(this.lastReviewed, LocalDateTime.now()).toMillis();
    }

    /*
    arranges children using a rotation matrix on the fixed
    connection to the parent to be maximally spaced
     */
    public void arrangeChildren() {
        int total = this.children.size();
        double angle = (double) (2 * Math.PI) / (total + 1);
        double centerX = this.graphBubble.getLayout()[0];
        double centerY = this.graphBubble.getLayout()[1];
        double parentX;
        double parentY;
        double newX;
        double newY;

        if (!this.parents.isEmpty()) {
            parentX = this.parents.get(0).getLayout()[0];
            parentY = this.parents.get(0).getLayout()[1];
        } else {
            parentX = centerX;
            parentY = centerY + Constants.DEFAULT_ND;
            this.children.get(0).setLayout(parentX, parentY);
            angle = (double) (2 * Math.PI) / total;
        }

        newX = parentX - centerX;
        newY = parentY - centerY;

        this.nodeDistance = Constants.DEFAULT_ND + 20 * this.getTotalChildren();
        double radiusMultiplier = (double) this.nodeDistance / Math.sqrt(Math.pow(newX, 2) + Math.pow(newY, 2));
        newX *= radiusMultiplier;
        newY *= radiusMultiplier;

        for (int i = 0; i < total; i++) {
            double tempX = newX;
            newX = newX * Math.cos(angle) - newY * Math.sin(angle);
            newY = tempX * Math.sin(angle) + newY * Math.cos(angle);
            Card child = this.children.get(i);
            child.setLayout(newX + centerX, newY + centerY);
            child.arrangeLines();
            if (!child.getChildren().isEmpty()) {
                child.arrangeChildren();
            }
        }
    }

    /*
    arranges lines to be at the coordinate
    of the parent
     */
    public void arrangeLines() {
        for (int i = 0; i < this.parents.size(); i++) {
            this.graphBubble.arrangeLine(i,
                    this.parents.get(i).getLayout()[0],
                    this.parents.get(i).getLayout()[1]);
        }
    }

    /*
    calculates total children through recursive function
    */
    public int getTotalChildren() {
        int total = this.children.size();
        for (Card child : this.children) {
            total += child.getTotalChildren();
        }
        return total;
    }

    public void addChild(Card child) {
        this.children.add(child);
    }

    public void addParent(Card parent) {
        this.parents.add(parent);
        this.graphBubble.addLine(parent.getLayout()[0], parent.getLayout()[1]);
    }

    /* sets the question field to be a label*/
    public Node prompt() {
        this.qLabel = new Label(this.question);
        this.qLabel.getStyleClass().add("title-label");
        this.qLabel.setWrapText(true);
        this.qLabel.setMaxWidth(800);
        return this.qLabel;
    }

    /* updates question field */
    public void updatePrompt() {
        this.qLabel.setText(this.question);
    }

    /* sets up answer field */
    public Node response() {
        this.aLabel = new Label();
        this.aLabel.setFont(new Font(24));
        this.aLabel.getStyleClass().add("title-label");
        this.aLabel.setWrapText(true);
        this.aLabel.setMaxWidth(800);
        return this.aLabel;
    }

    /* updates answer field */
    public void updateResponse() {
        this.aLabel.setText(this.answer);
    }

    public List<Card> getParents() {
        return this.parents;
    }
    public List<Card> getChildren() {
        return this.children;
    }

    /* set closeness between parents, altering the thickness
    of the line connecting the nodes */
    public void setCloseness(double c) {
        this.closeness = c;
        this.graphBubble.setStroke(0, c);
    }

    public void setTitle(String t) {
        this.title = t;
        this.graphBubble.setTitle(t);
        this.sideBubble.setTitle(t);
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String getQuestion() {
        return this.question;
    }
    public String getAnswer() {
        return this.answer;
    }
    public String getTitle() {
        return this.title;
    }

    public void setLayout(double x, double y) {
        this.graphBubble.setLayout(x, y);
    }
    public double[] getLayout() {
        return this.graphBubble.getLayout();
    }

    /* creates a newSideBubble (on side pane) */
    public void newSideBubble() {
        this.sideBubble = new CardBubble(this.title, this);
        this.hoverInteraction(this.sideBubble);
        this.sideBubble.getStackPane().setOnMouseClicked(e -> {
            this.graph.setCurrent(this, false);
            this.graph.reviewCard();
        });
    }

    public CardBubble getSideBubble() {
        return this.sideBubble;
    }

    public CardBubble getGraphBubble() {
        return this.graphBubble;
    }

}
