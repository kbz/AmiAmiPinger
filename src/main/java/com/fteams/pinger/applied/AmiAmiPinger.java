package com.fteams.pinger.applied;

import com.fteams.pinger.PropertyLoader;
import com.fteams.pinger.base.Pinger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class AmiAmiPinger extends Pinger{
    @Override
    public String getBaseURL() {
        return this.loader.getBaseUrl();
    }

    @Override
    public String getSearchString() {
        return this.loader.getSearchString();
    }

    @Override
    public void loadProperties(PropertyLoader loader) {
        this.loader = loader;
    }

    @Override
    public boolean refreshConfig(PropertyLoader loader) {
        this.refreshing = true;
        // refreshing here
        loadProperties(loader);
        loader.getFigureCodeList().removeAll(availableItems);
        userInterface.updateListContent();
        // done refreshing
        this.refreshing = false;
        return true;
    }
}
