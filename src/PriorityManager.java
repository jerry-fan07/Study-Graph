import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class PriorityManager {
    private Card[] priorityQueue;
    private int heapSize;
    private ScrollPane scrollPane;
    private VBox contentBox;
    private Pane sceneRoot;

    public PriorityManager(Pane sceneRoot) {
        this.priorityQueue = new Card[100];

        this.sceneRoot = sceneRoot;

        // Initialize container for bubbles
        this.contentBox = new VBox(20);
        this.contentBox.setPadding(new Insets(20));
        this.contentBox.setAlignment(Pos.TOP_CENTER);

        // Initialize ScrollPane
        this.scrollPane = new ScrollPane(this.contentBox);
        this.scrollPane.setLayoutX(Constants.SIDEBAR_X);
        this.scrollPane.setLayoutY(15);
        this.scrollPane.setPrefSize(Constants.SIDEBAR_WIDTH - 10, Constants.SCENE_HEIGHT - 80);
        this.scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // Style
        this.scrollPane.getStyleClass().add("sidebar");
        this.contentBox.setStyle("-fx-background-color: transparent;");
        this.scrollPane.setFitToWidth(true); // Ensure VBox fills width

        this.sceneRoot.getChildren().add(this.scrollPane);

        this.setRefresh();
    }

    private void setRefresh() {
        KeyFrame kf = new KeyFrame(Duration.millis(1), e -> {
            for (int i = 0; i < this.heapSize; i++) {
                this.priorityQueue[i].calculatePriority();
            }
        });
        Timeline time = new Timeline(kf);
        time.setCycleCount(-1);
        time.play();
    }

    public void generateQueue() {
        this.buildHeap(this.heapSize);
        Card[] sorted = this.sortedList();

        this.contentBox.getChildren().clear();

        System.out.println("SORTED :   ");
        for (int i = 0; i < sorted.length; i++) {
            System.out.println(i + " " + sorted[i].getTitle() + " " + sorted[i].calculatePriority());
            if (sorted[i].getSideBubble() == null) {
                sorted[i].newSideBubble();
            }
            this.contentBox.getChildren().add(sorted[i].getSideBubble().getStackPane());
        }

    }

    public void printQueue() {
        for (int i = 0; i < 5; i++) {
            if (this.priorityQueue[i] != null) {
                System.out.println(
                        i + " " + this.priorityQueue[i].getTitle() + " " + this.priorityQueue[i].calculatePriority());
            } else {
                System.out.println(i + ": null");
            }
        }
        System.out.println("HEAPSIZE: " + this.heapSize);
    }

    public void insertCard(Card card) {
        int i = this.heapSize;
        this.priorityQueue[i] = card;
        this.heapSize++;
        double newP = card.calculatePriority();

        while (i > 0 && this.priorityQueue[this.getParent(i)].calculatePriority() < newP) {
            this.priorityQueue[i] = this.priorityQueue[this.getParent(i)];
            this.priorityQueue[this.getParent(i)] = card;
            i = this.getParent(i);
        }

        this.generateQueue();
    }

    public boolean removeCard(Card card) {
        int j = 0;
        while (j < this.heapSize && this.priorityQueue[j] != card) {
            j++;
        }
        if (this.priorityQueue[j] == card) {
            for (int i = j; i < this.heapSize; i++) {
                this.priorityQueue[i] = this.priorityQueue[i + 1];
            }
            this.heapSize--;
            this.generateQueue();
            return true;
        }
        return false;
    }

    public void heapify(int i) {
        int l = this.getLeft(i);
        int r = this.getRight(i);
        int max = i;
        if (l < this.heapSize
                && this.priorityQueue[l].calculatePriority() > this.priorityQueue[i].calculatePriority()) {
            max = l;
        }
        if (r < this.heapSize
                && this.priorityQueue[r].calculatePriority() > this.priorityQueue[max].calculatePriority()) {
            max = r;
        }
        if (max != i) {
            Card temp = this.priorityQueue[i];
            this.priorityQueue[i] = this.priorityQueue[max];
            this.priorityQueue[max] = temp;
            this.heapify(max);
        }
    }

    public void buildHeap(int size) {
        this.heapSize = size;
        for (int i = this.getParent(size - 1); i >= 0; i--) {
            this.heapify(i);
        }
    }

    public Card pop() {
        if (this.heapSize < 1) {
            return null;
        }
        Card max = this.priorityQueue[0];
        this.priorityQueue[0] = this.priorityQueue[this.heapSize - 1];
        this.heapSize--;
        this.heapify(0);
        return max;
    }

    public Card[] sortedList() {
        Card[] copy = new Card[this.priorityQueue.length];
        for (int i = 0; i < this.priorityQueue.length; i++) {
            copy[i] = this.priorityQueue[i];
        }

        Card[] sorted = new Card[this.heapSize];
        int size = this.heapSize;
        for (int i = 0; i < size; i++) {
            sorted[i] = this.pop();
        }

        this.heapSize = size;
        this.priorityQueue = copy;
        return sorted;
    }

    public Card[] getActiveCards() {
        Card[] active = new Card[this.heapSize];
        for (int i = 0; i < this.heapSize; i++) {
            active[i] = this.priorityQueue[i];
        }
        return active;
    }

    public int getParent(int i) {
        return (i - 1) / 2;
    }

    public int getLeft(int i) {
        return 2 * i + 1;
    }

    public int getRight(int i) {
        return 2 * (i + 1);
    }

    public void setVisible(boolean visible) {
        this.scrollPane.setVisible(visible);
    }

    public void toFront() {
        this.scrollPane.toFront();
    }

}
