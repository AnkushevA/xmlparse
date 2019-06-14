package lyrix;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TopMenu extends JPanel implements ActionListener {

    private JButton expandTreeButton;

    private JButton collapseTreeButton;

//    private JButton clearTree;

    private TreeExpandListener treeExpandListener;

    public TopMenu(){
        expandTreeButton = new JButton("Развернуть дерево");

        collapseTreeButton = new JButton("Свернуть дерево");

        expandTreeButton.addActionListener(this);

        collapseTreeButton.addActionListener(this);

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(expandTreeButton);

        add(collapseTreeButton);
    }

    public void setTreeExpandListener(TreeExpandListener treeListener){
        this.treeExpandListener = treeListener;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JButton buttonClicked = (JButton)actionEvent.getSource();
        if (buttonClicked != null && buttonClicked == expandTreeButton){
            treeExpandListener.expandOrCollapseTree(true);
        }
        else if (buttonClicked != null && buttonClicked == collapseTreeButton){
            treeExpandListener.expandOrCollapseTree(false);
        }
    }
}
