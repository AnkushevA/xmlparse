package lyrix;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.*;


class NodeEditMenu extends JPanel implements LeftMenuUpdateListener {
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
    private DefaultListModel<String> model;

    NodeEditMenu(MainFrame mainFrame, JTree tree) {
        this.tree = tree;
        this.mainFrame = mainFrame;
        nameLabel = new JLabel("Node name");
        nameLabel.setFont(nameLabel.getFont().deriveFont(20.0f));
        dataField = new JTextField(15);

        removeButton = ButtonFactory.makeButton("-", actionEvent -> {
            int[] selectedItems = itemsList.getSelectedIndices();
            if (selectedItems.length == 1) {
                node.remove(selectedItems[0]);

                updateTreeModel();
                model.removeElementAt(selectedItems[0]);
            }
        });

        addButton = ButtonFactory.makeButton("+", actionEvent -> {
            if (node != null) {
                TextFieldNode textFieldNode = ((TextFieldNode) node.getUserObject());
                String attribute = textFieldNode.getAttribute();
                boolean isEnabled = ((TextFieldNode) node.getUserObject()).isIncluded();

                TextFieldNode fieldToAdd = new TextFieldNode("item", "", isEnabled);
                DefaultMutableTreeNode nodeToAdd = null;

                if (attribute.equals("fields")) {
                    nodeToAdd = makeFieldsNode(fieldToAdd, isEnabled);
                } else if (attribute.equals("accessLevels")) {
                    nodeToAdd = makeAccessLevelsNode(fieldToAdd, isEnabled);
                }

                if (nodeToAdd != null) {
                    DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
                    treeModel.insertNodeInto(nodeToAdd, node, node.getChildCount());
                    model.addElement(fieldToAdd.getDefaultString());
                }
            }
        });

        okButton = ButtonFactory.makeButton("OK", actionEvent -> {
            if (node != null) {
                TextFieldNode textFieldNode = ((TextFieldNode) node.getUserObject());
                if (textFieldNode.getAttribute().equals("accessLevels") || textFieldNode.getAttribute().equals("fields") || node.isLeaf()) {
                    textFieldNode.setText(node.isLeaf() ? dataField.getText() : "");
                    textFieldNode.setIncluded(includeToOutput.isSelected());
                    if (textFieldNode.getAttribute().equals("MName")) {
                        DefaultMutableTreeNode temp = (DefaultMutableTreeNode) node.getParent();
                        ((TextFieldNode) temp.getUserObject()).setText(dataField.getText());
                    } else if (textFieldNode.getAttribute().equals("primaryID")) {
                        DefaultMutableTreeNode temp = (DefaultMutableTreeNode) node.getParent().getParent();
                        TextFieldNode itemNode = (TextFieldNode) temp.getUserObject();
                        if (itemNode.getAttribute().equals("item")) {
                            itemNode.setText(dataField.getText());
                        }
                    }
                }
                makeNodeEnabled(node, includeToOutput.isSelected());
                updateTreeModel();
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

    public void setTree(JTree tree) {
        this.tree = tree;
    }

    @Override
    public void update(String xmlPath) {
        setDefaultState();
    }

    private void setDefaultState() {
        setLeafEditPanel();
        nameLabel.setText("Node name");
        dataField.setText("");
        includeToOutput.setSelected(false);
        model.removeAllElements();
        node = null;
    }

    private void updateTreeModel() {
        ((DefaultTreeModel) tree.getModel()).reload();
        mainFrame.expandTree(true);
    }

    private DefaultMutableTreeNode makeFieldsNode(TextFieldNode fieldToAdd, boolean isEnabled) {
        DefaultMutableTreeNode nodeToAdd = new DefaultMutableTreeNode(fieldToAdd);
        nodeToAdd.add(new DefaultMutableTreeNode(new TextFieldNode("MName", "", isEnabled)));
        nodeToAdd.add(new DefaultMutableTreeNode(new TextFieldNode("MLabel", "", isEnabled)));
        nodeToAdd.add(new DefaultMutableTreeNode(new TextFieldNode("MType", "", isEnabled)));
        nodeToAdd.add(new DefaultMutableTreeNode(new TextFieldNode("subType", "", isEnabled)));
        nodeToAdd.add(new DefaultMutableTreeNode(new TextFieldNode("MValue", "", isEnabled)));
        return nodeToAdd;
    }

    private DefaultMutableTreeNode makeAccessLevelsNode(TextFieldNode fieldToAdd, boolean isEnabled) {
        DefaultMutableTreeNode nodeToAdd = new DefaultMutableTreeNode(fieldToAdd);
        DefaultMutableTreeNode idNode = new DefaultMutableTreeNode(new TextFieldNode("id", "", isEnabled));
        idNode.add(new DefaultMutableTreeNode(new TextFieldNode("additionalID", "", isEnabled)));
        idNode.add(new DefaultMutableTreeNode(new TextFieldNode("primaryID", "", isEnabled)));
        idNode.add(new DefaultMutableTreeNode(new TextFieldNode("systemID", "", isEnabled)));
        nodeToAdd.add(idNode);
        nodeToAdd.add(new DefaultMutableTreeNode(new TextFieldNode("label", "", isEnabled)));
        return nodeToAdd;
    }

    void showEditFields(DefaultMutableTreeNode node) {
        this.node = node;
        TextFieldNode textFieldNode = (TextFieldNode) node.getUserObject();
        includeToOutput.setSelected(textFieldNode.isIncluded());
        nameLabel.setText(textFieldNode.getAttribute());

        if (textFieldNode.getAttribute().equals("accessLevels") || textFieldNode.getAttribute().equals("fields")) {
            setListEditPanel();
            TreeModel treeModel = tree.getModel();
            model.removeAllElements();
            int childCount = treeModel.getChildCount(node);
            for (int i = 0; i < childCount; i++) {
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) (treeModel.getChild(node, i));
                Object userObject = treeNode.getUserObject();
                TextFieldNode childNode = (TextFieldNode) userObject;
                model.addElement(childNode.getDefaultString());
            }
        } else if (node.isLeaf()) {
            setLeafEditPanel();
            dataField.setText(textFieldNode.getText());
        } else {
            setDefaultEditPanel();
            includeToOutput.setSelected(textFieldNode.isIncluded());
            nameLabel.setText(textFieldNode.getAttribute());
        }

    }

    private void makeNodeEnabled(DefaultMutableTreeNode node, boolean enable) {
        TextFieldNode leafNode = (TextFieldNode) node.getUserObject();
        leafNode.setIncluded(enable);
        if (!node.isLeaf()) {
            TreeModel treeModel = tree.getModel();
            int childCount = treeModel.getChildCount(node);
            for (int i = 0; i < childCount; i++) {
                makeNodeEnabled((DefaultMutableTreeNode) (treeModel.getChild(node, i)), enable);
            }
        }
    }

    private void removeComponents() {
        for (Component component : getComponents()) {
            remove(component);
            revalidate();
        }
    }

    private void setDefaultEditPanel() {
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

    private void setListEditPanel() {
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
        gc.insets = new Insets(0, 5, 5, 5);
        add(addButton, gc);

        gc.gridwidth = 1;
        gc.gridx = 0;
        gc.gridy = 3;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(0, 5, 0, 5);
        add(removeButton, gc);


        gc.weightx = 0.4;
        gc.gridheight = 3;
        gc.gridx = 1;
        gc.gridy = 2;
        gc.fill = GridBagConstraints.HORIZONTAL;
        JScrollPane scrollPane = new JScrollPane(itemsList);
        add(scrollPane, gc);
        scrollPane.setPreferredSize(new Dimension(200, 120));

        gc.gridwidth = 1;
        gc.weighty = 1;
        gc.gridy = 6;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(5, 0, 0, 0);
        add(okButton, gc);
        repaint();
    }
}
