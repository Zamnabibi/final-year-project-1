

package com.bbms.ui;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class FooterPanel extends JPanel {
    public FooterPanel() {
    	setBackground(Color.PINK);
        setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.");
        add(footerLabel);
    }
}
