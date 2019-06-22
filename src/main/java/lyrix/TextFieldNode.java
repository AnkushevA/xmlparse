package lyrix;

public class TextFieldNode {
    private String attribute;
    private String text;
    private boolean include;

    public TextFieldNode(String attribute, String text, boolean include) {
        this.attribute = attribute.substring(attribute.indexOf(":") + 1);
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

    public String getDefaultString() {
        return String.format("[%s]:%s", attribute, text);
    }

    @Override
    public String toString() {
        String output = (include ? "<font color='green'>[" + attribute + "]:</font>" : "<font color='red'>[" + attribute + "]:</font>") + text;
        return "<html>"+ output + "</html>";
    }


}
