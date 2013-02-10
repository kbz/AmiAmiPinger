package com.fteams.pinger;

import com.fteams.pinger.structures.ProxyInformation;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class PropertyLoader {

    private long ping_timer;
    private List<String> urlList;
    private String baseUrl;
    private String searchString;
    private String codes;
    private ProxyInformation proxyInformation;

    public void loadProperties(File input) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(input));
        urlList = new LinkedList<String>();
        this.ping_timer = Long.parseLong(properties.getProperty("ping.timer"));
        this.baseUrl = properties.getProperty("ping.url.base");
        this.codes = properties.getProperty("ping.url.codes");
        String [] figCodes = properties.getProperty("ping.url.codes").split(",");
        Collections.addAll(urlList, figCodes);
        this.searchString = properties.getProperty("ping.url.search_string");
        proxyInformation = new ProxyInformation();
        proxyInformation.host = properties.getProperty("ping.proxy.host");
        proxyInformation.port = properties.getProperty("ping.proxy.port");
        proxyInformation.proxy_enabled =  properties.getProperty("ping.use_proxy").equals("true");
        proxyInformation.user_auth = properties.getProperty("ping.proxy.user_auth") != null && properties.getProperty("ping.proxy.user_auth").equals("true");
        proxyInformation.p_username = properties.getProperty("ping.proxy.username");
        proxyInformation.p_password = properties.getProperty("ping.proxy.password");
     }

    public String getCodes()
    {
        return codes;
    }
    public ProxyInformation getProxyInformation()
    {
        return this.proxyInformation;
    }
    public Long getPingTimer()
    {
        return this.ping_timer;
    }
    public List<String> getFigureCodeList()
    {
        return this.urlList;
    }
    public String getBaseUrl()
    {
        return this.baseUrl;
    }

    public String getSearchString() {
        return this.searchString;
    }

    public String toString()
    {
        String out = "";
        out +=( "INFO: loaded " + urlList.size() + " figure codes for lookup.\n");
        out +=("INFO: ping timer : " + (ping_timer/60000) + " minutes.\n");
        out +=("INFO: base URL for lookup: " + baseUrl+"\n");
        out +=("INFO: code list: " + urlList + "\n");
        out +=("INFO: using proxy info: " + proxyInformation);
        return out;
    }

    public boolean equals(Object o)
    {
        if (o instanceof PropertyLoader)
        {
            PropertyLoader tmp = (PropertyLoader)o;
            return tmp.getCodes().equals(getCodes())
                    && tmp.getBaseUrl().equals(getBaseUrl())
                    && tmp.getSearchString().equals(getSearchString())
                    && tmp.getProxyInformation().equals(getProxyInformation())
                    && tmp.getPingTimer().equals(getPingTimer());
        }
        return false;
    }
}
