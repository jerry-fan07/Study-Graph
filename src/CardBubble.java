import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * CardBubble class handles the graphics of the card node
 * - each node has 1 side CardBubble, 1 graph CardBubble
 */
public class CardBubble {
    private Ellipse nodeCircle;
    private Label display;
    private StackPane stackPane;
    private Timeline expand;
    private Timeline deflate;
    private double layoutX;
    private double layoutY;
    private List<Line> lines;

    public CardBubble(String title, Card card) {
        this.nodeCircle = new Ellipse(Constants.NODE_RADIUS, Constants.NODE_RADIUS);
        this.nodeCircle.setFill(Color.WHITE);
        this.nodeCircle.setStroke(Color.web("#e5e7eb"));
        this.nodeCircle.setStrokeWidth(2);

        this.lines = new ArrayList<>();

        this.display = new Label(title);
        this.display.setTextAlignment(TextAlignment.CENTER);
        this.display.setWrapText(true);
        this.display.setMaxWidth(80);
        this.display.setTextFill(Color.WHITE);
        this.display.setStyle("-fx-font-weight: bold;");

        this.stackPane = new StackPane(nodeCircle, display);
        this.stackPane.setAlignment(Pos.CENTER);
    }
    
    /*
    sets colors to a value based on the priority
     */
    public void setColor(double value) {
        // gradient from green to red
        double t = Math.min(Math.max(value / 100.0, 0.0), 1.0);
        int r = (int) (16 + (239 - 16) * t);
        int g = (int) (185 + (68 - 185) * t);
        int b = (int) (129 + (68 - 129) * t);

        this.nodeCircle.setFill(Color.rgb(r, g, b));
    }

    /*
    expands the bubble when hovering over
     */
    public void expand() {
        KeyFrame kf = new KeyFrame(Duration.millis(2), ActionEvent -> {
            if (this.nodeCircle.getRadiusX() < 60) {
                this.nodeCircle.setRadiusX(this.nodeCircle.getRadiusX() + 0.1);
                this.nodeCircle.setRadiusY(this.nodeCircle.getRadiusY() + 0.1);
                this.setLayoutCircles(this.getLayout()[0] - 0.1, this.getLayout()[1] - 0.1);
            }
        });
        this.expand = new Timeline(kf);
        this.expand.setCycleCount(100);
        this.expand.play();
    }

    public void pauseExpand() {
        if (this.expand != null) {
            this.expand.stop();
        }
    }

    /* deflates the bubble when mouse leaves or
    when defocusing the card
     */
    public void deflate() {
        KeyFrame kf = new KeyFrame(Duration.millis(2), ActionEvent -> {
            if (this.nodeCircle.getRadiusX() > 50) {
                this.nodeCircle.setRadiusX(this.nodeCircle.getRadiusX() - 0.1);
                this.nodeCircle.setRadiusY(this.nodeCircle.getRadiusY() - 0.1);
                this.setLayoutCircles(this.getLayout()[0] + 0.1, this.getLayout()[1] + 0.1);
            }
        });
        this.deflate = new Timeline(kf);
        this.deflate.setCycleCount(100);
        this.deflate.play();
    }

    /* adds a line connecting the parent to the child node
     */
    public void addLine(double parentX, double parentY) {
        Line line = new Line(this.layoutX + Constants.NODE_RADIUS, this.layoutY + Constants.NODE_RADIUS,
                parentX + Constants.NODE_RADIUS, parentY + Constants.NODE_RADIUS);
        line.setStroke(Color.WHITE);
        line.toBack();
        this.lines.add(line);
    }

    /* arranges lines after moving parent node */
    public void arrangeLine(int i, double parentX, double parentY) {
        Line line = this.lines.get(i);
        line.setStartX(this.layoutX + Constants.NODE_RADIUS);
        line.setStartY(this.layoutY + Constants.NODE_RADIUS);
        line.setEndX(parentX + Constants.NODE_RADIUS);
        line.setEndY(parentY + Constants.NODE_RADIUS);
    }

    /* sets line thickness for closeness rating */
    public void setStroke(int i, double weight) {
        Line line = this.lines.get(i);
        line.setStrokeWidth(weight * 5);
    }

    public Ellipse getNodeCircle() {
        return this.nodeCircle;
    }
    public StackPane getStackPane() {
        return this.stackPane;
    }
    public List<Line> getLines() {
        return this.lines;
    }
    public double[] getLayout() {
        double[] layout = { this.layoutX, this.layoutY };
        return layout;
    }
    public void setTitle(String t) {
        this.display.setText(t);
    }
    
    /*
    sets layout while adjusting the lines underneath as well */
    public void setLayout(double x, double y) {
        this.layoutX = x;
        this.layoutY = y;
        this.stackPane.setLayoutX(x);
        this.stackPane.setLayoutY(y);
        for (Line line : this.lines) {
            line.setStartX(x + Constants.NODE_RADIUS);
            line.setStartY(y + Constants.NODE_RADIUS);
        }
    }

    /* sets layout without adjusting the lines underneath
    - for expand and deflate */
    public void setLayoutCircles(double x, double y) {
        this.layoutX = x;
        this.layoutY = y;
        this.stackPane.setLayoutX(x);
        this.stackPane.setLayoutY(y);
    }
}
