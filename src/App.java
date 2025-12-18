import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class App extends Application {

    public void start(Stage stage) {
        GraphScreen home = new GraphScreen();
        Scene scene = new Scene(home.getRoot(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("StudyGraph");
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
