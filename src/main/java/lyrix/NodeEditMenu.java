package lyrix;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.xml.soap.Text;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class NodeEditMenu extends JPanel {
    private JLabel nameLabel;
    private JTextField dataField;
    private JCheckBox includeToOutput;
    private JButton okButton;
    private JButton addButton;
    private JButton removeButton;
    private DefaultMutableTreeNode node;
    private JTree tree;
    private TextFieldNode textFieldNode;
//    private UpdateTreeListener updateTreeListener;
    private JList itemsList;
    private DefaultListModel model;
    private ArrayList<DefaultMutableTreeNode> nodeElements;
    /*
    public void setUpdateTreeListener(UpdateTreeListener updateTreeListener) {
        this.updateTreeListener = updateTreeListener;
    }*/

    private void updateTreeModel(){
        ((DefaultTreeModel) tree.getModel()).reload();
    }

    public NodeEditMenu() {
        nameLabel = new JLabel("Node name");
        nameLabel.setFont(nameLabel.getFont().deriveFont(20.0f));
        dataField = new JTextField(15);
        okButton = new JButton("OK");
        addButton = new JButton("+");
        removeButton = new JButton("-");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (node != null) {
                    TextFieldNode fieldNode = new TextFieldNode(textFieldNode.getAttribute(), node.isLeaf() ? dataField.getText() : "", includeToOutput.isSelected());
                    node.setUserObject(fieldNode);
                    if (textFieldNode.getAttribute().equals("car:MName")) {
                        DefaultMutableTreeNode temp = (DefaultMutableTreeNode) node.getParent();
                        if (temp.getUserObject() instanceof TextFieldNode) {
                            ((TextFieldNode)temp.getUserObject()).setText(dataField.getText());
                        }
                    }
                    updateTreeModel();
                    //updateTreeListener.updateTree(node);
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (node != null) {
                    if (textFieldNode.getAttribute().contains("fields")){
                        DefaultMutableTreeNode nodeToAdd = new DefaultMutableTreeNode(new TextFieldNode("car:item", "", true));
                        nodeToAdd.add(new DefaultMutableTreeNode(new TextFieldNode("car:MName", "", true)));
                        nodeToAdd.add(new DefaultMutableTreeNode(new TextFieldNode("car:MLabel", "", true)));
                        nodeToAdd.add(new DefaultMutableTreeNode(new TextFieldNode("car:MType", "", true)));
                        nodeToAdd.add(new DefaultMutableTreeNode(new TextFieldNode("car:subType", "", true)));
                        nodeToAdd.add(new DefaultMutableTreeNode(new TextFieldNode("car:MValue", "", true)));
                        node.add(nodeToAdd);
                        updateTreeModel();
                        model.addElement("car:item");
                    }
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int[] selectedItems = itemsList.getSelectedIndices();
                if (selectedItems.length == 1){
                    node.remove(selectedItems[0]);
                    updateTreeModel();
                    model.removeElementAt(selectedItems[0]);
                }
            }
        });

        includeToOutput = new JCheckBox("Enabled");

        model = new DefaultListModel();
        itemsList = new JList(model);

        itemsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        itemsList.setVisibleRowCount(-1);
        setLayout(new GridBagLayout());
        setLeafEditPanel();
    }

    public void showEditFields(DefaultMutableTreeNode node, JTree tree){
        this.node = node;
        this.tree = tree;
        Object object = node.getUserObject();
        textFieldNode = (TextFieldNode) object;
        includeToOutput.setSelected(textFieldNode.isIncluded());
        nameLabel.setText(textFieldNode.getAttribute());
        if (node.isLeaf()){
            setLeafEditPanel();
            dataField.setText(textFieldNode.getText());
        }
        else {
            setListEditPanel();
            TreeModel treeModel = tree.getModel();
            model.removeAllElements();
            int childCount = treeModel.getChildCount(node);
            for (int i = 0; i < childCount; i++) {
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) (treeModel.getChild(node, i));
                Object userObject = treeNode.getUserObject();
                if (userObject instanceof TextFieldNode) {
//                    nodeElements.add(treeNode);
                    TextFieldNode childNode = (TextFieldNode) userObject;
                    model.addElement(childNode.getAttribute());
                }
            }
        }
    }

    private void removeComponents(){
        for (Component component: getComponents()) {
            remove(component);
            revalidate();
        }
    }

    private void setLeafEditPanel() {
        removeComponents();
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.CENTER;
        add(nameLabel, gc);

        gc.gridy = 1;
        gc.anchor = GridBagConstraints.CENTER;
        add(includeToOutput, gc);

        gc.gridy = 2;
        gc.anchor = GridBagConstraints.CENTER;
        add(dataField, gc);

        gc.weighty = 1;

        gc.gridy = 3;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(10, 0, 0, 0);
        add(okButton, gc);
        repaint();
    }

    private void setListEditPanel(){
        removeComponents();
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridwidth = 6;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        add(nameLabel, gc);

        gc.gridwidth = 6;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridy = 1;
        add(includeToOutput, gc);

        gc.gridwidth = 1;
        gc.gridx = 0;
        gc.gridy = 2;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(0, 5 , 5 ,5);
        add(addButton, gc);

        gc.gridwidth = 1;
        gc.gridx = 0;
        gc.gridy = 3;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(0, 5 , 0 ,5);
        add(removeButton, gc);


        gc.weightx = 0.4;
        gc.gridheight = 3;
        gc.gridx = 1;
        gc.gridy = 2;
        gc.fill = GridBagConstraints.HORIZONTAL;
//        gc.anchor = GridBagConstraints.PAGE_START;
        JScrollPane scrollPane = new JScrollPane(itemsList);
        scrollPane.setPreferredSize(new Dimension(200,120));
        add(scrollPane, gc);

        gc.gridwidth = 1;
        gc.weighty = 1;
        gc.gridy = 6;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(5, 0 , 0 ,0);
        add(okButton, gc);
        repaint();
    }

}
