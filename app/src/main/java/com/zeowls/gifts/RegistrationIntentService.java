package com.zeowls.gifts;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.zeowls.gifts.BackEndOwl.Core;
import com.zeowls.gifts.Utility.PrefUtils;

import java.io.IOException;

/**
 * Created by nezar on 5/9/16.
 */
public class RegistrationIntentService extends IntentService {

//    private static final String TAG = "RegIntentService";
//    private static final String[] TOPICS = {"global"};
//
//    public RegistrationIntentService() {
//        super(TAG);
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//
//        try {
//            // [START register_for_gcm]
//            // Initially this call goes out to the network to retrieve the token, subsequent calls
//            // are local.
//            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
//            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
//            // [START get_token]
//            InstanceID instanceID = InstanceID.getInstance(this);
//            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
//                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
//            // [END get_token]
//            Log.w(TAG, "GCM Registration Token: " + token);
//
//            // TODO: Implement this method to send any registration to your app's servers.
//            sendRegistrationToServer(token);
//
//            // Subscribe to topic channels
//            subscribeTopics(token);
//
//            // You should store a boolean that indicates whether the generated token has been
//            // sent to your server. If the boolean is false, send the token to your server,
//            // otherwise your server should have already received the token.
//            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
//            // [END register_for_gcm]
//        } catch (Exception e) {
//            Log.d(TAG, "Failed to complete token refresh", e);
//            // If an exception happens while fetching the new token or updating our registration data
//            // on a third-party server, this ensures that we'll attempt the update at a later time.
//            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
//        }
//        // Notify UI that registration has completed, so the progress indicator can be hidden.
//        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
//    }
//
//    /**
//     * Persist registration to third-party servers.
//     *
//     * Modify this method to associate the user's GCM registration token with any server-side account
//     * maintained by your application.
//     *
//     * @param token The new token.
//     */
//    private void sendRegistrationToServer(String token) {
//        // Add custom implementation, as needed.
//    }
//
//    /**
//     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
//     *
//     * @param token GCM token
//     * @throws IOException if unable to reach the GCM PubSub service
//     */
//    // [START subscribe_topics]
//    private void subscribeTopics(String token) throws IOException {
//        GcmPubSub pubSub = GcmPubSub.getInstance(this);
//        for (String topic : TOPICS) {
//            pubSub.subscribe(token, "/topics/" + topic, null);
//        }
//    }
//    // [END subscribe_topics]


    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String GCM_TOKEN = "gcmToken";


    // abbreviated tag name
    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    String token = "";
    int id = 0;


    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Make a call to Instance API
        InstanceID instanceID = InstanceID.getInstance(this);
        String senderId = getResources().getString(R.string.gcm_defaultSenderId);
        // Fetch token here
        try {
            // request token that will be used by the server to send push notifications
            token = instanceID.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
            Log.d(TAG, "GCM Registration Token: " + token);
            // save token
            sharedPreferences.edit().putString(GCM_TOKEN, token).apply();
            // pass along this data
            sendRegistrationToServer(token);
        } catch (IOException e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
        }
    }

    private void sendRegistrationToServer(String token) {
        try {

            try {
                id = PrefUtils.getCurrentUser(getBaseContext()).getId();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            SharedPreferences preferences = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
//            id = preferences.getInt("shopID", 0);
            if (id != 0) {

                // send network request
                Core core = new Core(getBaseContext());
                core.registerDevice(id, token);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        // if registration sent was successful, store a boolean that indicates whether the generated token has been sent to server
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
    }

//    class registerDeviceToServer extends AsyncTask{
//
//        @Override
//        protected Object doInBackground(Object[] params) {
//            Core core = new Core(getBaseContext());
//            core.registerDevice();
//            return null;
//        }
//    }
}
