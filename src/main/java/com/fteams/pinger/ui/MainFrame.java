package com.fteams.pinger.ui;

import com.fteams.pinger.PropertyLoader;
import com.fteams.pinger.applied.AmiAmiPing;
import com.fteams.pinger.base.PingBase;
import com.fteams.pinger.base.PingPropertyUpdater;

import static com.fteams.pinger.app.Main.CONFIG_PATH;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("unchecked")
public class MainFrame extends JFrame implements ActionListener
{
    private PingBase pingBase;
    private PingPropertyUpdater updater;

    private final DefaultListModel modelAvailable;
    private final DefaultListModel modelUnavailable;
    private PropertyLoader loader;

    private final JTextArea consoleOutput;
    private ConfigurationFrame config_frame;
    private AboutFrame about_frame;

    public MainFrame() {
        JMenuBar menuBar = new JMenuBar();

        JPanel panelContainer = new JPanel();
        JPanel codesPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        consoleOutput = new JTextArea();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(this);

        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem settingsItem = new JMenuItem("Settings");
        settingsItem.addActionListener(this);

        JMenu infoMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(this);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(this);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(this);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        modelAvailable = new DefaultListModel();
        modelUnavailable = new DefaultListModel();
        JList availableList = new JList(modelAvailable);
        JList unavailableList = new JList(modelUnavailable);

        JScrollPane availableItemPanel = new JScrollPane(availableList);
        JScrollPane unavailableItemPanel = new JScrollPane(unavailableList);
        availableItemPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Available Items"));
        unavailableItemPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Unavailable Items"));

        codesPanel.add(unavailableItemPanel);
        codesPanel.add(availableItemPanel);

        codesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Lookup IDs"));
        codesPanel.setLayout(new GridLayout(1, 2));

        JScrollPane consolePanel = new JScrollPane(consoleOutput);
        consolePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Console output"));
        consolePanel.getVerticalScrollBar().setUnitIncrement(25);
        consoleOutput.setEditable(false);
        consoleOutput.setWrapStyleWord(true);
        consoleOutput.setLineWrap(true);

        fileMenu.add(exitItem);
        toolsMenu.add(settingsItem);
        infoMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        menuBar.add(infoMenu);

        setTitle("AmiAmi PingBase Interface");

        setJMenuBar(menuBar);

        panelContainer.setLayout(new GridLayout(2, 1));
        panelContainer.add(codesPanel);


        panelContainer.add(consolePanel);

        add(panelContainer, BorderLayout.CENTER);

        buttonPanel.add(startButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setSize(new Dimension(450, 400));
//        pack();
        setVisible(true);
//        setResizable(false);
    }
    public void updateConsole(String text)
    {
        consoleOutput.setText(text+"\n"+consoleOutput.getText());
    }
    public void updateListContent()
    {
        List<String> avFigs = pingBase.getAvailableItems();
        List<String> unFigs = pingBase.getUnavailableItems();
        modelAvailable.removeAllElements();
        for (String item : avFigs)
        {
            modelAvailable.addElement(item);
        }
        modelUnavailable.removeAllElements();
        for (String item : unFigs)
        {
            modelUnavailable.addElement(item);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand().toLowerCase();
        if (command.equals("settings"))
        {
            if (config_frame != null)
            {
                config_frame.setVisible(true);
            }
            else
                try
                {
                    config_frame = new ConfigurationFrame();
                } catch (IOException e1)
                {
                    e1.printStackTrace();
                }
        }
        else if (command.equals("exit"))
        {
            System.exit(0);
        }
        else if (command.equals("about"))
        {

            if (about_frame != null)
            {
                about_frame.setVisible(true);
            }
            else
            {
                about_frame = new AboutFrame();
            }
        }
        else if (command.equals("start"))
        {
            if (loader == null)
            {
                loader = new PropertyLoader();
                try
                {
                    loader.loadProperties(new File(CONFIG_PATH));
                } catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (pingBase == null)
            {
                pingBase = new AmiAmiPing();
                pingBase.setUserInterface(this);
                pingBase.loadProperties(loader);
                pingBase.refreshConfig(loader);
                pingBase.start();
            }
            if (updater == null)
            {
                updater = new PingPropertyUpdater(loader, pingBase, this);
                updater.start();
            }
        }
        else if (command.equals("refresh"))
        {
            try
            {
                if (pingBase == null)
                {
                    updateConsole("Press START before attempting to refresh.");
                    return;
                }
                loader.loadProperties(new File(CONFIG_PATH));
            } catch (IOException e1)
            {
                e1.printStackTrace();
            }
            if (pingBase.refreshConfig(loader))
            {
                updateConsole("Successfully updated the configuration.");
            }

        }
    }
}
