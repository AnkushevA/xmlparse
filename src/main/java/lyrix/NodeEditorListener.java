package lyrix;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public interface NodeEditorListener {
    public void showEditFields(DefaultMutableTreeNode node, JTree tree);
}
