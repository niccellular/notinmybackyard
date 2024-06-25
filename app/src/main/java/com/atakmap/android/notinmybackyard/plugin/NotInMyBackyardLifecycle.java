package com.atakmap.android.notinmybackyard.plugin;

import com.atak.plugins.impl.AbstractPlugin;

import com.atakmap.android.notinmybackyard.NotInMyBackyardMapComponent;

import gov.tak.api.plugin.IPlugin;
import gov.tak.api.plugin.IServiceController;

public class NotInMyBackyardLifecycle extends AbstractPlugin implements IPlugin {
    private final static String TAG = "NotInMyBackyardLifecycle";
    public NotInMyBackyardLifecycle(IServiceController isc) {
        super(isc, new NotInMyBackyardMapComponent());
    }
}
