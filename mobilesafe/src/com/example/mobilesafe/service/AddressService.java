package com.example.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.engine.NumberAddressService;

public class AddressService extends Service {
	public static final String TAG = "AddressService";
	private TelephonyManager manager;
	private MyPhoneListener listener;
	private WindowManager windowmanager ;
	private SharedPreferences sp;
	private View view;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		listener = new MyPhoneListener();
		sp = getSharedPreferences("config", MODE_PRIVATE);
	    windowmanager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
		// ע��ϵͳ�绰�������ļ�����
		manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	private class MyPhoneListener extends PhoneStateListener{

		// �绰״̬�����ı��ʱ�� ���õķ��� 
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE: // ���ھ�ֹ״̬: û�к���
				if(view!=null){
					windowmanager.removeView(view);
					view = null;
				}
				// �ٻ�ȡһ��ϵͳ��ʱ�� 
				break;

			case TelephonyManager.CALL_STATE_RINGING: // ����״̬
				Log.i(TAG,"�������Ϊ"+ incomingNumber);
				String address = NumberAddressService.getAddress(incomingNumber);
				Log.i(TAG,"������Ϊ"+ address);
				//Toast.makeText(getApplicationContext(), "������Ϊ"+ address, 1).show();
				showLoaction(address);
				// ��ȡһ�µ�ǰϵͳ��ʱ�� 
				
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK: //��ͨ�绰״̬
				if(view!=null){
					windowmanager.removeView(view);
					view = null;
				}
				break;
			}
			
		}
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		manager.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
	}

	/**
	 * �ڴ�������ʾ����λ����Ϣ
	 * @param address
	 */
	public void showLoaction(String address) {
        WindowManager.LayoutParams params = new LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
        params.gravity = Gravity.LEFT | Gravity.TOP;

        params.x =     sp.getInt("lastx", 0);
        params.y =     sp.getInt("lasty", 0);
        
        
        view = View.inflate(getApplicationContext(), R.layout.show_location, null);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_location);
        
        
        
        int backgroundid = sp.getInt("background", 0);
        if(backgroundid==0){
        	ll.setBackgroundResource(R.drawable.call_locate_gray);
        }else if(backgroundid==1){
        	ll.setBackgroundResource(R.drawable.call_locate_orange);
        }else {
        	ll.setBackgroundResource(R.drawable.call_locate_green);
        }
        
        TextView tv = (TextView) view.findViewById(R.id.tv_location);
        tv.setTextSize(24);
        tv.setText(address);
        windowmanager.addView(view , params);
	}
	
}
