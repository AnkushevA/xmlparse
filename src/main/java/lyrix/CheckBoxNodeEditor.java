package lyrix;

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

    public CheckBoxNodeEditor(JTree tree) {
        this.tree = tree;
    }

    //возвращает значение, которое находится в узле
    public Object getCellEditorValue() {
        if (isLeaf) {
            JLabel textField = renderer.getLeafRenderer();
            return new TextFieldNode(textField.getText());
        }
        else {
            JCheckBox checkbox = renderer.getNodeRenderer();
            return new CheckBoxNode(checkbox.getText(), checkbox.isSelected());
        }
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
                    Object userObject = treeNode.getUserObject();
                    isLeaf = userObject instanceof TextFieldNode;
                    return true;
                }
            }
        }
        return false;
    }

    //вернуть узел
    public Component getTreeCellEditorComponent(final JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row) {
        Component editor = renderer.getTreeCellRendererComponent(tree, value, true, expanded, leaf, row, true);

        if (editor instanceof JCheckBox) {
            ((JCheckBox) editor).addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent itemEvent) {
                    if (stopCellEditing()) {
                        fireEditingStopped();
                    }
                }
            });
        }
        else if (editor instanceof JLabel && leaf){
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            String text = (String)JOptionPane.showInputDialog(null, node.getParent().toString() + ":");
            if (text != null && !text.equals("")){
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
                treeNode.setUserObject(new TextFieldNode(text));
                DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                model.reload();
            }
        }
        return editor;
    }
}