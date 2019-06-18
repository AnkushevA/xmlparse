package lyrix;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

class CheckBoxNodeRenderer implements TreeCellRenderer {
    //для листьев, объект, который будет отрисован
    private JLabel leafRenderer = new JLabel();
    //для всех элементов, кроме листьев
    private JCheckBox nodeRenderer = new JCheckBox();

    //для других элементов
    private DefaultTreeCellRenderer nonLeafRenderer = new DefaultTreeCellRenderer();

    private Color selectionBorderColor, selectionForeground, selectionBackground,
            textForeground, textBackground;

    protected JLabel getLeafRenderer() {
        return leafRenderer;
    }

    protected JCheckBox getNodeRenderer() {
        return nodeRenderer;
    }

    //параметры отрисовки
    public CheckBoxNodeRenderer() {
        Font fontValue;

        fontValue = UIManager.getFont("Tree.font");
        if (fontValue != null) {
            nodeRenderer.setFont(fontValue);
        }
        Boolean booleanValue = (Boolean) UIManager.get("Tree.drawsFocusBorderAroundIcon");
        nodeRenderer.setFocusPainted((booleanValue != null) && (booleanValue.booleanValue()));

        selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
        selectionForeground = UIManager.getColor("Tree.selectionForeground");
        selectionBackground = UIManager.getColor("Tree.selectionBackground");
        textForeground = UIManager.getColor("Tree.textForeground");
        textBackground = UIManager.getColor("Tree.textBackground");
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean selected, boolean expanded, boolean leaf, int row,
                                                  boolean hasFocus) {
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
        if (treeNode.isRoot() || tree.getModel().getIndexOfChild(tree.getModel().getRoot(), treeNode) != -1) {
            return nonLeafRenderer.getTreeCellRendererComponent(tree,
                    value, selected, expanded, leaf, row, hasFocus);
        } /*else if (!leaf) {
            if (selected) {
                nodeRenderer.setForeground(selectionForeground);
                nodeRenderer.setBackground(selectionBackground);
            } else {
                nodeRenderer.setForeground(textForeground);
                nodeRenderer.setBackground(textBackground);
            }

            Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
            if (userObject instanceof CheckBoxNode) { //todo redundant
                CheckBoxNode node = (CheckBoxNode) userObject;
                nodeRenderer.setText(node.getText());
                nodeRenderer.setSelected(node.isSelected());
            }
            return nodeRenderer;
            }*/
        else {
            String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, false);
            leafRenderer.setText(stringValue);

            Object userObject = treeNode.getUserObject();
            if (userObject instanceof TextFieldNode) {
                TextFieldNode node = (TextFieldNode) userObject;
                leafRenderer.setText(node.getText());
            }
            return leafRenderer;
        }


    }
}
