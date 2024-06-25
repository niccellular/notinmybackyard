
package com.atakmap.android.notinmybackyard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.atakmap.android.importexport.CotEventFactory;
import com.atakmap.android.maps.MapItem;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.dropdown.DropDownMapComponent;

import com.atakmap.app.preferences.ToolsPreferenceFragment;
import com.atakmap.coremap.cot.event.CotEvent;
import com.atakmap.coremap.log.Log;
import com.atakmap.android.notinmybackyard.plugin.R;
import com.atakmap.coremap.maps.coords.GeoPointMetaData;

public class NotInMyBackyardMapComponent extends DropDownMapComponent {

    private static final String TAG = "NotInMyBackyardMapComponent";

    private Context pluginContext;
    private SharedPreferences sharedPreference;

    public void onCreate(final Context context, Intent intent, final MapView view) {
        context.setTheme(R.style.ATAKPluginTheme);
        super.onCreate(context, intent, view);
        pluginContext = context;

        ToolsPreferenceFragment.register(
                new ToolsPreferenceFragment.ToolPreference(
                        pluginContext.getString(R.string.preferences_title),
                        pluginContext.getString(R.string.preferences_summary),
                        pluginContext.getString(R.string.nimb_preferences),
                        pluginContext.getResources().getDrawable(R.drawable.ic_launcher),
                        new PluginPreferencesFragment(
                                pluginContext)));


        this.sharedPreference = PreferenceManager.getDefaultSharedPreferences(view.getContext().getApplicationContext());

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                String distance = sharedPreference.getString("plugin_nimb_distance", "10000");
                for (MapItem mi: view.getRootGroup().getAllItems()) {
                    GeoPointMetaData cp = view.getCenterPoint();
                    CotEvent cotEvent = CotEventFactory.createCotEvent(mi);
                    if (cotEvent==null) continue;
                    if (mi.getType().equalsIgnoreCase("self")) continue;
                    try {
                        if (cotEvent.getGeoPoint().distanceTo(cp.get()) > Double.parseDouble(distance)) {
                            mi.setVisible(false);
                        } else {
                            mi.setVisible(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "Not a valid double");
                    }
                }
                handler.postDelayed(this, 6000);
            }
        };
        handler.postDelayed(r, 10000);
}

    @Override
    protected void onDestroyImpl(Context context, MapView view) {
        super.onDestroyImpl(context, view);
        view.getMapEventDispatcher().popListeners();
    }
}
