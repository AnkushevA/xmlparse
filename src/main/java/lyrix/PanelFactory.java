package lyrix;

import javax.swing.*;

public class PanelFactory {
    public static JPanel createPanel(String panelName, MainFrame mainFrame) {
        switch (panelName) {
            case "TreeMenu":
                return new TreeMenu(mainFrame);
            case "LeftMenu":
                return new LeftMenu(mainFrame);
            case "TopMenu":
                return new TopMenu(mainFrame);
            case "NodeEditMenu":
                return new NodeEditMenu(mainFrame);
            default:
                return new JPanel();
        }
    }

}
