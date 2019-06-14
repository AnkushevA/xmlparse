package lyrix;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private XmlTree xmlTree;
    private LeftMenu leftMenu;
    private TopMenu topMenu;
    private BottomMenu bottomMenu;

    public MainFrame() {
        super("Window");

        setLayout(new BorderLayout());

        xmlTree = new XmlTree();

        leftMenu = new LeftMenu();

        topMenu = new TopMenu();

        bottomMenu = new BottomMenu();

        topMenu.setTreeExpandListener(new TreeExpandListener() { //анонимный класс, определяющий интерфейс
            @Override
            public void expandOrCollapseTree(boolean expand) {
                xmlTree.expandAll(expand);
//                xmlTree.addTree("");
                // create a toast message
            }
        });

        JScrollPane treeScrollPane = new JScrollPane(xmlTree);
        treeScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(treeScrollPane, BorderLayout.CENTER);

        JScrollPane leftMenuScrollPane = new JScrollPane(leftMenu);
        leftMenuScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(leftMenuScrollPane, BorderLayout.WEST);

        add(topMenu, BorderLayout.NORTH);

        add(bottomMenu, BorderLayout.SOUTH);

        bottomMenu.setPreferredSize(new Dimension(getWidth(), 25));
        //xmlTree.addTree("C:\\Users\\BASS4x4\\IntelliJIDEAProjects\\xmlparse\\src\\main\\resources\\example.xml");

        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

   /* private void setXml(String xmlPath)
    {
        xmlTree = new XmlDials(xmlPath);
        add(xmlTree, BorderLayout.EAST);
    }*/
}
