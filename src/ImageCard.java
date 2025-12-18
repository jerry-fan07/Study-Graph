import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.io.File;

/**
 * ImageCard inherits the Card class (which generates a normal card)
 * the image card allows for image as a response field
 */
public class ImageCard extends Card {

    private String imagePath;
    private Image image;
    private ImageView imageView;

    public ImageCard(String title, Graph graph) {
        super(title, graph);
    }

    public void setImagePath(String path) {
        this.imagePath = path;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    @Override
    public VBox response() {
        if (this.imagePath != null) {
            // absolute file path is invalid, needs uri
            String uri = new File(this.imagePath).toURI().toString();
            this.image = new Image(uri);

            this.imageView = new ImageView();
            this.imageView.setFitWidth(400);
            this.imageView.setPreserveRatio(true);
            return new VBox(this.imageView);
        }
        return new VBox();
    }

    @Override
    public void updateResponse() {
        if (this.imageView != null && this.image != null) {
            this.imageView.setImage(this.image);
        }
    }
}
