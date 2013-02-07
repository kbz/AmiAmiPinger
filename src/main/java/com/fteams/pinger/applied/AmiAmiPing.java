package com.fteams.pinger.applied;

import com.fteams.pinger.PropertyLoader;
import com.fteams.pinger.base.PingBase;

public class AmiAmiPing extends PingBase {
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
        // refreshing here
        try {
            loadProperties(loader);
        }
        catch (Exception e)
        {
            // something went wrong
            return false;
        }
        loader.getFigureCodeList().removeAll(availableItems);
        userInterface.updateListContent();
        // done refreshing
        return true;
    }
}
