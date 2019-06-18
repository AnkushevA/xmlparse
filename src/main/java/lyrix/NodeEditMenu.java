package lyrix;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NodeEditMenu extends JPanel {
    private JLabel nameLabel;
    private JTextField dataField;
    private NodeEditorListener nodeEditorListener;
    private JCheckBox includeToOutput;
    private JButton okButton;
    private JButton cancelButton;
    private JPanel southPanel;
    private DefaultMutableTreeNode node;


    public void setNodeEditorListener(NodeEditorListener nodeEditorListener) {
        this.nodeEditorListener = nodeEditorListener;
    }

    public NodeEditMenu() {
        nameLabel = new JLabel("Node name");
        nameLabel.setFont(nameLabel.getFont().deriveFont(20.0f));
        dataField = new JTextField(15);
        okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (node != null) {

                }
            }
        });

        cancelButton = new JButton("Cancel");
        includeToOutput = new JCheckBox("Include to output");
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        //две ячейки в одном ряде
        gc.gridwidth = 2;

        //сколько места будет занимать элемент относительно в оставшемся пространстве
        gc.weightx = 1;
        gc.weighty = 0;

        gc.gridx = 0;
        gc.gridy = 0;
        add(nameLabel, gc);

        gc.weightx = 1;
        gc.weighty = 0.1;
        gc.gridx = 0;
        gc.gridy = 1;
        add(includeToOutput, gc);

        gc.weightx = 1;
        gc.weighty = 0;
        gc.gridx = 0;
        gc.gridy = 2;
        add(dataField, gc);


        gc.weightx = 1;
        gc.weighty = 0.2;

        gc.gridx = 0;
        gc.gridy = 3;
        add(okButton, gc);


        gc.weightx = 1;
        gc.weighty = 2;

        gc.gridx = 1;
        gc.gridy = 4;
        gc.anchor = GridBagConstraints.PAGE_START;
        add(cancelButton, gc);
    }

    public void showEditFields(DefaultMutableTreeNode node, JTree tree){
        this.node = node;
        if (node.isLeaf()){
            String treeNodeString = node.toString();
            int firstClosingBracket = treeNodeString.indexOf("]");
            if (firstClosingBracket != -1) {
                nameLabel.setText(treeNodeString.substring(0, firstClosingBracket + 1));
                if (treeNodeString.length() != firstClosingBracket + 1){
                    dataField.setText(treeNodeString.substring(firstClosingBracket + 2));
                }
            }
        }
    }

}
