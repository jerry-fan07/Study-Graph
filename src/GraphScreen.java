import javafx.scene.control.Button;
import javafx.scene.layout.Pane;


/**
 * GraphScreen class organizes
 * pane by instantiating a Graph instance
 */
public class GraphScreen {
    public Pane root;
    public Graph graph;

    public GraphScreen() {
        this.root = new Pane();
        this.graph = new Graph(this);

        // quit button
        Button quit = new Button("Quit");
        quit.getStyleClass().add("button-secondary");
        quit.setLayoutX(Constants.QUIT_BUTTON_X);
        quit.setLayoutY(Constants.QUIT_BUTTON_Y);
        quit.setOnAction(e -> System.exit(0));
        this.root.getChildren().add(quit);
    }

    public Pane getRoot() {
        return this.root;
    }

}
