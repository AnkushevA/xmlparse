package lyrix;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

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
        addStatusBar();

        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void createNodeEditMenu() {
        nodeEditMenu = new NodeEditMenu();
        /*nodeEditMenu.setUpdateTreeListener(new UpdateTreeListener() {
            @Override
            public void updateTree(DefaultMutableTreeNode node) {
                treeMenu.nodeChanded(node);
            }
        });*/
        nodeEditMenuScrollPane = new JScrollPane(nodeEditMenu);
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
//                xmlTree.expandAll(expand);
//                xmlTree.drawTree("");
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
        add(topMenu, BorderLayout.NORTH);
    }

    private void createLeftMenu() {
        leftMenu = new LeftMenu();
        leftMenu.setListItemChooseListener(new ListItemChooseListener() {
            @Override
            public void redrawTree(String xmlPath) {
                //xmlTree.drawTree(xmlPath);
//                JOptionPane.showMessageDialog(null, xmlPath);
                treeMenu.update(xmlPath);
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
//        xmlTree = new XmlTree();
        treeMenu = new TreeMenu();
        treeMenu.setNodeEditorListener(new NodeEditorListener() {
            @Override
            public void showEditFields(DefaultMutableTreeNode node, JTree tree) {
                nodeEditMenu.showEditFields(node, tree);
            }
        });
//        treeScrollPane = new JScrollPane(xmlTree);
        treeScrollPane = new JScrollPane(treeMenu);
        treeScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        treeScrollPane.setBorder(BorderFactory.createTitledBorder("Tree view:"));
    }
}
