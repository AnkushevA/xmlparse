package lyrix;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LeftMenu extends JPanel {

    private JList itemsList;
    private DefaultListModel items;

    public LeftMenu(){
         items = new DefaultListModel();
         itemsList = new JList(items);

         items.addElement("exampleasdsadsadsad");
         items.addElement("exampleasdsadsadsad");
         items.addElement("exampleasdsadsadsad");
         items.addElement("exampleasdsadsadsad");

         itemsList.addMouseListener(new MouseAdapter() {
             @Override
             public void mouseClicked(MouseEvent mouseEvent) {
                 /*String selectedValue = (String) itemsList.getSelectedValue();
                 items.addElement("asddddddddasdsadsadsaddsad");*/
             }
         });

         add(itemsList);
    }

}
