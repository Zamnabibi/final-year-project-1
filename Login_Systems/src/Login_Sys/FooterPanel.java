

package Login_Sys;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class FooterPanel extends JPanel {
    public FooterPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.");
        add(footerLabel);
    }
}
