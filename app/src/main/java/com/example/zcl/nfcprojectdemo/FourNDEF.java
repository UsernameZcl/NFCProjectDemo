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
 * 非NDEF数据的读取
 */
public class FourNDEF extends BaseNfcActivity {
    private static final String TAG = "TAG";
    private TextView title_tv;
    private TextView content_tv;
    private NdefMessage msgs[] = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {

        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            ////1.获取Tag对象
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (supportedTechs(detectedTag.getTechList())) {
                MifareClassic mifareClassic = MifareClassic.get(detectedTag);
                if (mifareClassic != null) {
                    try {
                        mifareClassic.connect();

                        if(!mifareClassic.authenticateSectorWithKeyA(1, MifareClassic.KEY_DEFAULT)){
                            //必须是16个的字节
                            mifareClassic.writeBlock(1,"012345678912345".getBytes());

                        }else {
                            Log.e(TAG, "resolveIntent: 写入失败 " );
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("TAG", "TAG  不是经典的 MifareClassic  ");
                }

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

}