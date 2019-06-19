package lyrix;

public class TextFieldNode {
    private String attribute;
    private String text;
    private boolean include;

    public TextFieldNode(String attribute, String text, boolean include) {
        this.attribute = attribute;
        this.text = text;
        this.include = include;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public boolean isIncluded() {
        return include;
    }

    public void setIncluded(boolean include) {
        this.include = include;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        String output = "[" + attribute + "]:" + text;
        return include ? "<html><font color='green'>"+ output + "</font></html>" : "<html><font color='red'>"+ output + "</font></html>";
    }


}
