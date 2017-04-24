package com.example.dheerajp.nfc3;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.example.dheerajp.nfc3.status.ReadDataFragment;

import android.util.Log;

/**
 * Created by Dheeraj P on 4/20/2017.
 */

public abstract class SIC43N1xService extends FragmentActivity {
    protected static final byte SIC = (byte) 57;
    protected static final byte SIC4310 = (byte) 2;
    protected static final byte SIC43C1_16P_1 = (byte) 8;
    protected static final byte SIC43C1_16P_2 = (byte) 16;
    protected static final byte SIC43C1_16P_3 = (byte) 56;
    protected static final byte SIC43N1Fx03_50p = (byte) 28;
    protected static final byte SIC43N1Fx13_50p = (byte) 44;
    protected static final byte SIC43N1_50p = (byte) 36;
    protected static final byte SIC43U1_16p = (byte) 24;
    protected static final byte SIC_SAMPLE = (byte) 0;
    public static NfcAdapter adapter;
    public static String[][] mTechlists;
    public static Ndef ndef;
    public static NfcA nfc;
    public static PendingIntent pendingIntent;
    public static String[] tech;
    public static byte[] uid;
    public static String taguid = "";

    /* renamed from: com.sic.app.sic43n1x.SIC43N1xService.1 */
    class C01481 implements Runnable {
        final /* synthetic */ Intent val$intent;
        final /* synthetic */ ProgressDialog val$ringProgressDialog;

        /* renamed from: com.sic.app.sic43n1x.SIC43N1xService.1.1 */
        class C01471 implements Runnable {
            C01471() {
            }

            public void run() {
                ReadDataFragment.ClearshowData();
            }
        }

        C01481(Intent intent, ProgressDialog progressDialog) {
            this.val$intent = intent;
            this.val$ringProgressDialog = progressDialog;
        }

        public void run() {
            taguid = "";
            SIC43N1xService.this.runOnUiThread(new C01471());
            Tag tag = (Tag) this.val$intent.getParcelableExtra("android.nfc.extra.TAG");
            SIC43N1xService.nfc = NfcA.get(tag);
//            Log.d("check Log service", "Testing Check Log");
//            Log.d("check Log1", "service1 Check Log");
//            Log.i("check Log2", "service2 Check Log");
//            Log.v("check Log3", "service3 Check Log");
            SIC43N1xService.ndef = Ndef.get(tag);
            SIC43N1xService.uid = tag.getId();
            byte uidTagtype = SIC43N1xService.uid[1];
            Log.i("Tag id is", String.format("%20x", SIC43N1xService.uid[2]));
            for (byte b: SIC43N1xService.uid){
                Log.i("Tag formatted", String.format("%x", b));
                taguid = taguid + String.format("%x", b);
            }
            Log.i("Final taguid is", taguid);
//            Log.d("check uid", String.format("%02X", new Object[]{Byte.valueOf(uidTagtype)}));
//            Log.i("check uid", String.format("%02X", new Object[]{Byte.valueOf(uidTagtype)}));
//            Log.v("check uid", String.format("%02X", new Object[]{Byte.valueOf(uidTagtype)}));
            SIC43N1xService.tech = tag.getTechList();
            SIC43N1xService.nfc.setTimeout(2000);
            SIC43N1xService.this.onTagDetected();
            this.val$ringProgressDialog.dismiss();
        }
    }

    protected abstract void onTagDetected();

    static {
        uid = new byte[0];
        tech = new String[0];
    }

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        adapter = NfcAdapter.getDefaultAdapter(this);
        //Log.d("check Log oncraete", "Testing Check Log oncreate");
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(536870912), 0);
        String[][] strArr = new String[1][];
        strArr[0] = new String[]{NfcA.class.getName()};
        mTechlists = strArr;
    }

    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.enableForegroundDispatch(this, pendingIntent, null, mTechlists);
        }
    }

    public void onPause() {
        super.onPause();
        if (adapter != null) {
            adapter.disableForegroundDispatch(this);
        }
    }

    public void onNewIntent(Intent intent) {
        ProgressDialog ringProgressDialog = ProgressDialog.show(this, "Please wait ...", "Downloading Data ...", true);
        ringProgressDialog.setCancelable(true);
        new Thread(new C01481(intent, ringProgressDialog)).start();
    }

    public void nfcReConnect() {
        if (nfc != null) {
            try {
                if (nfc.isConnected()) {
                    nfc.close();
                }
                nfc.connect();
            } catch (Exception e) {
            }
        }
    }

    public void finish() {
        if (nfc != null) {
            try {
                nfc.close();
            } catch (Exception e) {
            }
        }
        super.finish();
    }
}
