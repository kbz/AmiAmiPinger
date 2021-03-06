package com.fteams.pinger.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AboutFrame extends JFrame implements ActionListener
{
    public AboutFrame()
    {
        setTitle("About AmiAmiPing project...");
        setSize(450, 400);
        String about = "This program has been brought to you by kb_z\n" +
                "under the GNU GPL license, you are allowed to\n" +
                "use any parts of this code for non-commercial\n" +
                "purposes :P";
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
