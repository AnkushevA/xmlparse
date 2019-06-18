package lyrix;

import sun.reflect.generics.tree.Tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;


class CheckBoxNodeEditor extends AbstractCellEditor implements TreeCellEditor {

    private boolean isLeaf;
    private JTree tree;
    private CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
    private TreeMenu parentPanel;


    public CheckBoxNodeEditor(JTree tree, TreeMenu parentPanel) {
        this.tree = tree;
        this.parentPanel = parentPanel;
    }

    //возвращает значение, которое находится в узле
    public Object getCellEditorValue() {
        JLabel textField = renderer.getLeafRenderer();
        return new TextFieldNode(textField.getText());
        /*if (isLeaf) {
            JLabel textField = renderer.getLeafRenderer();
            return new TextFieldNode(textField.getText());
        }

        else { //проверить все случаи
            JCheckBox checkbox = renderer.getNodeRenderer();
            return new CheckBoxNode(checkbox.getText(), checkbox.isSelected());
        }*/
    }

    //какие узлы можно редактироватьы
    public boolean isCellEditable(EventObject event) {
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
            if (path != null) {
                Object node = path.getLastPathComponent();
                if (node instanceof DefaultMutableTreeNode) {
                    DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
                    if (treeNode.isRoot() || tree.getModel().getIndexOfChild(tree.getModel().getRoot(), treeNode) != -1){
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    //вернуть узел
    public Component getTreeCellEditorComponent(final JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row) {
        Component editor = renderer.getTreeCellRendererComponent(tree, value, true, expanded, leaf, row, true);

        if (editor instanceof JLabel){
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            parentPanel.showEditFields(node, tree);
        }
        return editor;
    }
}