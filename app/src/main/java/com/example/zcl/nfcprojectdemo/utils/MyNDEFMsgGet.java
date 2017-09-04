package com.example.zcl.nfcprojectdemo.utils;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Created by zcl on 2017/8/29.
 */
//将要加入的数据转化为NDEF的数据
public class MyNDEFMsgGet {
    public static NdefMessage getNdefMsg_Rid_URI(String data, byte identifierCode, boolean needaar) {
        Log.e("TAG", "getNdefMsg_Rid_URI: ");
        byte[] us_asciis = data.getBytes(Charset.forName("US-ASCII"));
        byte[] payload = new byte[us_asciis.length + 1];
        payload[0] = identifierCode;//0x01=http://www.
        System.arraycopy(us_asciis, 0, payload, 1, us_asciis.length);
        NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_URI, new byte[0], payload);
        if (needaar) {
            //

            return new NdefMessage(new NdefRecord[]{ndefRecord, NdefRecord.createApplicationRecord("这是安卓应用程序的报名： 如： android.com.app")});


        } else {
            return new NdefMessage(new NdefRecord[]{ndefRecord});
        }


    }

    public static NdefMessage getNdefMsg_Rid_Text(String data, boolean encodeInUtf8, boolean needaar) {
        Log.e("TAG", "getNdefMsg_Rid_Text: ");


        Locale locale = new Locale("en", "US");
        byte[] langbytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEndcodeing = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        int utfbit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfbit + langbytes.length);
        byte[] bytes = data.getBytes(utfEndcodeing);
        byte[] payload = new byte[1 + langbytes.length + bytes.length];
        payload[0] = (byte) status;
        System.arraycopy(langbytes, 0, payload, 1, langbytes.length);//复制语言码
        System.arraycopy(bytes, 0, payload, 1 + langbytes.length, bytes.length);//复制实际文本数据

        NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);
        if (needaar) {
            //

            return new NdefMessage(new NdefRecord[]{ndefRecord, NdefRecord.createApplicationRecord("这是安卓应用程序的报名： 如： android.com.app")});


        } else {
            return new NdefMessage(new NdefRecord[]{ndefRecord});
        }

    }

    public static NdefMessage getNdefMsg_Absolut_uri(String data, boolean needaar) {
        Log.e("TAG", "getNdefMsg_Rid_Text: ");
        byte[] payload = data.getBytes(Charset.forName("US-ASCII"));

        NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_ABSOLUTE_URI, payload, new byte[0], new byte[0]);
        if (needaar) {
            //

            return new NdefMessage(new NdefRecord[]{ndefRecord, NdefRecord.createApplicationRecord("这是安卓应用程序的报名： 如： android.com.app")});


        } else {
            return new NdefMessage(new NdefRecord[]{ndefRecord});
        }

    }

    public static NdefMessage getNdefMsg_External_uri(String data, boolean needaar) {
        byte[] bytes = data.getBytes();
        String domain = "这是安卓应用程序的报名： 如： android.com.app";
        String type = "externalType";
        String externalType = domain + ":" + type;

        NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_EXTERNAL_TYPE, externalType.getBytes(), new byte[0], bytes);
        if (needaar) {
            //

            return new NdefMessage(new NdefRecord[]{ndefRecord, NdefRecord.createApplicationRecord("这是安卓应用程序的报名： 如： android.com.app")});


        } else {
            return new NdefMessage(new NdefRecord[]{ndefRecord});
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static NdefMessage getNdefMsg_Mine_uri(String data, boolean needaar) {
        byte[] payload = data.getBytes(Charset.forName("US-ASCII"));
        String minetYPE="application/这是安卓应用程序的报名： 如： android.com.app";
        NdefRecord ndefRecord = NdefRecord.createMime(minetYPE, payload);
        if (needaar) {
            //

            return new NdefMessage(new NdefRecord[]{ndefRecord, NdefRecord.createApplicationRecord("这是安卓应用程序的报名： 如： android.com.app")});


        } else {
            return new NdefMessage(new NdefRecord[]{ndefRecord});
        }

    }

}
