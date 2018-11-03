package com.vanshika.bobble.notification;

import android.content.Intent;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListener extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification arg0) {
        /*String name=Util.processNotification(this, arg0);
            Intent intent=new Intent(this,Confirmation.class);
            intent.putExtra("name",name);
            startActivity(intent);*/

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification arg0) {
        //do nothing.
    }

    @Override
    public void onDestroy() {
        try {
            if (Util.textToSpeech != null) {
                //Kill
                Util.textToSpeech.shutdown();
                Util.textToSpeech = null;
            }
        } catch (Exception e) {
        }
    }

}
