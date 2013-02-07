/* Copyright 2012 Sabre Holdings */
package com.fteams.pinger.base;

import com.fteams.pinger.PropertyLoader;

import java.io.File;
import java.io.IOException;

public class PingerPropertyUpdater extends Thread
{
    private String CONFIG_PATH;
    private PropertyLoader loader;
    private Pinger pinger;

    public PingerPropertyUpdater(String config_file, PropertyLoader loader, Pinger pinger)
    {
        this.CONFIG_PATH = config_file;
        this.loader = loader;
        this.pinger = pinger;
    }

    public void run() {
        while (true)
        {
            try {
                Thread.sleep(1111);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            File configFile = new File(CONFIG_PATH);
            loader = new PropertyLoader();
            try {
                loader.loadProperties(configFile);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            pinger.refreshConfig(loader);
        }
    }
}
