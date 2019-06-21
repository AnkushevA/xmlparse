package lyrix;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class LeftMenu extends JPanel {

    private JList itemsList;
    private DefaultListModel items;
    private HashMap<String, String> menuFiles;

    private ListItemChooseListener listItemChooseListener;

    public LeftMenu(){
        menuFiles = new HashMap<>();

        items = new DefaultListModel();
        itemsList = new JList(items);
        itemsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                String selectedValue = (String) itemsList.getSelectedValue();
                String path = menuFiles.get(selectedValue);
                if (Files.notExists(Paths.get(path))) {
                    JOptionPane.showMessageDialog(null, String.format("%s doesn't exist!", path));
                }
                else {
                    listItemChooseListener.redrawTree(path);
                }
            }
        });
        itemsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        itemsList.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        add(itemsList);
    }

    public void setListItemChooseListener(ListItemChooseListener listItemChooseListener) {
        this.listItemChooseListener = listItemChooseListener;
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
