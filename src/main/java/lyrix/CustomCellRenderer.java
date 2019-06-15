package lyrix;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class CustomCellRenderer implements TreeCellRenderer {
    private JCheckBox checkBox = new JCheckBox();
    private JTextField textField = new JTextField();

    private DefaultTreeCellRenderer defaultTreeCellRenderer = new DefaultTreeCellRenderer();

    public CustomCellRenderer() {
        defaultTreeCellRenderer.add(checkBox);
        defaultTreeCellRenderer.add(textField);
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
                                                  boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component returnValue = null;
        if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
            Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
            if (userObject instanceof CustomNode) {
                CustomNode e = (CustomNode) userObject;

                checkBox.setSelected(e.isChecked());

                textField.setText(e.getText());

                defaultTreeCellRenderer.setEnabled(tree.isEnabled());
                returnValue = defaultTreeCellRenderer;
            }
        }
        if (returnValue == null) {
            returnValue = defaultTreeCellRenderer.getTreeCellRendererComponent(tree, value, selected, expanded,
                    leaf, row, hasFocus);
        }
        return returnValue;
    }
}
