package lyrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

class TopMenu extends JPanel{

    private final MainFrame mainFrame;

    TopMenu(final MainFrame mainFrame){
        this.mainFrame = mainFrame;
        JButton expandTreeButton = ButtonFactory.makeButton("Развернуть дерево", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mainFrame.expandTree(true);
            }
        });

        JButton collapseTreeButton = ButtonFactory.makeButton("Свернуть дерево", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mainFrame.expandTree(false);
            }
        });

        JButton chooseFolderButton = ButtonFactory.makeButton("Выберите папку", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chooseFolder();
            }
        });

        JButton makeXMLButton = ButtonFactory.makeButton("Создать XML", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mainFrame.makeXML();
            }
        });

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(expandTreeButton);
        add(collapseTreeButton);
        add(chooseFolderButton);
        add(makeXMLButton);
    }

    private void chooseFolder(){
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Выбор директории");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        File workingDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setCurrentDirectory(workingDirectory);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION ){
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            mainFrame.refreshList(path);
        }
    }
}
