package com.example.zcl.nfcprojectdemo;

import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import com.example.zcl.nfcprojectdemo.base.BaseNfcActivity;
import com.example.zcl.nfcprojectdemo.utils.RecordParse;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by zcl on 2017/8/30.
 */

/**
 * 非NDEF数据的读取
 */
public class SecondNFC extends BaseNfcActivity {

    private TextView title_tv;
    private TextView content_tv;
    private NdefMessage msgs[] = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    private void init() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        resolveIntent(intent);
    }

    public static final byte[] KEY_D =
            {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

    private void resolveIntent(Intent intent) {

        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            ////1.获取Tag对象
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            boolean isAuth = false;
            if (supportedTechs(detectedTag.getTechList())) {
                MifareClassic mifareClassic = MifareClassic.get(detectedTag);
                if (mifareClassic != null) {
                    try {
                        mifareClassic.connect();
                        //获取散曲的个数
                        int sectorCount = mifareClassic.getSectorCount();
                        for (int i = 0; i < sectorCount; i++) {
                            // 访问散曲  每一个厂商的散曲的key都是不同的，要想解析数据，就必修知道每一个key
                            if (mifareClassic.authenticateSectorWithKeyA(i, MifareClassic.KEY_DEFAULT)) {
                                isAuth = true;

                            } else if (mifareClassic.authenticateSectorWithKeyA(i, KEY_D)) {
                                isAuth = true;
                            } else {
                                isAuth = false;
                            }
                            if (isAuth) {
                                int mBlock = mifareClassic.getBlockCountInSector(i);
                                for (int j = 0; j < mBlock; j++) {
                                    //每一个块的数据。 转化为String类型的数据
                                    byte[] bytes = mifareClassic.readBlock(j);

                                }
                            }
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
