import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

/**
 * Graph class controls all the nodes/cards
 * to determine review, card changes, etc.
 */
public class Graph {
    private Pane sceneRoot;
    private Card rootNode;
    private Card current;
    private boolean isNodeSelected;
    private boolean isCardBeingClicked;
    private boolean isAnySelected;
    private boolean isGraphBubble;
    private Review reviewScreen;
    private Edit editScreen;
    private Add addScreen;
    private NodeMenu menu;
    private PriorityManager priorityManager;
    private double clickX;
    private double clickY;
    private boolean isDragging;

    public Graph(GraphScreen screen) {
        this.sceneRoot = screen.getRoot();
        this.rootNode = null;

        Card card1 = new Card("Subject with lots of text see if it wraps", this);
        Card card2 = new Card("Concept1", this);
        Card card3 = new Card("Concept2", this);
        Card card4 = new Card("Concept3", this);

        card1.setQuestion("Question: Recall the three parts of Sylow's Theorem.");
        card1.setAnswer("Answer:\n" +
                "a) There is at least one p-sylow subgroup for every prime factor p of #G.\n" +
                "b) Each of the p-sylow subgroups are conjugates of one another\n" +
                "c) k = 1 mod p, k divides the order of G ");

        this.priorityManager = new PriorityManager(this.sceneRoot);
        this.addCard(null, card1);
        this.addCard(card1, card2);
        this.addCard(card1, card3);
        this.addCard(card1, card4);

        this.mouseInteraction();
    }

    /*
    sets up mouse interaction on the graph to determine which objects
    are being pressed */
    private void mouseInteraction() {

        /*
        on mouse being pressed (either drag/clicked),
        remove any current node selection
        */
        this.sceneRoot.setOnMousePressed(e -> {
            if (this.rootNode == null) {
                Card root = new Card("New Card", this);
                root.setQuestion("Put question here...");
                root.setAnswer("Put answer here...");
                this.addCard(null, root);
            }
            if (this.isNodeSelected) {
                this.removeActions();
            }
            this.clickX = e.getSceneX();
            this.clickY = e.getSceneY();
            this.isDragging = false;
            this.isNodeSelected = false;
            this.isAnySelected = false;
            e.consume();
        });

        /*
        on mouse dragging, the nodes are moved along
        to give appearance of dragging diagram
         */
        this.sceneRoot.setOnMouseDragged(e -> {
            if (!this.isCardBeingClicked) {
                this.isDragging = true;
                double deltaX = e.getSceneX() - this.clickX;
                double deltaY = e.getSceneY() - this.clickY;

                this.clickX = e.getSceneX();
                this.clickY = e.getSceneY();

                for (Card card : this.priorityManager.getActiveCards()) {
                    card.getGraphBubble().deflate();
                    double newX = card.getLayout()[0] + deltaX / 2;  // resistance
                    double newY = card.getLayout()[1] + deltaY / 2;
                    card.setLayout(newX, newY);
                    card.arrangeLines();
                }
            }
            e.consume();
        });

        /* if a card is pressed also, card calls
        method that sets isCardBeingClicked = true
        */
        this.sceneRoot.setOnMouseClicked(e -> {
            if (this.isDragging) {
                this.isDragging = false;
                return;
            }
            // if card clicked, or sideBubble clicked
            else if (!this.isCardBeingClicked || !this.isGraphBubble) {
                if (this.isNodeSelected) {
                    this.current.getGraphBubble().deflate();
                    this.removeActions();
                }
                if (this.isGraphBubble) {
                    this.isAnySelected = false;
                }
                this.isNodeSelected = false;
            }
            this.isCardBeingClicked = false;
            e.consume();
        });
    }

    /*
    method adds a new node to the graph from selecting the 
    parent node it branches off of
    */
    public boolean addCard(Card parentNode, Card newNode) {
        // if no nodes exist
        if (this.rootNode == null) {
            this.rootNode = newNode;
            newNode.setLayout(Constants.SIDEBAR_X / 2 - Constants.NODE_RADIUS,
                    Constants.SCENE_HEIGHT / 2 - Constants.NODE_RADIUS);
            this.sceneRoot.getChildren().add(newNode.getGraphBubble().getStackPane());
            this.priorityManager.insertCard(newNode);
            this.priorityManager.toFront();
            return true;
        }
        // nodes exist, add to current grap
        if (parentNode != null) {
            parentNode.addChild(newNode);
            newNode.addParent(parentNode);
            this.rootNode.arrangeChildren();
            // moves lines backward
            for (Line line : newNode.getGraphBubble().getLines()) {
                this.sceneRoot.getChildren().add(line);
                line.toBack();
            }
            this.sceneRoot.getChildren().add(newNode.getGraphBubble().getStackPane());
            // inserts card to priority queue
            this.priorityManager.insertCard(newNode);
            this.priorityManager.toFront();
            return true;
        }
        return false;
    }

