package lyrix;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;

class MainFrame extends JFrame {
    private SendRequestFrame sendRequestFrame;
    private TreeMenu treeMenu;
    private LeftMenu leftMenu;
    private TopMenu topMenu;
    private JSplitPane centralSplitMenu;
    private JScrollPane leftMenuScrollPane;
    private JScrollPane treeScrollPane;
    private NodeEditMenu nodeEditMenu;
    private JScrollPane nodeEditMenuScrollPane;

    MainFrame() {
        super("WSDL loader");

        setLayout(new BorderLayout());

        addTopMenu();
        createTreeMenu();
        createNodeEditMenu();
        createLeftMenu();
        addSplitMenu();
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    void expandTree(boolean expand) {
        treeMenu.expandAll(expand);
    }

    void refreshList(String path) {
        leftMenu.refreshMenu(path);
    }

    String makeXML() {
        return treeMenu.makeXML();
    }

    void redrawTree(String xmlPath) {
        if (Files.exists(Paths.get(xmlPath))) {
            treeMenu.drawTree(xmlPath);
        }
    }

    void showXMLRequestWindow(String xmlString) {
        if (sendRequestFrame == null) {
            sendRequestFrame = new SendRequestFrame(xmlString);
            sendRequestFrame.pack();
        }
        sendRequestFrame.showText(xmlString);
        sendRequestFrame.setVisible(true);
    }

    void showEditFields(DefaultMutableTreeNode node) {
        nodeEditMenu.showEditFields(node);
    }

    private void createNodeEditMenu() {
        nodeEditMenu = new NodeEditMenu(this, treeMenu.getTree());
        nodeEditMenuScrollPane = new JScrollPane(nodeEditMenu);
        nodeEditMenuScrollPane.setBorder(BorderFactory.createTitledBorder("Info"));
    }

    private void addSplitMenu() {
        JSplitPane leftSplitMenu = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftMenuScrollPane, treeScrollPane);
        leftSplitMenu.setOneTouchExpandable(true);
        leftSplitMenu.setDividerLocation(100);

        centralSplitMenu = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplitMenu, nodeEditMenuScrollPane);
        centralSplitMenu.setDividerLocation(450);

        add(centralSplitMenu, BorderLayout.CENTER);
    }

    private void addTopMenu() {
        topMenu = new TopMenu(this);
        add(topMenu, BorderLayout.NORTH);
    }

    private void createLeftMenu() {
        leftMenu = new LeftMenu(this);
        leftMenuScrollPane = new JScrollPane(leftMenu);
        leftMenuScrollPane.setPreferredSize(new Dimension(150, getHeight()));
        leftMenuScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        leftMenuScrollPane.setBorder(BorderFactory.createTitledBorder(".XML"));
    }

    private void createTreeMenu() {
        treeMenu = new TreeMenu(this);
        treeScrollPane = new JScrollPane(treeMenu);
        treeScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        treeScrollPane.setBorder(BorderFactory.createTitledBorder("Tree view:"));
    }
}
