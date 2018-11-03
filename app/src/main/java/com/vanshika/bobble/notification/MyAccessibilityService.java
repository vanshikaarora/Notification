package com.vanshika.bobble.notification;

import android.Manifest;
import android.Manifest.permission;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

import static android.Manifest.permission.*;
import static android.content.pm.PackageManager.*;
import static android.support.v4.app.ActivityCompat.requestPermissions;

public class MyAccessibilityService extends AccessibilityService implements EasyPermissions.PermissionCallbacks {
    String name = "";
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onServiceConnected() {
        //api 16+
        //getServiceInfo().eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        Log.v("NotificationTTS", "Connected");
        //http://stackoverflow.com/questions/13853304/accessibility-events-not-recognized-by-accessibility-service-in-android-2-3gb
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.notificationTimeout = 0;
        info.flags = AccessibilityServiceInfo.DEFAULT;
        setServiceInfo(info);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent arg0) {
        if (arg0 == null) {
            return;
        }
        if (arg0.getEventType() != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            return;
        }
// hello hhello hello rinkkiya ke papa haas dele hihihi
        // kamariya lollipop lagelu
        // bol na aunty aaaaaaaaaaau qa? sot mai lagau qa? aee aaaunty, kkknock knock ding ding ding bol n
        //Process Notification

        name = Util.processNotification(this, arg0);
        if(name        == null){
            return ;
        }
        if (!name.isEmpty()) {
            Intent intent=new Intent(this,Dialog.class);
            startActivity(intent);

            /*Intent intent=new Intent(this,Confirmation.class);
            intent.putExtra("name",name);
            startActivity(intent);*/


            /*Handler h = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    new AlertDialog.Builder(getApplicationContext()) // it wont let you add           dialog bbox                  with context of application. an aactiviiiiiiiity is rrequuuuuuuuuired...so do u mean custom dialog?, i gues yesshalhow u some code of it..that also might not worksl i
                            .setTitle("Your Alert")
                            .setMessage("Your Message")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                            getPermission();
                                    sendMsg(getContact(name));
                                }
                            }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                            .show();
                }
            };
h.sendEmptyMessage(0);*/

        }





        /*String phone_no=getPhone_no(name);
        Log.v("line39",phone_no);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Title")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();



        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();*/
    }

/*
    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

        } else {
            String phone_no = getContact(name);
            Log.v("line32", phone_no + " ");
            sendMessage(phone_no);
        }
    }
*/

    private void sendMsg(String num) {
        if (num != null) {
            num = num.replace("+", "").replace(" ", "");

            /*Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.putExtra("jid", num+ "@s.whatsapp.net");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Test");
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setPackage("com.whatsapp");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);*/
            PackageManager packageManager = getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);

            try {
                String url = "https://api.whatsapp.com/send?phone=" + num + "&text=" + URLEncoder.encode("Hey", "UTF-8");
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                if (i.resolveActivity(packageManager) != null) {
                    startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getContact(String name) {
        final String[] projection = {
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.Data.MIMETYPE,
                "account_type",
                ContactsContract.Data.DATA3,
        };

        final String selection = ContactsContract.Data.MIMETYPE + " =? and account_type=?";
        final String[] selectionArgs = {
                "vnd.android.cursor.item/vnd.com.whatsapp.profile",
                "com.whatsapp"
        };

        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
        String watssappNumber = null;
        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex(ContactsContract.Data.CONTACT_ID));
            String number = c.getString(c.getColumnIndex(ContactsContract.Data.DATA3));
            String name1 = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            Log.v("WhatsApp", "name " + name1 + " - number - " + number);
            if (name1.equals(name)) {
                watssappNumber = number;
                Log.v("line80", name + " " + watssappNumber);
            }

        }
        Log.v("WhatsApp", "Total WhatsApp Contacts: " + c.getCount());
        c.close();
        return watssappNumber;
    }

    /*private String getPhone_no(String name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(READ_CONTACTS)
                    != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{READ_CONTACTS});
            } else {
               // dispatchTakePictureIntent();
            }
        }
    }*/

    /*private void requestPermissions(String[] strings, int i) {
        if (i == 1) {
            if (strings[0] == 0) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                *//*Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);*//*
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }*/


    @Override
    public void onInterrupt() {
        //do nothin'
    }

    @Override
    public void onDestroy() {
        //super.onDestroy(); //To change body of generated methods, choose Tools | Templates.
        if (Util.textToSpeech != null) {
            //Kill
            Util.textToSpeech.shutdown();
            Util.textToSpeech = null;
        }
    }

    @Override
    public Context getBaseContext() {
        return super.getBaseContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strings, @NonNull int[] ints) {

    }
}
