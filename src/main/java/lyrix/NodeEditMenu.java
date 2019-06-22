package lyrix;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class NodeEditMenu extends JPanel {
    private final MainFrame mainFrame;
    private JLabel nameLabel;
    private JTextField dataField;
    private JCheckBox includeToOutput;
    private JButton okButton;
    private JButton addButton;
    private JButton removeButton;
    private JList<String> itemsList;
    private DefaultMutableTreeNode node;
    private JTree tree;
    private TextFieldNode textFieldNode;
    private DefaultListModel<String> model;

    private void updateTreeModel(){
        ((DefaultTreeModel) tree.getModel()).reload();
        mainFrame.expandTree(true);
    }

    NodeEditMenu(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        nameLabel = new JLabel("Node name");
        nameLabel.setFont(nameLabel.getFont().deriveFont(20.0f));
        dataField = new JTextField(15);

        removeButton = ButtonFactory.makeButton("-", new ActionListener() {
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

        addButton = ButtonFactory.makeButton("+", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (node != null) {
                    String atribute = textFieldNode.getAttribute().toLowerCase();
                    if (atribute.contains("fields")){
                        TextFieldNode fieldToAdd = new TextFieldNode("item", "", true);
                        DefaultMutableTreeNode nodeToAdd = new DefaultMutableTreeNode(fieldToAdd);
                        nodeToAdd.add(new DefaultMutableTreeNode(new TextFieldNode("MName", "", true)));
                        nodeToAdd.add(new DefaultMutableTreeNode(new TextFieldNode("MLabel", "", true)));
                        nodeToAdd.add(new DefaultMutableTreeNode(new TextFieldNode("MType", "", true)));
                        nodeToAdd.add(new DefaultMutableTreeNode(new TextFieldNode("subType", "", true)));
                        nodeToAdd.add(new DefaultMutableTreeNode(new TextFieldNode("MValue", "", true)));
                        node.add(nodeToAdd);
                        makeNodeEnabled(nodeToAdd, ((TextFieldNode)node.getUserObject()).isIncluded());
                        updateTreeModel();
                        model.addElement(fieldToAdd.getDefaultString());
                    }
                    else if (atribute.contains("accessLevels")){
                        TextFieldNode fieldToAdd = new TextFieldNode("item", "", true);
                        DefaultMutableTreeNode nodeToAdd = new DefaultMutableTreeNode(fieldToAdd);

                        DefaultMutableTreeNode idNode = new DefaultMutableTreeNode(new TextFieldNode("id", "", true));
                        idNode.add(new DefaultMutableTreeNode(new TextFieldNode("additionalID", "", true)));
                        idNode.add(new DefaultMutableTreeNode(new TextFieldNode("primaryID", "", true)));
                        idNode.add(new DefaultMutableTreeNode(new TextFieldNode("systemID", "", true)));

                        nodeToAdd.add(idNode);

                        nodeToAdd.add(new DefaultMutableTreeNode(new TextFieldNode("label", "", true)));

                        makeNodeEnabled(nodeToAdd, ((TextFieldNode)node.getUserObject()).isIncluded());

                        node.add(nodeToAdd);
                        updateTreeModel();
                        model.addElement(fieldToAdd.getDefaultString());
                    }
                }
            }
        });

        okButton = ButtonFactory.makeButton("OK", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (node != null) {
                    if (textFieldNode.getAttribute().equals("accessLevels") || textFieldNode.getAttribute().equals("fields") || node.isLeaf()) {
                        TextFieldNode fieldNode = new TextFieldNode(textFieldNode.getAttribute(), node.isLeaf() ? dataField.getText() : "", includeToOutput.isSelected());
                        textFieldNode = fieldNode;
                        node.setUserObject(fieldNode);
                        if (textFieldNode.getAttribute().equals("MName")) {
                            DefaultMutableTreeNode temp = (DefaultMutableTreeNode) node.getParent();
                            if (temp.getUserObject() instanceof TextFieldNode) {
                                ((TextFieldNode)temp.getUserObject()).setText(dataField.getText());
                            }
                        }
                        else if (textFieldNode.getAttribute().equals("primaryID")) {
                            DefaultMutableTreeNode temp = (DefaultMutableTreeNode) node.getParent().getParent();
                            if (temp.getUserObject() instanceof TextFieldNode) {
                                TextFieldNode itemNode = (TextFieldNode) temp.getUserObject();
                                if (itemNode.getAttribute().equals("item")) {
                                    itemNode.setText(dataField.getText());
                                }
                            }
                        }
                    }
                    makeNodeEnabled(node, includeToOutput.isSelected());
                    updateTreeModel();
                }
            }
        });


        includeToOutput = new JCheckBox("Enabled");

        model = new DefaultListModel<>();
        itemsList = new JList<>(model);

        itemsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        itemsList.setVisibleRowCount(-1);
        setLayout(new GridBagLayout());
        setLeafEditPanel();
    }

    void showEditFields(DefaultMutableTreeNode node, JTree tree){
        this.node = node;
        this.tree = tree;
        Object object = node.getUserObject();
        textFieldNode = (TextFieldNode) object;
        includeToOutput.setSelected(textFieldNode.isIncluded());
        nameLabel.setText(textFieldNode.getAttribute());

        if (textFieldNode.getAttribute().equals("accessLevels") || textFieldNode.getAttribute().equals("fields")){
            setListEditPanel();
            TreeModel treeModel = tree.getModel();
            model.removeAllElements();
            int childCount = treeModel.getChildCount(node);
            for (int i = 0; i < childCount; i++) {
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) (treeModel.getChild(node, i));
                Object userObject = treeNode.getUserObject();
                if (userObject instanceof TextFieldNode) { //todo нужна ли проверка?
//                    nodeElements.add(treeNode);
                    TextFieldNode childNode = (TextFieldNode) userObject;
                    model.addElement(childNode.getDefaultString());
                }
            }
        }
        else if (node.isLeaf()){
            setLeafEditPanel();
            dataField.setText(textFieldNode.getText());
        }
        else {
            setDefaultEditPanel();
            includeToOutput.setSelected(textFieldNode.isIncluded());
            nameLabel.setText(textFieldNode.getAttribute());
        }

    }

    private void makeNodeEnabled(DefaultMutableTreeNode node, boolean enable) {
        TextFieldNode leafNode = (TextFieldNode) node.getUserObject();
        leafNode.setIncluded(enable);
        updateTreeModel();
        if (!node.isLeaf()){
            TreeModel treeModel = tree.getModel();
            int childCount = treeModel.getChildCount(node);
            for (int i = 0; i < childCount; i++) {
                makeNodeEnabled((DefaultMutableTreeNode)(treeModel.getChild(node, i)), enable);
            }
        }
    }

    private void removeComponents(){
        for (Component component: getComponents()) {
            remove(component);
            revalidate();
        }
    }

    private void setDefaultEditPanel(){
        removeComponents();
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridy = 0;
        gc.weightx = 1;
        gc.anchor = GridBagConstraints.CENTER;
        add(nameLabel, gc);

        gc.gridy = 1;
        gc.weightx = 1;
        gc.anchor = GridBagConstraints.CENTER;
        add(includeToOutput, gc);

        gc.gridy = 2;
        gc.weighty = 1;
        gc.weightx = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        add(okButton, gc);
        repaint();
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
        JScrollPane scrollPane = new JScrollPane(itemsList);
        add(scrollPane, gc);
        scrollPane.setPreferredSize(new Dimension(200,120));

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
