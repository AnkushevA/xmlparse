package lyrix;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private XmlTree xmlTree;
    private LeftMenu leftMenu;
    private TopMenu topMenu;
    private StatusBar statusBar;
    private JSplitPane centralSplitMenu;
    private JScrollPane leftMenuScrollPane;
    private JScrollPane treeScrollPane;

    public MainFrame() {
        super("Window");

        setLayout(new BorderLayout());

        addTopMenu();
        createTreeMenu();
        createLeftMenu();
        addSplitMenu();
        addStatusBar();

        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addStatusBar() {
        statusBar = new StatusBar();
        statusBar.setPreferredSize(new Dimension(getWidth(), 25));
        add(statusBar, BorderLayout.SOUTH);
    }

    private void addSplitMenu() {
        centralSplitMenu = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftMenuScrollPane, treeScrollPane);
        centralSplitMenu.setOneTouchExpandable(true);
        centralSplitMenu.setDividerLocation(150);

        add(centralSplitMenu, BorderLayout.CENTER);
    }

    private void addTopMenu() {
        topMenu = new TopMenu();

        topMenu.setTreeExpandListener(new TreeExpandListener() { //анонимный класс, определяющий интерфейс
            @Override
            public void expandOrCollapseTree(boolean expand) {
                xmlTree.expandAll(expand);
//                xmlTree.drawTree("");
            }
        });

        topMenu.setListRefreshListener(new ListRefreshListener() {
            @Override
            public void refreshList(String path) {
                leftMenu.refreshMenu(path);
            }
        });

        topMenu.setStatusbarListener(new StatusbarListener() {
            @Override
            public void changeStatus(String message) {
                statusBar.refreshStatus(message);
            }
        });
        add(topMenu, BorderLayout.NORTH);
    }

    private void createLeftMenu() {
        leftMenu = new LeftMenu();

        leftMenu.setListItemChooseListener(new ListItemChooseListener() {
            @Override
            public void redrawTree(String xmlPath) {
                //xmlTree.drawTree(xmlPath);
                JOptionPane.showMessageDialog(null, xmlPath);
            }
        });

        leftMenuScrollPane = new JScrollPane(leftMenu);
//        leftMenuScrollPane.setLayout(new BorderLayout());
//        leftMenuScrollPane.add(leftMenu, BorderLayout.EAST);
        leftMenuScrollPane.setPreferredSize(new Dimension(150, getHeight()));
        leftMenuScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        leftMenuScrollPane.setBorder(BorderFactory.createTitledBorder(".XML"));
    }

    private void createTreeMenu() {
        xmlTree = new XmlTree();
        treeScrollPane = new JScrollPane(xmlTree);
        treeScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        treeScrollPane.setBorder(BorderFactory.createTitledBorder("Tree view:"));
    }
}
