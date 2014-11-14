package com.example.mobilesafe.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.mobilesafe.adapter.MainUIAdapter;

public class MainActivity extends Activity implements OnItemClickListener {

	private static final String TAG = "MainActivity";
	private GridView gv_main;
	private MainUIAdapter adapter;
	// �����־û�һЩ������Ϣ
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainscreen);
		sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);
		gv_main = (GridView) this.findViewById(R.id.gv_main);
		adapter = new MainUIAdapter(this);
		gv_main.setAdapter(adapter);
		gv_main.setOnItemClickListener(this);

		gv_main.setOnItemLongClickListener(new OnItemLongClickListener() {

			// �޸Ĺ�������
			public boolean onItemLongClick(AdapterView<?> parent,
					final View view, int position, long id) {
				if (position == 0) {
					AlertDialog.Builder buider = new Builder(MainActivity.this);
					buider.setTitle("����");
					buider.setMessage("������Ҫ���ĵ�����");
					final EditText et = new EditText(MainActivity.this);
					et.setHint("�������ı�");
					buider.setView(et);
					buider.setPositiveButton("ȷ��", new OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							String name = et.getText().toString().trim();
							if ("".equals(name)) {
								Toast.makeText(getApplicationContext(),
										"���ݲ���Ϊ��", 0).show();
								return;
							} else {
								Editor editor = sp.edit();
								editor.putString("lost_name", name);
								// ������ݵ��ύ
								editor.commit();
								TextView tv = (TextView) view
										.findViewById(R.id.tv_main_name);
								tv.setText(name);
							}

						}
					});

					buider.setNegativeButton("ȡ��", new OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
						}
					});

					buider.create().show();
				}
				return false;
			}
		});
	}

	/**
	 * ��gridview����Ŀ�������ʱ�� ��Ӧ�Ļص� parent :��gridview view : ��ǰ���������Ŀ Linearlayout
	 * position : �����Ŀ��Ӧ��λ�� id : ������к�
	 */

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.i(TAG, "�����λ��" + position);
		switch (position) {
		case 0: // �ֻ�����
			//Toast.makeText(this, "�ֻ�����", 0).show();
			Intent lostIntent = new Intent(MainActivity.this,
					LostProtectedActivity.class);
			startActivity(lostIntent);
			break;
		case 1:// ͨѶ��ʿ
			//Toast.makeText(this, "ͨѶ��ʿ", 0).show();
			Intent callsmsIntent = new Intent(MainActivity.this, CallSmsActivity.class);
			startActivity(callsmsIntent);
			break;
		case 2:// �������
			Intent appmanagerIntent = new Intent(MainActivity.this, AppManagerActivity.class);
			startActivity(appmanagerIntent);
			break;
		case 3:// �������
			Intent taskmanagerIntent = new Intent(MainActivity.this,
					TaskManagerActivity.class);
			startActivity(taskmanagerIntent);
			//
			break;
		case 4:// ��������
			Intent trafficmanagerIntent = new Intent(MainActivity.this,
					TrafficManagerActivity.class);
			startActivity(trafficmanagerIntent);
			break;
		case 5:// �ֻ��鶾
			Intent taskmanagerIntent2 = new Intent(MainActivity.this,
					DemoActivity.class);
			startActivity(taskmanagerIntent2);
			break;
		case 6:// ϵͳ�Ż�
			Log.i(TAG, "����ϵͳ�Ż�");
			Toast.makeText(this, "ϵͳ�Ż�", 0).show();
			Intent clearcacheIntent = new Intent(MainActivity.this,
					ClearCacheActivity.class);
			startActivity(clearcacheIntent);
			break;
		case 7:// �߼�����
			Log.i(TAG, "����߼�����");
			Toast.makeText(this, "�߼�����", 0).show();
			Intent atoolsIntent = new Intent(MainActivity.this, AtoolsActivity.class);
			startActivity(atoolsIntent);
			break;
		case 8:// ��������
			Log.i(TAG, "������������");
			Toast.makeText(this, "��������", 0).show();
			Intent settingcenterIntent = new Intent(MainActivity.this,
					SettingCenterActivity.class);
			startActivity(settingcenterIntent);
			break;
		default:
			break;

		}

	}

}
