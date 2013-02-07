/* Copyright 2012 Sabre Holdings */
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
}
