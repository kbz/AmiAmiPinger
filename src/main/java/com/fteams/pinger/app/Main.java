package com.fteams.pinger.app;

import com.fteams.pinger.ui.MainFrame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    final static String CONFIG_PATH = "./config.properties";
    public static void main(String [] args)
    {
        try
        {
            File configFile = new File(CONFIG_PATH);
            if (!configFile.exists())
            {
                configFile.createNewFile();
                fillDefaultValues(configFile);
            }

            MainFrame ui = new MainFrame(CONFIG_PATH);
        } catch (IOException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    private static void fillDefaultValues(File configFile)
    {
        Properties properties = new Properties();
        properties.setProperty("ping.timer", "600000"); // default : 10 minute
        properties.setProperty("ping.url.base", "http://www.amiami.com/top/detail/detail?gcode=");
        properties.setProperty("ping.url.codes", "");
        properties.setProperty("ping.url.search_string", "btn_cart02_off.gif");
        properties.setProperty("ping.use_proxy", "false");
        properties.setProperty("ping.proxy.host", "");
        properties.setProperty("ping.proxy.port", "");
        properties.setProperty("ping.proxy.user_auth", "false");
        properties.setProperty("ping.proxy.uname", "" );
        properties.setProperty("ping.proxy.pword", "");
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
    }
}
