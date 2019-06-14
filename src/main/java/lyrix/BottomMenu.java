package lyrix;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class BottomMenu extends JPanel {

//    private

    public BottomMenu()
    {

        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel statusLabel = new JLabel("status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(statusLabel);
    }

}