    /*
    removes a card by removing it from its parents
    then recursively removes its children 
     */
    public void removeCard(Card card) {
        if (card == this.rootNode) {
            this.rootNode = null;
        }
        for (Card parent : card.getParents()) {
            parent.getChildren().remove(card);
        }
        for (Line line : card.getGraphBubble().getLines()) {
            this.sceneRoot.getChildren().remove(line);
        }
        this.priorityManager.removeCard(card);
        this.sceneRoot.getChildren().remove(card.getGraphBubble().getStackPane());
        this.sceneRoot.getChildren().remove(card.getSideBubble().getStackPane());

        while (!card.getChildren().isEmpty()) {
            this.removeCard(card.getChildren().get(0));
        }
    }

    /*
    opens up NodeMenu that gives clickable actions
     */
    public void clickActions() {
        System.out.println("click action");
        this.menu = new NodeMenu(current, this);
        this.sceneRoot.getChildren().addAll(
                this.menu.getRemovePane(),
                this.menu.getAddPane(),
                this.menu.getReviewPane());
        this.priorityManager.toFront();
    }

    /*
    removes the node menu from the pane
     */
    public void removeActions() {
        this.sceneRoot.getChildren().removeAll(
                this.menu.getRemovePane(),
                this.menu.getAddPane(),
                this.menu.getReviewPane());
    }

    /*
    adds card menu (side bar)
     */
    public void addCardMenu() {
        this.addScreen = new Add(this.current, this);
        this.sceneRoot.getChildren().add(this.addScreen);
        this.priorityManager.setVisible(false);
    }

    /* shows edit menu
     */
    public void editCard() {
        this.editScreen = new Edit(this.current, this);
        this.sceneRoot.getChildren().add(this.editScreen);
    }

    /* removes edit menu
     */
    public void cancelEdit() {
        this.sceneRoot.getChildren().remove(this.editScreen);
    }

    /* sets the edit
     */
    public void confirmEdit() {
        this.sceneRoot.getChildren().remove(this.editScreen);
        this.reviewScreen.updateFields();
    }

    /* reviews the card
     */
    public void reviewCard() {
        this.reviewScreen = new Review(this.current, this);
        this.sceneRoot.getChildren().add(this.reviewScreen);
        this.priorityManager.setVisible(false);
    }

    /* ends the review (completing it)
     */
    public void endReview(boolean isCorrect) {
        this.sceneRoot.getChildren().remove(this.reviewScreen);
        if (this.isNodeSelected) {
            this.current.getGraphBubble().deflate();
            this.removeActions();
            this.isNodeSelected = false;
        } else {
            this.current.getSideBubble().deflate();
        }
        this.isAnySelected = false;
        this.current.reviewUpdate(isCorrect);
        this.priorityManager.generateQueue();
        this.priorityManager.setVisible(true);
    }

    /* cancels review by removing reviewScreen
    from the scene
     */
    public void cancelReview() {
        this.sceneRoot.getChildren().remove(this.reviewScreen);
        if (this.isNodeSelected) {
            this.removeActions();
            this.current.getGraphBubble().deflate();
            this.isNodeSelected = false;
        } else {
            this.current.getSideBubble().deflate();
        }
        this.isAnySelected = false;
        this.priorityManager.setVisible(true);
    }

    /* cancel add by removing addScreen
     */
    public void cancelAdd() {
        this.sceneRoot.getChildren().remove(this.addScreen);
        this.current.getGraphBubble().deflate();
        this.isNodeSelected = false;
        this.isAnySelected = false;
        this.priorityManager.setVisible(true);
    }

    /* sets current card (called by card after clicking)
     */
    public void setCurrent(Card card, boolean isGraphBubble) {
        if (this.isNodeSelected && (this.current != card || !isGraphBubble)) {
            this.current.getGraphBubble().deflate();
            this.removeActions();
        }
        this.current = card;

        this.isGraphBubble = isGraphBubble;
        this.isNodeSelected = isGraphBubble;
        this.isAnySelected = true;
        this.isCardBeingClicked = true;
    }

    /* checks if any card is currently
    selected (including side pane)
     */
    public boolean isAnySelected() {
        return this.isAnySelected;
    }

}
