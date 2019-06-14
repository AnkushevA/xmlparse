package lyrix;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class TopMenu extends JPanel implements ActionListener {

    private JButton expandTreeButton;

    private JButton collapseTreeButton;

    private JButton chooseFolderButton;

//    private JButton clearTree;

    private TreeExpandListener treeExpandListener;
    private ListRefreshListener listRefreshListener;
    private StatusbarListener statusbarListener;



    public TopMenu(){
        expandTreeButton = new JButton("Развернуть дерево");
        collapseTreeButton = new JButton("Свернуть дерево");
        chooseFolderButton = new JButton("Выберите папку");

        expandTreeButton.addActionListener(this);
        collapseTreeButton.addActionListener(this);
        chooseFolderButton.addActionListener(this);

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(expandTreeButton);
        add(collapseTreeButton);
        add(chooseFolderButton);
    }

    public void setTreeExpandListener(TreeExpandListener treeExpandListener){
        this.treeExpandListener = treeExpandListener;
    }

    public void setListRefreshListener(ListRefreshListener listRefreshListener) {
        this.listRefreshListener = listRefreshListener;
    }

    public void setStatusbarListener(StatusbarListener statusbarListener) {
        this.statusbarListener = statusbarListener;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JButton buttonClicked = (JButton)actionEvent.getSource();
        if (buttonClicked != null) {
            if (buttonClicked == expandTreeButton){
                treeExpandListener.expandOrCollapseTree(true);
            }
            else if (buttonClicked == collapseTreeButton){
                treeExpandListener.expandOrCollapseTree(false);
            }
            else if (buttonClicked == chooseFolderButton){
                chooseFolder();
            }
        }
    }

    private void chooseFolder(){
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Выбор директории");
        // Определение режима - только каталог
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("xml", "XML");
        fileChooser.setFileFilter(filter);

        File workingDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setCurrentDirectory(workingDirectory);

        int result = fileChooser.showOpenDialog(this);

        // Если директория выбрана
        if (result == JFileChooser.APPROVE_OPTION ){
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            listRefreshListener.refreshList(path);
        }
        else{
            statusbarListener.changeStatus("Выберите папку.");
//            JOptionPane.showMessageDialog(this, "Выберите папку.");
        }
    }
}
