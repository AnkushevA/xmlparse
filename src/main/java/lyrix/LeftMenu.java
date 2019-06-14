package lyrix;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;

public class LeftMenu extends JPanel {

    private JList itemsList;
    private DefaultListModel items;
    private HashMap<String, String> menuFiles;

    public LeftMenu(){
        menuFiles = new HashMap<>();

        items = new DefaultListModel();
        itemsList = new JList(items);

        itemsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) { //todo добавить обработчик нажатий на список XMl
                 /*String selectedValue = (String) itemsList.getSelectedValue();
                 items.addElement("asddddddddasdsadsadsaddsad");*/
            }
        });

        add(itemsList);
    }

    public void refreshMenu(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".xml");
            }
        });
        items.removeAllElements();
        menuFiles.clear();

        for (int i = 0; i < listOfFiles.length; i++) {
            menuFiles.put(listOfFiles[i].getName(), listOfFiles[i].getAbsolutePath());
            items.addElement(listOfFiles[i].getName());
//            System.out.println("File " + listOfFiles[i].getName());
        }
    }

}
