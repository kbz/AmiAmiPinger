/* Copyright 2012 Sabre Holdings */
package com.fteams.pinger.ui;

import com.fteams.pinger.PropertyLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationFrame extends JFrame implements ActionListener
{
    String CONFIG_PATH;
    JButton updateButton;
    JButton cancelButton;
    JTabbedPane panelContainer;
    JPanel buttonPanel;
    JPanel basicSettingsPanel;
    JPanel proxySettingsPanel;
    JTextField timerField;
    JTextField baseURLField;
    JTextField searchStringField;
    JTextField itemListField;
    JCheckBox proxyEnabledCB;
    JCheckBox userAuthEnabledCB;
    JTextField proxyHostField;
    JTextField proxyPortField;
    JTextField proxyUserNameField;
    JLabel proxyPasswordLabel;
    JLabel timerLabel;
    JLabel baseURLLabel;
    JLabel searchStringLabel;
    JLabel itemListLabel;
    JLabel proxyEnabledLabel;
    JLabel userAuthEnabledLabel;
    JLabel proxyHostLabel;
    JLabel proxyPortLabel;
    JLabel proxyUserNameLabel;
    JPasswordField proxy_password;

    private PropertyLoader loader;

    public ConfigurationFrame(String configFilePath) throws IOException
    {
        this.CONFIG_PATH = configFilePath;
        loader = new PropertyLoader();
        loader.loadProperties(new File(configFilePath));
        panelContainer = new JTabbedPane();
        basicSettingsPanel = new JPanel();
        timerLabel = new JLabel("Time between update calls:");
        timerField = new JTextField(new Long(loader.getPingTimer()).toString());
        timerField.setColumns(10);
        baseURLLabel = new JLabel("Base Look-up URL:");
        baseURLField = new JTextField(loader.getBaseUrl());
        baseURLField.setColumns(10);
        searchStringLabel = new JLabel("Matching string:");
        searchStringField = new JTextField(loader.getSearchString());
        searchStringField.setColumns(10);
        itemListLabel = new JLabel("Item Codes (CSV format):");
        itemListField = new JTextField(loader.getCodes());
        itemListField.setColumns(10);

        basicSettingsPanel.setLayout(new GridLayout(4,2));
        basicSettingsPanel.add(timerLabel);
        basicSettingsPanel.add(timerField);
        basicSettingsPanel.add(baseURLLabel);
        basicSettingsPanel.add(baseURLField);
        basicSettingsPanel.add(searchStringLabel);
        basicSettingsPanel.add(searchStringField);
        basicSettingsPanel.add(itemListLabel);
        basicSettingsPanel.add(itemListField);

        proxySettingsPanel = new JPanel();
        proxyEnabledLabel = new JLabel("Enable proxy:");
        proxyEnabledCB = new JCheckBox();
        proxyEnabledCB.setSelected(loader.getProxyInformation() != null && loader.getProxyInformation().proxy_enabled);

        proxyHostLabel = new JLabel("Proxy host:");
        proxyHostField = new JTextField(loader.getProxyInformation().host);
        proxyPortLabel = new JLabel("Proxy port:");
        proxyPortField = new JTextField(loader.getProxyInformation().port);

        userAuthEnabledLabel = new JLabel("Enable user authentication:");
        userAuthEnabledCB = new JCheckBox();
        userAuthEnabledCB.setSelected(loader.getProxyInformation() != null && loader.getProxyInformation().user_auth);

        proxyUserNameLabel = new JLabel("Username:");
        proxyUserNameField = new JTextField(loader.getProxyInformation().p_username);
        proxyPasswordLabel = new JLabel("Password:");
        proxy_password = new JPasswordField();
        proxy_password.setText(loader.getProxyInformation().p_password);

        proxySettingsPanel.setLayout(new GridLayout(6,2));
        proxySettingsPanel.add(proxyEnabledLabel);
        proxySettingsPanel.add(proxyEnabledCB);
        proxySettingsPanel.add(proxyHostLabel);
        proxySettingsPanel.add(proxyHostField);
        proxySettingsPanel.add(proxyPortLabel);
        proxySettingsPanel.add(proxyPortField);
        proxySettingsPanel.add(userAuthEnabledLabel);
        proxySettingsPanel.add(userAuthEnabledCB);
        proxySettingsPanel.add(proxyUserNameLabel);
        proxySettingsPanel.add(proxyUserNameField);
        proxySettingsPanel.add(proxyPasswordLabel);
        proxySettingsPanel.add(proxy_password);

        buttonPanel = new JPanel();
        updateButton = new JButton("Update");
        cancelButton = new JButton("Cancel");

        updateButton.addActionListener(this);
        cancelButton.addActionListener(this);

        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        panelContainer.addTab("Basic Settings", basicSettingsPanel);
        panelContainer.addTab("Proxy Settings", proxySettingsPanel);

        add(panelContainer, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setTitle("Configuration");
        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand().toLowerCase();
        if (command.equals("update"))
        {
            Properties properties = new Properties();
            properties.setProperty("ping.timer", timerField.getText() != null ? timerField.getText() : "60000"); // default : 1 minute
            properties.setProperty("ping.url.base", baseURLField.getText() != null ? baseURLField.getText() : "");
            properties.setProperty("ping.url.codes", itemListField.getText() != null ? itemListField.getText() : "");
            properties.setProperty("ping.url.search_string", searchStringField.getText() != null ? searchStringField.getText() : "");
            properties.setProperty("ping.use_proxy", proxyEnabledCB.isSelected() ? "true" : "false");
            properties.setProperty("ping.proxy.host", proxyHostField.getText() != null ? proxyHostField.getText() : "");
            properties.setProperty("ping.proxy.port", proxyPortField.getText() != null ? proxyPortField.getText() : "");
            properties.setProperty("ping.proxy.user_auth", userAuthEnabledCB.isSelected() ? "true" : "false");
            properties.setProperty("ping.proxy.uname", proxyUserNameField.getText() != null ? proxyUserNameField.getText() : "" );
            String password = new String(proxy_password.getPassword());
            properties.setProperty("ping.proxy.pword", password);

            try
            {
                File file = new File(CONFIG_PATH);
                if (!file.exists())
                {
                    file.createNewFile();
                }
                properties.store(new FileOutputStream(file),"");
            } catch (IOException e1)
            {
                e1.printStackTrace();
            }
            setVisible(false);
        }
        else if (command.equals("cancel"))
        {
            try
            {
                reloadFields();
            } catch (IOException e1)
            {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            setVisible(false);
        }
    }

    private void reloadFields() throws IOException
    {
        loader.loadProperties(new File(CONFIG_PATH));

        timerField.setText(new Long(loader.getPingTimer()).toString());
        baseURLField.setText(loader.getBaseUrl());
        searchStringField.setText(loader.getSearchString());
        itemListField.setText(loader.getCodes());

        proxyEnabledCB.setSelected(loader.getProxyInformation() != null && loader.getProxyInformation().proxy_enabled);
        if (proxyEnabledCB.isSelected())
        {
            proxyHostField.setText(loader.getProxyInformation().host);
            proxyPortField.setText(loader.getProxyInformation().port);
            userAuthEnabledCB.setSelected(loader.getProxyInformation() != null && loader.getProxyInformation().user_auth);
            if (userAuthEnabledCB.isSelected())
            {
                proxyUserNameField.setText(loader.getProxyInformation().p_username);
                proxy_password.setText(loader.getProxyInformation().p_password);
            }
        }

    }
}
