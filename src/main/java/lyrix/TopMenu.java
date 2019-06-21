package lyrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class TopMenu extends JPanel implements ActionListener {

    private JButton expandTreeButton;
    private JButton collapseTreeButton;
    private JButton chooseFolderButton;
    private JButton makeXMLButton;
//    private JButton clearTree;

    private TreeExpandListener treeExpandListener;
    private ListRefreshListener listRefreshListener;
//    private StatusbarListener statusbarListener;
    private MakeXMLListener makeXMLListener;

    TopMenu(){
        expandTreeButton = new JButton("Развернуть дерево");
        collapseTreeButton = new JButton("Свернуть дерево");
        chooseFolderButton = new JButton("Выберите папку");
        makeXMLButton = new JButton("Создать XML");

        expandTreeButton.addActionListener(this);
        collapseTreeButton.addActionListener(this);
        chooseFolderButton.addActionListener(this);
        makeXMLButton.addActionListener(this);

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(expandTreeButton);
        add(collapseTreeButton);
        add(chooseFolderButton);
        add(makeXMLButton);
    }

    void setMakeXMLListener(MakeXMLListener makeXMLListener) {
        this.makeXMLListener = makeXMLListener;
    }

    void setTreeExpandListener(TreeExpandListener treeExpandListener){
        this.treeExpandListener = treeExpandListener;
    }

    void setListRefreshListener(ListRefreshListener listRefreshListener) {
        this.listRefreshListener = listRefreshListener;
    }

    /*public void setStatusbarListener(StatusbarListener statusbarListener) {
        this.statusbarListener = statusbarListener;
    }*/

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
            }else if (buttonClicked == makeXMLButton){
                makeXMLListener.makeXML();
            }
        }
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
            listRefreshListener.refreshList(path);
        }
    }
}
