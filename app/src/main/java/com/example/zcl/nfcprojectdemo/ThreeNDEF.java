package com.example.zcl.nfcprojectdemo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.zcl.nfcprojectdemo.base.BaseNfcActivity;
import com.example.zcl.nfcprojectdemo.utils.MyNDEFMsgGet;
import com.example.zcl.nfcprojectdemo.utils.RecordParse;

import java.io.IOException;

/**
 * Created by zcl on 2017/8/30.
 */

/**
 * NDEF数据的写入
 */
public class ThreeNDEF extends BaseNfcActivity {
    private TextView title_tv;
    private TextView content_tv;
    private NdefMessage msgs[] = null;
    public static final String TAG = "TAG";
    private String payload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    private void init() {
        // title_tv = (TextView) findViewById(R.id.title_tv);
        content_tv = (TextView) findViewById(R.id.content_tv);
        title_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //enableForegroupDispath();
                AlertDialog.Builder builder = new AlertDialog.Builder(ThreeNDEF.this);
                builder.setTitle("标签")
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                //disableForegroupDispath();
                            }
                        });
                AlertDialog alertDialog = builder.create();

                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        });
        content_tv.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                payload = editable.toString();
                Log.e(TAG, "afterTextChanged: " + payload);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        resolveIntent(intent);
    }

    public static final byte[] KEY_D =
            {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

    private void resolveIntent(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {

            Tag parcelableExtra = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (supportedTechs(parcelableExtra.getTechList())) {
                //带有AAR写入的
                NdefMessage ndefMsg_rid_uri = MyNDEFMsgGet.getNdefMsg_Rid_URI(payload, (byte) 0x01, true);
                new WriteTask(this, ndefMsg_rid_uri, parcelableExtra).execute();

            }
        }

    }

    /**
     * TAG  的类型
     *
     * @param techList
     * @return
     */
    private boolean supportedTechs(String[] techList) {
        boolean issupport = false;
        for (String s : techList) {
            Log.e("TAG", "supportedTechs: ");
            //列举出支持的类型
            if (s.equals("android.nfc_tech_filter.tech.Ndef")) {
                issupport = true;
            } else if (s.equals("android.nfc_tech_filter.tech.MifareClassic")) {
                issupport = true;
            } else if (s.equals("android.nfc_tech_filter.tech.NdefFormatable")) {
                issupport = true;
            } else if (s.equals("android.nfc_tech_filter.tech.Ndef")) {
                issupport = true;
            } else if (s.equals("android.nfc_tech_filter.tech.NfcA")) {
                issupport = true;
            } else if (s.equals("android.nfc_tech_filter.tech.NfcB")) {
                issupport = true;

            } else if (s.equals("android.nfc_tech_filter.tech.Ndef")) {
                issupport = true;
            } else if (s.equals("android.nfc_tech_filter.tech.Nfcv")) {
                issupport = true;
            } else {
                issupport = false;
            }


        }

        return issupport;
    }

    /**
     * 把数据写入到NDEF中
     */
    static class WriteTask extends AsyncTask<Void, Void, Void> {
        private static final String TAG = "TAG";
        Activity activity = null;
        NdefMessage message = null;
        Tag tag = null;

        WriteTask(Activity host, NdefMessage message, Tag tag) {
            this.activity = host;
            this.message = message;
            this.tag = tag;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//if(){}
            activity.finish();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //message 的长度
            int size = message.toByteArray().length;
            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                //获取tag 对象
                NdefFormatable ndefFormatable = NdefFormatable.get(tag);
                if (ndefFormatable != null) {
                    try {
                        //连接
                        ndefFormatable.connect();
                        ndefFormatable.format(message);
                    } catch (IOException e) {
                        Log.e(TAG, "connect连接失败");
                        e.printStackTrace();
                    } catch (FormatException e) {
                        Log.e(TAG, "format连接失败");
                        e.printStackTrace();
                    } finally {
                        try {
                            ndefFormatable.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    Log.e(TAG, "ndef 不支持你得tag: ");
                }


            } else {
                try {
                    ndef.connect();
                    //判断是否可写
                    if (!ndef.isWritable()) {
                        Log.e(TAG, "只读模式 ");

                    } else if (ndef.getMaxSize() > size) {
                        Log.e(TAG, "标签太长 ");
                    } else {

                        ndef.writeNdefMessage(message);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (FormatException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        ndef.connect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            return null;
        }
    }


}
