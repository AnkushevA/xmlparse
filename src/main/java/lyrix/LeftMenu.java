package lyrix;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

class LeftMenu extends JPanel {

    private final MainFrame mainFrame;
    private JList<String> itemsList;
    private DefaultListModel<String> items;
    private HashMap<String, String> menuFiles;

    LeftMenu(final MainFrame mainFrame){
        this.mainFrame = mainFrame;
        menuFiles = new HashMap<>();

        items = new DefaultListModel<>();
        itemsList = new JList<>(items);
        itemsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                String selectedValue = itemsList.getSelectedValue();
                String path = menuFiles.get(selectedValue);
                if (Files.notExists(Paths.get(path))) {
                    JOptionPane.showMessageDialog(null, String.format("%s doesn't exist!", path));
                }
                else {
                    mainFrame.redrawTree(path);
                }
            }
        });
        itemsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        itemsList.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        add(itemsList);
    }

    void refreshMenu(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
        items.removeAllElements();
        menuFiles.clear();

        if (listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                menuFiles.put(listOfFile.getName(), listOfFile.getAbsolutePath());
                items.addElement(listOfFile.getName());
            }
        }
    }

}
