package com.example.dheerajp.nfc3.status;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.dheerajp.nfc3.BuildConfig;
import com.example.dheerajp.nfc3.C0146R;
import com.example.dheerajp.nfc3.SIC43N1xFragment;

/**
 * Created by Dheeraj P on 4/20/2017.
 */

public class ReadDataFragment extends SIC43N1xFragment {
    private static TextView tvReadBlock;

    /* renamed from: com.sic.app.sic43n1x.status.ReadDataFragment.1 */
    static class C02001 implements Runnable {
        final /* synthetic */ String val$desiredText;
        final /* synthetic */ Handler val$handler;

        /* renamed from: com.sic.app.sic43n1x.status.ReadDataFragment.1.1 */
        class C01991 implements Runnable {
            C01991() {
            }

            public void run() {
                if (ReadDataFragment.tvReadBlock != null) {
                    ReadDataFragment.tvReadBlock.setText(C02001.this.val$desiredText);
                }
            }
        }

        C02001(Handler handler, String str) {
            this.val$handler = handler;
            this.val$desiredText = str;
        }

        public void run() {
            this.val$handler.post(new C01991());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (this.rootView == null) {
            //this.rootView = inflater.inflate(C0146R.layout.page_read_block, container, false);
            //tvReadBlock = (TextView) this.rootView.findViewById(C0146R.id.tvReadBlock);
            tvReadBlock.setText(BuildConfig.FLAVOR);
        } else {
            ViewGroup viewGroup = (ViewGroup) this.rootView.getParent();
        }
        return this.rootView;
    }

    public static void showData(byte[] rxRead, byte addr) {
        int i = 0;
        while (i < rxRead.length) {
            tvReadBlock.append(rxRead[i] + BuildConfig.FLAVOR);
            if (i != 0 && i % 3 == 0) {
                tvReadBlock.append("\n" + addr + "\n");
            }
            i++;
        }
    }

    public static void ClearshowData() {
        mUpdateTextView(BuildConfig.FLAVOR);
    }

    public static void showDataHexAll(String str) {
        mUpdateTextView(str);
    }

    private static void mUpdateTextView(String desiredText) {
        new Thread(new C02001(new Handler(), desiredText)).start();
    }
}
