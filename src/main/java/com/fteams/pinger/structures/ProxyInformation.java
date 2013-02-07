package com.fteams.pinger.structures;

public class ProxyInformation
{
    public boolean proxy_enabled;
    public String host;
    public String port;
    public boolean user_auth;
    public String p_username;
    public String p_password;

    public String toString()
    {
        String out = "";
        if (proxy_enabled)
        {
            out += "Proxy enabled: \n";
            out += "Host: " + host +"\n";
            out += "Port: " + port +"\n";
        }
        else
        {
            out += "Proxy disabled. \n";
        }
        if (user_auth)
        {
            out += "User Authentication enabled: \n";
            out += "User: " + p_username.replaceAll("[a-zA-Z0-9]", "*") +"\n";
            out += "Pass: " + p_password.replaceAll("[a-zA-Z0-9]", "*");
        }
        else
        {
            out += "User Authentication disabled";
        }
        return out;
    }

    public boolean equals(Object o)
    {
        if (o instanceof ProxyInformation)
        {
            ProxyInformation tmp = (ProxyInformation)o;
            if (tmp.proxy_enabled != proxy_enabled)
                return false;
            if (tmp.user_auth != user_auth)
                return false;
            if (!tmp.host.equals(host))
                return false;
            if (!tmp.port.equals(port))
                return false;
            if (!tmp.p_password.equals(p_password))
                return false;
            if (!tmp.p_username.equals(p_username))
                return false;
            return true;
        }
        return false;
    }
}
