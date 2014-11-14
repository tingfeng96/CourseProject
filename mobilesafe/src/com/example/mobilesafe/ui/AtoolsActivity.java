package com.example.mobilesafe.ui;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.mobilesafe.engine.DownLoadFileTask;
import com.example.mobilesafe.engine.SmsInfoService;
import com.example.mobilesafe.service.AddressService;
import com.example.mobilesafe.service.BackupSmsService;

public class AtoolsActivity extends Activity implements OnClickListener {

	protected static final int ERROR = 10;
	protected static final int SUCCESS = 11;
	private TextView tv_query;
	private ProgressDialog pd;
	private TextView tv_atools_address;
	private TextView tv_atools_select_bg;
	private Intent serviceintent;
	private CheckBox cb_atools_address;
	private TextView tv_atools_change_location;
	private TextView tv_atools_sms_backup;
	private TextView tv_atools_sms_restore;
	private TextView tv_atools_app_lock;
	private TextView tv_atools_common_num;
	private SmsInfoService mSmsInfoService;
	private SharedPreferences sp;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ERROR:
				Toast.makeText(getApplicationContext(), "�������ݿ�ʧ��", 0).show();
				break;
			case SUCCESS:
				Toast.makeText(getApplicationContext(), "�������ݿ�ɹ�", 0).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atools);
		tv_atools_common_num = (TextView)this.findViewById(R.id.tv_atools_common_num);
		tv_atools_common_num.setOnClickListener(this);
		
		tv_atools_app_lock = (TextView)this.findViewById(R.id.tv_atools_app_lock);
		tv_atools_app_lock.setOnClickListener(this);
		
		tv_atools_sms_restore = (TextView) this
				.findViewById(R.id.tv_atools_sms_restore);
		tv_atools_sms_backup = (TextView) this
				.findViewById(R.id.tv_atools_sms_backup);
		tv_atools_sms_backup.setOnClickListener(this);
		tv_atools_sms_restore.setOnClickListener(this);

		tv_atools_change_location = (TextView) this
				.findViewById(R.id.tv_atools_change_location);
		tv_atools_change_location.setOnClickListener(this);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		tv_atools_select_bg = (TextView) this
				.findViewById(R.id.tv_atools_select_bg);
		tv_atools_select_bg.setOnClickListener(this);
		tv_query = (TextView) this.findViewById(R.id.tv_atools_query);
		cb_atools_address = (CheckBox) this
				.findViewById(R.id.cb_atools_address);
		serviceintent = new Intent(this, AddressService.class);
		tv_atools_address = (TextView) this
				.findViewById(R.id.tv_atools_address);
		cb_atools_address
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							startService(serviceintent);
							tv_atools_address.setTextColor(Color.BLACK);
							tv_atools_address.setText("��������ط����Ѿ�����");
						} else {
							stopService(serviceintent);
							tv_atools_address.setTextColor(Color.RED);
							tv_atools_address.setText("��������ط���δ����");
						}

					}
				});

		tv_query.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_atools_query:
			// �ж�������������ݿ��Ƿ����
			if (isDBexist()) {

				Intent intent = new Intent(this, QueryNumberActivity.class);
				startActivity(intent);
			} else {
				// ��ʾ�û��������ݿ�
				pd = new ProgressDialog(this);
				pd.setMessage("�����������ݿ�");
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.show();
				// �������ݿ�
				new Thread() {
					@Override
					public void run() {
						String path = getResources().getString(
								R.string.addressdburl);
						String filepath = "/sdcard/address.db";
						try {
							DownLoadFileTask.getFile(path, filepath, pd);
							pd.dismiss();
							Message msg = new Message();
							msg.what = SUCCESS;
							handler.sendMessage(msg);
						} catch (Exception e) {
							e.printStackTrace();
							pd.dismiss();
							Message msg = new Message();
							msg.what = ERROR;
							handler.sendMessage(msg);
						}
					}
				}.start();

			}

			break;
		case R.id.tv_atools_select_bg:
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("��������ʾ��ʾ���");
			String[] items = new String[] { "��͸��", "������", "ƻ����" };
			builder.setSingleChoiceItems(items, 0,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							Editor editor = sp.edit();
							editor.putInt("background", which);
							editor.commit();
						}
					});

			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.create().show();
			break;

		case R.id.tv_atools_change_location:
			// ������������ص���ʾλ��
			Intent intent = new Intent(this, DragViewActivity.class);
			startActivity(intent);

			break;
		case R.id.tv_atools_sms_backup:
			//���÷��񱸷ݶ���
			Intent service = new Intent(this,BackupSmsService.class);
			startService(service);
			
			
			break;
		case R.id.tv_atools_sms_restore:
			//��ȡ���ݵ�xml�ļ� 
			//�����ļ��������Ϣ 
			//���뵽����Ӧ������
			final ProgressDialog pd = new ProgressDialog(this);
			pd.setCancelable(false);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setMessage("���ڻ�ԭ����");
			pd.show();
			mSmsInfoService = new SmsInfoService(this);
			new Thread(){
				@Override
				public void run() {
					try {
						mSmsInfoService.restoreSms("/sdcard/smsbackup.xml",pd);
						pd.dismiss();
						Looper.prepare();
						Toast.makeText(getApplicationContext(), "��ԭ�ɹ�", 0).show();
						Looper.loop();
					} catch (Exception e) {
						e.printStackTrace();
						pd.dismiss();
						Looper.prepare();
						Toast.makeText(getApplicationContext(), "��ԭʧ��", 0).show();
						Looper.loop();
					}
				}
			}.start();
			
			
			break;
		case R.id.tv_atools_app_lock:
			Intent applockIntent = new Intent(this,AppLockActivity.class);
			startActivity(applockIntent);
			
			break;
			
		case R.id.tv_atools_common_num:
			Intent commonnumintent = new Intent(this,CommonNumActivity.class);
			startActivity(commonnumintent);
			//TODO : 
			break;
		}

	}

	/**
	 * �ж����ݿ��Ƿ����
	 * 
	 * @return
	 */
	public boolean isDBexist() {
		File file = new File("/sdcard/address.db");
		return file.exists();

	}

}
