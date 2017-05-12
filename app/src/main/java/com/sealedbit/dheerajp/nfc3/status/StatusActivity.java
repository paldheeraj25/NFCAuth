package com.sealedbit.dheerajp.nfc3.status;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.sealedbit.dheerajp.nfc3.SIC43N1xService;
import java.util.Arrays;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import com.sealedbit.dheerajp.nfc3.R;
import com.squareup.picasso.Picasso;

public class StatusActivity extends SIC43N1xService {

    public static AlertDialog alertCloneStatus;
    //private static State state;
    private boolean tag_start;
    private String temp;
    private byte[] uid_temp;
    public static String final_tag_uid;
    private static TextView productId;
    private static TextView productname;
    private static TextView TamperInfo;
    private static Context context;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("product");

    class C02031 implements Runnable {
        final String displaUid;
        C02031(String uid) {
            this.displaUid = uid;
        }

        public void run() {
            //Toast.makeText(StatusActivity.this, this.displaUid, Toast.LENGTH_LONG).show();
            Log.i("Checking data base23", "Inside Set Tamper Check");
            TextView productId = (TextView)findViewById(R.id.textView);
            productId.setText(this.displaUid);

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    //String value = dataSnapshot.getValue(String.class);
                    showData(dataSnapshot);
                    //database.child("Campaigns").child(key).child("count");
                    //Log.d("test", "Value is: " + value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("test", "Failed to read value.", error.toException());
                }
            });
        }

        private void showData(DataSnapshot dataSnapshot){
            Log.i("Checking data base1", "Inside Set Tamper Check");
            TextView productname = (TextView)findViewById(R.id.textView3);
            TextView productDescrption = (TextView)findViewById(R.id.description);
            TextView productManufacture = (TextView)findViewById(R.id.manufacturing_date);
            TextView productExpire = (TextView)findViewById(R.id.expiry_date);
            ImageView productImage = (ImageView)findViewById(R.id.imageView2) ;

            productname.setText(dataSnapshot.child(displaUid).child("productName").getValue().toString());
            productDescrption.setText(dataSnapshot.child(displaUid).child("description").getValue().toString());
            productManufacture.setText(dataSnapshot.child(displaUid).child("manufacturingDate").getValue().toString());
            productExpire.setText(dataSnapshot.child(displaUid).child("expiryDate").getValue().toString());
            //Picasso.with(StatusActivity.this).load("https://firebasestorage.googleapis.com/v0/b/fir-1-a158d.appspot.com/o/minion1.jpg?alt=media&token=dd72e136-1e01-4ed4-afdc-f1b96ddab6dc").into(productImage);
            Picasso.with(StatusActivity.this).load(dataSnapshot.child(displaUid).child("productImage").getValue().toString()).into(productImage);
            Log.d("snapshot Data", dataSnapshot.child(displaUid).getValue().toString());
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
            TextView TamperInfo = (TextView)findViewById(R.id.textView2);
            //Log.i("Tring to set1", "set");
            if (setCheckTamper(this.val$rx)){
                TamperInfo.setText("Sealed");
                TamperInfo.setTypeface(TamperInfo.getTypeface(), Typeface.BOLD_ITALIC);
            } else {
                TamperInfo.setText("Broken Seal");
                TamperInfo.setTextColor(Color.RED);
                TamperInfo.setTypeface(TamperInfo.getTypeface(), Typeface.BOLD_ITALIC);
            }

        }
    }

    //calling this method for tamper check
    public static boolean setCheckTamper(byte[] rx) {
//        Log.i("Setting Tamper Check", "Inside Set Tamper Check");
//        Log.i("Tamper check byte", String.format("%x", rx[0]));
//        Log.i("Tamper check byte", String.format("%x", rx[1]));
        // just check the values according to rx[] and with help of loag and toast
        if (rx[0] == 0 && rx[1] == 0) {
            //Toast.makeText(StatusActivity.context, "unTampred", Toast.LENGTH_LONG).show();
            return true;
        } else if (rx[0] == (byte) -1 && rx[0] == (byte) -1) {
            //Toast.makeText(StatusActivity.context, "Tampred", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
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
        runOnUiThread(new C02031(SIC43N1xService.taguid));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        TextView productId = (TextView)findViewById(R.id.textView);
        TextView TamperInfo = (TextView)findViewById(R.id.textView2);
        TamperInfo.setText("Tamper Staus");
        Log.i("Checking data base2", "Inside Set Tamper Check");
        Log.d("Checking data base2", "Inside Set Tamper Check");
        Log.v("Checking data base2", "Inside Set Tamper Check");
        //StatusActivity.context = getApplicationContext();
    }

}
