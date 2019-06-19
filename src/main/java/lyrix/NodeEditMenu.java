package lyrix;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.soap.Text;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NodeEditMenu extends JPanel {
    private JLabel nameLabel;
    private JTextField dataField;
    private JCheckBox includeToOutput;
    private JButton okButton;
//    private JButton cancelButton;
    private JButton addButton;
    private JButton removeButton;
    private DefaultMutableTreeNode node;
    private UpdateTreeListener updateTreeListener;
    private JList itemsList;

    public void setUpdateTreeListener(UpdateTreeListener updateTreeListener) {
        this.updateTreeListener = updateTreeListener;
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
                    TextFieldNode textFieldNode = (TextFieldNode)node.getUserObject();
                    TextFieldNode fieldNode = new TextFieldNode(textFieldNode.getAttribute(), dataField.getText(), includeToOutput.isSelected());
                    node.setUserObject(fieldNode);
                    updateTreeListener.updateTree(node);
                }
            }
        });

//        cancelButton = new JButton("Cancel");

        includeToOutput = new JCheckBox("Enabled");

        DefaultListModel model = new DefaultListModel();
        itemsList = new JList(model);

        itemsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        itemsList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        itemsList.setVisibleRowCount(-1);

        model.addElement(111);
        setLayout(new GridBagLayout());
        setLeafEditPanel();
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
//        gc.anchor = GridBagConstraints.PAGE_START;
        add(dataField, gc);

        gc.weightx = 1;
        gc.weighty = 1;

        gc.gridx = 0;
        gc.gridy = 3;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(10, 0, 0, 0);
        add(okButton, gc);


/*        gc.weightx = 1;
        gc.weighty = 2;

        gc.gridx = 1;
        gc.gridy = 5;
        gc.anchor = GridBagConstraints.PAGE_START;
        add(cancelButton, gc);*/
    }

    private void setListEditPanel(){
        removeComponents();
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

        gc.weightx = 0;
        gc.weighty = 0;
        gc.gridx = 0;
        gc.gridy = 2;
//        gc.anchor = GridBagConstraints.;
        add(addButton, gc);


        gc.weightx = 1;
        gc.weighty = 0;
        gc.gridx = 1;
        gc.gridy = 2;
        JScrollPane scrollPane = new JScrollPane(itemsList);
        scrollPane.setPreferredSize(new Dimension(200,120));
        add(scrollPane, gc);

        gc.weightx = 1;
        gc.weighty = 1;

        gc.gridx = 0;
        gc.gridy = 3;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.insets = new Insets(10, 0, 0, 0);
        add(okButton, gc);
    }

    public void showEditFields(DefaultMutableTreeNode node, JTree tree){
        this.node = node;
        if (node.isLeaf()){
            setLeafEditPanel();
            TextFieldNode textFieldNode = (TextFieldNode)node.getUserObject();
            nameLabel.setText(textFieldNode.getAttribute());
            dataField.setText(textFieldNode.getText());
            includeToOutput.setSelected(textFieldNode.isIncluded());
        }
        else{
            setListEditPanel();
        }
    }

}
