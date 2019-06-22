package lyrix;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

class MainFrame extends JFrame {

    private TreeMenu treeMenu;
    private LeftMenu leftMenu;
    private TopMenu topMenu;
    private StatusBar statusBar;
    private JSplitPane centralSplitMenu;
    private JScrollPane leftMenuScrollPane;
    private JScrollPane treeScrollPane;
    private NodeEditMenu nodeEditMenu;
    private JScrollPane nodeEditMenuScrollPane;

    MainFrame() {
        super("WSDL loader");

        setLayout(new BorderLayout());

        addTopMenu();
        createNodeEditMenu();
        createTreeMenu();
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

    void makeXML() {
        treeMenu.makeXML();
    }

    void redrawTree(String xmlPath) {
        treeMenu.update(xmlPath);
    }

    void showEditFields(DefaultMutableTreeNode node, JTree tree) {
        nodeEditMenu.showEditFields(node, tree);
    }

    private void createNodeEditMenu() {
        nodeEditMenu = new NodeEditMenu(this);
        nodeEditMenuScrollPane = new JScrollPane(nodeEditMenu);
        nodeEditMenuScrollPane.setBorder(BorderFactory.createTitledBorder("Info"));
    }

    private void addStatusBar() {
        statusBar = new StatusBar();
        statusBar.setPreferredSize(new Dimension(getWidth(), 25));
        add(statusBar, BorderLayout.SOUTH);
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
