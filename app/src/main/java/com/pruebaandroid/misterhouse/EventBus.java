package com.pruebaandroid.misterhouse;

import com.squareup.otto.Bus;

/**
 * Created by Andres on 10/09/2016.
 */
public class EventBus {

    private static Bus instance = null;

    private EventBus()
    {
        instance = new Bus();
    }

    public static Bus getInstance()
    {
        if(instance == null)
        {
            instance = new Bus();
        }
        return instance;
    }
}
