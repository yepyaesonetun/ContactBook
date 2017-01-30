package com.prime.awitd.contactbook.model;

import android.app.Application;

/**
 * Created by SantaClaus on 10/01/2017.
 */

public class MyApplication extends Application{
        private static MyApplication mInstance;

        @Override
        public void onCreate() {
            super.onCreate();

            mInstance = this;
        }

        public static synchronized MyApplication getInstance() {
            return mInstance;
        }

        public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
            ConnectivityReceiver.connectivityReceiverListener = listener;
        }
}
