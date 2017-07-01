package com.wuwei.mymobliesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.wuwei.mymobliesafe.app.Constants;
import com.wuwei.mymobliesafe.utils.SPUtil;

/**
 * Created by 无为 on 2017/5/10.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG","BOOT 手机,监听到相应广播------");

        TelephonyManager manager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String serialNumber = manager.getSimSerialNumber();
        String sim_number = SPUtil.getString(context, Constants.SIM_NUMBER,"");

        if(!serialNumber.equals(sim_number)){
            SmsManager smsManager = SmsManager.getDefault();
            String phone = SPUtil.getString(context,Constants.CONTACT_PHONE,"");
            if (!TextUtils.isEmpty(phone))
                smsManager.sendTextMessage(phone,null,"sim change",null,null);
            else
                smsManager.sendTextMessage("15670698550",null,"sim change",null,null);
        }
    }
}
