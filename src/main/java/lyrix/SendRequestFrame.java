package lyrix;

import javax.swing.*;
import java.awt.*;

public class SendRequestFrame extends JFrame {
    private final JSplitPane centralSplitMenu;
    private SendRequestPanel sendRequestPanel;
    private GetRequestPanel getRequestPanel;

    public SendRequestFrame(String xmlString) {
        super("Отправить запрос");
        sendRequestPanel = new SendRequestPanel(xmlString, this);
        getRequestPanel = new GetRequestPanel(this);

        setSize(800, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        centralSplitMenu = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sendRequestPanel, getRequestPanel);
        centralSplitMenu.setDividerLocation(450);
        add(centralSplitMenu, BorderLayout.CENTER);
    }

    public void showText(String xmlString) {
        sendRequestPanel.showText(xmlString);
    }
}
