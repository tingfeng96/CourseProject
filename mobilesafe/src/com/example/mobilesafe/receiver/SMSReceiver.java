package com.example.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.mobilesafe.R;
import com.example.mobilesafe.engine.GPSInfoProvider;

public class SMSReceiver extends BroadcastReceiver {

	private static final String TAG = "SMSReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// ��ȡ���ŵ�����
		// #*location*#123456
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		for (Object pdu : pdus) {
			SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
			String content = sms.getMessageBody();
			Log.i(TAG, "��������" + content);
			String sender = sms.getOriginatingAddress();
			if ("#*location*#".equals(content)) {
				// ��ֹ�㲥
				abortBroadcast();
				GPSInfoProvider provider = GPSInfoProvider.getInstance(context);
				String location = provider.getLocation();
				SmsManager smsmanager = SmsManager.getDefault();
				if ("".equals(location)) {

				} else {
					smsmanager.sendTextMessage(sender, null, location, null,
							null);
				}
			}else if("#*locknow*#".equals(content)){
				DevicePolicyManager manager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
				manager.resetPassword("123456", 0);
				manager.lockNow();
				abortBroadcast();
			}else if("#*wipedata*#".equals(content)){
				DevicePolicyManager manager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
				manager.wipeData(0);
				abortBroadcast();
			}else if("#*alarm*#".equals(content)){
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.setVolume(1.0f, 1.0f);
				player.start();
				abortBroadcast();
			}
		}

	}

}
