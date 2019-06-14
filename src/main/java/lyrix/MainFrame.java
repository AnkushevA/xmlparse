package lyrix;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private XmlTree xmlTree;
    private LeftMenu leftMenu;
    private TopMenu topMenu;
    private StatusBar statusBar;

    public MainFrame() {
        super("Window");

        setLayout(new BorderLayout());

        xmlTree = new XmlTree();
        statusBar = new StatusBar();

        addTopMenu();
        addTreeMenu();
        addLeftMenu();

        statusBar.setPreferredSize(new Dimension(getWidth(), 25));
        add(statusBar, BorderLayout.SOUTH);

        //xmlTree.drawTree("C:\\Users\\BASS4x4\\IntelliJIDEAProjects\\xmlparse\\src\\main\\resources\\example.xml");

        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addTopMenu() {
        topMenu = new TopMenu();

        topMenu.setTreeExpandListener(new TreeExpandListener() { //анонимный класс, определяющий интерфейс
            @Override
            public void expandOrCollapseTree(boolean expand) {
                xmlTree.expandAll(expand);
//                xmlTree.drawTree("");
                // create a toast message
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

    private void addLeftMenu() {
        leftMenu = new LeftMenu();

        leftMenu.setListItemChooseListener(new ListItemChooseListener() {
            @Override
            public void redrawTree(String xmlPath) {
                //xmlTree.drawTree(xmlPath);
                JOptionPane.showMessageDialog(null, xmlPath);
            }
        });

        JScrollPane leftMenuScrollPane = new JScrollPane(leftMenu);
        leftMenuScrollPane.setPreferredSize(new Dimension(150, getHeight()));
        leftMenuScrollPane.getViewport().setBackground(Color.white);
        leftMenuScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(leftMenuScrollPane, BorderLayout.WEST);
    }

    private void addTreeMenu() {
        JScrollPane treeScrollPane = new JScrollPane(xmlTree);
        treeScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(treeScrollPane, BorderLayout.CENTER);
    }
}
