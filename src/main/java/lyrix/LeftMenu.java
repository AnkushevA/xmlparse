package lyrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JButton parseXmlsButton;

    LeftMenu(final MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        menuFiles = new HashMap<>();
        setLayout(new BorderLayout());
        parseXmlsButton = new JButton("Отобразить методы");
        parseXmlsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
//                String url = JOptionPane.showInputDialog(mainFrame, "Вставьте ссылку:");
            }
        });

        items = new DefaultListModel<>();
        itemsList = new JList<>(items);
        itemsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                String selectedValue = itemsList.getSelectedValue();
                String path = menuFiles.get(selectedValue);
                if (Files.notExists(Paths.get(path))) {
                    JOptionPane.showMessageDialog(null, String.format("%s doesn't exist!", path));
                } else {
//                    mainFrame.redrawTree(path);
                    mainFrame.setLeftMenuState(path);
                }
            }
        });
        itemsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        itemsList.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        add(itemsList, BorderLayout.CENTER);
        add(parseXmlsButton, BorderLayout.SOUTH);
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
