package lyrix;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class NodeEditMenu extends JPanel {
    private JTextField name;
    private NodeEditorListener nodeEditorListener;

    public void setNodeEditorListener(NodeEditorListener nodeEditorListener) {
        this.nodeEditorListener = nodeEditorListener;
    }

    public NodeEditMenu() {
        name = new JTextField("name");
//        setLayout(new );

    }

    public void showEditFields(DefaultMutableTreeNode node, JTree tree){

    }
}
