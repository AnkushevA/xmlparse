package lyrix;

public class TextFieldNode {
    String text;

    public TextFieldNode(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "[" + text + "]";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
