/* Copyright 2012 Sabre Holdings */
package com.fteams.pinger.base;

import com.fteams.pinger.PropertyLoader;
import com.fteams.pinger.ui.MainFrame;

import java.io.File;
import java.io.IOException;

import static com.fteams.pinger.app.Main.CONFIG_PATH;

public class PingPropertyUpdater extends Thread
{
    private final MainFrame parent;
    private PropertyLoader loader;
    private final PingBase pingBase;

    public PingPropertyUpdater(PropertyLoader loader, PingBase pingBase, MainFrame parent)
    {
        this.loader = loader;
        this.pingBase = pingBase;
        this.parent = parent;
    }

    public void run() {
        while (true)
        {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            File configFile = new File(CONFIG_PATH);
            PropertyLoader localLoader = new PropertyLoader();
            try {
                localLoader.loadProperties(configFile);
                if (loader != null && localLoader.equals(loader))
                    continue;

                parent.updateConsole("Configuration changed.");
                loader = new PropertyLoader();
                // only load if there's changes in the configuration file.
                loader.loadProperties(configFile);
                if (!pingBase.refreshConfig(loader))
                {
                    throw new IOException("Failed to update the config file.");
                }
                parent.updateConsole("Configuration reloaded.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
