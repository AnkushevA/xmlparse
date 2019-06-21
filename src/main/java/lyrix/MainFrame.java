package lyrix;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.namespace.QName;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

public class MainFrame extends JFrame {

    private TreeMenu treeMenu;
    private LeftMenu leftMenu;
    private TopMenu topMenu;
    private StatusBar statusBar;
    private JSplitPane centralSplitMenu;
    private JScrollPane leftMenuScrollPane;
    private JScrollPane treeScrollPane;
    private NodeEditMenu nodeEditMenu;
    private JScrollPane nodeEditMenuScrollPane;

    public MainFrame() {
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


    private void createNodeEditMenu() {
        nodeEditMenu = new NodeEditMenu();
        nodeEditMenu.setExpandTreeAfterChangeListener(new ExpandTreeAfterChangeListener() {
            @Override
            public void expandTree() {
                treeMenu.expandAll(true);
            }
        });
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
        topMenu = new TopMenu();

        topMenu.setTreeExpandListener(new TreeExpandListener() { //анонимный класс, определяющий интерфейс
            @Override
            public void expandOrCollapseTree(boolean expand) {
                treeMenu.expandAll(expand);
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
        topMenu.setMakeXMLListener(new MakeXMLListener() {
            @Override
            public void makeXML() {
                treeMenu.makeXML();
            }
        });
        add(topMenu, BorderLayout.NORTH);
    }

    private void createLeftMenu() {
        leftMenu = new LeftMenu();
        leftMenu.setListItemChooseListener(new ListItemChooseListener() {
            @Override
            public void redrawTree(String xmlPath) {
                treeMenu.update(xmlPath);
            }
        });

        leftMenuScrollPane = new JScrollPane(leftMenu);
        leftMenuScrollPane.setPreferredSize(new Dimension(150, getHeight()));
        leftMenuScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        leftMenuScrollPane.setBorder(BorderFactory.createTitledBorder(".XML"));
    }

    private void createTreeMenu() {
        treeMenu = new TreeMenu();
        treeMenu.setNodeEditorListener(new NodeEditorListener() {
            @Override
            public void showEditFields(DefaultMutableTreeNode node, JTree tree) {
                nodeEditMenu.showEditFields(node, tree);
            }
        });
        treeScrollPane = new JScrollPane(treeMenu);
        treeScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        treeScrollPane.setBorder(BorderFactory.createTitledBorder("Tree view:"));
    }
}
