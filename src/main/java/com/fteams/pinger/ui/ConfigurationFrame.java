package com.fteams.pinger.ui;

import com.fteams.pinger.PropertyLoader;

import static com.fteams.pinger.app.Main.CONFIG_PATH;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

class ConfigurationFrame extends JFrame implements ActionListener
{
    private JTextField timerField;
    private JTextField baseURLField;
    private JTextField searchStringField;
    private JTextField itemListField;
    private JCheckBox proxyEnabledCB;
    private JCheckBox userAuthEnabledCB;
    private JTextField proxyHostField;
    private JTextField proxyPortField;
    private JTextField proxyUserNameField;
    private JPasswordField proxy_password;

    private final PropertyLoader loader;

    public ConfigurationFrame() throws IOException
    {
        loader = new PropertyLoader();
        loader.loadProperties(new File(CONFIG_PATH));
        JTabbedPane panelContainer = new JTabbedPane();
        JPanel basicSettingsPanel = new JPanel();
        JLabel timerLabel = new JLabel("Time between update calls:");
        timerField = new JTextField(Long.toString(loader.getPingTimer()));
        timerField.setColumns(10);
        JLabel baseURLLabel = new JLabel("Base Look-up URL:");
        baseURLField = new JTextField(loader.getBaseUrl());
        baseURLField.setColumns(10);
        JLabel searchStringLabel = new JLabel("Matching string:");
        searchStringField = new JTextField(loader.getSearchString());
        searchStringField.setColumns(10);
        JLabel itemListLabel = new JLabel("Item Codes (CSV format):");
        itemListField = new JTextField(loader.getCodes());
        itemListField.setColumns(10);

        basicSettingsPanel.setLayout(new GridLayout(4, 2));
        basicSettingsPanel.add(timerLabel);
        basicSettingsPanel.add(timerField);
        basicSettingsPanel.add(baseURLLabel);
        basicSettingsPanel.add(baseURLField);
        basicSettingsPanel.add(searchStringLabel);
        basicSettingsPanel.add(searchStringField);
        basicSettingsPanel.add(itemListLabel);
        basicSettingsPanel.add(itemListField);

        JPanel proxySettingsPanel = new JPanel();
        JLabel proxyEnabledLabel = new JLabel("Enable proxy:");
        proxyEnabledCB = new JCheckBox();
        proxyEnabledCB.setSelected(loader.getProxyInformation() != null && loader.getProxyInformation().proxy_enabled);

        JLabel proxyHostLabel = new JLabel("Proxy host:");
        proxyHostField = new JTextField(loader.getProxyInformation().host);
        JLabel proxyPortLabel = new JLabel("Proxy port:");
        proxyPortField = new JTextField(loader.getProxyInformation().port);

        JLabel userAuthEnabledLabel = new JLabel("Enable user authentication:");
        userAuthEnabledCB = new JCheckBox();
        userAuthEnabledCB.setSelected(loader.getProxyInformation() != null && loader.getProxyInformation().user_auth);

        JLabel proxyUserNameLabel = new JLabel("Username:");
        proxyUserNameField = new JTextField(loader.getProxyInformation().p_username);
        JLabel proxyPasswordLabel = new JLabel("Password:");
        proxy_password = new JPasswordField();
        proxy_password.setText(loader.getProxyInformation().p_password);

        proxySettingsPanel.setLayout(new GridLayout(6, 2));
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

        JPanel buttonPanel = new JPanel();
        JButton updateButton = new JButton("Update");
        JButton cancelButton = new JButton("Cancel");

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
            properties.setProperty("ping.timer",timerField.getText());
            properties.setProperty("ping.url.base", baseURLField.getText());
            properties.setProperty("ping.url.codes", itemListField.getText());
            properties.setProperty("ping.url.search_string", searchStringField.getText());
            properties.setProperty("ping.use_proxy", proxyEnabledCB.isSelected() ? "true" : "false");
            properties.setProperty("ping.proxy.host", proxyHostField.getText());
            properties.setProperty("ping.proxy.port", proxyPortField.getText());
            properties.setProperty("ping.proxy.user_auth", userAuthEnabledCB.isSelected() ? "true" : "false");
            properties.setProperty("ping.proxy.username", proxyUserNameField.getText());
            String password = new String(proxy_password.getPassword());
            properties.setProperty("ping.proxy.password", password);

            try
            {
                File file = new File(CONFIG_PATH);
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
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            setVisible(false);
        }
    }

    private void reloadFields() throws IOException
    {
        loader.loadProperties(new File(CONFIG_PATH));

        timerField.setText(Long.toString(loader.getPingTimer()));
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
