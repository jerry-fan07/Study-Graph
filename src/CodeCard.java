import javafx.scene.control.Label;

/*
CodeCard extends card class which changes
the returned label for the card to have
a code style
 */
public class CodeCard extends Card {
    private Label aField;

    public CodeCard(String title, Graph graph) {
        super(title, graph);
    }

    /* overrides the response outputted to
    allow for stylized text
    */
    @Override
    public Label response() {
        this.aField = new Label();
        this.aField.setStyle("-fx-font-family: Monospaced;" +
                "-fx-font-size: 24px;" +
                "-fx-text-fill: blue;");
        this.aField.setWrapText(true);
        this.aField.setMaxWidth(800);
        return this.aField;
    }

    // overrides the update response
    @Override
    public void updateResponse() {
        this.aField.setText(super.getAnswer());
    }

}
