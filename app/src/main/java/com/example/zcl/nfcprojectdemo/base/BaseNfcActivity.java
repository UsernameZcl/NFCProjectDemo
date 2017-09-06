package com.example.zcl.nfcprojectdemo.base;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by zcl on 2017/8/29.
 */

/**
 * 1.子类需要在onCreate方法中做Activity初始化。
 * 2.子类需要在onNewIntent方法中进行NFC标签相关操作。
 * 当launchMode设置为singleTop时，第一次运行调用onCreate方法，
 * 第二次运行将不会创建新的Activity实例，将调用onNewIntent方法
 * 所以我们获取intent传递过来的Tag数据操作放在onNewIntent方法中执行
 * 如果在栈中已经有该Activity的实例，就重用该实例(会调用实例的onNewIntent())
 * 只要NFC标签靠近就执行
 */
public class BaseNfcActivity extends AppCompatActivity {
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;

    /**
     * 启动Activity，界面可见时
     */
    @Override
    protected void onStart() {
        super.onStart();

        //检查NFC
        NfcCheck();
       // mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //init NFC
        //一旦截获NFC消息，就会通过PendingIntent调用窗口
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void NfcCheck() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "设备不支持NDEF", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (mNfcAdapter.isEnabled()) {
                //默认跳转到安卓默认的nfc界面，去开启nfc
              Toast.makeText(this, "请在系统设置中先启用NFC功能！", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
//                startActivity(intent);
//finish();
            }
        }
    }

    private void disableForegroupDispath() {
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }

    }

    private void enableForegroupDispath() {
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }

    }

    /**
     * 获得焦点，按钮可以点击
     */
    @Override
    protected void onResume() {
        super.onResume();
        //设置处理优于所有其他NFC的处理
        enableForegroupDispath();
    }

    /**
     * 暂停Activity，界面获取焦点，按钮可以点击
     */
    @Override
    protected void onPause() {
        //恢复默认状态
        super.onPause();
        disableForegroupDispath();
    }
}
