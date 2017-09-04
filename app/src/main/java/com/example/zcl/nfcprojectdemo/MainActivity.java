package com.example.zcl.nfcprojectdemo;

import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zcl.nfcprojectdemo.base.BaseNfcActivity;
import com.example.zcl.nfcprojectdemo.utils.RecordParse;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * uri数据的获取 NDEF 类型
 */
public class MainActivity extends BaseNfcActivity {

    private static final String TAG = "TAG";
    private NdefMessage[] ndefMessages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onNewIntent(Intent intent) {
        String action = intent.getAction();
        String type = intent.getType();

        int contentSize = 0;
        NdefMessage msgs[] = null;
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    contentSize += msgs[i].toByteArray().length;
                }
            } else {
                byte[] empey = new byte[]{};
                NdefRecord ndefRecord = new NdefRecord((short) 0x05, empey, empey, empey);
                NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});
                ndefMessages = new NdefMessage[]{ndefMessage};
            }
            processNdefMsg(msgs);
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

        } else if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {


        } else {
            Log.e(TAG, "aaaaa: ");
            finish();
        }
    }

    //获取待解析的 NdefMessage
    private void processNdefMsg(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) {

            return;
        } else {
            for (int i = 0; i < msgs.length; i++) {
                int length = msgs[i].getRecords().length;
                NdefRecord[] records = msgs[i].getRecords();
                for (int j = 0; j < length; j++) {
                    for (NdefRecord record : records) {
                        if (isuri(record)) {
                            parseRTuriRecord(record);
                        }
                    }
                }
            }
        }
    }

    private void parseRTuriRecord(NdefRecord record) {
        short tnf = record.getTnf();
        if (tnf == NdefRecord.TNF_ABSOLUTE_URI) {
            RecordParse.parseAbsulotUriRecord(record);
        }
    }
    public static boolean isuri(NdefRecord ndefRecord) {
        if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN) {
            if (Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_URI)) {
                return true;
            } else {
                return false;
            }
        } else if (ndefRecord.getTnf() == NdefRecord.TNF_ABSOLUTE_URI) {
            return true;
        } else {
            return false;
        }
    }
}