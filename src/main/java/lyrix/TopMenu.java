package lyrix;

import javax.swing.*;
import java.awt.*;
import java.io.File;

class TopMenu extends JPanel {

    private final MainFrame mainFrame;

    TopMenu(final MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        JButton expandTreeButton = ButtonFactory.makeButton("Развернуть дерево", actionEvent -> mainFrame.expandTree(true));

        JButton collapseTreeButton = ButtonFactory.makeButton("Свернуть дерево", actionEvent -> mainFrame.expandTree(false));

        JButton chooseFolderButton = ButtonFactory.makeButton("Выберите папку", actionEvent -> chooseFolder());

        JButton makeXMLButton = ButtonFactory.makeButton("Отправить запрос", actionEvent -> mainFrame.showXMLRequestWindow(mainFrame.makeXML()));

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(expandTreeButton);
        add(collapseTreeButton);
        add(chooseFolderButton);
        add(makeXMLButton);
    }

    private void chooseFolder() {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Выбор директории");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        File workingDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setCurrentDirectory(workingDirectory);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            mainFrame.refreshList(path);
        }
    }
}
