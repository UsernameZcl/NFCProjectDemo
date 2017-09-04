package com.example.zcl.nfcprojectdemo.utils;

import android.net.Uri;
import android.nfc.NdefRecord;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Bytes;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by zcl on 2017/8/29.
 */
//NDEF数据的解析类
public class RecordParse {

    private static String textRecord;

    public static Uri parseWellKnowUriRecord(NdefRecord ndefRecord) {
        //检查
        Preconditions.checkArgument(Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_URI));
        //获取 payload
        byte[] payload = ndefRecord.getPayload();
        //解析 prefix 是uri的前缀
        String prefix = UriPrefix.URI_PREFIX_MAP.get(payload[0]);
        byte[] fulUri = Bytes.concat(prefix.getBytes(Charset.forName("UTF-8")), Arrays.copyOfRange(payload, 1, payload.length));
        //uri
        Uri parse = Uri.parse(new String(fulUri, Charset.forName("UTF-8")));
        return parse;

    }

    public static Uri parseAbsulotUriRecord(NdefRecord ndefRecord) {
        byte[] payload = ndefRecord.getPayload();
        Uri parse = Uri.parse(new String(payload, Charset.forName("UTF-8")));
        return parse;
    }

    //文本
    public static String parseWellKnowTextRecord(NdefRecord ndefRecord) {
        //检查
        Preconditions.checkArgument(Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT));
        //获取 payload //获得字节数组，然后进行分析
        byte[] payload = ndefRecord.getPayload();
        byte b = ndefRecord.getPayload()[0];
        //下面开始NDEF文本数据第一个字节，状态字节
        //判断文本是基于UTF-8还是UTF-16的，取第一个字节"位与"上16进制的80，16进制的80也就是最高位是1，
        //其他位都是0，所以进行"位与"运算后就会保留最高位
        String textEncoding = ((b & 0x80) == 0) ? "UTF-8" : "UTF-16";
        //3f最高两位是0，第六位是1，所以进行"位与"运算后获得第六位
        int languageCodeLength = b & 0x3f;
        //下面开始NDEF文本数据第二个字节，语言编码
        //获得语言编码
        String languageCode = new String(payload, 1, languageCodeLength, Charset.forName("UTF-8"));
        try {
            //真实的文本信息
            textRecord = new String(payload, languageCodeLength + 1,
                    payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return textRecord;
    }

    public static void parseMineRecord(NdefRecord ndefRecord) {
        byte[] payload = ndefRecord.getPayload();
        Uri parse = Uri.parse(new String(payload, Charset.forName("UTF-8")));

    }

    public static void parseExternalRecord(NdefRecord ndefRecord) {
        byte[] payload = ndefRecord.getPayload();
        Uri parse = Uri.parse(new String(payload, Charset.forName("UTF-8")));
    }


}
