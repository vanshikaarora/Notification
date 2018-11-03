package com.vanshika.bobble.notification;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AccessibilityServiceSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Notification TTS Accessibility Service Settings.\nThere are no settings");
        setContentView(tv);
    }
}
