package com.example.dheerajp.nfc3.status;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.media.TransportMediator;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.TextView;
import com.example.dheerajp.nfc3.BuildConfig;
import com.example.dheerajp.nfc3.C0146R;
import com.example.dheerajp.nfc3.SIC43N1xService;
import java.io.IOException;
import java.util.Arrays;
import android.widget.Toast;

import com.example.dheerajp.nfc3.R;

public class StatusActivity extends SIC43N1xService {

    public static AlertDialog alertCloneStatus;
    //private static State state;
    private boolean tag_start;
    private String temp;
    private byte[] uid_temp;
    public static String final_tag_uid;

    class C02031 implements Runnable {
        final String displaUid;
        C02031(String uid) {
            this.displaUid = uid;
        }

        public void run() {
            Toast.makeText(StatusActivity.this, this.displaUid, Toast.LENGTH_LONG).show();
        }
    }
    //This class getting the byte for tamper and passing in function
    class C02053 implements Runnable {
        final /* synthetic */ byte[] val$rx;
        //Log.i("tamper check C02053", "Tamper check for class");
        C02053(byte[] bArr) {
            this.val$rx = bArr;
        }

        public void run() {
            setCheckTamper(this.val$rx);
        }
    }
    //calling this method for tamper check
    public static boolean setCheckTamper(byte[] rx) {
        Log.i("Setting Tamper Check", "Inside Set Tamper Check");
        Log.i("Tamper check byte", String.format("%x", rx[0]));
        Log.i("Tamper check byte", String.format("%x", rx[1]));
        // just check the values according to rx[] and with help of loag and toast
        if (rx[0] == 0 && rx[1] == 0) {
            //txtReturnTmp.setText("Untampered");
            return true;
        } else if (rx[0] == (byte) -1 && rx[0] == (byte) -1) {
            //txtReturnTmp.setText("Tampered");
        } else {
            //clearChkTamper();
            return false;
        }
        return true;
    }

    class C02064 implements Runnable {
        //Log.i("tamper check C02064", "Tamper check for class");
        C02064() {
        }

        public void run() {
            clearChkTamper();
        }
    }
    static void clearChkTamper() {
//        if (txtReturnTmp != null) {
//            //txtReturnTmp.setText(BuildConfig.FLAVOR);
//            //imgViewTagTamper.setImageResource(C0146R.drawable.tag);
//        }
    }

    private void TamperCheck() {
        Log.i("tag id check is", SIC43N1xService.taguid);
        byte[] tx = new byte[]{(byte) -81, (byte) 0};
        nfcReConnect();
        try {
            Log.e("Tx_tamper", Arrays.toString(tx));
            runOnUiThread(new C02053(nfc.transceive(tx)));
        } catch (Exception e) {
            runOnUiThread(new C02064());
        }
    }

    @Override
    protected void onTagDetected() {
        Log.i("StatusActivity Log", "inside onTagDetect");
        Log.i("tag id check is 1", SIC43N1xService.taguid);
        if (!this.tag_start) {
            Log.i("tag id check is", SIC43N1xService.taguid);
        }
        TamperCheck();
        final_tag_uid = SIC43N1xService.taguid;
        //TextView displayUid = (TextView)findViewById(R.id.textView);
        //displayUid.setText("Hi there");
        runOnUiThread(new C02031(SIC43N1xService.taguid));
        //nfcReConnect();
        //Toast.makeText(StatusActivity.this, final_tag_uid, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        //Toast.makeText(getApplicationContext(), final_tag_uid, Toast.LENGTH_LONG).show();
    }
}
