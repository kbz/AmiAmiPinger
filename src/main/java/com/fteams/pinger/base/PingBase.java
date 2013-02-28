package com.fteams.pinger.base;

import com.fteams.pinger.PropertyLoader;
import com.fteams.pinger.ui.MainFrame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.LinkedList;
import java.util.List;

public abstract class PingBase extends Thread{

    protected MainFrame userInterface;
    protected PropertyLoader loader;
    protected final List<String> availableItems = new LinkedList<String>();
    
    protected abstract String getBaseURL();
    protected abstract String getSearchString();
    public abstract void loadProperties(PropertyLoader loader);
    public abstract boolean refreshConfig(PropertyLoader loader);
    public List<String> getAvailableItems()
    {
        return availableItems;
    }
    public List<String> getUnavailableItems()
    {
        List<String> unavailableItems = loader.getFigureCodeList();
        unavailableItems.removeAll(getAvailableItems());
        return unavailableItems;
    }
    public void setUserInterface(MainFrame mainFrame)
    {
        this.userInterface = mainFrame;
    }

    public void run()
    {
        if (userInterface != null )
        {
            userInterface.updateConsole("Initializing the data:");
            userInterface.updateConsole(loader.toString());
        }
        while (true)
        {
            try {
                // ping and parse
                int size_before = loader.getFigureCodeList().size();
                pingAndParse();
                if (size_before != loader.getFigureCodeList().size())
                {
                    announceStatus();
                    if (userInterface != null)
                    {
                        userInterface.updateListContent();
                    }
                }
                Thread.sleep(loader.getPingTimer());

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void announceStatus() {
        if (availableItems.size() != 0)
        {
            if (userInterface != null )
            {
                userInterface.updateConsole("INFO: Updates Available for " + availableItems.size() + " elements.");
            }
            for (String code: availableItems)
            {
                if (userInterface != null )
                {
                    userInterface.updateConsole("> "+getBaseURL()+code);
                }
            }

            if (userInterface != null )
            {
                userInterface.updateConsole("INFO: Unchanged Items: " + loader.getFigureCodeList().size() + " elements.");
            }
            for (String code: loader.getFigureCodeList())
            {
                if (userInterface != null )
                {
                    userInterface.updateConsole("> "+getBaseURL()+code);
                }
            }
        }
    }

    void pingAndParse() throws IOException {
        for (String code : loader.getFigureCodeList())
        {
            URL url = new URL(getBaseURL() + code);
            HttpURLConnection urlConnection;

            // load user authentication information > if specified in the config
            if (loader.getProxyInformation().user_auth)
            {
                Authenticator authenticator = new Authenticator(){
                    public PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(loader.getProxyInformation().p_username, loader.getProxyInformation().p_password.toCharArray());
                    }

                };
                Authenticator.setDefault(authenticator);
            }
            // load proxy config if specified
            if (loader.getProxyInformation().proxy_enabled)
            {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(loader.getProxyInformation().host, Integer.parseInt(loader.getProxyInformation().port)));
                try {
                    urlConnection = (HttpURLConnection) url.openConnection(proxy);
                }catch (UnknownHostException e)
                {
                    if (userInterface != null )
                    {
                        userInterface.updateConsole("Host Unreachable: verify user data - (username, password) - Tools > Setting > Proxy ");
                    }
                    // connection error while reaching the site, prevent additional calls by returning.
                    return;
                }
            }
            else
            {
                try{
                    urlConnection = (HttpURLConnection) url.openConnection();
                }catch (UnknownHostException e)
                {
                    if (userInterface != null )
                    {
                        userInterface.updateConsole("Host Unreachable: are you behind a proxy? if so, set the proxy configuration in the Tools > Setting > Proxy tab");
                    }
                    // connection error while reaching the site, prevent additional calls by returning.
                    return;
                }
            }
            urlConnection.setRequestMethod("GET");
            urlConnection.setInstanceFollowRedirects(false);
            try {
                urlConnection.connect();
            }catch (UnknownHostException e)
            {
                if (userInterface != null )
                {
                    userInterface.updateConsole("Host Unreachable: are you behind a proxy? if so, set the proxy configuration in the Tools > Setting > Proxy tab");
                }
                // connection error while reaching the site, prevent additional calls by returning.
                return;
            } catch (SocketException e)
            {
                if (userInterface != null )
                {
                    userInterface.updateConsole("Host Unreachable: unable to connect, verify your network connection as well as the search URL, Tools > Setting > Basic tab");
                }
                // connection error while reaching the site, prevent additional calls by returning.
                return;

            }
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            while (true){
                String line = reader.readLine();
                if ( line == null ){
                    break;
                }
                if (line.contains(getSearchString()))
                {
                    if (userInterface != null )
                    {
                        userInterface.updateConsole("WARNING: \n> Item: "+code + "(url: "+ loader.getBaseUrl() + code + ") is available!");
                    }
                    availableItems.add(code);
                }
            }
            reader.close();
            urlConnection.disconnect();
        }
        loader.getFigureCodeList().removeAll(availableItems);
    }
}
