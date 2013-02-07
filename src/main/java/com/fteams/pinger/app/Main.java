package com.fteams.pinger.app;

import com.fteams.pinger.ui.MainFrame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public final static String CONFIG_PATH = "./config.properties";
    public static void main(String [] args)
    {
        // any args may be used against you, please refrain from using args.
        File configFile = new File(CONFIG_PATH);
        if (!configFile.exists())
        {
            createConfigurationWithDefaultValues();
        }

        new MainFrame();
    }


    private static void createConfigurationWithDefaultValues()
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
        properties.setProperty("ping.proxy.username", "" );
        properties.setProperty("ping.proxy.password", "");
        try
        {
            File file = new File(CONFIG_PATH);
            if (!file.exists())
            {
                if (!file.createNewFile())
                {
                    throw new IOException("Failed to create the configuration file, " +
                            "do you have enough read/access rights for this location?");
                }
            }
            properties.store(new FileOutputStream(file),"");
        } catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }
}
