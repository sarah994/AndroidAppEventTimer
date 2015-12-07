package com.example.r0462870.eventtimer;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by r0462870 on 7/12/2015.
 */
public class FragmentActivity extends PreferenceFragment{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
