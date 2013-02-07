/* Copyright 2012 Sabre Holdings */
package com.fteams.pinger.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AboutFrame extends JFrame implements ActionListener
{
    String about = "This program has been brought to you by kb_z\n under the GNU GPL license," +
            "you are allowed to use any parts of this\n code for non-commercial purposes :P";
    public AboutFrame()
    {
        setTitle("About Pinger...");
        setSize(450, 400);
        JTextArea field = new JTextArea(about);
        field.setEditable(false);
        add(field, BorderLayout.CENTER);
        JButton close = new JButton("OK");
        add(close, BorderLayout.SOUTH);
        close.addActionListener(this);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        setVisible(false);
    }
}
