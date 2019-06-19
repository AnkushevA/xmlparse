package lyrix;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

class CheckBoxNodeRenderer implements TreeCellRenderer {
    //для листьев, объект, который будет отрисован
    private JLabel leafRenderer = new JLabel();

    private Object object;
    //для других элементов
    private DefaultTreeCellRenderer nonLeafRenderer = new DefaultTreeCellRenderer();

    /*private Color selectionBorderColor, selectionForeground, selectionBackground,
            textForeground, textBackground;*/

    public Object getObject() {
        return object;
    }

    public JLabel getLeafRenderer() {
        return leafRenderer;
    }

    //параметры отрисовки

    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean selected, boolean expanded, boolean leaf, int row,
                                                  boolean hasFocus) {
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
        if (treeNode.isRoot() || tree.getModel().getIndexOfChild(tree.getModel().getRoot(), treeNode) != -1) {
            object = value;
            return nonLeafRenderer.getTreeCellRendererComponent(tree,
                    value, selected, expanded, leaf, row, hasFocus);
        }
        else {
            Object userObject = treeNode.getUserObject();
            TextFieldNode node = (TextFieldNode) userObject;
            object = node;
            leafRenderer.setText(node.toString());
            return leafRenderer;
        }
    }
}
