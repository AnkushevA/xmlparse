package lyrix;

import javax.swing.*;
import java.awt.*;

public class SendRequestPanel extends JPanel {
    private SendRequestFrame sendRequestFrame;
    private JTextArea requestTextArea;

    public SendRequestPanel(String xmlString, SendRequestFrame sendRequestFrame) {
        this.sendRequestFrame = sendRequestFrame;
        setLayout(new BorderLayout());
        requestTextArea = new JTextArea(30, 30);
        requestTextArea.setLineWrap(true);
        requestTextArea.setEditable(false);
        requestTextArea.setWrapStyleWord(true);
        requestTextArea.setText(xmlString);
        requestTextArea.setBorder(BorderFactory.createTitledBorder("Отправленное сообщение"));
        ;
        add(new JScrollPane(requestTextArea), BorderLayout.CENTER);
    }

    public void showText(String xmlString) {
        requestTextArea.setText(xmlString);
    }
}
