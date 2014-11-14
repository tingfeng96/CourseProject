package com.example.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.mobilesafe.util.TaskUtil;

public class LockScreenReceiver extends BroadcastReceiver {

	private static final String TAG = "LockScreenReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {

		//����Ļ������ʱ�� ��������onReceve ����

		Log.i(TAG,"����");
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean killprocess = sp.getBoolean("killprocess", false);
		if(killprocess){
			TaskUtil.killAllProcess(context);
			Log.i(TAG,"ɱ�����н���");
		}
	}
}
