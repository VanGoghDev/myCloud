package messages;

public class MyMessage extends AbstractMessage {
    private static final long serialVersionUID = 5193392663743561680L;

    private String text;

    public String getText() {
        return text;
    }

    public MyMessage(String text) {
        this.text = text;
    }
}
